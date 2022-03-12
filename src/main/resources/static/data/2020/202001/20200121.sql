COMMENT ON TABLE usuario_usrp IS '2020-01-21';

CREATE OR REPLACE FUNCTION create_function_propiedad() RETURNS void LANGUAGE plpgsql AS '
DECLARE
	doc_record RECORD;
	query text;
BEGIN
	FOR doc_record IN select cppd_llave, cppd_valor from propiedad_ppdp  where cppd_propiedadvalor  = ''PROP_32'' and cppd_valor != ''OK'' and cppd_estado = ''A'' LOOP
		query := ''CREATE FUNCTION propiedad_'' || regexp_replace(doc_record.cppd_llave,''-'', ''_'', ''g'');
		query := query || '' (documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying)'';
		query := query || '' RETURNS SETOF pedidoventa_pdvp LANGUAGE plpgsql AS $function$'';
		query := query || '' BEGIN return query SELECT * from grid_'' || lower(regexp_replace(doc_record.cppd_valor,''-'', ''_'', ''g'')) || '' (documento,cant,pagina,fechaminima,fechamaxima,filtro); END; $function$;'';
		EXECUTE  query;
	END LOOP;
  RETURN;
END;
';

SELECT create_function_propiedad();

DROP FUNCTION create_function_propiedad();

update propiedad_ppdp  set cppd_valor = 'BEGIN return query SELECT * from grid_' || lower(replace(cppd_valor,'-', '_')) || '(documento,cant,pagina,fechaminima,fechamaxima,filtro); END;'
where cppd_propiedadvalor = 'PROP_32' and cppd_valor != 'OK' and cppd_estado  = 'A';
