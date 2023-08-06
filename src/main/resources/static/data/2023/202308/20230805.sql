COMMENT ON TABLE usuario_usrp IS '2023-08-05';

update propiedadvalordefinido_pvdp set bpvd_textoculto = true, bpvd_propiedadboolean= false where cpvd_llave= 'PROP_190';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_232' , 'C', 'GUARDAR AL SELECCIONAR', 'SAVE_TO_SELECT', 'REQUISITO', 'Z', true);