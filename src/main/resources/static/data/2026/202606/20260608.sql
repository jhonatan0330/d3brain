COMMENT ON TABLE usuario_usrp IS '2026-06-08';

update propiedad_ppdp pp
set cppd_valor = '1', cppd_texto =  null, cppd_motivo =  null
where pp.cppd_propiedadvalor = 'PROP_105' and pp.cppd_estado = 'A';

update propiedad_ppdp pp
set cppd_motivo =  null
where pp.cppd_propiedadvalor = 'PROP_44' and pp.cppd_estado = 'A';

update propiedad_ppdp pp
set cppd_valor = '1', cppd_texto =  null, cppd_motivo =  null
where pp.cppd_propiedadvalor = 'PROP_94' and pp.cppd_estado = 'A';

update propiedad_ppdp pp
set cppd_valor = '1', cppd_texto =  null, cppd_motivo =  null
where pp.cppd_propiedadvalor = 'PROP_108' and pp.cppd_estado = 'A';