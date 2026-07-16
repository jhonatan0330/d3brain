COMMENT ON TABLE usuario_usrp IS '2026-03-30';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean, cpvd_origencategoria)
    SELECT 'PROP_306' , 'C', 'RELACIONAR_MISMOS', 'RELACIONAR EL DOCUMENTO A LOS EXPEDIENTES DEL CAMPO', 'REQUISITO', true, 'Z'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_306');