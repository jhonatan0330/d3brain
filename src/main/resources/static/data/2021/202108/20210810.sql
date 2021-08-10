COMMENT ON TABLE usuario_usrp IS '2021-08-10';

CREATE OR REPLACE FUNCTION campo4code(_documento character varying, _code character varying, _plantilla character varying, _historico integer) RETURNS TABLE(cpvc_llave character varying, cpvc_documento character varying, dpvc_valorfecha timestamp with time zone, mpvc_valornumero numeric, cpvc_valortext character varying, cpvc_valoropcion character varying, cpvc_estado character varying, cpvc_campo character varying, cpvc_valorauxiliar character varying, cpvc_transaccionregistro character varying, cpvc_transaccioninactivo character varying)
    LANGUAGE plpgsql
    AS $$
declare 
	_campo documentoplantillacaracteristica_dpcp;
begin
	select * into _campo from documentoplantillacaracteristica_dpcp where cdpc_plantilla = _plantilla and cdpc_estado = 'A' and cdpc_codigo = _code;
	if found then
		return query select
				tb.cpvc_llave,
				tb.cpvc_documento,
				tb.dpvc_valorfecha,
				tb.mpvc_valornumero,
				tb.cpvc_valortext,
				tb.cpvc_valoropcion,
				tb.cpvc_estado,
				tb.cpvc_campo,
				tb.cpvc_valorauxiliar,
				tb.cpvc_transaccionregistro,
				tb.cpvc_transaccioninactivo
			from campo4id(_documento, _campo.cdpc_llave, _historico) tb;
	end if;
END;$$;

CREATE OR REPLACE FUNCTION saldo4documento(_documento character varying, _historico integer) RETURNS TABLE(cpvd_llave character varying, cpvd_documento character varying, mpvd_valortotal numeric, mpvd_saldo numeric, cpvd_estado character varying, dpvd_fecha timestamp with time zone)
    LANGUAGE plpgsql
    AS $$
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
				t.dpvd_fecha 
			from pedidoventadinero_pvdp t where t.cpvd_documento = _documento and t.cpvd_estado = 'A';
	else
		return query select
				z.cpvd_llave, 
				z.cpvd_documento, 
				z.mpvd_valortotal, 
				z.mpvd_saldo, 
				z.cpvd_estado, 
				z.dpvd_fecha 
			from pedidoventadinero_pvdp z where z.cpvd_documento = _documento and z.cpvd_estado = 'A';
	end if;
END;$$;
