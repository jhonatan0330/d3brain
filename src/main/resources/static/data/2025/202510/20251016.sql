COMMENT ON TABLE usuario_usrp IS '2025-10-16';

update propiedad_ppdp pp
	set cppd_valor = (select cco.ccpr_plantilla from categoriaproducto_cprp cco where cco.ccpr_llave = cppd_valor)
where pp.cppd_propiedadvalor = 'PROP_127'
	and pp.cppd_estado = 'A'
	and pp.cppd_valor not in (select cdpl_llave from documentoplantilla_dplp dd where dd.cdpl_estado = 'A');