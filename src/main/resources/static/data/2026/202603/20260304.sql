COMMENT ON TABLE usuario_usrp IS '2026-03-04';

update propiedadvalordefinido_pvdp pp 
set bpvd_multiple = true
where pp.cpvd_llave = 'PROP_262';
