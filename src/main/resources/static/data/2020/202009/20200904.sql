
COMMENT ON TABLE usuario_usrp IS '2020-09-04';

update propiedadvalordefinido_pvdp set bpvd_propiedadboolean = true
where cpvd_llave = 'PROP_91';

update propiedad_ppdp set cppd_valor = replace(cppd_valor,'movimiento_descripcion(cbog_documento)', 'movimiento_descripcion(cbod_documento)') 
where cppd_propiedadvalor = 'PROP_138' AND cppd_estado = 'A';