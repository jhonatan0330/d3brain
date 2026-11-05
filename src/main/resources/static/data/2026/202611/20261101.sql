COMMENT ON TABLE usuario_usrp IS '2026-11-01';

update propiedadvalordefinido_pvdp
set bpvd_propiedadboolean = true, bpvd_multiple=false, cpvd_nombre = 'ESTILOS DINAMICOS DE LA APP'
where cpvd_llave in ('PROP_273');
