COMMENT ON TABLE usuario_usrp IS '2024-03-11';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
	SELECT 'PROP_256' , 'O', 'PLANTILLA IMAGENES CARROUSEL', 'COVERAGE_TEMPLATE', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_256');
	
	