COMMENT ON TABLE usuario_usrp IS '2025-05-14';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo)
    SELECT 'PROP_284' , 'C', 'SECCION FUNCION PARA VISIBILIDAD', 'SECCION_FUNCION_SQL', 'REQUISITO', 'S', true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_284');

    
    
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
and proargnames IN (''{documento,parametros}'')
and p.proname like ''propiedad_%''
and pg_get_function_result(p.oid) != ''pedidoventacaracteristica_pvcp'') LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, token character varying, parametros character varying[])'';
		query := query || '' RETURNS ''|| doc_record.return_type ||'' LANGUAGE plpgsql AS '' || chr(36) ||chr(36) || doc_record.definicion || chr(36) || chr(36) ||'' ;'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
' LANGUAGE plpgsql STRICT;

SELECT create_function_propiedad_iteracion();

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
and proargnames IN (''{documento,parametros}'')
and p.proname like ''propiedad_%''
and pg_get_function_result(p.oid) = ''pedidoventacaracteristica_pvcp'') LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, token character varying, parametros character varying[])'';
		query := query || '' RETURNS SETOF ''|| doc_record.return_type ||'' LANGUAGE plpgsql AS '' || chr(36) ||chr(36) || doc_record.definicion || chr(36) || chr(36) ||'' ;'';
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
and proargnames IN (''{documento,parametros}'')
and p.proname like ''propiedad_%'') LOOP
		query := ''DROP function if EXISTS '' || doc_record.function_name || '' (varchar,_varchar);'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
' LANGUAGE plpgsql STRICT;

SELECT create_function_propiedad_iteracion_drop();

DROP FUNCTION create_function_propiedad_iteracion_drop();
   

    