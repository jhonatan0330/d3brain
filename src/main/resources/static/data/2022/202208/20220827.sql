COMMENT ON TABLE usuario_usrp IS '2022-08-27';

update propiedad_ppdp set cppd_texto = 'Authorization=Bearer ' where cppd_llave in (
select pp.cppd_llave from propiedad_ppdp pp 
	 left join relacioninterna_ritp rr on (rr.crit_propiedad = pp.cppd_llave)
where cppd_propiedadvalor = 'PROP_192' and cppd_estado = 'A'
and rr.crit_llave is null);