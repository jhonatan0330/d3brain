COMMENT ON TABLE usuario_usrp IS '2025-06-02';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo, bpvd_textoculto, cpvd_origencategoria, bpvd_solicitamotivo)
    SELECT 'PROP_156' , 'A', 'ITERACION_SQL', 'ITERACION_SQL', 'REQUISITO', true, 'R', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_156');

update propiedadvalordefinido_pvdp pp 
set cpvd_origencategoria= 'E'
where cpvd_llave in ('PROP_91', 'PROP_92', 'PROP_89', 'PROP_90', 'PROP_136');

update propiedadvalordefinido_pvdp pp 
set cpvd_origencategoria= 'D'
where cpvd_llave in ('PROP_146');

update propiedadvalordefinido_pvdp pp 
set bpvd_piderol = true, bpvd_pideusuario = true
where cpvd_llave in ('PROP_91');


INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean)
    SELECT 'PROP_154' , 'C', 'LLENAR AL GUARDAR', 'AUTOLOAD_SAVE', 'REQUISITO', 'Z', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_154');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean)
    SELECT 'PROP_155' ,'L', 'OCULTAR GUARDAR', 'PLANTILLA_OCULTAR_GUARDAR', 'REQUISITO', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_155');

update propiedadvalordefinido_pvdp 
set cpvd_estado = 'I'
where cpvd_llave in ('PROP_222', 'PROP_226');

update propiedad_ppdp pp  
set cppd_cambioeliminacion = 'SC_20250529', cppd_estado = 'I'
where cppd_propiedadvalor  in ('PROP_222','PROP_226') and cppd_estado = 'A';
