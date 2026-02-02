COMMENT ON TABLE usuario_usrp IS '2026-01-30';


--Creo una funcion para crear las nuevas funciones con el campo parametros
CREATE OR REPLACE FUNCTION create_function_propiedad_iteracion() RETURNS void AS 
'
DECLARE
	doc_record RECORD;
	query text;
BEGIN

	FOR doc_record IN (select p.proname as function_name, prosrc  as definicion, pg_get_function_result(p.oid) AS return_type
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in (''pg_catalog'', ''information_schema'')
and proargnames IN (''{documento, modificador, token}'')
and p.proname like ''propiedad_%''
and pg_get_function_result(p.oid) = ''SETOF pedidoventacaracteristica_pvcp'') LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, modificador character varying, iterador character varying, token character varying)'';
		query := query || '' RETURNS ''|| doc_record.return_type ||'' LANGUAGE plpgsql AS '' || chr(36) ||chr(36) || doc_record.definicion || chr(36) || chr(36) ||'' ;'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
' LANGUAGE plpgsql STRICT;

SELECT create_function_propiedad_iteracion();

DROP FUNCTION create_function_propiedad_iteracion();
--funcion de eliminar
CREATE OR REPLACE FUNCTION create_function_propiedad_iteracion_drop() RETURNS void AS 
'
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN (select p.proname as function_name
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in (''pg_catalog'', ''information_schema'')
and proargnames IN (''{documento, modificador, token}'')
and p.proname like ''propiedad_%''
and pg_get_function_result(p.oid) = ''SETOF pedidoventacaracteristica_pvcp'') LOOP
		query := ''DROP function if EXISTS '' || doc_record.function_name || '' (varchar,varchar,varchar);'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
' LANGUAGE plpgsql STRICT;

SELECT create_function_propiedad_iteracion_drop();

DROP FUNCTION create_function_propiedad_iteracion_drop();
   

    