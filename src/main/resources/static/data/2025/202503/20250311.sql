COMMENT ON TABLE usuario_usrp IS '2025-03-11';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean, cpvd_origencategoria)
    SELECT 'PROP_282' , 'A', 'ADD_ITERATION_DOCUMENT', 'AGREGAR EL DOCUMENTO CREADO A UN CAMPO', 'REQUISITO', true, 'R'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_282');