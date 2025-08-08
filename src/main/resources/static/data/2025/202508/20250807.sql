COMMENT ON TABLE usuario_usrp IS '2025-08-07';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto)
    SELECT 'PROP_293' , 'A', 'API_SQL', 'API_SQL', 'REQUISITO', 'P', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_293');