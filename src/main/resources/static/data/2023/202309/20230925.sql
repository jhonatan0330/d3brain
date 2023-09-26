COMMENT ON TABLE usuario_usrp IS '2023-09-25';

update propiedad_ppdp set cppd_valor = cppd_texto , cppd_texto = cppd_valor
where cppd_llave in (
select pp.cppd_llave  from documentoplantillacaracteristica_dpcp p
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = p.cdpc_plantilla and dd.cdpl_estado = 'A')
	inner join propiedad_ppdp pp on (pp.cppd_campo = p.cdpc_llave and pp.cppd_estado = 'A')
where cdpc_estado = 'A' and cdpc_formato = 'G'
	and pp.cppd_propiedadvalor = 'PROP_118'
	and pp.cppd_texto is not null
	and pp.cppd_valor != pp.cppd_texto );