
COMMENT ON TABLE usuario_usrp IS '2019-05-08';

ALTER TABLE actividad_actp
	DROP COLUMN dact_fechainicio,
	ADD COLUMN cact_comentario character varying(4000);

ALTER TABLE proceso_prcp
	ADD COLUMN nprc_prioridad integer DEFAULT 0 NOT NULL;

ALTER TABLE procesoestado_pesp
	RENAME COLUMN npes_nivel TO npes_avance;

ALTER TABLE pedidoventa_pdvp
	DROP COLUMN npdv_version;


CREATE OR REPLACE FUNCTION sw42_prioridad_a(character varying)
  RETURNS integer LANGUAGE plpgsql AS '
DECLARE id_cliente character varying;
DECLARE valor_facturacion numeric;
BEGIN 
	select cpvc_valoropcion INTO id_cliente from campo_documento 
		where cdrc_documento = $1 and cdpf_codigo =''CLIENTE'';
	IF (id_cliente is null) THEN
		return 5;
	ELSE
   		select mpvc_valornumero into valor_facturacion from campo_documento 
		where cdrc_documento = id_cliente and  cdpf_codigo =''FACT_PRO'';

		CASE 
			WHEN valor_facturacion is null THEN return 4;
			WHEN valor_facturacion BETWEEN 1 AND 100000 THEN return 3;
			WHEN valor_facturacion BETWEEN 100001 AND 1000000 THEN return 2;
			ELSE return 1;
		END CASE;
	END IF;
END;
';

ALTER TABLE actividad_actp
	ADD COLUMN cact_responsablesiguiente character varying(32);
