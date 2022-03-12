
COMMENT ON TABLE usuario_usrp IS '2019-01-08';
update plantillacampoparametro_pcpp set cpcp_valor= replace(cpcp_key, 'CONFIGURACION_', ''), cpcp_texto = replace(cpcp_key, 'CONFIGURACION_', '')
where cpcp_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato = 'G');

update plantillacampoparametro_pcpp set cpcp_key = 'CONFIGURACION_ENTIDAD'
where cpcp_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato = 'G');


CREATE OR REPLACE FUNCTION public.dcs_saldo_cero(character varying)
  RETURNS character varying LANGUAGE plpgsql AS '
BEGIN

IF Exists (select mpvd_saldo from pedidoventadinero_pvdp where cpvd_documento  = $1 and cpvd_estado = ''A'' and mpvd_saldo !=0) then
	return ''T'';
else
	return ''F'';
End if;
END;
';
