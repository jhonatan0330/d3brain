COMMENT ON TABLE usuario_usrp IS '2022-08-29';

update propiedadvalordefinido_pvdp set cpvd_origencategoria = 'T'
where cpvd_llave = 'PROP_75';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_209' , 'C', 'PERMITIR LINKS DIRECTAMENTE', 'ARCHIVO_URL_USUARIO', 'REQUISITO', 'A', true);