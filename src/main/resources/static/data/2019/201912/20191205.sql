COMMENT ON TABLE usuario_usrp IS '2019-12-05';
COMMENT ON TABLE usuariosesion_ussp IS '2019.12.05.00';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo) 
	VALUES('PROP_94' , 'C', 'FILTRO', 'FILTRO', 'www.softwareparati.com', 'REQUISITO', 'Permite filtrar los documentos por este campo');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo) 
	VALUES('PROP_95' , 'C', 'VALOR POR DEFECTO', 'DEFAULT', 'www.softwareparati.com', 'REQUISITO', 'El valor inicial del campo va a ser XXX');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo) 
	VALUES('PROP_96' , 'C', 'CAMPOS DEPENDENCIA', 'DEPENDE', 'www.softwareparati.com', 'REQUISITO', 'Utiliza el campo XX para YY');

INSERT INTO cambio_cmbp (ccmb_llave, ccmb_nombre, ccmb_motivo, dcmb_fecha, dcmb_fechaaplicacion)
	VALUES('SC-20191205', 'SC-SIS-20191205', 'Cambiar los campos a propiedades del codigo depende, filtro y valr por defecto', now(), now());
	
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion) 
	select substring('FILTRO' || cdpc_llave, 0, 32), 'PROP_94', cdpc_llave, 'TRUE', 'Permite filtrar los documentos por este campo', now(), now(), 'SC-20191205' from documentoplantillacaracteristica_dpcp where bdpc_filtro and cdpc_estado = 'A';
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion) 
	select substring('DEFAULT' || cdpc_llave, 0, 32), 'PROP_95', cdpc_llave, cdpc_valordefecto, 'El valor inicial del campo va a ser XXX', now(), now(), 'SC-20191205' from documentoplantillacaracteristica_dpcp where cdpc_valordefecto is not null and cdpc_estado = 'A';
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_texto) 
	select substring('DEPENDE' || a || campo.cdpc_llave, 0, 32), 'PROP_96', campo.cdpc_llave, (select rel.cdpc_llave from documentoplantillacaracteristica_dpcp rel where rel.cdpc_plantilla = campo.cdpc_plantilla and rel.cdpc_codigo = a),
	'Permite filtrar los documentos por este campo', now(), now(), 'SC-20191205',a
	from documentoplantillacaracteristica_dpcp as campo, regexp_split_to_table(campo.cdpc_codigodepende , E'[\\;]+') AS a
	where campo.cdpc_codigodepende is not null and campo.cdpc_estado = 'A';

INSERT INTO relacioninterna_ritp (crit_llave, crit_propiedad, crit_plantilla, crit_campo)
	select substring('DEPENDE' || a || campo.cdpc_llave, 0, 32), substring('DEPENDE' || a || campo.cdpc_llave, 0, 32), campo.cdpc_plantilla, (select rel.cdpc_llave from documentoplantillacaracteristica_dpcp rel where rel.cdpc_plantilla = campo.cdpc_plantilla and rel.cdpc_codigo = a)
	from documentoplantillacaracteristica_dpcp as campo, regexp_split_to_table(campo.cdpc_codigodepende , E'[\\;]+') AS a
	where campo.cdpc_codigodepende is not null and campo.cdpc_estado = 'A';

ALTER TABLE documentoplantillacaracteristica_dpcp
	DROP COLUMN bdpc_filtro,
	DROP COLUMN cdpc_codigodepende,
	DROP COLUMN cdpc_valordefecto;
