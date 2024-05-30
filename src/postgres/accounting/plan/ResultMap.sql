
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
