
COMMENT ON TABLE usuario_usrp IS '2018-12-04';
COMMENT ON TABLE usuariosesion_ussp IS '2018.12.04.00';

ALTER TABLE documentoplantillarol_dprp
	ADD COLUMN bdpr_eliminar boolean DEFAULT false NOT NULL;
