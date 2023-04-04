--Valida que no existan 2 modulos iguales para un usaurio
ALTER TABLE permiso_perp
  ADD CONSTRAINT uk_permiso_rolaccesomodulo UNIQUE(cper_rolacceso , cper_modulo);
--Validacion de roles que no se repitan de un mismo usuario
--ALTER TABLE usuariorol_erlp
--  ADD CONSTRAINT uk_usuariorol_rolacceso_usuario UNIQUE(cerl_rolacceso, cerl_usuario);
--Validar preguntas y grupos de los cdigos de encuesta
ALTER TABLE encuestagrupo_egrp
  ADD CONSTRAINT uk_encuestagrupo_codigo_encuesta UNIQUE(cegr_codigo, cegr_encuesta);
ALTER TABLE encuestapregunta_eprp 
  ADD CONSTRAINT uk_encuestapregunta_codigo_grupo UNIQUE(cepr_codigo, cepr_grupo);

ALTER TABLE documentoplantillacaracteristica_dpcp    
  ADD CONSTRAINT uk_documentoplantillacaracteristica_plantillacodigo UNIQUE(cdpc_plantilla , cdpc_codigo);
ALTER TABLE productocaracteristica_pcrp    
  ADD CONSTRAINT uk_productocaracteristica_basecodigo UNIQUE(cpcr_base , cpcr_codigo);
  
CREATE INDEX ix_pedidoventacaracteristica_documento
  ON pedidoventacaracteristica_pvcp
  USING btree
  (cpvc_documento);
  
CREATE INDEX ix_pedidoventadinero_documento
  ON pedidoventadinero_pvdp
  USING btree
  (cpvd_documento);
  
CREATE INDEX ix_documentorelacionexpediente_campomaestro
  ON documentorelacionexpediente_dexp
  USING btree
  (cdex_campomaestro);

CREATE INDEX ix_pedidoventacaracteristica_valoropcion
	ON pedidoventacaracteristica_pvcp
	  USING btree
	  (cpvc_valoropcion);
	  
CREATE INDEX ix_documentorelaciongestor_documentoprincipal
	ON documentorelaciongestor_drgp
  USING btree
  (cdrg_documentoprincipal);
  
CREATE INDEX ix_pedidoventa_nombre
	ON pedidoventa_pdvp
  USING btree
  (cpdv_nombre);

CREATE INDEX ix_pedidoventa_plantillafecha
	ON pedidoventa_pdvp
  USING btree
  (cpdv_plantilla, dpdv_fecha);

CREATE INDEX ix_propiedad_ppdp_campoestado 
  ON propiedad_ppdp USING btree (cppd_campo, cppd_estado);
  
ALTER TABLE pedidoventacaracteristica_pvcp ALTER COLUMN mpvc_valornumero DROP NOT NULL;
ALTER TABLE pedidoventacaracteristica_pvcp ALTER COLUMN mpvc_valornumero DROP DEFAULT;

ALTER TABLE pedidoventa_pdvp ALTER COLUMN npdv_historico DROP NOT NULL;
ALTER TABLE pedidoventa_pdvp ALTER COLUMN npdv_historico DROP DEFAULT;

CREATE TABLE z_pvc_pedidoventacaracteristica (
	cpvc_llave varchar(32) NOT NULL,
	cpvc_documento varchar(32) NOT NULL,
	cpvc_campo varchar(32) NOT NULL,
	cpvc_valortext varchar(4000) NOT NULL,
	dpvc_valorfecha timestamptz NULL,
	cpvc_valoropcion varchar(32) NULL,
	cpvc_valorauxiliar varchar(32) NULL,
	mpvc_valornumero numeric(18,6) NULL,
	cpvc_transaccionregistro varchar(32) NOT NULL,
	cpvc_transaccioninactivo varchar(32) NULL,
	cpvc_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_z_pvc_pedidoventacaracteristica PRIMARY KEY (cpvc_llave)
);
CREATE INDEX ix_z_pvc_pedidoventacaracteristica_documento ON z_pvc_pedidoventacaracteristica USING btree (cpvc_documento);
CREATE INDEX ix_z_pvc_pedidoventacaracteristica_valoropcion ON z_pvc_pedidoventacaracteristica USING btree (cpvc_valoropcion);


