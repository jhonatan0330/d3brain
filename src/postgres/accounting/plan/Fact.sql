
CREATE TABLE hecho_hch (
	chch_llave varchar(32) NOT NULL,
	chch_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_hecho_hch PRIMARY KEY (chch_llave)
);

ALTER TABLE hecho_hch ADD dhch_fecharegistro timestamp with time zone NOT NULL ;
ALTER TABLE hecho_hch ADD dhch_fechaevento timestamp with time zone NOT NULL ;
ALTER TABLE hecho_hch ADD chch_dimension varchar(32) NOT NULL ;
ALTER TABLE hecho_hch ADD chch_valor varchar(100) NOT NULL ;
ALTER TABLE hecho_hch ADD chch_codigo varchar(100) NOT NULL ;
ALTER TABLE hecho_hch ADD chch_plantilla varchar(32) NOT NULL ;
ALTER TABLE hecho_hch ADD chch_id varchar(100) NOT NULL ;
