COMMENT ON TABLE usuario_usrp IS '2019-07-01';
COMMENT ON TABLE usuariosesion_ussp IS '2019.07.01.00';

ALTER TABLE pedidoventacaracteristica_pvcp
	DROP CONSTRAINT fk_pedidoventacaracteristicausuarioinactivo;

ALTER TABLE pedidoventacaracteristica_pvcp
	DROP CONSTRAINT fk_pedidoventacaracteristicausuarioregistro;

ALTER TABLE documentorelacionexpediente_dexp
	DROP CONSTRAINT fk_documentorelacionexpedienteusuarioinactivo;

ALTER TABLE documentorelacionexpediente_dexp
	DROP CONSTRAINT fk_documentorelacionexpedienteusuarioregistro;
	
CREATE TABLE propiedad_ppdp (
	cppd_llave character varying(32) NOT NULL,
	cppd_tipo character varying(1) NOT NULL,
	cppd_campo character varying(32) NOT NULL,
	cppd_key character varying(100) NOT NULL,
	cppd_valor character varying(100) NOT NULL,
	cppd_texto character varying(100),
	cppd_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE propiedad_ppdp
	ADD CONSTRAINT pk_propiedad_ppdp PRIMARY KEY (cppd_llave);

insert into propiedad_ppdp (cppd_llave, cppd_tipo, cppd_campo, cppd_key, cppd_valor, cppd_texto)
select substring('C-' ||cpcp_llave,0,32), 'C', cpcp_campo, cpcp_key, cpcp_valor, cpcp_texto from plantillacampoparametro_pcpp where cpcp_estado = 'A';

DROP TABLE plantillacampoparametro_pcpp;

insert into propiedad_ppdp (cppd_llave, cppd_tipo, cppd_campo, cppd_key, cppd_valor, cppd_texto)
select substring('L-' ||cppr_llave,0,32), 'L', cppr_plantilla, cppr_key, cppr_valor, cppr_texto from plantillapropiedad_pprp where cppr_estado = 'A';

DROP TABLE plantillapropiedad_pprp;

insert into propiedad_ppdp (cppd_llave, cppd_tipo, cppd_campo, cppd_key, cppd_valor, cppd_texto)
select substring('S-' ||cpsi_llave,0,32), 'S', 'S', cpsi_key, cpsi_valor, cpsi_texto from propiedadsistema_psip where cpsi_estado = 'A';

DROP TABLE propiedadsistema_psip;

ALTER TABLE detallepedidoventa_dpvp
	ADD COLUMN cdpv_documento character varying(32),
 	ADD ddpv_fecharegistro timestamptz,
 	ADD ddpv_fechainactivo timestamptz,
 	ADD cdpv_usuarioregistro varchar(32),
 	ADD cdpv_usuarioinactivo varchar(32);

CREATE INDEX documentorelaciondetalle_drdp_cdrd_detalle_idx ON documentorelaciondetalle_drdp (cdrd_detalle);

update detallepedidoventa_dpvp 
set 
cdpv_documento = (select cdrd_documento from documentorelaciondetalle_drdp where cdrd_detalle = cdpv_llave),
ddpv_fecharegistro = (select ddrd_fecharegistro from documentorelaciondetalle_drdp where cdrd_detalle = cdpv_llave),
ddpv_fechainactivo = (select ddrd_fechainactivo from documentorelaciondetalle_drdp where cdrd_detalle = cdpv_llave),
cdpv_usuarioregistro = (select cdrd_usuarioregistro from documentorelaciondetalle_drdp where cdrd_detalle = cdpv_llave),
cdpv_usuarioinactivo = (select cdrd_usuarioinactivo from documentorelaciondetalle_drdp where cdrd_detalle = cdpv_llave);



delete from detallecaracteristicaproducto_dcpp where cdcp_entidad in (select cdpv_llave from detallepedidoventa_dpvp where cdpv_documento is null);
delete from detallepedidoventa_dpvp where cdpv_documento is null;

update detallepedidoventa_dpvp 
set cdpv_estado = (select cdrd_estado from documentorelaciondetalle_drdp where cdrd_detalle = cdpv_llave);


ALTER TABLE detallepedidoventa_dpvp
	ALTER COLUMN cdpv_documento SET NOT null;

DROP INDEX documentorelaciondetalle_drdp_cdrd_detalle_idx;

DROP TABLE documentorelaciondetalle_drdp;

CREATE TABLE documentotransaccion_trap (
	ctra_llave character varying(32) NOT NULL,
	dtra_fecha timestamp with time zone NOT NULL,
	ctra_usuario character varying(32) NOT NULL,
	ctra_documento character varying(32) NOT NULL,
	ctra_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

INSERT INTO documentotransaccion_trap(ctra_llave, dtra_fecha, ctra_usuario, ctra_documento )
	SELECT cpdv_llave, dpdv_fecharegistro, cpdv_funcionario, cpdv_llave FROM pedidoventa_pdvp;

ALTER TABLE documentotransaccion_trap
	ADD CONSTRAINT pk_documentotransaccion_trap PRIMARY KEY (ctra_llave);

ALTER TABLE pedidoventacaracteristica_pvcp
	ADD COLUMN IF NOT EXISTS cpvc_transaccionregistro character varying(32),
	ADD COLUMN IF NOT EXISTS cpvc_transaccioninactivo character varying(32);

ALTER TABLE detallecaracteristicaproducto_dcpp
	ADD COLUMN cdcp_transaccionregistro character varying(32),
	ADD COLUMN cdcp_transaccioninactivo character varying(32);

ALTER TABLE detallepedidoventa_dpvp
	ADD COLUMN cdpv_transaccionregistro character varying(32),
	ADD COLUMN cdpv_transaccioninactivo character varying(32);

ALTER TABLE documentorelacionexpediente_dexp
	ADD COLUMN IF NOT EXISTS cdex_transaccionregistro character varying(32),
	ADD COLUMN IF NOT EXISTS cdex_transaccioninactivo character varying(32);


ALTER TABLE pedidoventacaracteristica_pvcp
	ADD CONSTRAINT fk_pedidoventacaracteristicatransaccionregistro FOREIGN KEY (cpvc_transaccionregistro) REFERENCES documentotransaccion_trap(ctra_llave);
	
ALTER TABLE detallepedidoventa_dpvp
	ADD CONSTRAINT fk_detallepedidoventadocumento FOREIGN KEY (cdpv_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE documentorelacionexpediente_dexp
	ADD CONSTRAINT fk_documentorelacionexpedientetransaccioninactivo FOREIGN KEY (cdex_transaccioninactivo) REFERENCES documentotransaccion_trap(ctra_llave);

ALTER TABLE documentorelacionexpediente_dexp
	ADD CONSTRAINT fk_documentorelacionexpedientetransaccionregistro FOREIGN KEY (cdex_transaccionregistro) REFERENCES documentotransaccion_trap(ctra_llave);

ALTER TABLE documentotransaccion_trap
	ADD CONSTRAINT fk_documentotransaccionusuario FOREIGN KEY (ctra_usuario) REFERENCES usuario_usrp(cusr_llave);

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'plantillapropiedad_pprp', 'propiedad_ppdp');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'plantillapropiedad_pprp', 'propiedad_ppdp');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'plantillapropiedad_pprp', 'propiedad_ppdp');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'plantillapropiedad_pprp', 'propiedad_ppdp');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cppr_valor', 'cppd_valor');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cppr_valor', 'cppd_valor');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cppr_valor', 'cppd_valor');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cppr_valor', 'cppd_valor');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cppr_key', 'cppd_key');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cppr_key', 'cppd_key');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cppr_key', 'cppd_key');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cppr_key', 'cppd_key');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cppr_plantilla', 'cppd_campo');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cppr_plantilla', 'cppd_campo');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cppr_plantilla', 'cppd_campo');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cppr_plantilla', 'cppd_campo');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'documentorelaciondetalle_drdp, detallepedidoventa_dpvp', 'detallepedidoventa_dpvp');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'documentorelaciondetalle_drdp, detallepedidoventa_dpvp', 'detallepedidoventa_dpvp');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'documentorelaciondetalle_drdp, detallepedidoventa_dpvp', 'detallepedidoventa_dpvp');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'documentorelaciondetalle_drdp, detallepedidoventa_dpvp', 'detallepedidoventa_dpvp');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cdex_expedientedetalle = cdrd_documento and cdrd_detalle = cdpv_llave', 'cdex_expedientedetalle = cdpv_documento');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cdex_expedientedetalle = cdrd_documento and cdrd_detalle = cdpv_llave', 'cdex_expedientedetalle = cdpv_documento');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cdex_expedientedetalle = cdrd_documento and cdrd_detalle = cdpv_llave', 'cdex_expedientedetalle = cdpv_documento');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cdex_expedientedetalle = cdrd_documento and cdrd_detalle = cdpv_llave', 'cdex_expedientedetalle = cdpv_documento');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cdex_expedientedetalle = cdrd_documento and cdrd_detalle=cdpv_llave', 'cdex_expedientedetalle = cdpv_documento');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cdex_expedientedetalle = cdrd_documento and cdrd_detalle=cdpv_llave', 'cdex_expedientedetalle = cdpv_documento');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cdex_expedientedetalle = cdrd_documento and cdrd_detalle=cdpv_llave', 'cdex_expedientedetalle = cdpv_documento');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cdex_expedientedetalle = cdrd_documento and cdrd_detalle=cdpv_llave', 'cdex_expedientedetalle = cdpv_documento');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'and cdrd_estado = ''A''', 'and cdpv_estado = ''A''');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'and cdrd_estado = ''A''', 'and cdpv_estado = ''A''');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'and cdrd_estado = ''A''', 'and cdpv_estado = ''A''');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'and cdrd_estado = ''A''', 'and cdpv_estado = ''A''');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'inner join documentorelaciondetalle_drdp productos on (pedido.cpdv_llave = productos.cdrd_documento and productos.cdrd_estado !=''I'')', '');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'inner join documentorelaciondetalle_drdp productos on (pedido.cpdv_llave = productos.cdrd_documento and productos.cdrd_estado !=''I'')', '');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'inner join documentorelaciondetalle_drdp productos on (pedido.cpdv_llave = productos.cdrd_documento and productos.cdrd_estado !=''I'')', '');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'inner join documentorelaciondetalle_drdp productos on (pedido.cpdv_llave = productos.cdrd_documento and productos.cdrd_estado !=''I'')', '');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'inner join detallepedidoventa_dpvp detallado on(productos.cdrd_detalle = detallado.cdpv_llave)', 'inner join detallepedidoventa_dpvp detallado on(pedido.cpdv_llave = detallado.cdpv_documento and detallado.cdpv_estado !=''I'')');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'inner join detallepedidoventa_dpvp detallado on(productos.cdrd_detalle = detallado.cdpv_llave)', 'inner join detallepedidoventa_dpvp detallado on(pedido.cpdv_llave = detallado.cdpv_documento and detallado.cdpv_estado !=''I'')');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'inner join detallepedidoventa_dpvp detallado on(productos.cdrd_detalle = detallado.cdpv_llave)', 'inner join detallepedidoventa_dpvp detallado on(pedido.cpdv_llave = detallado.cdpv_documento and detallado.cdpv_estado !=''I'')');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'inner join detallepedidoventa_dpvp detallado on(productos.cdrd_detalle = detallado.cdpv_llave)', 'inner join detallepedidoventa_dpvp detallado on(pedido.cpdv_llave = detallado.cdpv_documento and detallado.cdpv_estado !=''I'')');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'documentorelaciondetalle_drdp c, detallepedidoventa_dpvp b', 'detallepedidoventa_dpvp b');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'documentorelaciondetalle_drdp c, detallepedidoventa_dpvp b', 'detallepedidoventa_dpvp');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'documentorelaciondetalle_drdp c, detallepedidoventa_dpvp b', 'detallepedidoventa_dpvp');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'documentorelaciondetalle_drdp c, detallepedidoventa_dpvp b', 'detallepedidoventa_dpvp');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'a.cpdv_llave = c.cdrd_documento and c.cdrd_detalle = b.cdpv_llave and c.cdrd_estado = ''A'' and  cdrc_documento = cpdv_llave', 'a.cpdv_llave = b.cdpv_documento and b.cdpv_estado = ''A'' and cdrc_documento = cpdv_llave');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'a.cpdv_llave = c.cdrd_documento and c.cdrd_detalle = b.cdpv_llave and c.cdrd_estado = ''A'' and  cdrc_documento = cpdv_llave', 'a.cpdv_llave = b.cdpv_documento and b.cdpv_estado = ''A'' and cdrc_documento = cpdv_llave');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'a.cpdv_llave = c.cdrd_documento and c.cdrd_detalle = b.cdpv_llave and c.cdrd_estado = ''A'' and  cdrc_documento = cpdv_llave', 'a.cpdv_llave = b.cdpv_documento and b.cdpv_estado = ''A'' and cdrc_documento = cpdv_llave');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'a.cpdv_llave = c.cdrd_documento and c.cdrd_detalle = b.cdpv_llave and c.cdrd_estado = ''A'' and  cdrc_documento = cpdv_llave', 'a.cpdv_llave = b.cdpv_documento and b.cdpv_estado = ''A'' and cdrc_documento = cpdv_llave');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'documentorelaciondetalle_drdp,  detallepedidoventa_dpvp', 'detallepedidoventa_dpvp');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'documentorelaciondetalle_drdp,  detallepedidoventa_dpvp', 'detallepedidoventa_dpvp');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'documentorelaciondetalle_drdp,  detallepedidoventa_dpvp', 'detallepedidoventa_dpvp');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'documentorelaciondetalle_drdp,  detallepedidoventa_dpvp', 'detallepedidoventa_dpvp');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cdex_expedientedetalle = cdrd_documento and cpdv_llave = cpvc_documento and cdrd_detalle = cdpv_llave', 'cdex_expedientedetalle = cdpv_documento and cpdv_llave = cpvc_documento');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cdex_expedientedetalle = cdrd_documento and cpdv_llave = cpvc_documento and cdrd_detalle = cdpv_llave', 'cdex_expedientedetalle = cdpv_documento and cpdv_llave = cpvc_documento');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cdex_expedientedetalle = cdrd_documento and cpdv_llave = cpvc_documento and cdrd_detalle = cdpv_llave', 'cdex_expedientedetalle = cdpv_documento and cpdv_llave = cpvc_documento');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cdex_expedientedetalle = cdrd_documento and cpdv_llave = cpvc_documento and cdrd_detalle = cdpv_llave', 'cdex_expedientedetalle = cdpv_documento and cpdv_llave = cpvc_documento');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cpdv_llave =cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cpdv_llave =cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cpdv_llave =cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cpdv_llave =cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'a.cpdv_llave = c.cdrd_documento and c.cdrd_detalle =  b.cdpv_llave and c.cdrd_estado = ''A''', 'a.cpdv_llave = b.cdpv_documento and b.cdpv_estado = ''A''');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'a.cpdv_llave = c.cdrd_documento and c.cdrd_detalle =  b.cdpv_llave and c.cdrd_estado = ''A''', 'a.cpdv_llave = b.cdpv_documento and b.cdpv_estado = ''A''');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'a.cpdv_llave = c.cdrd_documento and c.cdrd_detalle =  b.cdpv_llave and c.cdrd_estado = ''A''', 'a.cpdv_llave = b.cdpv_documento and b.cdpv_estado = ''A''');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'a.cpdv_llave = c.cdrd_documento and c.cdrd_detalle =  b.cdpv_llave and c.cdrd_estado = ''A''', 'a.cpdv_llave = b.cdpv_documento and b.cdpv_estado = ''A''');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cpdv_llave= cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cpdv_llave= cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cpdv_llave= cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cpdv_llave= cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cdrd_estado =''A''', 'cdpv_estado = ''A''');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cdrd_estado =''A''', 'cdpv_estado = ''A''');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cdrd_estado =''A''', 'cdpv_estado = ''A''');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cdrd_estado =''A''', 'cdpv_estado = ''A''');

