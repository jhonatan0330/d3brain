COMMENT ON TABLE usuario_usrp IS '2020-01-29';

CREATE OR REPLACE FUNCTION create_function_propiedad() RETURNS void LANGUAGE plpgsql AS '
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN select cpdc_llave, cpdc_funcion from procesodecision_pdcp  where cpdc_estado = ''I'' LOOP
		query := ''DROP function if EXISTS dcs_'' || lower(regexp_replace(doc_record.cpdc_funcion,''-'', ''_'', ''g'')) || '' ("varchar");'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
';

SELECT create_function_propiedad();

DROP FUNCTION create_function_propiedad();


CREATE OR REPLACE FUNCTION create_function_propiedad() RETURNS void LANGUAGE plpgsql AS '
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN select cpdc_llave, cpdc_funcion from procesodecision_pdcp  where cpdc_estado = ''A'' LOOP
		query := ''CREATE FUNCTION decision_'' || regexp_replace(doc_record.cpdc_llave,''-'', ''_'', ''g'');
		query := query || '' (documento character varying)'';
		query := query || '' RETURNS character varying LANGUAGE plpgsql AS $function$'';
		query := query || '' BEGIN return (SELECT * from dcs_'' || lower(regexp_replace(doc_record.cpdc_funcion,''-'', ''_'', ''g'')) || ''(documento)); END; $function$;'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
';

SELECT create_function_propiedad();

DROP FUNCTION create_function_propiedad();

ALTER TABLE procesodecision_pdcp
	DROP COLUMN cpdc_codigo,
	DROP COLUMN cpdc_funcion;

