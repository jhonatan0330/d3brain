COMMENT ON TABLE usuario_usrp IS '2018-03-01';

ALTER TABLE reportebase_rpbp
	ADD COLUMN brpb_soloexistente boolean DEFAULT false NOT NULL;

