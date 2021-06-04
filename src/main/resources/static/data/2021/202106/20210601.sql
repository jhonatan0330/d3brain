COMMENT ON TABLE usuario_usrp IS '2021-06-01';

DELETE FROM webserviceejecucion_wsep;

ALTER TABLE webserviceejecucion_wsep
	ADD COLUMN cwse_usuario character varying(32) NOT NULL,
	ALTER COLUMN cwse_entrada TYPE character varying(4000) /* TYPE change - table: webserviceejecucion_wsep original: character varying(120000) new: character varying(4000) */,
	ALTER COLUMN cwse_salida TYPE character varying(4000) /* TYPE change - table: webserviceejecucion_wsep original: character varying(120000) new: character varying(4000) */;
