COMMENT ON TABLE usuario_usrp IS '2020-03-19';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_121' , 'C', 'MODIFICAR CAMPO PRINCIPAL', 'MODIFICAR_CAMPO', 'www.softwareparati.com', 'REQUISITO', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_122' , 'C', 'CONSULTA PRODUCTOS FUNCION ', 'PRODUCTOS_FUNCION_SQL', 'www.softwareparati.com', 'REQUISITO', 'J', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_123' , 'C', 'CONSULTA PRODUCTOS CAMPO', 'PRODUCTOS_FUNCION_CAMPO', 'www.softwareparati.com', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_124' , 'C', 'CONSULTA PRODUCTOS TERCERO', 'PRODUCTOS_TERCERO', 'www.softwareparati.com', 'REQUISITO', 'J');
	
INSERT INTO propiedad_ppdp(cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_motivo, cppd_cambiocreacion, cppd_cambioeliminacion, cppd_tipo, cppd_rol, dppd_fechainicial, dppd_fechafinal, cppd_usuario)
select substring('TERC'|| cppd_llave, 0, 32), cppd_campo, cppd_valor, cppd_texto, 'PROP_124', dppd_fechadefinicion, dppd_fechaimplementacion, cppd_motivo, cppd_cambiocreacion, cppd_cambioeliminacion, cppd_tipo, cppd_rol, dppd_fechainicial, dppd_fechafinal, cppd_usuario from propiedad_ppdp, documentoplantillacaracteristica_dpcp 
where cppd_propiedadvalor = 'PROP_96' and cppd_estado = 'A'
and cppd_campo = cdpc_llave and cdpc_formato = 'J';
