COMMENT ON TABLE usuario_usrp IS '2026-11-04';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple)
    SELECT 'PROP_318' , 'C', 'ACCIONES CRUD', 'PROCESO_ACCIONES', 'REQUISITO', 'J', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_318');