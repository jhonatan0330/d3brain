COMMENT ON TABLE usuario_usrp IS '2024-11-05';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto)
	SELECT 'PROP_270' , 'C', 'CROQUIS DISPONIBILIDAD', 'DISPONIBILIDAD_FUNCION_SQL', 'REQUISITO', 'M', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_270');
