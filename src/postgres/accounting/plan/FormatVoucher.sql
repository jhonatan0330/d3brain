
CREATE TABLE formato_frm (
	cfrm_llave varchar(32) NOT NULL,
	cfrm_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_formato_frm PRIMARY KEY (cfrm_llave)
);

ALTER TABLE formato_frm ADD cfrm_catalogo varchar(32) NOT NULL ;
ALTER TABLE formato_frm ADD cfrm_plantilla varchar(32) NOT NULL ;
