COMMENT ON TABLE usuario_usrp IS '2025-03-06';    
   
--Creo una funcion para crear las nuevas funciones con el campo parametros
CREATE OR REPLACE FUNCTION create_function_propiedad_iteracion() RETURNS void AS 
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
and p.proname like ''iteracion_%'') LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, modificador character varying, ramdom character varying)'';
		query := query || '' RETURNS SETOF pedidoventa_pdvp LANGUAGE plpgsql AS '' || chr(36) ||chr(36) || doc_record.definicion || chr(36) || chr(36) ||'' ;'';
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
and proargnames IN (''{documento,modificador}'')
and p.proname like ''iteracion_%'') LOOP
		query := ''DROP function if EXISTS '' || doc_record.function_name || '' (varchar,varchar);'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
' LANGUAGE plpgsql STRICT;

SELECT create_function_propiedad_iteracion_drop();

DROP FUNCTION create_function_propiedad_iteracion_drop();
   

--Creo una funcion para crear las nuevas funciones con el campo parametros
CREATE OR REPLACE FUNCTION create_function_propiedad_decision() RETURNS void AS 
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
and p.proname like ''decision_%'') LOOP
		query := ''CREATE FUNCTION '' || regexp_replace(doc_record.function_name,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, modificador character varying, ramdom character varying)'';
		query := query || '' RETURNS character varying LANGUAGE plpgsql AS '' || chr(36) ||chr(36) || doc_record.definicion || chr(36) || chr(36) ||'' ;'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
' LANGUAGE plpgsql STRICT;

SELECT create_function_propiedad_decision();

DROP FUNCTION create_function_propiedad_decision();
--funcion de eliminar
CREATE OR REPLACE FUNCTION create_function_propiedad_decision_drop() RETURNS void AS 
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
and p.proname like ''decision_%'') LOOP
		query := ''DROP function if EXISTS '' || doc_record.function_name || '' (varchar,varchar);'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
' LANGUAGE plpgsql STRICT;

SELECT create_function_propiedad_decision_drop();

DROP FUNCTION create_function_propiedad_decision_drop();

DROP FUNCTION public.saldo4documento(varchar, int4);

CREATE OR REPLACE FUNCTION public.saldo4documento(_documento character varying, _historico integer, _ramdom character varying)
 RETURNS TABLE(cpvd_llave character varying, cpvd_documento character varying, mpvd_valortotal numeric, mpvd_saldo numeric, cpvd_estado character varying, bpvd_controlarsaldo bool, dpvd_fecha timestamp with time zone)
 LANGUAGE plpgsql
AS '
begin
	if _historico = 0 then
		select npdv_historico into _historico from pedidoventa_pdvp where cpdv_llave = _documento;
	end if;
	if _historico is null then
		return query select
				t.cpvd_llave, 
				t.cpvd_documento, 
				t.mpvd_valortotal, 
				t.mpvd_saldo, 
				t.cpvd_estado, 
				t.bpvd_controlarsaldo,
				t.dpvd_fecha 
			from pedidoventadinero_pvdp t where t.cpvd_documento = _documento and t.cpvd_estado = ''A'';
	else
		return query select
				z.cpvd_llave, 
				z.cpvd_documento, 
				z.mpvd_valortotal, 
				z.mpvd_saldo, 
				z.cpvd_estado,
				z.bpvd_controlarsaldo, 
				z.dpvd_fecha 
			from historic.z_pvd_pedidoventadinero z where z.cpvd_documento = _documento and z.cpvd_estado = ''A'';
	end if;
END;'
;
   