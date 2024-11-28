COMMENT ON TABLE usuario_usrp IS '2024-11-27';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple)
    SELECT 'PROP_273' , 'O', 'HEADER PAGE', 'HEADER_PAGE', 'REQUISITO', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_273');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_piderol, bpvd_propiedadboolean)
    SELECT 'PROP_274' , 'L', 'ACCESO RAPIDO', 'PLANTILLA_ACCESO_RAPIDO', 'PERMISOS', true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_274');

update propiedadvalordefinido_pvdp set bpvd_multiple = true where cpvd_llave = 'PROP_264';