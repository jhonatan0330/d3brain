COMMENT ON TABLE usuario_usrp IS '2020-12-17';
COMMENT ON TABLE usuariosesion_ussp IS '2020.12.17.00';

delete from postcalificacion_pclp;
delete from postrespuesta_prsp ;
delete from postpregunta_pprp;

ALTER TABLE postpregunta_pprp
	DROP COLUMN cppr_keywords,
	ADD COLUMN cppr_campo character varying(32) NOT NULL,
	ADD COLUMN cppr_tipo character varying(1) NOT NULL;


