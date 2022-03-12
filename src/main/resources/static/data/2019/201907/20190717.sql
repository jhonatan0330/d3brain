COMMENT ON TABLE usuario_usrp IS '2019-07-17';
COMMENT ON TABLE usuariosesion_ussp IS '2019.07.17.00';

ALTER TABLE mensajeprocesotransicion_mptp
	ALTER COLUMN cmpt_host DROP NOT NULL,
	ALTER COLUMN cmpt_usuario DROP NOT NULL,
	ALTER COLUMN cmpt_clave DROP NOT NULL;

ALTER TABLE usuario_usrp
	ADD COLUMN cusr_correo character varying(50);

