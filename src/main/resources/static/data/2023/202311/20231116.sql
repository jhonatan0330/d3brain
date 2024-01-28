COMMENT ON TABLE usuario_usrp IS '2023-11-16';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	select
	'PROP_248' , 'C', 'NUMERO FUNCION SIEMPRE CALCULAR AL GUARDAR', 'FUNCION_NUMBER_ALL_CALCULATE_SAVE', 'REQUISITO', 'N', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_248');
