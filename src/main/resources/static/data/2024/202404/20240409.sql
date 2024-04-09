COMMENT ON TABLE usuario_usrp IS '2024-04-09';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria)
	SELECT 'PROP_258' , 'C', 'FECHA_MINIMA_CAMPO', 'FECHA_MINIMA_CAMPO', 'REQUISITO', 'F'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_258');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria)
	SELECT 'PROP_259' , 'C', 'FECHA_MAXIMA_CAMPO', 'FECHA_MAXIMA_CAMPO', 'REQUISITO', 'F'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_259');