COMMENT ON TABLE usuario_usrp IS '2025-09-10';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean)
    SELECT 'PROP_294' , 'C', 'GENERAR CAMPO VINCULO DEL DOICUMENTO SELECCIONADO', 'VINCULO_MAKE_IN_OTHER_FORM', 'REQUISITO', 'Z', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_294');