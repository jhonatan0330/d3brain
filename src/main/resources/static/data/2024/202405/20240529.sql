COMMENT ON TABLE usuario_usrp IS '2024-05-29';

DROP INDEX account.ix_maparesultados_tipo_cuenta;


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

ALTER TABLE account.periodotiempo_ptm ADD dptm_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.periodotiempo_ptm ADD cptm_creacionusuario varchar(32);
ALTER TABLE account.periodotiempo_ptm ADD cptm_creacionusuarionombre varchar(200);
ALTER TABLE account.periodotiempo_ptm ADD dptm_modificacionfecha timestamptz;

DROP TABLE account.mapa_resultados_rmp;

CREATE TABLE account.maparesultados_rmp (
	crmp_llave varchar(32) NOT NULL,
	crmp_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_maparesultados_rmp PRIMARY KEY (crmp_llave)
);

ALTER TABLE account.maparesultados_rmp ADD drmp_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.maparesultados_rmp ADD crmp_creacionusuario varchar(32);
ALTER TABLE account.maparesultados_rmp ADD crmp_creacionusuarionombre varchar(200);
ALTER TABLE account.maparesultados_rmp ADD drmp_modificacionfecha timestamptz;

ALTER TABLE account.maparesultados_rmp ADD crmp_cuenta varchar(32) NOT NULL ;
ALTER TABLE account.maparesultados_rmp ADD crmp_periodo varchar(32) NOT NULL ;
ALTER TABLE account.maparesultados_rmp ADD nrmp_cantidad int NOT NULL DEFAULT 0;
ALTER TABLE account.maparesultados_rmp ADD nrmp_promedio NUMERIC(10,2) NOT NULL DEFAULT 0;
ALTER TABLE account.maparesultados_rmp ADD mrmp_saldoanterior NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.maparesultados_rmp ADD mrmp_saldosiguiente NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.maparesultados_rmp ADD mrmp_positivo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.maparesultados_rmp ADD mrmp_negativo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.maparesultados_rmp ADD mrmp_valor NUMERIC(18,6) NOT NULL DEFAULT 0;

ALTER TABLE account.maparesultados_rmp ADD CONSTRAINT FK_MapaResultadoscuenta FOREIGN KEY (crmp_cuenta) REFERENCES account.cuenta_cue(ccue_llave);
ALTER TABLE account.maparesultados_rmp ADD CONSTRAINT FK_MapaResultadosperiodo FOREIGN KEY (crmp_periodo) REFERENCES account.periodotiempo_ptm(cptm_llave);


