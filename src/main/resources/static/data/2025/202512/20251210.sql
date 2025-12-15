COMMENT ON TABLE usuario_usrp IS '2025-12-10';

update  propiedad_ppdp pp 
set cppd_tipo = 'A'
where pp.cppd_propiedadvalor = 'PROP_133'
and pp.cppd_estado = 'A';