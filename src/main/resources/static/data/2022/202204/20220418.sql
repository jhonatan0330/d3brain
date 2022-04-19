COMMENT ON TABLE usuario_usrp IS '2022-04-18';

ALTER TABLE transaccionerror_terp
	ADD COLUMN cter_usuario character varying(32);

ALTER TABLE webserviceejecucion_wsep 
	ADD COLUMN IF NOT EXISTS cwse_sincrona varchar(1) NULL;
