COMMENT ON TABLE usuario_usrp IS '2022-08-11';

CREATE OR REPLACE FUNCTION public.ultima_gestion(_documento character varying, _estado character varying)
 RETURNS TABLE(cdrg_llave character varying, cdrg_documentoprincipal character varying, cdrg_documentomodificador character varying, ddrg_fecha timestamp with time zone, cdrg_estadoinicial character varying, cdrg_estadofinal character varying, cdrg_estado character varying, cdrg_ubicacion character varying, cdrg_valores character varying, cdrg_usuario character varying, ddrg_cierre timestamp with time zone, cdrg_nombre character varying, cdrg_transaccion character varying)
 LANGUAGE plpgsql
AS $function$
begin
	return query select 
		drg.cdrg_llave,
		drg.cdrg_documentoprincipal,
		drg.cdrg_documentomodificador,
		drg.ddrg_fecha,
		drg.cdrg_estadoinicial,
		drg.cdrg_estadofinal,
		drg.cdrg_estado,
		drg.cdrg_ubicacion,
		drg.cdrg_valores,
		drg.cdrg_usuario,
		drg.ddrg_cierre,
		drg.cdrg_nombre,
		drg.cdrg_transaccion
	from documentorelaciongestor_drgp drg 
	where drg.cdrg_documentoprincipal = _documento and drg.cdrg_estado = 'A' and drg.cdrg_estadofinal = _estado
	order by drg.ddrg_fecha desc
	limit 1;	
END;$function$
;
