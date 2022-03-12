COMMENT ON TABLE usuario_usrp IS '2020-02-28';

CREATE OR REPLACE FUNCTION create_function_propiedad() RETURNS void LANGUAGE plpgsql AS '
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN (select p.proname as function_name, prosrc  as definicion
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in (''pg_catalog'', ''information_schema'')
and proargnames IN (''{documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto}'')) LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying)'';
		query := query || '' RETURNS SETOF pedidoventa_pdvp LANGUAGE plpgsql AS $function$'';
		query := query || regexp_replace(doc_record.definicion,''documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto'', ''documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto,token'', ''g'') || '' $function$;'';
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
and proargnames IN (''{documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto}'')) LOOP
		query := ''DROP function if EXISTS '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'') || '' ("varchar","int4","int4","timestamptz","timestamptz","varchar","varchar");'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
';

SELECT create_function_propiedad();

DROP FUNCTION create_function_propiedad();

update propiedad_ppdp set cppd_valor = replace(cppd_valor,'documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto', 'documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto,token') where cppd_valor like '%documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto%';
