COMMENT ON TABLE usuario_usrp IS '2020-08-27';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_151' , 'T', 'GENERA_DOCUMENTO_CAMPO', 'CAMPO PARA GENERAR DOCUMENTO', 'REQUISITO', true);
	
delete from propiedad_ppdp where cppd_propiedadvalor = 'PROP_119';
delete from propiedadvalordefinido_pvdp where cpvd_llave = 'PROP_119';