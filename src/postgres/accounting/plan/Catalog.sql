
CREATE TABLE account.catalogo_ctg (
	cctg_llave varchar(32) NOT NULL,
	cctg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_catalogo_ctg PRIMARY KEY (cctg_llave)
);

ALTER TABLE account.catalogo_ctg ADD cctg_nombre varchar(100) NOT NULL ;
ALTER TABLE account.catalogo_ctg ADD cctg_codigo varchar(20) NOT NULL ;
ALTER TABLE account.catalogo_ctg ADD dctg_fechainicial timestamp with time zone NOT NULL ;
ALTER TABLE account.catalogo_ctg ADD dctg_fechafinal timestamp with time zone NOT NULL ;
ALTER TABLE account.catalogo_ctg ADD cctg_documento varchar(32) NOT NULL ;
ALTER TABLE account.catalogo_ctg ADD cctg_plantila varchar(32) NOT NULL ;
