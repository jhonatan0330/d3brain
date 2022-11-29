COMMENT ON TABLE usuario_usrp IS '2022-11-29';

ALTER TABLE mensajeplantillacorreo_mplp
	ALTER COLUMN cmpl_servidor DROP NOT NULL;

update mensajeplantillacorreo_mplp set cmpl_servidor = null;