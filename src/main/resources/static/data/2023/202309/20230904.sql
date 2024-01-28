COMMENT ON TABLE usuario_usrp IS '2023-09-04';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean)
	select
	'PROP_233' , 'C', 'BUSQUEDA SIN TEXTO', 'BUSQUEDA_SIN_TEXTO', 'REQUISITO', 'J', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_233');