
COMMENT ON TABLE usuario_usrp IS '2022-07-30';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_205' , 'E', 'REPORTE VISIBLE EN EL ESTADO', 'REP_VISIBLE_STATE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_206' , 'E', 'IMPRESION UNICA DEL REPORTE', 'REP_PRINT_ONE', 'REQUISITO', true);