DROP TABLE modeladonegocio_mngp;

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'inner join documentorelaciondetalle_drdp productos on (pedido.cpdv_llave = productos.cdrd_documento and productos.cdrd_estado !=''I'' )', '');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'inner join documentorelaciondetalle_drdp productos on (pedido.cpdv_llave = productos.cdrd_documento and productos.cdrd_estado !=''I'' )', '');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'inner join documentorelaciondetalle_drdp productos on (pedido.cpdv_llave = productos.cdrd_documento and productos.cdrd_estado !=''I'' )', '');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'inner join documentorelaciondetalle_drdp productos on (pedido.cpdv_llave = productos.cdrd_documento and productos.cdrd_estado !=''I'' )', '');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'inner join detallepedidoventa_dpvp detallado on(productos.cdrd_detalle = detallado.cdpv_llave and (mdpv_cantidadtotal - mdpv_cantidad)!=0)', 'inner join detallepedidoventa_dpvp detallado on(pedido.cpdv_llave = detallado.cdpv_documento and (mdpv_cantidadtotal - mdpv_cantidad)!=0)');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'inner join detallepedidoventa_dpvp detallado on(productos.cdrd_detalle = detallado.cdpv_llave and (mdpv_cantidadtotal - mdpv_cantidad)!=0)', 'inner join detallepedidoventa_dpvp detallado on(pedido.cpdv_llave = detallado.cdpv_documento and (mdpv_cantidadtotal - mdpv_cantidad)!=0)');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'inner join detallepedidoventa_dpvp detallado on(productos.cdrd_detalle = detallado.cdpv_llave and (mdpv_cantidadtotal - mdpv_cantidad)!=0)', 'inner join detallepedidoventa_dpvp detallado on(pedido.cpdv_llave = detallado.cdpv_documento and (mdpv_cantidadtotal - mdpv_cantidad)!=0)');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'inner join detallepedidoventa_dpvp detallado on(productos.cdrd_detalle = detallado.cdpv_llave and (mdpv_cantidadtotal - mdpv_cantidad)!=0)', 'inner join detallepedidoventa_dpvp detallado on(pedido.cpdv_llave = detallado.cdpv_documento and (mdpv_cantidadtotal - mdpv_cantidad)!=0)');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cdrd_estado = ''A''', 'cdpv_estado = ''A''');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cdrd_estado = ''A''', 'cdpv_estado = ''A''');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cdrd_estado = ''A''', 'cdpv_estado = ''A''');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cdrd_estado = ''A''', 'cdpv_estado = ''A''');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cpdv_llave = cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cpdv_llave = cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cpdv_llave = cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cpdv_llave = cdrd_documento and cdrd_detalle = cdpv_llave', 'cpdv_llave = cdpv_documento');


