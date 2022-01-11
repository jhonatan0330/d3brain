COMMENT ON TABLE usuario_usrp IS '2022-01-10';

update propiedadvalordefinido_pvdp set cpvd_origencategoria = 'E' where cpvd_llave = 'PROP_91';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_193' , 'A', 'API', 'API', 'REQUISITO', 'P');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo) 
	VALUES('PROP_194' , 'W', 'API_MAX_TRY', 'API MAXIMO NUMERO DE INTENTOS', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo) 
	VALUES('PROP_195' , 'W', 'API_AUTHENTICATION', 'API EJECUTAR PARA AUTENTICAR', 'REQUISITO');

update propiedadvalordefinido_pvdp set cpvd_estado = 'I' where cpvd_llave = 'PROP_176';
update propiedad_ppdp set cppd_estado = 'I' where cppd_propiedadvalor = 'PROP_176';

update propiedadvalordefinido_pvdp set bpvd_solicitamotivo = false where cpvd_llave = 'PROP_191';
update propiedadvalordefinido_pvdp set bpvd_solicitamotivo = false where cpvd_llave = 'PROP_192';

update propiedadvalordefinido_pvdp set bpvd_textoculto = false where cpvd_llave = 'PROP_192';