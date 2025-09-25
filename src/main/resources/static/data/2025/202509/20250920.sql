COMMENT ON TABLE usuario_usrp IS '2025-09-20';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'GENERAR CAMPO VINCULO DEL DOCUMENTO SELECCIONADO'
where cpvd_llave  = 'PROP_294';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto)
    SELECT 'PROP_295' , 'C', 'CONSULTAR SI EXISTE DOCUMENTO VINCULO', 'VINCULO_GET_PREVIOUS_SQL', 'REQUISITO', 'C', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_295');

   