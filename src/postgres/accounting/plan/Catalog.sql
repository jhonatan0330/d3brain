
CREATE TABLE catalogo_ctg (
	cctg_llave varchar(32) NOT NULL,
	cctg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_catalogo_ctg PRIMARY KEY (cctg_llave)
);

ALTER TABLE catalogo_ctg ADD dctg_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE catalogo_ctg ADD cctg_creacionusuario varchar(32);
ALTER TABLE catalogo_ctg ADD cctg_creacionusuarionombre varchar(200);
ALTER TABLE catalogo_ctg ADD dctg_modificacionfecha timestamptz;


ALTER TABLE catalogo_ctg ADD cctg_nombre varchar(100) NOT NULL ;
ALTER TABLE catalogo_ctg ADD cctg_codigo varchar(8) NOT NULL ;
ALTER TABLE catalogo_ctg ADD dctg_fechainicial timestamp with time zone NOT NULL ;
ALTER TABLE catalogo_ctg ADD dctg_fechafinal timestamp with time zone NOT NULL ;
ALTER TABLE catalogo_ctg ADD cctg_consecutivo varchar(32);
