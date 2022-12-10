COMMENT ON TABLE usuario_usrp IS '2022-12-06';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_217' , 'W', 'CONNECT TIMEOUT', 'API_CONNECT_TIMEOUT', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_218' , 'W', 'READ TIMEOUT', 'API_READ_TIMEOUT', 'REQUISITO');