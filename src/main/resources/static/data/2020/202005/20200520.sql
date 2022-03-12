COMMENT ON TABLE usuario_usrp IS '2020-05-20';

update propiedadvalordefinido_pvdp set cpvd_codigo = 'FUNCION_SQL_ESTADO_ASIGNAR' where cpvd_llave = 'PROP_90';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_ayuda,  cpvd_grupo,  cpvd_motivo) 
	VALUES('PROP_136', 'A', 'ESTADO_ASIGNAR', 'ASIGNACION DE USUARIO', 'www.softwareparati.com', 'REQUISITO', 'Todos los documentos que tengan este estado se asignan a este usuario');
