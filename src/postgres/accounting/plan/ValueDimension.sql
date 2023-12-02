
CREATE TABLE valor_vlr (
	cvlr_llave varchar(32) NOT NULL,
	cvlr_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_valor_vlr PRIMARY KEY (cvlr_llave)
);

ALTER TABLE valor_vlr ADD dvlr_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE valor_vlr ADD cvlr_creacionusuario varchar(32);
ALTER TABLE valor_vlr ADD cvlr_creacionusuarionombre varchar(200);
ALTER TABLE valor_vlr ADD dvlr_modificacionfecha timestamptz;


ALTER TABLE valor_vlr ADD cvlr_dimension varchar(32) NOT NULL ;
ALTER TABLE valor_vlr ADD cvlr_valor varchar(100) NOT NULL ;
ALTER TABLE valor_vlr ADD cvlr_codigo varchar(100) NOT NULL ;
ALTER TABLE valor_vlr ADD cvlr_plantilla varchar(32) NOT NULL ;
