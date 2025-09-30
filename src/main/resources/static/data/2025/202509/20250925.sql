COMMENT ON TABLE usuario_usrp IS '2025-09-25';


update propiedadvalordefinido_pvdp
set bpvd_multiple = true, cpvd_origen='O'
where cpvd_llave in ('PROP_297');

