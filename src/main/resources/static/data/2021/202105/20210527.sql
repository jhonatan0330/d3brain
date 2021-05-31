COMMENT ON TABLE usuario_usrp IS '2021-05-27';

CREATE TABLE webservice_wbsp (
	cwbs_llave character varying(32) NOT NULL,
	cwbs_nombre character varying(50) NOT NULL,
	cwbs_template character varying(120000) NOT NULL,
	cwbs_servidor character varying(32) NOT NULL,
	cwbs_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE webserviceejecucion_wsep (
	cwse_llave character varying(32) NOT NULL,
	cwse_servicio character varying(32) NOT NULL,
	dwse_fecha timestamp with time zone NOT NULL,
	cwse_documento character varying(32) NOT NULL,
	cwse_entrada character varying(120000) NOT NULL,
	cwse_salida character varying(120000) NOT NULL,
	cwse_error character varying(4000),
	cwse_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE webservice_wbsp
	ADD CONSTRAINT pk_webservice_wbsp PRIMARY KEY (cwbs_llave);

ALTER TABLE webserviceejecucion_wsep
	ADD CONSTRAINT pk_webserviceejecucion_wsep PRIMARY KEY (cwse_llave);

ALTER TABLE webserviceejecucion_wsep
	ADD CONSTRAINT fk_webserviceejecuciondocumento FOREIGN KEY (cwse_documento) REFERENCES public.pedidoventa_pdvp(cpdv_llave);

ALTER TABLE webserviceejecucion_wsep
	ADD CONSTRAINT fk_webserviceejecucionservicio FOREIGN KEY (cwse_servicio) REFERENCES public.webservice_wbsp(cwbs_llave);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_169' , 'L', 'API', 'API', 'REQUISITO');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, bpvd_solicitamotivo, bpvd_textoculto) 
	VALUES('PROP_170' , 'W', 'API_HEADER', 'API_HEADER', 'REQUISITO', true, true, true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, bpvd_solicitamotivo, bpvd_textoculto) 
	VALUES('PROP_171' , 'W', 'API_NEW_DOCUMENT', 'API_NEW_DOCUMENT', 'REQUISITO', true, true, true);