ALTER TABLE z_pvc_pedidoventacaracteristica ADD CONSTRAINT fk_z_pvc_pedidoventacaracteristicacampo FOREIGN KEY (cpvc_campo) REFERENCES documentoplantillacaracteristica_dpcp(cdpc_llave);
ALTER TABLE z_pvc_pedidoventacaracteristica ADD CONSTRAINT fk_z_pvc_pedidoventacaracteristicadocumento FOREIGN KEY (cpvc_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);


CREATE TABLE z_dex_documentorelacionexpediente (
	cdex_llave varchar(32) NOT NULL,
	cdex_campomaestro varchar(32) NOT NULL,
	cdex_expedientedetalle varchar(32) NOT NULL,
	cdex_transaccionregistro varchar(32) NOT NULL,
	cdex_transaccioninactivo varchar(32) NULL,
	cdex_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	mdex_valor numeric(18,6) NOT NULL DEFAULT 0,
	CONSTRAINT pk_z_dex_documentorelacionexpediente PRIMARY KEY (cdex_llave)
);

CREATE INDEX ix_z_dex_documentorelacionexpediente_campomaestro ON z_dex_documentorelacionexpediente USING btree (cdex_campomaestro);

ALTER TABLE z_dex_documentorelacionexpediente ADD CONSTRAINT fk_z_dex_documentorelacionexpedientecampomaestro FOREIGN KEY (cdex_campomaestro) REFERENCES z_pvc_pedidoventacaracteristica(cpvc_llave);
ALTER TABLE z_dex_documentorelacionexpediente ADD CONSTRAINT fk_z_dex_documentorelacionexpedienteexpedientedetalle FOREIGN KEY (cdex_expedientedetalle) REFERENCES pedidoventa_pdvp(cpdv_llave);


CREATE TABLE z_pvd_pedidoventadinero (
	cpvd_llave varchar(32) NOT NULL,
	cpvd_documento varchar(32) NOT NULL,
	dpvd_fecha timestamptz NOT NULL,
	mpvd_valortotal numeric(18,6) NOT NULL DEFAULT 0,
	mpvd_saldo numeric(18,6) NOT NULL DEFAULT 0,
	bpvc_controlarsaldo boolean NOT NULL DEFAULT false,
	cpvd_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_z_pvd_pedidoventadinero_pvdp PRIMARY KEY (cpvd_llave),
	CONSTRAINT fk_z_pvd_pedidoventadinerodocumento FOREIGN KEY (cpvd_documento) REFERENCES pedidoventa_pdvp(cpdv_llave)
);
CREATE INDEX ix_z_pvd_pedidoventadinero_documento ON z_pvd_pedidoventadinero USING btree (cpvd_documento);

