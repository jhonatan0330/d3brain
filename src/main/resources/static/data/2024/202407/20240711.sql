COMMENT ON TABLE usuario_usrp IS '2024-07-11';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo)
	SELECT 'PROP_262' , 'W', 'API_BASE', 'PARAMETROS BASE DEL API', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_262');
