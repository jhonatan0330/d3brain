COMMENT ON TABLE usuario_usrp IS '2026-09-15';

update propiedadvalordefinido_pvdp pp 
set cpvd_uso_motivo = null 
where pp.cpvd_codigo like '%SQL%';