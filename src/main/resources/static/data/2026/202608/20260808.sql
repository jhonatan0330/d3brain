COMMENT ON TABLE usuario_usrp IS '2026-08-08';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
    SELECT 'PROP_308' , 'W', 'FIRMAR HEADER FACTURA ELECTRONICA', 'API_FE_HEADER', 'REQUISITO'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_308');
