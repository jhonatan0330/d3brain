COMMENT ON TABLE usuario_usrp IS '2023-04-11';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_223' , 'W', 'API - HORAS EN QUE EL API APLAZA', 'API_SCHEDULE_TIME_BLOCK', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_multiple) 
	VALUES('PROP_224' , 'W', 'API - VALIDAR ANTES DE EJECUTAR', 'FUNCION_SQL_PREVALIDATE_API', 'REQUISITO', true,  true);