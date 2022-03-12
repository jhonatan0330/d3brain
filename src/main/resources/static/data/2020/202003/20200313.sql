COMMENT ON TABLE usuario_usrp IS '2020-03-13';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo) 
	VALUES('PROP_120' , 'L', 'FUNCION CONSULTA DATOS', 'PROCESO_FUNCION_SQL', 'www.softwareparati.com', 'REQUISITO');

UPDATE propiedadvalordefinido_pvdp SET cpvd_nombre = 'FUNCION CONSULTA DATOS' where cpvd_llave = 'PROP_41';
