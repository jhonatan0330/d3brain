COMMENT ON TABLE catalogo_ctg IS '2023-10-05';

CREATE TABLE valor_vlr (
	cvlr_llave varchar(32) NOT NULL,
	cvlr_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_valor_vlr PRIMARY KEY (cvlr_llave)
);

ALTER TABLE valor_vlr ADD cvlr_dimension varchar(32) NOT NULL;
ALTER TABLE valor_vlr ADD cvlr_valor varchar(100) NOT NULL;
ALTER TABLE valor_vlr ADD cvlr_codigo varchar(100) NOT NULL;
ALTER TABLE valor_vlr ADD cvlr_plantilla varchar(32) NOT NULL;

CREATE TABLE cuenta_cue (
	ccue_llave varchar(32) NOT NULL,
	ccue_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_cuenta_cue PRIMARY KEY (ccue_llave)
);

ALTER TABLE cuenta_cue ADD ccue_catalogo varchar(32) NOT NULL;
ALTER TABLE cuenta_cue ADD ccue_codigo varchar(100) NOT NULL;
ALTER TABLE cuenta_cue ADD ccue_nombre varchar(100) NOT NULL;
ALTER TABLE cuenta_cue ADD ccue_padre varchar(32);
ALTER TABLE cuenta_cue ADD ccue_plantilla varchar(32);
ALTER TABLE cuenta_cue ADD ccue_campo varchar(32);
ALTER TABLE cuenta_cue ADD ccue_naturaleza varchar(1) NOT NULL;


CREATE TABLE dimension_dim (
	cdim_llave varchar(32) NOT NULL,
	cdim_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_dimension_dim PRIMARY KEY (cdim_llave)
);

ALTER TABLE dimension_dim ADD cdim_cuenta varchar(32) NOT NULL;
ALTER TABLE dimension_dim ADD cdim_nombre varchar(100) NOT NULL;
ALTER TABLE dimension_dim ADD cdim_codigo varchar(100) NOT NULL;
ALTER TABLE dimension_dim ADD cdim_campo varchar(32) NOT NULL;
ALTER TABLE dimension_dim ADD cdim_tipo varchar(1) NOT NULL;


CREATE TABLE hecho_hch (
	chch_llave varchar(32) NOT NULL,
	chch_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_hecho_hch PRIMARY KEY (chch_llave)
);

ALTER TABLE hecho_hch ADD dhch_fecharegistro timestamp with time zone NOT NULL;
ALTER TABLE hecho_hch ADD dhch_fechaevento timestamp with time zone NOT NULL;
ALTER TABLE hecho_hch ADD chch_dimension varchar(32) NOT NULL;
ALTER TABLE hecho_hch ADD chch_valor varchar(100) NOT NULL;
ALTER TABLE hecho_hch ADD chch_codigo varchar(100) NOT NULL;
ALTER TABLE hecho_hch ADD chch_plantilla varchar(32) NOT NULL;
ALTER TABLE hecho_hch ADD chch_id varchar(100) NOT NULL;


CREATE TABLE linea_lin (
	clin_llave varchar(32) NOT NULL,
	clin_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_linea_lin PRIMARY KEY (clin_llave)
);

ALTER TABLE linea_lin ADD clin_formato varchar(32) NOT NULL;
ALTER TABLE linea_lin ADD clin_cuenta varchar(32) NOT NULL;
ALTER TABLE linea_lin ADD clin_description varchar(200);
ALTER TABLE linea_lin ADD clin_positivo varchar(100);
ALTER TABLE linea_lin ADD clin_negativo varchar(100);


CREATE TABLE formato_frm (
	cfrm_llave varchar(32) NOT NULL,
	cfrm_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_formato_frm PRIMARY KEY (cfrm_llave)
);

ALTER TABLE formato_frm ADD cfrm_catalogo varchar(32) NOT NULL;
ALTER TABLE formato_frm ADD cfrm_plantilla varchar(32) NOT NULL;
