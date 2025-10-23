COMMENT ON TABLE usuario_usrp IS '2025-10-22';


update propiedadvalordefinido_pvdp
	set cpvd_origen = 'W'
where cpvd_llave = 'PROP_278';

update propiedad_ppdp pp 
	set cppd_tipo = 'W'
where pp.cppd_propiedadvalor = 'PROP_278'
and pp.cppd_estado = 'A';

update propiedad_ppdp pp 
	set cppd_motivo = cppd_valor
where pp.cppd_propiedadvalor = 'PROP_278'
and pp.cppd_estado = 'A';

update propiedad_ppdp pp 
	set cppd_valor = cppd_campo
where pp.cppd_propiedadvalor = 'PROP_278'
and pp.cppd_estado = 'A';

update propiedad_ppdp pp 
	set cppd_campo = cppd_motivo
where pp.cppd_propiedadvalor = 'PROP_278'
and pp.cppd_estado = 'A';

update propiedad_ppdp pp 
	set cppd_motivo = null
where pp.cppd_propiedadvalor = 'PROP_278'
and pp.cppd_estado = 'A';

update propiedad_ppdp pp 
	set cppd_texto = (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = cppd_valor)
where pp.cppd_propiedadvalor = 'PROP_278'
and pp.cppd_estado = 'A';