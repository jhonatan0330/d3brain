COMMENT ON TABLE usuario_usrp IS '2023-05-19';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_226' , 'L', 'INVENTARIO_OPCIONAL', 'OMITIR CREAR INVENTARIO EN BODEGA DEL PRODUCTO', 'REQUISITO', true);

update propiedadvalordefinido_pvdp set cpvd_estado = 'A' where cpvd_llave= 'PROP_222';