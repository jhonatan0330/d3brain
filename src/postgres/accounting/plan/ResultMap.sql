
CREATE TABLE account.mapa_resultados_rmp (
	crmp_llave varchar(32) NOT NULL,
	crmp_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_mapa_resultados_rmp PRIMARY KEY (crmp_llave)
);

ALTER TABLE account.mapa_resultados_rmp ADD drmp_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.mapa_resultados_rmp ADD crmp_creacionusuario varchar(32);
ALTER TABLE account.mapa_resultados_rmp ADD crmp_creacionusuarionombre varchar(200);
ALTER TABLE account.mapa_resultados_rmp ADD drmp_modificacionfecha timestamptz;


ALTER TABLE account.mapa_resultados_rmp ADD crmp_catalogo varchar(32) NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD crmp_cuenta varchar(32) NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_nivel int NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD drmp_fechainicio timestamp with time zone NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD drmp_fechafin timestamp with time zone NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD crmp_periodo varchar(100) NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_ano int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_mes int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_dia int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_hora int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_minuto int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_cantidad int NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_promedio NUMERIC(10,2) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_saldoanterior NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_saldosiguiente NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_positivo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_negativo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_valor NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD crmp_tipo varchar(1) NOT NULL ;
