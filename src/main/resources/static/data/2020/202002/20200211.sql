COMMENT ON TABLE usuario_usrp IS '2020-02-11';

CREATE OR REPLACE FUNCTION create_function_propiedad() RETURNS void LANGUAGE plpgsql AS '
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN (select p.proname as function_name, prosrc  as definicion
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in (''pg_catalog'', ''information_schema'')
and proargnames IN (''{documento,cant,pagina,fechaminima,fechamaxima,filtro}'')) LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying)'';
		query := query || '' RETURNS SETOF pedidoventa_pdvp LANGUAGE plpgsql AS $function$'';
		query := query || regexp_replace(doc_record.definicion,''documento,cant,pagina,fechaminima,fechamaxima,filtro'', ''documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto'', ''g'') || '' $function$;'';
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
and proargnames IN (''{documento,cant,pagina,fechaminima,fechamaxima,filtro}'')) LOOP
		query := ''DROP function if EXISTS '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'') || '' ("varchar","int4","int4","timestamptz","timestamptz","varchar");'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
';

SELECT create_function_propiedad();

DROP FUNCTION create_function_propiedad();

update propiedad_ppdp set cppd_valor = replace(cppd_valor,'documento,cant,pagina,fechaminima,fechamaxima,filtro', 'documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto') where cppd_valor like '%documento,cant,pagina,fechaminima,fechamaxima,filtro%';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_114' , 'L', 'PERMISO VER FORMULARIOS', 'PERMISO_PLANTILLA_VER', 'www.softwareparati.com', 'PERMISOS', 'Se tiene permiso de visualizar un registro', true);