COMMENT ON TABLE usuario_usrp IS '2024-09-18';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo)
	SELECT 'PROP_267' , 'C', 'FECHA_FUNCION', 'FECHA_FUNCION_SQL', 'REQUISITO', 'F', true, true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_267');
	