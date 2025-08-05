COMMENT ON TABLE usuario_usrp IS '2025-08-03';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
    SELECT 'PROP_290' , 'C', 'FUNCION PARA CAMPOS VINCULO', 'VINCULO_FIELD_SQL', 'REQUISITO', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_290');
    
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean)
    SELECT 'PROP_291' , 'C', 'FORMULARIO DETALLADO ITEM', 'ITEM_DETAIL_FORM_VISIBLE', 'REQUISITO', 'J', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_291');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria)
    SELECT 'PROP_292' , 'C', 'FORMULARIO ELIMINACION VINCULO', 'VINCULO_DELETE', 'REQUISITO', 'C'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_292');
    