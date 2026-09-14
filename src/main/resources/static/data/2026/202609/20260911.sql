COMMENT ON TABLE usuario_usrp IS '2026-09-11';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
    SELECT 'PROP_309' , 'E', 'QUERY REPORTE MAESTRO', 'REPORT_QUERY_ENCABEZADO', 'REQUISITO', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_309');
