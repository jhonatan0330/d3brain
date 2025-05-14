COMMENT ON TABLE usuario_usrp IS '2025-05-13';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
    SELECT 'PROP_283' ,  'L', 'PLANTILLA ACTIVAR', 'PLANTILLA_ACTIVAR', 'REQUISITO'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_283');
