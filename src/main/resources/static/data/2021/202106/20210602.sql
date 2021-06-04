COMMENT ON TABLE usuario_usrp IS '2021-06-02';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_173' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO FORMULARIO', 'API_CODE_DIRECT', 'REQUISITO', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_174' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO REFERENCIADO', 'API_CODE_REFERENCE', 'REQUISITO', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_175' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO ESPECIAL', 'API_CODE_ESPECIAL', 'REQUISITO', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_176' , 'T', 'API', 'API_TRANSACCION', 'REQUISITO');