COMMENT ON TABLE usuario_usrp IS '2019-06-13';

ALTER TABLE cupoviaje_cvjp
	DROP CONSTRAINT IF EXISTS fk_cupoviajeviaje;

ALTER TABLE reportebase_rpbp
	ALTER COLUMN crpb_subreporte TYPE character varying(120000) /* TYPE change - table: reportebase_rpbp original: character varying(120000) new: character varying(4000) */,
	ALTER COLUMN crpb_subreporte2 TYPE character varying(120000) /* TYPE change - table: reportebase_rpbp original: character varying(120000) new: character varying(4000) */,
	ALTER COLUMN crpb_jasperexcel TYPE character varying(120000) /* TYPE change - table: reportebase_rpbp original: character varying(120000) new: character varying(4000) */;

update proceso_prcp set nprc_prioridad = 100;

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

CREATE OR REPLACE FUNCTION sw42_prioridad_b(character varying)
  RETURNS integer LANGUAGE plpgsql AS '
BEGIN 
	return (select nprc_prioridad from pedidoventa_pdvp, proceso_prcp where cpdv_plantilla = cprc_llave and cpdv_llave = $1); 
END;
';


CREATE OR REPLACE FUNCTION sw42_prioridad_c(character varying)
  RETURNS int LANGUAGE plpgsql AS '
DECLARE id_estado_expediente character varying;
BEGIN 
	select cpdv_estadoexpediente INTO id_estado_expediente from pedidoventa_pdvp where cpdv_llave = $1;
	IF (id_estado_expediente is null) THEN
		return 0;
	ELSE
   		return 100 - (select npes_avance from procesoestado_pesp where cpes_llave =  id_estado_expediente);
	END IF;
	return 0; 
END;
';