CREATE INDEX ix_pedidoventacaracteristica_fecha
  ON pedidoventacaracteristica_pvcp  USING btree
  (dpvc_fecharegistro);

update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2016-01-01';
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-01-01' and dpvc_fecharegistro >= '2016-01-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-02-01' and dpvc_fecharegistro >= '2017-01-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-03-01' and dpvc_fecharegistro >= '2017-02-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-04-01' and dpvc_fecharegistro >= '2017-03-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-05-01' and dpvc_fecharegistro >= '2017-04-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-06-01' and dpvc_fecharegistro >= '2017-05-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-07-01' and dpvc_fecharegistro >= '2017-06-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-08-01' and dpvc_fecharegistro >= '2017-07-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-09-01' and dpvc_fecharegistro >= '2017-08-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-10-01' and dpvc_fecharegistro >= '2017-09-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-11-01' and dpvc_fecharegistro >= '2017-10-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2017-12-01' and dpvc_fecharegistro >= '2017-11-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-01-01' and dpvc_fecharegistro >= '2017-12-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-02-01' and dpvc_fecharegistro >= '2018-01-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-03-01' and dpvc_fecharegistro >= '2018-02-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-04-01' and dpvc_fecharegistro >= '2018-03-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-05-01' and dpvc_fecharegistro >= '2018-04-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-06-01' and dpvc_fecharegistro >= '2018-05-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-07-01' and dpvc_fecharegistro >= '2018-06-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-08-01' and dpvc_fecharegistro >= '2018-07-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-09-01' and dpvc_fecharegistro >= '2018-08-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-10-01' and dpvc_fecharegistro >= '2018-09-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-11-01' and dpvc_fecharegistro >= '2018-10-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2018-12-01' and dpvc_fecharegistro >= '2018-11-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-01-01' and dpvc_fecharegistro >= '2018-12-01' and cpvc_transaccionregistro is null;

