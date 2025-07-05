 COMMENT ON TABLE usuario_usrp IS '2025-07-04';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria)
    SELECT 'PROP_288' , 'C', 'RELACIONAR DOCUMENTO AL CAMPO DEL NUEVO FORMULARIO', 'VINCULO_DATA', 'REQUISITO', 'C'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_288');
