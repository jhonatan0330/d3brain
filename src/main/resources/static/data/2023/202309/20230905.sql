COMMENT ON TABLE usuario_usrp IS '2023-09-05';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	select
	'PROP_234' , 'E', 'OCULTAR REPORTE', 'OCULTAR_REPORTE', 'REQUISITO', true, true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_234');