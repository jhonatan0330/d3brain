COMMENT ON TABLE usuario_usrp IS '2018-04-12';
/**
* Introducir la plantilla periodo de tiempo
*/
INSERT INTO consecutivo_conp(ccon_llave, ccon_nombre, bcon_manual) VALUES ('PERIODO_TIEMPO', 'FORMATO PERIODO DE TIEMPO', TRUE);
INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_codigo, cdpl_nombre, cdpl_consecutivo, cdpl_tipo) VALUES ('PERIODO_TIEMPO', 'PERIODO', 'PERIODO TIEMPO', 'PERIODO_TIEMPO', 'F');
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, cdpc_nombre, cdpc_codigo, ndpc_orden, cdpc_formato, bdpc_obligatorio, bdpc_filtro, bdpc_editable, bdpc_modificable)VALUES ('PERIODO_TIEMPO_FORMATO', 'PERIODO_TIEMPO', 'FORMATO', 'FORMATO', 1, 'T', TRUE, TRUE, TRUE, TRUE);
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, cdpc_nombre, cdpc_codigo, ndpc_orden, cdpc_formato, bdpc_obligatorio, bdpc_filtro, bdpc_editable, bdpc_modificable)VALUES ('PERIODO_TIEMPO_NOMBRE', 'PERIODO_TIEMPO', 'NOMBRE', 'NOMBRE', 1, 'T', TRUE, TRUE, TRUE, TRUE);