
CREATE TABLE catalogo_ctg (
	cctg_llave varchar(32) NOT NULL,
	cctg_nombre varchar(100) NOT NULL,
	dctg_fechainicial timestamptz NOT NULL,
	dctg_fechafinal timestamptz NOT NULL,
	cctg_codigo varchar(8) NOT NULL,
	cctg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_catalogo_ctg PRIMARY KEY (cctg_llave)
);

COMMENT ON TABLE catalogo_ctg IS '2023-10-01';