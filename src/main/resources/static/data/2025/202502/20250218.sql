COMMENT ON TABLE usuario_usrp IS '2025-02-18';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo,  bpvd_textoculto)
    SELECT 'PROP_280' , 'E', 'CADENA DE CONEXION', 'CONNECTION_STRING_DB', 'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_280');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple)
    SELECT 'PROP_281' , 'L', 'VALIDACION ANTES DE CREAR', 'FUNCION_SQL_NEW_ANTES', 'REQUISITO', true, true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_281');