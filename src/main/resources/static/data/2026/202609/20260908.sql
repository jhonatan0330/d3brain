COMMENT ON TABLE usuario_usrp IS '2026-09-08';

ALTER TABLE propiedadvalordefinido_pvdp
  ADD COLUMN cpvd_uso_motivo VARCHAR(200),
  ADD COLUMN cpvd_imagen  varchar(2000),
  ADD COLUMN cpvd_uso_relaciones  varchar(200);

update propiedadvalordefinido_pvdp
set cpvd_uso_motivo = 'MOTIVO DE USO'
where bpvd_solicitamotivo = true;

update propiedadvalordefinido_pvdp
set cpvd_uso_motivo = 'NOMBRE DEL TEMPORIZADOR'
where cpvd_llave in ('PROP_140','PROP_189');

update propiedadvalordefinido_pvdp
set cpvd_uso_motivo = 'VALOR DEL HEADER'
where cpvd_llave in ('PROP_170');

ALTER TABLE propiedadvalordefinido_pvdp
  DROP COLUMN IF EXISTS bpvd_solicitamotivo;
  
  
CREATE TABLE historic.z_ter_transaccionerror (
	cter_llave varchar(32) NOT NULL,
	dter_fechainicio timestamptz NOT NULL,
	dter_fechafin timestamptz NOT NULL,
	cter_error varchar(4000) NULL,
	cter_usuario varchar(32) NOT NULL,
	cter_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	cter_entrada varchar(2000) NULL,
	CONSTRAINT pk_z_ter_transaccionerror PRIMARY KEY (cter_llave)
);


CREATE TABLE historic.z_pta_procesotransicionautomatica (
	cpta_llave varchar(32) NOT NULL,
	dpta_fecha timestamptz NOT NULL,
	cpta_transicion varchar(32) NULL,
	cpta_propiedad varchar(32) NOT NULL,
	dpta_ejecucion timestamptz NULL,
	cpta_mensaje varchar(4000) NOT NULL,
	cpta_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	CONSTRAINT pk_z_procesotransicionautomatica PRIMARY KEY (cpta_llave),
	CONSTRAINT fk_z_procesotransicionautomaticapropiedad FOREIGN KEY (cpta_propiedad) REFERENCES public.propiedad_ppdp(cppd_llave),
	CONSTRAINT fk_z_procesotransicionautomaticatransicion FOREIGN KEY (cpta_transicion) REFERENCES public.procesotransicion_ptrp(cptr_llave)
);


CREATE TABLE historic.z_tlg_transaccionlog (
	ctlg_llave varchar(32) NOT NULL,
	dtlg_fechainicio timestamptz NOT NULL,
	dtlg_fechafin timestamptz NOT NULL,
	ctlg_transaccion varchar(32) NULL,
	ctlg_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	ctlg_sesion varchar(100) NULL,
	ctlg_usuario varchar(32) NULL,
	ctlg_entrada varchar(2000) NULL,
	ctlg_salida varchar(2000) NULL,
	CONSTRAINT pk_z_transaccionlog PRIMARY KEY (ctlg_llave)
);

CREATE TABLE historic.z_uss_usuariosesion (
	cuss_llave varchar(32) NOT NULL,
	cuss_usuario varchar(32) NOT NULL,
	duss_fecha timestamptz NOT NULL,
	duss_fechacierre timestamptz NULL,
	cuss_ip varchar(100) NULL,
	cuss_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	buss_privada bool DEFAULT false NOT NULL,
	CONSTRAINT pk_z_usuariosesion PRIMARY KEY (cuss_llave),
	CONSTRAINT fk_z_usuariosesionusuario FOREIGN KEY (cuss_usuario) REFERENCES public.usuario_usrp(cusr_llave)
);