CREATE TABLE Z_drg_documentorelaciongestor (
	cdrg_llave varchar(32) NOT NULL,
	cdrg_documentoprincipal varchar(32) NOT NULL,
	cdrg_documentomodificador varchar(32) NULL,
	cdrg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	ddrg_fecha timestamptz NOT NULL,
	cdrg_estadoinicial varchar(32) NULL,
	cdrg_estadofinal varchar(32) NULL,
	cdrg_ubicacion varchar(32) NULL,
	cdrg_valores varchar(32) NULL,
	cdrg_usuario varchar(32) NOT NULL,
	ddrg_cierre timestamptz NULL,
	cdrg_nombre varchar(100) NOT NULL,
	cdrg_transaccion varchar(32) NULL,
	CONSTRAINT pk_z_drg_documentorelaciongestor_drgp PRIMARY KEY (cdrg_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestordocumentomodificador FOREIGN KEY (cdrg_documentomodificador) REFERENCES pedidoventa_pdvp(cpdv_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestordocumentoprincipal FOREIGN KEY (cdrg_documentoprincipal) REFERENCES pedidoventa_pdvp(cpdv_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorestadofinal FOREIGN KEY (cdrg_estadofinal) REFERENCES procesoestado_pesp(cpes_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorestadoinicial FOREIGN KEY (cdrg_estadoinicial) REFERENCES procesoestado_pesp(cpes_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorusuario FOREIGN KEY (cdrg_usuario) REFERENCES usuario_usrp(cusr_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorvalores FOREIGN KEY (cdrg_valores) REFERENCES z_pvd_pedidoventadinero(cpvd_llave)
);
CREATE INDEX ix_z_drg_documentorelaciongestor_documentoprincipal ON Z_drg_documentorelaciongestor USING btree (cdrg_documentoprincipal);

CREATE TABLE z_rej_reporteejecucion (
	crej_llave varchar(32) NOT NULL,
	crej_reporte varchar(32) NOT NULL,
	crej_documento varchar(32) NULL,
	drej_fechainicio timestamptz NOT NULL,
	drej_fechafin timestamptz NOT NULL,
	crej_error varchar(4000) NULL,
	crej_usuario varchar(32) NULL,
	crej_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_z_rej_reporteejecucion_rejp PRIMARY KEY (crej_llave),
	CONSTRAINT fk_z_rej_reporteejecuciondocumento FOREIGN KEY (crej_documento) REFERENCES pedidoventa_pdvp(cpdv_llave),
	CONSTRAINT fk_z_rej_reporteejecucionreporte FOREIGN KEY (crej_reporte) REFERENCES reportebase_rpbp(crpb_llave)
);

CREATE TABLE z_dpv_detallepedidoventa (
	cdpv_llave varchar(32) NOT NULL,
	cdpv_producto varchar(32) NOT NULL,
	cdpv_nombre character varying(200),
    cdpv_campo character varying(32),
	mdpv_cantidad numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_valorunitario numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_valorsubtotal numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_valortotal numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_cantidadtotal numeric(18, 6) NOT NULL DEFAULT 0,
	cdpv_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	cdpv_productotercero varchar(32) NULL,
	ndpv_cantidadpromocion int4 NOT NULL DEFAULT 0,
	ndpv_cantidadpromocionbase int4 NOT NULL DEFAULT 0,
	mdpv_valorminimo numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_valormaximo numeric(18, 6) NOT NULL DEFAULT 0,
	cdpv_plantilla varchar(32) NOT NULL,
	cdpv_documento varchar(32) NOT NULL,
	cdpv_transaccionregistro varchar(32) NOT NULL,
	cdpv_transaccioninactivo varchar(32) NULL,
	CONSTRAINT pk_z_dpv_detallepedidoventa PRIMARY KEY (cdpv_llave)
);

ALTER TABLE detallepedidoventa_dpvp ADD CONSTRAINT fk_z_dpv_detallepedidoventadocumento FOREIGN KEY (cdpv_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);
ALTER TABLE detallepedidoventa_dpvp ADD CONSTRAINT fk_z_dpv_detallepedidoventaproducto FOREIGN KEY (cdpv_producto) REFERENCES producto_prop(cpro_llave);
ALTER TABLE detallepedidoventa_dpvp ADD CONSTRAINT fk_z_dpv_detallepedidoventaproductotercero FOREIGN KEY (cdpv_productotercero) REFERENCES usuariorolproducto_urpp(curp_llave);

CREATE TABLE z_dcp_detallecaracteristicaproducto (
	cdcp_llave varchar(32) NOT NULL,
	cdcp_entidad varchar(32) NOT NULL,
	cdcp_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	ddcp_valorfecha timestamptz NULL,
	cdcp_valortext varchar(4000) NULL,
	mdcp_valornumero numeric(18, 6) NOT NULL DEFAULT 0,
	cdcp_valoropcion varchar(32) NULL,
	cdcp_campo varchar(32) NOT NULL,
	cdcp_transaccionregistro varchar(32) NOT NULL,
	cdcp_transaccioninactivo varchar(32) NULL,
	CONSTRAINT pk_z_dcp_detallecaracteristicaproducto PRIMARY KEY (cdcp_llave)
);

CREATE INDEX IF NOT EXISTS ix_procesotransicionautomatica_ejecucion ON procesotransicionautomatica_ptap USING btree (dpta_ejecucion);
CREATE INDEX IF NOT EXISTS ix_procesotransicionautomatica_transicion ON procesotransicionautomatica_ptap USING btree (cpta_transicion);