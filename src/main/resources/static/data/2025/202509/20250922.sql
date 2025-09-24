COMMENT ON TABLE usuario_usrp IS '2025-09-22';

update propiedadvalordefinido_pvdp
set bpvd_piderol = true, cpvd_origen='O',bpvd_pideusuario = true
where cpvd_llave in ('PROP_179', 'PROP_263');



update propiedad_ppdp pp 
set cppd_tipo  = 'O', cppd_rol = cppd_campo 
where pp.cppd_propiedadvalor in ('PROP_179', 'PROP_263');


update propiedad_ppdp pp 
set cppd_campo = (select corg_llave from organizacion_orgp where corg_principal is null)
where pp.cppd_propiedadvalor in ('PROP_179', 'PROP_263');
