COMMENT ON TABLE usuario_usrp IS '2023-09-08';

update relacioninterna_ritp set crit_estado = 'I'
where crit_llave in (
	select rr.crit_llave from propiedadvalordefinido_pvdp pp
	inner join propiedad_ppdp pp2 on (pp2.cppd_propiedadvalor = pp.cpvd_llave and pp2.cppd_estado = 'A')
	inner join documentoplantillacaracteristica_dpcp dd on (dd.cdpc_llave = pp2.cppd_campo)
	inner join relacioninterna_ritp rr on (rr.crit_propiedad = pp2.cppd_llave and rr.crit_estado = 'A' )
	where cpvd_codigo = 'DEPENDE'
	and rr.crit_plantilla  = dd.cdpc_plantilla
)