
COMMENT ON TABLE usuario_usrp IS '2019-08-12';

COMMENT ON TABLE usuariosesion_ussp IS '2019.08.10.00';

ALTER TABLE categoriaproducto_cprp
	ADD COLUMN ccpr_nodosuperior character varying(32);
