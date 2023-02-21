COMMENT ON TABLE usuario_usrp IS '2023-02-20';

ALTER TABLE gpslocalizacion_gplp ADD dgpl_fechareporte timestamptz;

update gpslocalizacion_gplp set dgpl_fechareporte = now();

ALTER TABLE gpslocalizacion_gplp ALTER dgpl_fechareporte SET NOT NULL;