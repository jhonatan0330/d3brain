COMMENT ON TABLE usuario_usrp IS '2026-10-08';

DROP INDEX IF  EXISTS idx_tnu_usuario ;

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'CAMPOS DEL API A REEMPLAZAR'
where cpvd_llave in ('PROP_174');

