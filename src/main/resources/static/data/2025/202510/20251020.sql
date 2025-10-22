COMMENT ON TABLE usuario_usrp IS '2025-10-20';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol, bpvd_pideusuario)
    SELECT 'PROP_303' , 'O', 'PERMISO DE CONSULTAS', 'APP_READER', 'REQUISITO', true, true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_303');