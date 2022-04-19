COMMENT ON TABLE usuario_usrp IS '2022-04-19';

ALTER TABLE webserviceejecucion_wsep 
	ADD COLUMN IF NOT EXISTS cwse_sincrona varchar(1) NULL;
