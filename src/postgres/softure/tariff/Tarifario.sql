
CREATE TABLE tarifario_trfp (
	ctrf_llave varchar(32) NOT NULL,
	ctrf_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_tarifario_trfp PRIMARY KEY (ctrf_llave)
);

ALTER TABLE tarifario_trfp ADD ctrf_nombre varchar(32) NOT NULL ;
ALTER TABLE tarifario_trfp ADD ctrf_tiporecurso varchar(32);
ALTER TABLE tarifario_trfp ADD ctrf_tiporecursonombre varchar(100);
ALTER TABLE tarifario_trfp ADD ctrf_tipodimension2 varchar(32);
ALTER TABLE tarifario_trfp ADD ctrf_tipodimension2nombre varchar(100);
ALTER TABLE tarifario_trfp ADD ctrf_tipodimension3 varchar(32);
ALTER TABLE tarifario_trfp ADD ctrf_tipodimension3nombre varchar(100);
ALTER TABLE tarifario_trfp ADD ctrf_tipodimension4 varchar(32);
ALTER TABLE tarifario_trfp ADD ctrf_tipodimension4nombre varchar(100);
ALTER TABLE tarifario_trfp ADD btrf_productoopcional bool NOT NULL DEFAULT false;
ALTER TABLE tarifario_trfp ADD btrf_rangovalores bool NOT NULL DEFAULT false;
ALTER TABLE tarifario_trfp ADD btrf_rangocantidad bool NOT NULL DEFAULT false;
ALTER TABLE tarifario_trfp ADD dtrf_fechainicial timestamp with time zone NOT NULL ;
ALTER TABLE tarifario_trfp ADD dtrf_fechafinal timestamp with time zone;
ALTER TABLE tarifario_trfp ADD ctrf_documento varchar(32) NOT NULL ;
