COMMENT ON TABLE usuario_usrp IS '2026-01-19';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol, bpvd_pideusuario)
    SELECT 'PROP_305' ,  'T', 'TRANSICION VISIBLE EN CAMPOS VINCULO', 'TRANSICION_VISIBLE_VINCULO', 'REQUISITO', true, true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_305');
