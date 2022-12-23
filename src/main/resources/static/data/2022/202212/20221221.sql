COMMENT ON TABLE usuario_usrp IS '2022-12-21';

ALTER TABLE pedidoventa_pdvp
	DROP CONSTRAINT fk_pedidoventatransaccion;

ALTER TABLE pedidoventacaracteristica_pvcp
	DROP CONSTRAINT fk_pedidoventacaracteristicatransaccionregistro;

ALTER TABLE detallepedidoventa_dpvp
	DROP CONSTRAINT fk_detallepedidoventatransaccionregistro;

ALTER TABLE documentorelacionexpediente_dexp
	DROP CONSTRAINT fk_documentorelacionexpedientetransaccioninactivo;

ALTER TABLE documentorelacionexpediente_dexp
	DROP CONSTRAINT fk_documentorelacionexpedientetransaccionregistro;

ALTER TABLE z_dex_documentorelacionexpediente
	DROP CONSTRAINT fk_z_dex_documentorelacionexpedientetransaccioninactivo;

ALTER TABLE z_dex_documentorelacionexpediente
	DROP CONSTRAINT fk_z_dex_documentorelacionexpedientetransaccionregistro;

ALTER TABLE z_pvc_pedidoventacaracteristica
	DROP CONSTRAINT fk_z_pvc_pedidoventacaracteristicatransaccionregistro;

DROP TABLE auditoria_audp;

CREATE TABLE z_dcp_detallecaracteristicaproducto (
	cdcp_llave character varying(32) NOT NULL,
	cdcp_entidad character varying(32) NOT NULL,
	cdcp_estado character varying(1) DEFAULT 'A'::character varying NOT NULL,
	ddcp_valorfecha timestamp with time zone,
	cdcp_valortext character varying(4000),
	mdcp_valornumero numeric(18,6) DEFAULT 0 NOT NULL,
	cdcp_valoropcion character varying(32),
	cdcp_campo character varying(32) NOT NULL,
	cdcp_transaccionregistro character varying(32) NOT NULL,
	cdcp_transaccioninactivo character varying(32)
);

CREATE TABLE z_dpv_detallepedidoventa (
	cdpv_llave character varying(32) NOT NULL,
	cdpv_producto character varying(32) NOT NULL,
	mdpv_cantidad numeric(18,6) DEFAULT 0 NOT NULL,
	mdpv_valorunitario numeric(18,6) DEFAULT 0 NOT NULL,
	mdpv_valorsubtotal numeric(18,6) DEFAULT 0 NOT NULL,
	mdpv_valortotal numeric(18,6) DEFAULT 0 NOT NULL,
	mdpv_cantidadtotal numeric(18,6) DEFAULT 0 NOT NULL,
	cdpv_estado character varying(1) DEFAULT 'A'::character varying NOT NULL,
	cdpv_productotercero character varying(32),
	ndpv_cantidadpromocion integer DEFAULT 0 NOT NULL,
	ndpv_cantidadpromocionbase integer DEFAULT 0 NOT NULL,
	mdpv_valorminimo numeric(18,6) DEFAULT 0 NOT NULL,
	mdpv_valormaximo numeric(18,6) DEFAULT 0 NOT NULL,
	cdpv_plantilla character varying(32) NOT NULL,
	cdpv_documento character varying(32) NOT NULL,
	cdpv_transaccionregistro character varying(32) NOT NULL,
	cdpv_transaccioninactivo character varying(32)
);

ALTER TABLE procesoestado_pesp
	ADD COLUMN cpes_codigo character varying(50);

ALTER TABLE relacioninterna_ritp
	ALTER COLUMN drit_fechainicio DROP NOT NULL;

ALTER TABLE transaccionerror_terp
	ALTER COLUMN cter_usuario SET NOT NULL;

ALTER TABLE webservice_wbsp
	ADD COLUMN cwbs_codigo character varying(50);

ALTER TABLE z_dcp_detallecaracteristicaproducto
	ADD CONSTRAINT pk_z_dcp_detallecaracteristicaproducto PRIMARY KEY (cdcp_llave);

ALTER TABLE z_dpv_detallepedidoventa
	ADD CONSTRAINT pk_z_dpv_detallepedidoventa PRIMARY KEY (cdpv_llave);

ALTER TABLE detallecaracteristicaproducto_dcpp
	ADD CONSTRAINT fk_z_dcp_detallecaracteristicaproductocampo FOREIGN KEY (cdcp_campo) REFERENCES productocaracteristica_pcrp(cpcr_llave);

