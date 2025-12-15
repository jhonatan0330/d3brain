COMMENT ON TABLE usuario_usrp IS '2025-12-11';


update propiedad_ppdp pp
set cppd_valor = replace(cppd_valor, ']]', '_KEY]]')
where pp.cppd_estado = 'A' and pp.cppd_valor like '%]]%'
and pp.cppd_propiedadvalor = 'PROP_300';