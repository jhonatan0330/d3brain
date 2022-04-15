COMMENT ON TABLE usuario_usrp IS '2022-04-14';

ALTER TABLE documentotransaccion_trap
	ADD COLUMN ctra_error character varying(4000),
	ADD COLUMN ctra_sincronize character varying(20);

ALTER TABLE mensaje_msjp
	ADD COLUMN cmsj_transaccion character varying(32);

ALTER TABLE webserviceejecucion_wsep
	ADD COLUMN dwse_fechaejecucion timestamp with time zone,
	ADD COLUMN cwse_transaccion character varying(32);
	
ALTER TABLE webserviceejecucion_wsep
	ADD COLUMN cwse_modificador character varying(32),
	ADD COLUMN cwse_parametros character varying(4000),
	ADD COLUMN cwse_extracciones character varying(4000);
	
ALTER TABLE webserviceejecucion_wsep ALTER COLUMN cwse_entrada DROP NOT NULL;

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_textoculto) 
	VALUES('PROP_198' , 'W', 'API ASYNCRONO AL FINALIZAR', 'API_ASYNCHRONOUS', 'REQUISITO', true, true);