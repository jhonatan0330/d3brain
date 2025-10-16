
CREATE TABLE tarifario_trfp (
	ctrf_llave varchar(32) NOT NULL,
	ctrf_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_tarifario_trfp PRIMARY KEY (ctrf_llave)
);

ALTER TABLE tarifario_trfp ADD ctrf_nombre varchar(32) NOT NULL ;
ALTER TABLE tarifario_trfp ADD dtrf_fechainicial timestamp with time zone NOT NULL ;
ALTER TABLE tarifario_trfp ADD dtrf_fechafinal timestamp with time zone;
ALTER TABLE tarifario_trfp ADD ctrf_documento varchar(32) NOT NULL ;
