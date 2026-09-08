COMMENT ON TABLE usuario_usrp IS '2026-09-04';
-- 2026-09-04
-- Tabla de indicadores de comportamiento de la plataforma.

CREATE TABLE account.indicador_ind (
	cind_llave varchar(32) NOT NULL,
	cind_nombre varchar(50) NOT NULL,
	cind_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	cind_codigo varchar(50) NULL,
	cind_proceso varchar(32) NULL,
	cind_imagen varchar(2000) NULL,
	CONSTRAINT pk_indicador_ind PRIMARY KEY (cind_llave)
);