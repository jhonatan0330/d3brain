COMMENT ON TABLE usuario_usrp IS '2022-03-21';

ALTER TABLE webserviceejecucion_wsep
	ADD COLUMN cwse_masivo character varying(2000),
	ALTER COLUMN cwse_entrada TYPE character varying(2000) /* TYPE change - table: webserviceejecucion_wsep original: character varying(4000) new: character varying(2000) */,
	ALTER COLUMN cwse_salida TYPE character varying(2000) /* TYPE change - table: webserviceejecucion_wsep original: character varying(4000) new: character varying(2000) */;

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_197' , 'L', 'PLANTILLA CARGA MASIVA MULTIPLE', 'PLANTILLA_CARGA_MASIVA_MULTIPLE', 'PERMISOS');