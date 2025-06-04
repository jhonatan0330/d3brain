
CREATE TABLE account.periodotiempo_ptm (
	cptm_llave varchar(32) NOT NULL,
	cptm_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_periodotiempo_ptm PRIMARY KEY (cptm_llave)
);

ALTER TABLE account.periodotiempo_ptm ADD nptm_nivel int NOT NULL DEFAULT 0;
ALTER TABLE account.periodotiempo_ptm ADD dptm_fechainicio timestamp with time zone NOT NULL ;
ALTER TABLE account.periodotiempo_ptm ADD dptm_fechafin timestamp with time zone NOT NULL ;
ALTER TABLE account.periodotiempo_ptm ADD cptm_codigo varchar(100) NOT NULL ;
ALTER TABLE account.periodotiempo_ptm ADD nptm_ano int;
ALTER TABLE account.periodotiempo_ptm ADD nptm_mes int;
ALTER TABLE account.periodotiempo_ptm ADD nptm_dia int;
ALTER TABLE account.periodotiempo_ptm ADD nptm_hora int;
ALTER TABLE account.periodotiempo_ptm ADD nptm_minuto int;
