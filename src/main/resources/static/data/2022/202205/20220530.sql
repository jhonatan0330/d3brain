COMMENT ON TABLE usuario_usrp IS '2022-05-30';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_204' , 'W', 'API_EXTRACTION_TO_BASE_64', 'API_EXTRACTION_TO_BASE_64', 'REQUISITO', true);
	
update propiedadvalordefinido_pvdp set cpvd_origencategoria =  'J' 
	where cpvd_llave = 'PROP_121';