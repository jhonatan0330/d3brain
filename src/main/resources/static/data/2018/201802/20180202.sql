/*
	Creo la plantilla para abrir cajas para reemplazar el espacio del encabezado
	Los reportes deben ser parte de una plantilla
	Elimino un reporte de nevado que muy seguramente no afecta a los otros sistemas
*/
COMMENT ON TABLE usuario_usrp IS '2018-02-03';

COMMENT ON TABLE usuariosesion_ussp IS '2018.02.03.08';

--Crear una plantilla Abrir caja
INSERT INTO consecutivo_conp (ccon_llave, ccon_nombre, ccon_prefijo, mcon_numeroactual) VALUES('CAJA', 'APERTURA CAJA', 'AC', 100);
INSERT INTO documentoplantilla_dplp (cdpl_llave, cdpl_codigo, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_tipo) VALUES ('CAJA', 'CAJA', 'CAJAS', 'CAJA', 'http://golyat.cloud/imagenes/modulo.png', 'F');
INSERT INTO documentoplantillacaracteristica_dpcp (cdpc_llave, cdpc_plantilla, cdpc_nombre, cdpc_codigo, ndpc_orden, cdpc_formato, bdpc_obligatorio, bdpc_editable, bdpc_visiblerender)
	VALUES('CAJA', 'CAJA', 'CAJA', 'CAJA', 1, 'C', true, true, true);
--select * from plantillacampoparametro_pcpp  
INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor) VALUES('CUENTA_ABRIR_CAJA','CAJA','CUENTA_ABRIR_CAJA', 'TRUE');
--Crear una plantilla Cerrar caja
INSERT INTO consecutivo_conp (ccon_llave, ccon_nombre, ccon_prefijo, mcon_numeroactual) VALUES('CAJA_CIERRE', 'CIERRE CAJA', 'CC', 100);
INSERT INTO documentoplantilla_dplp (cdpl_llave, cdpl_codigo, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_tipo) VALUES ('CAJA_CIERRE', 'CIERRE', 'CIERRE DE CAJAS', 'CAJA_CIERRE', 'http://golyat.cloud/imagenes/modulo.png', 'F');
INSERT INTO documentoplantillacaracteristica_dpcp (cdpc_llave, cdpc_plantilla, cdpc_nombre, cdpc_codigo, ndpc_orden, cdpc_formato, bdpc_obligatorio, bdpc_visiblerender)
	VALUES('CAJA_CIERRE', 'CAJA_CIERRE', 'TURNO', 'CAJA', 1, 'Z', true, true);
INSERT INTO documentoplantillacaracteristica_dpcp (cdpc_llave, cdpc_plantilla, cdpc_nombre, cdpc_codigo, ndpc_orden, cdpc_formato, bdpc_editable, bdpc_visiblerender)
	VALUES('CAJA_CIERRE_CUENTA_T', 'CAJA_CIERRE', 'CUENTA A TRANSFERIR', 'CAJA_FUERTE', 2, 'C',  true, true);
INSERT INTO documentoplantillacaracteristica_dpcp (cdpc_llave, cdpc_plantilla, cdpc_nombre, cdpc_codigo, ndpc_orden, cdpc_formato, bdpc_editable, bdpc_visiblerender)
	VALUES('CAJA_CIERRE_VALOR_T', 'CAJA_CIERRE', 'VALOR A TRANSFERIR', 'VALOR', 3, 'N',  true, true);

--INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor) VALUES('CAJA');
INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
select 'CAJA_CIERRE_CUENTA_T','CAJA_CIERRE_CUENTA_T','CUENTA_CERRAR_CAJA', ccat_llave from catalogo_catp where ccat_tipo  = 'T' and ccat_estado = 'A' limit 1;

INSERT INTO documentoplantillacosto_dpcp (cdpc_llave, cdpc_plantilla, cdpc_valortotal) VALUES ('CAJA_CIERRE','CAJA_CIERRE', 'CAJA_CIERRE_VALOR_T');
--Crear estados (Abierto y cerrado)
INSERT INTO expedienteestado_exep (cexe_llave, nexe_nivel, cexe_nombre, cexe_plantilla, cexe_estadodocumento)VALUES('CAJA', 0, 'ABIERTA', 'CAJA', 'A');
INSERT INTO expedienteestado_exep (cexe_llave, nexe_nivel, cexe_nombre, cexe_plantilla, cexe_estadodocumento)VALUES('CIERRE', 1, 'CIERRE', 'CAJA', 'C');
--Crear transición (De abierto a cerrado)
INSERT INTO expedientetransicion_extp (cext_llave, cext_estadopartida, cext_estadollegada, cext_plantilla, bext_documentador, cext_campo)VALUES('CAJA', 'CAJA', 'CIERRE', 'CAJA_CIERRE', true, 'CAJA_CIERRE');
--Crear permisos a los roles
INSERT INTO documentoplantillarol_dprp (cdpr_llave, cdpr_plantilla, cdpr_rol, bdpr_crear, bdpr_listable, bdpr_rangofiltro)
	select substring('CAJA' || cerl_rolacceso, 0,32), 'CAJA', cerl_rolacceso, true, true, true from usuariorol_erlp  where cerl_usuario  in (select ccpu_usuario from cuentapermisousuario_cpup  where bcpu_validarturno  = true) GROUP BY cerl_rolacceso;
INSERT INTO documentoplantillarol_dprp (cdpr_llave, cdpr_plantilla, cdpr_rol, bdpr_crear,  bdpr_rangofiltro)
	select substring('CIERRE' || cerl_rolacceso, 0,32), 'CAJA_CIERRE', cerl_rolacceso, true, true from usuariorol_erlp  where cerl_usuario  in (select ccpu_usuario from cuentapermisousuario_cpup  where bcpu_validarturno  = true) GROUP BY cerl_rolacceso;

update documentoplantilla_dplp set cdpl_maquinaestados  = 'CAJA' where cdpl_llave ='CAJA';

update reportebase_rpbp set crpb_plantilla = 'CAJA' where crpb_codigo = 'TURNO_REPORTES';
update reportebase_rpbp set crpb_plantilla = 'CAJA_CIERRE' where crpb_codigo = 'CAJA1';

delete from reportebase_rpbp where crpb_estado  = 'I';

delete from reportebase_rpbp where crpb_llave = 'POS008-TRANFER';

INSERT INTO consecutivo_conp(ccon_llave, ccon_nombre, ccon_prefijo, mcon_numeroinicial,  mcon_numeroactual)
    	select crpb_llave, crpb_nombre, crpb_llave || '-', 100, 100 from reportebase_rpbp where crpb_plantilla is null and (select ccon_llave from consecutivo_conp where ccon_llave  = crpb_llave) is null;
INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_codigo, cdpl_tipo)
    	select crpb_llave, crpb_nombre, crpb_llave, 'http://golyat.cloud/imagenes/modulo.png', crpb_llave, 'R' from reportebase_rpbp where crpb_plantilla is null;
INSERT INTO documentoplantillarol_dprp(cdpr_llave, cdpr_plantilla, cdpr_rol, bdpr_listable, bdpr_rangofiltro, bdpr_vertodos)
    	select substring(crpb_llave ||crac_llave,1,32), crpb_llave, crac_llave, true, true, true from rolacceso_racp, reportebase_rpbp where crpb_plantilla is null; 

update reportebase_rpbp set crpb_plantilla = crpb_llave where crpb_plantilla is null;

ALTER TABLE reportebase_rpbp
	ALTER COLUMN crpb_plantilla SET NOT NULL;
