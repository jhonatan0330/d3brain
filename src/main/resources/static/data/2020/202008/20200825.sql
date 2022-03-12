COMMENT ON TABLE usuario_usrp IS '2020-08-25';
COMMENT ON TABLE usuariosesion_ussp IS '2020.08.25.00';


INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo ) 
	VALUES('PROP_147' , 'C', 'FUNCION TARIFAS', 'DETALLE_TARIFARIO_SQL', 'REQUISITO', 'J', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_148' , 'L', 'PRODUCTO CAMPO VALOR UNITARIO', 'PRODUCTO_CAMPO_VALOR_UNITARIO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_149' , 'L', 'PRODUCTO CAMPO VALOR MINIMO', 'PRODUCTO_CAMPO_VALOR_MINIMO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo) 
	VALUES('PROP_150' , 'L', 'PRODUCTO_CAMPO_CANTIDAD_TARIFA', 'PRODUCTO CAMPO CANTIDAD TARIFA', 'REQUISITO');
	
ALTER TABLE tarifario_trfp ADD btrf_rangovalores bool NOT NULL DEFAULT false;
