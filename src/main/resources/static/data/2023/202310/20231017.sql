COMMENT ON TABLE usuario_usrp IS '2023-10-17';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	select
	'PROP_243' , 'C', 'CAMPO EN PLANTILLA DE DIFERENCIAS', 'CAMPO_DIFERENCIAS', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_243');
