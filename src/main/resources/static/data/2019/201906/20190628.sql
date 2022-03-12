

COMMENT ON TABLE usuario_usrp IS '2019-06-28';
COMMENT ON TABLE usuariosesion_ussp IS '2019.06.28.00';

ALTER TABLE categoriaproducto_cprp
	ADD COLUMN mcpr_cantidadmaxima numeric(16,2) DEFAULT 0 NOT NULL;
