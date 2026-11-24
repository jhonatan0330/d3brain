COMMENT ON TABLE usuario_usrp IS '2026-11-23';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean)
    SELECT 'PROP_320' , 'I', 'VER INDICADOR', 'PERMISO_VER_INDICADOR', 'REQUISITO', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_320');
    
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
    SELECT 'PROP_321' , 'I', 'PLANTILLAS QUE PUEDEN ALIMENTAR EL INDICADOR', 'INDICADOR_TEMPLATE_ACTION', 'REQUISITO'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_321');
    