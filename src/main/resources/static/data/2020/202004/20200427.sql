COMMENT ON TABLE usuario_usrp IS '2020-04-27';

CREATE OR REPLACE FUNCTION create_function_propiedad() RETURNS void LANGUAGE plpgsql AS '
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN (select p.proname as function_name, prosrc  as definicion
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in (''pg_catalog'', ''information_schema'')
and proargnames IN (''{documento}'')
and p.proname like ''decision_%'') LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, modificador character varying)'';
		query := query || '' RETURNS character varying LANGUAGE plpgsql AS $function$'';
		query := query || regexp_replace(doc_record.definicion,''documento character varying'', ''documento character varying, modificador character varying'', ''g'') || '' $function$;'';
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
	FOR doc_record IN (select p.proname as function_name
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in (''pg_catalog'', ''information_schema'')
and proargnames IN (''{documento}'')
and p.proname like ''decision_%'') LOOP
		query := ''DROP function if EXISTS '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'') || '' ("varchar");'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
';

SELECT create_function_propiedad();

DROP FUNCTION create_function_propiedad();