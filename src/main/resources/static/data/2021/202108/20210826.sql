COMMENT ON TABLE usuario_usrp IS '2021-08-26';

--Creo una funcion para crear las nuevas funciones con el campo parametros
CREATE OR REPLACE FUNCTION create_function_propiedad() RETURNS void AS 
'
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN (select p.proname as function_name, prosrc  as definicion,*
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in (''pg_catalog'', ''information_schema'')
and proargnames IN (''{documento,modificador}'')
and prorettype = 1043
and p.proname like ''propiedad_%'') LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, modificador character varying, token character varying)'';
		query := query || '' RETURNS character varying LANGUAGE plpgsql AS '' || chr(36) ||chr(36) || doc_record.definicion || chr(36) || chr(36) ||'' ;'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
' LANGUAGE plpgsql STRICT;

SELECT create_function_propiedad();

DROP FUNCTION create_function_propiedad();
--funcion de eliminar
CREATE OR REPLACE FUNCTION create_function_propiedad() RETURNS void AS 
'
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN (select p.proname as function_name
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in (''pg_catalog'', ''information_schema'')
and proargnames IN (''{documento,modificador}'')
and prorettype = 1043
and p.proname like ''propiedad_%'') LOOP
		query := ''DROP function if EXISTS '' || doc_record.function_name || '' (varchar, varchar);'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
' LANGUAGE plpgsql STRICT;

SELECT create_function_propiedad();

DROP FUNCTION create_function_propiedad();