ALTER TABLE detallecaracteristicaproducto_dcpp
	ADD CONSTRAINT fk_z_dcp_detallecaracteristicaproductovaloropcion FOREIGN KEY (cdcp_valoropcion) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE detallepedidoventa_dpvp
	ADD CONSTRAINT fk_z_dpv_detallepedidoventadocumento FOREIGN KEY (cdpv_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE detallepedidoventa_dpvp
	ADD CONSTRAINT fk_z_dpv_detallepedidoventaproducto FOREIGN KEY (cdpv_producto) REFERENCES producto_prop(cpro_llave);

ALTER TABLE detallepedidoventa_dpvp
	ADD CONSTRAINT fk_z_dpv_detallepedidoventaproductotercero FOREIGN KEY (cdpv_productotercero) REFERENCES usuariorolproducto_urpp(curp_llave);

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternacambiocreacion FOREIGN KEY (crit_cambiocreacion) REFERENCES cambio_cmbp(ccmb_llave);

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternacambioeliminacion FOREIGN KEY (crit_cambioeliminacion) REFERENCES cambio_cmbp(ccmb_llave);

CREATE OR REPLACE FUNCTION migrar_campos(_plantilla character varying, _fecha_maxima timestamp with time zone) RETURNS numeric
    LANGUAGE plpgsql
    AS '
declare 
	documentos character varying[];
	campos character varying[];
	items_documento character varying[];
	v_cnt numeric;
begin
	if
		(select count(*) from procesotransicion_ptrp where cptr_estado = ''A'' and cptr_estadopartida is null and cptr_plantilla = _plantilla) = 0
	then
		select array (
			select cpdv_llave from pedidoventa_pdvp 
				where cpdv_plantilla = _plantilla and dpdv_fecha < _fecha_maxima 
				and npdv_historico is null 
				limit 500) 
			into documentos;
	else
		select array (
			select cpdv_llave from pedidoventa_pdvp 
				where cpdv_plantilla = _plantilla and dpdv_fecha < _fecha_maxima 
				and npdv_historico is null and cpdv_estado != ''A''
				limit 500) 
			into documentos;
	end if;	
	select array (
		select cpvc_llave from pedidoventacaracteristica_pvcp 
			where cpvc_documento = any(documentos)) 
		into campos;
	select array (
		select cdpv_llave from detallepedidoventa_dpvp 
			where cdpv_documento = any(documentos))
		into items_documento;
	INSERT INTO z_pvc_pedidoventacaracteristica (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado) 
		select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado
	 		from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	INSERT INTO z_dex_documentorelacionexpediente (cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor)
		SELECT cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor
			FROM documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	INSERT INTO z_pvd_pedidoventadinero(cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha)
		SELECT cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha
			FROM pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	INSERT INTO z_drg_documentorelaciongestor (cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion)
		SELECT cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion
			FROM documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	INSERT INTO z_rej_reporteejecucion (crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado)
		SELECT crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado 
			FROM reporteejecucion_rejp where crej_documento = any(documentos);
	INSERT INTO z_dpv_detallepedidoventa (cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo)
		SELECT cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo
			FROM detallepedidoventa_dpvp where cdpv_llave = any(items_documento);
	INSERT INTO z_dcp_detallecaracteristicaproducto (cdcp_llave, cdcp_entidad, cdcp_estado, ddcp_valorfecha, cdcp_valortext, mdcp_valornumero, cdcp_valoropcion, cdcp_campo, cdcp_transaccionregistro, cdcp_transaccioninactivo)
		SELECT  cdcp_llave, cdcp_entidad, cdcp_estado, ddcp_valorfecha, cdcp_valortext, mdcp_valornumero, cdcp_valoropcion, cdcp_campo, cdcp_transaccionregistro, cdcp_transaccioninactivo
			FROM detallecaracteristicaproducto_dcpp where cdcp_entidad = any(items_documento);
	delete from detallecaracteristicaproducto_dcpp where cdcp_entidad = any(items_documento);
	delete from detallepedidoventa_dpvp where cdpv_llave = any(items_documento);
	delete from reporteejecucion_rejp where crej_documento = any(documentos);
	delete from documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	delete from pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	delete from documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	delete from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	update pedidoventa_pdvp set npdv_historico = 3 where cpdv_llave = any(documentos);
	GET DIAGNOSTICS v_cnt = ROW_COUNT;
	return v_cnt;
END; ';