update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-02-01' and dpvc_fecharegistro >= '2019-01-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-03-01' and dpvc_fecharegistro >= '2019-02-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-04-01' and dpvc_fecharegistro >= '2019-03-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-05-01' and dpvc_fecharegistro >= '2019-04-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-06-01' and dpvc_fecharegistro >= '2019-05-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-07-01' and dpvc_fecharegistro >= '2019-06-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-08-01' and dpvc_fecharegistro >= '2019-07-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-09-01' and dpvc_fecharegistro >= '2019-08-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-10-01' and dpvc_fecharegistro >= '2019-09-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2019-11-01' and dpvc_fecharegistro >= '2019-10-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where dpvc_fecharegistro < '2020-01-01' and dpvc_fecharegistro >= '2019-11-01' and cpvc_transaccionregistro is null;
update pedidoventacaracteristica_pvcp set cpvc_transaccionregistro = cpvc_documento where cpvc_transaccionregistro is null;

DROP INDEX ix_pedidoventacaracteristica_fecha;

update detallepedidoventa_dpvp set cdpv_transaccionregistro = cdpv_documento;

CREATE INDEX ix_documentorelacionexpediente_dexp_fecha
  ON documentorelacionexpediente_dexp  USING btree
  (ddex_fecharegistro);
  
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2016-01-01';
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-01-01' and ddex_fecharegistro >= '2016-01-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-02-01' and ddex_fecharegistro >= '2017-01-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-03-01' and ddex_fecharegistro >= '2017-02-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-04-01' and ddex_fecharegistro >= '2017-03-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-05-01' and ddex_fecharegistro >= '2017-04-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-06-01' and ddex_fecharegistro >= '2017-05-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-07-01' and ddex_fecharegistro >= '2017-06-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-08-01' and ddex_fecharegistro >= '2017-07-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-09-01' and ddex_fecharegistro >= '2017-08-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-10-01' and ddex_fecharegistro >= '2017-09-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-11-01' and ddex_fecharegistro >= '2017-10-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2017-12-01' and ddex_fecharegistro >= '2017-11-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-01-01' and ddex_fecharegistro >= '2017-12-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-02-01' and ddex_fecharegistro >= '2018-01-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-03-01' and ddex_fecharegistro >= '2018-02-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-04-01' and ddex_fecharegistro >= '2018-03-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-05-01' and ddex_fecharegistro >= '2018-04-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-06-01' and ddex_fecharegistro >= '2018-05-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-07-01' and ddex_fecharegistro >= '2018-06-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-08-01' and ddex_fecharegistro >= '2018-07-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-09-01' and ddex_fecharegistro >= '2018-08-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-10-01' and ddex_fecharegistro >= '2018-09-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-11-01' and ddex_fecharegistro >= '2018-10-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2018-12-01' and ddex_fecharegistro >= '2018-11-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-01-01' and ddex_fecharegistro >= '2018-12-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-02-01' and ddex_fecharegistro >= '2019-01-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-03-01' and ddex_fecharegistro >= '2019-02-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-04-01' and ddex_fecharegistro >= '2019-03-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-05-01' and ddex_fecharegistro >= '2019-04-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-06-01' and ddex_fecharegistro >= '2019-05-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-07-01' and ddex_fecharegistro >= '2019-06-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-08-01' and ddex_fecharegistro >= '2019-07-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-09-01' and ddex_fecharegistro >= '2019-08-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-10-01' and ddex_fecharegistro >= '2019-09-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-11-01' and ddex_fecharegistro >= '2019-10-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2019-12-01' and ddex_fecharegistro >= '2019-11-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where ddex_fecharegistro < '2020-01-01' and ddex_fecharegistro >= '2019-12-01' and cdex_transaccionregistro is null;
update documentorelacionexpediente_dexp set cdex_transaccionregistro = (select cpvc_transaccionregistro from pedidoventacaracteristica_pvcp where cpvc_llave =cdex_campomaestro) where cdex_transaccionregistro is null;

