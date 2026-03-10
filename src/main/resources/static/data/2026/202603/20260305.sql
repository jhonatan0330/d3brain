COMMENT ON TABLE usuario_usrp IS '2026-03-05';

update propiedadvalordefinido_pvdp pp 
set cpvd_nombre = 'ABRIR TURNO DE CAJA', bpvd_propiedadboolean = true
where pp.cpvd_llave = 'PROP_11';