CREATE TABLE historic.z_car_cargaarchivo (
	ccar_llave varchar(32) NOT NULL,
	ccar_servidor varchar(32) NULL,
	ncar_size int4 DEFAULT 0 NOT NULL,
	ccar_url varchar(4000) NULL,
	dcar_fechainicio timestamptz NOT NULL,
	dcar_fechafin timestamptz NOT NULL,
	ccar_error varchar(4000) NULL,
	ccar_usuario varchar(32) NULL,
	ccar_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	CONSTRAINT pk_z_cargaarchivo PRIMARY KEY (ccar_llave),
	CONSTRAINT fk_z_cargaarchivoservidor FOREIGN KEY (ccar_servidor) REFERENCES public.servidor_serp(cser_llave),
	CONSTRAINT fk_z_cargaarchivousuario FOREIGN KEY (ccar_usuario) REFERENCES public.usuario_usrp(cusr_llave)
);


CREATE TABLE historic.z_wse_webserviceejecucion (
	cwse_llave varchar(32) NOT NULL,
	cwse_servicio varchar(32) NOT NULL,
	cwse_usuario varchar(32) NOT NULL,
	dwse_fecha timestamptz NOT NULL,
	cwse_documento varchar(32) NOT NULL,
	cwse_modificador varchar(32) NULL,
	cwse_transaccion varchar(32) NULL,
	cwse_parametros varchar(4000) NULL,
	dwse_fechaejecucion timestamptz NULL,
	cwse_entrada varchar(2000) NULL,
	cwse_salida varchar(2000) NULL,
	cwse_error varchar(4000) NULL,
	cwse_masivo varchar(2000) NULL,
	cwse_extracciones varchar(4000) NULL,
	cwse_sincrona varchar(1) NULL,
	cwse_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	CONSTRAINT pk_z_webserviceejecucion PRIMARY KEY (cwse_llave),
	CONSTRAINT fk_z_webserviceejecuciondocumento FOREIGN KEY (cwse_documento) REFERENCES public.pedidoventa_pdvp(cpdv_llave),
	CONSTRAINT fk_z_webserviceejecucionservicio FOREIGN KEY (cwse_servicio) REFERENCES public.webservice_wbsp(cwbs_llave)
);
CREATE INDEX ix_z_webserviceejecucion_documento ON historic.z_wse_webserviceejecucion USING btree (cwse_documento);
CREATE INDEX ix_z_webserviceejecucion_fecha ON historic.z_wse_webserviceejecucion USING btree (dwse_fecha);


CREATE TABLE historic.z_msj_mensaje (
	cmsj_llave varchar(32) NOT NULL,
	dmsj_fecha timestamptz NOT NULL,
	cmsj_titulo varchar(200) NOT NULL,
	cmsj_usuario varchar(32) NULL,
	cmsj_documento varchar(32) NOT NULL,
	cmsj_template varchar(32) NOT NULL,
	cmsj_parametros varchar(4000) NOT NULL,
	dmsj_leido timestamptz NULL,
	dmsj_correoenviado timestamptz NULL,
	cmsj_correoerror varchar(4000) NULL,
	cmsj_correo varchar(2000) NULL,
	cmsj_reporte varchar(32) NULL,
	cmsj_transaccion varchar(32) NULL,
	cmsj_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	cmsj_adjuntourl varchar(2000) NULL,
	CONSTRAINT pk_z_mensaje_msjp PRIMARY KEY (cmsj_llave),
	CONSTRAINT fk_z_mensajetemplate FOREIGN KEY (cmsj_template) REFERENCES public.mensajeplantillacorreo_mplp(cmpl_llave),
	CONSTRAINT fk_z_mensajeusuario FOREIGN KEY (cmsj_usuario) REFERENCES public.usuario_usrp(cusr_llave)
);
CREATE INDEX ix_z_mensaje_documento ON historic.z_msj_mensaje USING btree (cmsj_documento);

DROP TABLE if exists config.configtemplaterelation_ctr;

DROP schema if exists config;

DROP table if exists learning.article_art;

DROP schema if exists learning;