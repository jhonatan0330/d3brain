COMMENT ON TABLE usuario_usrp IS '2020-03-09';

COMMENT ON TABLE usuariosesion_ussp IS '2020.03.09.00';

ALTER TABLE propiedad_ppdp
	DROP COLUMN cppd_codigo,
	DROP COLUMN bppd_necesario;

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_origencategoria, bpvd_multiple, bpvd_textoculto) 
	VALUES('PROP_118' , 'C', 'OPCIONES', 'OPCIONES', 'www.softwareparati.com', 'REQUISITO', 'G', true, true);