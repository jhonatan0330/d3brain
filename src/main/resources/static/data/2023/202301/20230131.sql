COMMENT ON TABLE usuario_usrp IS '2023-01-31';

ALTER TABLE gpslocalizacion_gplp
   ADD COLUMN cgpl_documento character varying(32),
   ADD COLUMN cgpl_codigo character varying(32);