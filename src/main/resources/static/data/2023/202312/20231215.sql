COMMENT ON TABLE usuario_usrp IS '2023-12-15';


INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, cpvd_origencategoria) 
	select
	'PROP_249' , 'A', 'PARAMETROS PARA EL API', 'API_PARAMETER', 'REQUISITO', true, 'P'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_249');