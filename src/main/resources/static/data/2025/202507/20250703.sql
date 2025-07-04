 COMMENT ON TABLE usuario_usrp IS '2025-07-03';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
    SELECT 'PROP_287' , 'O', 'LOGIN HTML', 'LOGIN_HTML', 'REQUISITO', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_287');
