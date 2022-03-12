
COMMENT ON TABLE usuario_usrp IS '2020-07-16';

ALTER TABLE usuarioorganizacion_uorp
	DROP COLUMN buor_funciones,
	ADD COLUMN cuor_tokenserver character varying(32) NOT NULL;

ALTER TABLE organizacion_orgp
	ALTER COLUMN corg_imagen DROP NOT NULL,
	ALTER COLUMN corg_slogan DROP NOT NULL;
