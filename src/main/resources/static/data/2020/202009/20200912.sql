COMMENT ON TABLE usuario_usrp IS '2020-09-12';

--Creo una propiedad
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_154' , 'C', 'LLENAR AL GUARDAR', 'AUTOLOAD_SAVE', 'REQUISITO', 'Z', true);
	
--Creo una funcion para crear las nuevas funciones con el campo parametros
CREATE OR REPLACE FUNCTION create_function_propiedad() RETURNS void LANGUAGE plpgsql AS '
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN (select p.proname as function_name, prosrc  as definicion,*
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in (''pg_catalog'', ''information_schema'')
and proargnames IN (''{documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto,token}'')
and p.proname like ''propiedad_%'') LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[])'';
		query := query || '' RETURNS SETOF pedidoventa_pdvp LANGUAGE plpgsql AS $function$'';
		query := query || doc_record.definicion || '' $function$;'';
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
and proargnames IN (''{documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto,token}'')
and p.proname like ''propiedad_%'') LOOP
		query := ''DROP function if EXISTS '' || doc_record.function_name || '' (varchar,int4,int4,timestamptz,timestamptz,varchar,varchar,varchar);'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
';

SELECT create_function_propiedad();

DROP FUNCTION create_function_propiedad();