
CREATE TABLE linea_lin (
	clin_llave varchar(32) NOT NULL,
	clin_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_linea_lin PRIMARY KEY (clin_llave)
);

ALTER TABLE linea_lin ADD dlin_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE linea_lin ADD clin_creacionusuario varchar(32);
ALTER TABLE linea_lin ADD clin_creacionusuarionombre varchar(200);
ALTER TABLE linea_lin ADD dlin_modificacionfecha timestamptz;


ALTER TABLE linea_lin ADD clin_formato varchar(32) NOT NULL ;
ALTER TABLE linea_lin ADD clin_cuenta varchar(32) NOT NULL ;
ALTER TABLE linea_lin ADD clin_description varchar(200);
ALTER TABLE linea_lin ADD clin_positivo varchar(100);
ALTER TABLE linea_lin ADD clin_negativo varchar(100);