DROP INDEX ix_documentorelacionexpediente_dexp_fecha;

update detallecaracteristicaproducto_dcpp set cdcp_transaccionregistro = (select cdpv_documento from detallepedidoventa_dpvp where cdpv_llave = cdcp_entidad);

ALTER TABLE pedidoventacaracteristica_pvcp
	ALTER COLUMN cpvc_transaccionregistro SET NOT NULL;

ALTER TABLE detallepedidoventa_dpvp
	ALTER COLUMN cdpv_transaccionregistro set not NULL;

ALTER TABLE detallecaracteristicaproducto_dcpp
	ALTER COLUMN cdcp_transaccionregistro SET NOT NULL;
	
ALTER TABLE documentorelacionexpediente_dexp
	ALTER COLUMN cdex_transaccionregistro SET NOT NULL;

ALTER TABLE detallecaracteristicaproducto_dcpp
	DROP COLUMN ddcp_fecharegistro,
	DROP COLUMN ddcp_fechainactivo,
	DROP COLUMN cdcp_usuarioregistro,
	DROP COLUMN cdcp_usuarioinactivo;
	
INSERT INTO documentotransaccion_trap (ctra_llave, dtra_fecha, ctra_usuario, ctra_documento)
select 
replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
TO_TIMESTAMP(to_char(dpvc_fechainactivo, 'YYYY/MM/DD HH24:MI'), 'YYYY/MM/DD HH24:MI'),
cpvc_usuarioinactivo,
cpvc_documento
from pedidoventacaracteristica_pvcp where cpvc_usuarioinactivo is not null
group by cpvc_documento, cpvc_usuarioinactivo, to_char(dpvc_fechainactivo, 'YYYY/MM/DD HH24:MI');

