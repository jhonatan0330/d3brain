COMMENT ON TABLE usuario_usrp IS '2019-07-20';

COMMENT ON TABLE usuariosesion_ussp IS '2019.07.20.00';

ALTER TABLE requerimiento_reqp
	DROP COLUMN creq_id,
	ADD COLUMN creq_key character varying(32) NOT NULL;