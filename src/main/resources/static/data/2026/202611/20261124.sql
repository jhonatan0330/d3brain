COMMENT ON TABLE usuario_usrp IS '2026-11-24';


update propiedadvalordefinido_pvdp
set  bpvd_multiple=true
where cpvd_llave in ('PROP_321');