CREATE INDEX ix_documentotransaccion_documento
  ON documentotransaccion_trap  USING btree
  (ctra_documento);
  
update pedidoventacaracteristica_pvcp set cpvc_transaccioninactivo = 
(select ctra_llave from documentotransaccion_trap where ctra_documento = cpvc_documento and TO_TIMESTAMP(to_char(dpvc_fechainactivo, 'YYYY/MM/DD HH24:MI'), 'YYYY/MM/DD HH24:MI')= dtra_fecha)
where cpvc_usuarioinactivo is not null;

DROP INDEX ix_documentotransaccion_documento;

ALTER TABLE pedidoventacaracteristica_pvcp
	DROP COLUMN dpvc_fecharegistro,
	DROP COLUMN dpvc_fechainactivo,
	DROP COLUMN cpvc_usuarioregistro,
	DROP COLUMN cpvc_usuarioinactivo;
	
ALTER TABLE detallepedidoventa_dpvp
	DROP COLUMN ddpv_fecharegistro,
	DROP COLUMN ddpv_fechainactivo,
	DROP COLUMN cdpv_usuarioregistro,
	DROP COLUMN cdpv_usuarioinactivo;

ALTER TABLE documentorelacionexpediente_dexp
	DROP COLUMN ddex_fecharegistro,
	DROP COLUMN ddex_fechainactivo,
	DROP COLUMN cdex_usuarioregistro,
	DROP COLUMN cdex_usuarioinactivo;
