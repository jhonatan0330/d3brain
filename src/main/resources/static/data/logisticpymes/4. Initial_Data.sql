
INSERT INTO usuario_usrp(cusr_llave, cusr_identificacion, cusr_nombre, cusr_imagen, cusr_correo) VALUES ('SYSTEM', 'SYSTEM', 'SYSTEM', 'https://fs.softwareparati.com/imagenes/avatar.png', 'jhonatan.garcia@colombiansofture.com');
INSERT INTO usuario_usrp(cusr_llave, cusr_identificacion, cusr_nombre, cusr_imagen, cusr_correo) VALUES ('PROCESS', 'PROCESS', 'PROCESS', 'https://fs.softwareparati.com/avatar.png', 'jhonatan.garcia@colombiansofture.com');
INSERT INTO usuarioautenticacion_uaup(cuau_llave, cuau_usuario, cuau_sesion, cuau_clave)VALUES ('SYSTEM', 'SYSTEM', '1', '1');
--Modulos
INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_url, cmod_estado)
    VALUES ('AdministracionLogisticpymes', 'Administracion', 'com.softure.logisticpymes.view.ui.UIAdministracion', 'A');
-- INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('Inventarios', 'INVENTARIOS', 'com.softure.logisticpymes.view.ui.UIInventario', 'A');
--INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('Productos', 'PRODUCTOS', 'Control de los productos de una empresa', 'com.softure.logisticpymes.view.ui.UIProducto', 'A');
-- INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('UITarifario', 'UITarifario', 'UITarifario', 'com.softure.logisticpymes.view.ui.UITarifario', 'A');
-- INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('UIVotacion', 'UIVotacion', 'PLantilla para entrar a votar', 'com.softure.logisticpymes.view.ui.UIVotacion', 'A');
-- INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('UIVotantes', 'UIVotantes', 'Configurar encuestas', 'com.softure.logisticpymes.view.ui.UIVotantes', 'A');

--
insert into pg_description (objoid, classoid, objsubid, description) select oid, 1259, 0, '2023-07-13' from pg_class where relname = 'usuario_usrp';
insert into pg_description (objoid, classoid, objsubid, description) select oid, 1259, 0, to_char(CURRENT_TIMESTAMP + CAST('1 Month' AS INTERVAL),'yyyy-MM-dd') from pg_class where relname = 'usuarioautenticacion_uaup';

INSERT INTO categoriaproducto_cprp (ccpr_llave, ccpr_nombre) VALUES('GENERAL', 'GENERAL');
INSERT INTO tarifario_trfp(ctrf_llave, ctrf_nombre)VALUES ('GENERAL', 'GENERAL');
INSERT INTO cambio_cmbp (ccmb_llave, ccmb_nombre, ccmb_motivo, dcmb_fecha, dcmb_fechaaplicacion) 
	VALUES('SC-1', 'SC-1', 'CONFIGURACION INICIAL DEL SISTEMA', now(), now());

INSERT INTO proceso_prcp ( cprc_llave, cprc_nombre, cprc_codigo, cprc_objetivo, nprc_prioridad, cprc_tipo, cprc_imagen)
	VALUES ('SOPORTE', 'PROCESOS DE SOPORTE', 'SOPORTE', 'Agrupar los procesos que permiten realizar la mision de la empresa',100, 'A', 'https://fs.softwareaparati.com/imagenes/modulo.png');

INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_codigo, cdpl_nombre, cdpl_imagen, cdpl_objetivo, cdpl_proceso)
    VALUES ('ADMINISTRADOR', 'ADM', 'SOPORTE SOFTWARE PARA TI', 'https://file.softwareparati.com/softure/2019/12/28/f580bc7ca449440f8e9b581b252790c9.png', 'Rol inicial para configurar el aplicativo', 'SOPORTE');

INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, ndpc_orden, cdpc_nombre, cdpc_codigo, cdpc_formato, cdpc_objetivo)
    VALUES ('ADMINISTRADOR-ID', 'ADMINISTRADOR',  1, 'ID', 'ID', 'T', 'Contiene el numero de cedula del administrador');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
	VALUES( 'ADMINISTRADOR-ID_2', 'C', 'PROP_105', 'ADMINISTRADOR-ID', '1', 'Se tiene permisos para modificar el campo', now(), now(), 'SC-1'); 

INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, ndpc_orden, cdpc_nombre, cdpc_codigo, cdpc_formato, cdpc_objetivo)
    VALUES ('ADMINISTRADOR-NM', 'ADMINISTRADOR', 2, 'NOMBRE', 'NOMBRE', 'T', 'Contiene el nombre completo del administrador');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
	VALUES( 'ADMINISTRADOR-NM_2', 'C', 'PROP_105', 'ADMINISTRADOR-NM', '1', 'Se tiene permisos para modificar el campo', now(), now(), 'SC-1'); 

insert into documentoplantillacaracteristica_dpcp (cdpc_llave,cdpc_plantilla,cdpc_estado,ndpc_orden,cdpc_imagen,cdpc_nombre,cdpc_codigo,cdpc_formato,cdpc_objetivo) 
	values ('4e3014ec945e4c718dc50481220fcf80','ADMINISTRADOR','A',7,NULL,'CORREO','CORREO','T','.');
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo) 
	values ('49e5294011124e138dd7c661621866de','4e3014ec945e4c718dc50481220fcf80','E',NULL,'A','PROP_75',now(),now(), 'SC-1','C');
	
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto,  dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo) 
	VALUES( 'DESC_ADMINISTRADOR' , 'PROP_44', 'ADMINISTRADOR', 'ADMINISTRADOR-NM', 'NOMBRE', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'CONSE_ADMINISTRADOR' , 'PROP_48', 'ADMINISTRADOR', 'ADMINISTRADOR-ID', 'ID', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'ORDE_ADMINISTRADOR' , 'PROP_51', 'ADMINISTRADOR', 'N', 'NOMBRE', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'ADMINISTRADOR_TIPO_ROL' , 'PROP_141', 'ADMINISTRADOR', '1', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'CORREO_ADMINISTRADOR' , 'PROP_207', 'ADMINISTRADOR', '4e3014ec945e4c718dc50481220fcf80', 'CORREO', now(), now(), 'SC-1', 'L');
		
INSERT INTO rolacceso_racp(crac_llave, brac_permisoscompletos, crac_plantilla)
    VALUES ('ADMINISTRADOR',  true, 'ADMINISTRADOR');

INSERT INTO modulocontratado_mdcp(cmdc_llave, cmdc_modulo, cmdc_nombre, cmdc_imagen)
    VALUES ('ADMINISTRACION', 'AdministracionLogisticpymes', 'ADMINISTRACION', 'https://fs.softwareaparati.com/imagenes/modulo.png');
    
INSERT INTO permiso_perp(cper_llave, cper_rolacceso, cper_modulo)
    VALUES ('ADMINISTRADOR', 'ADMINISTRADOR', 'ADMINISTRACION');
    
INSERT INTO documentotransaccion_trap(ctra_llave, dtra_fecha, ctra_usuario)
	VALUES('SYSTEM', now(), 'SYSTEM');
	
INSERT INTO pedidoventa_pdvp(cpdv_llave, dpdv_fecharegistro, dpdv_fecha, cpdv_nombre, cpdv_plantilla, cpdv_funcionario, cpdv_transaccion )
    VALUES ('SYSTEM', current_timestamp, current_timestamp, 'SYSTEM', 'ADMINISTRADOR', 'SYSTEM', 'SYSTEM');

INSERT INTO pedidoventacaracteristica_pvcp(cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, cpvc_transaccionregistro)
    VALUES ('SYSTEM-ID', 'SYSTEM', 'ADMINISTRADOR-ID', 'SYSTEM', 'SYSTEM');

INSERT INTO pedidoventacaracteristica_pvcp(cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, cpvc_transaccionregistro)
    VALUES ('SYSTEM-NM', 'SYSTEM', 'ADMINISTRADOR-NM', 'SISTEMA', 'SYSTEM');
        
INSERT INTO usuariorol_erlp(cerl_llave, cerl_usuario, cerl_rolacceso, cerl_documento, derl_fechainicial)
    VALUES ('ADMINISTRADOR', 'SYSTEM', 'ADMINISTRADOR', 'SYSTEM', current_timestamp);
    
INSERT INTO organizacion_orgp (corg_llave, corg_nombre, corg_imagen, corg_slogan,  corg_mensajeingreso, corg_codigo, corg_usuariosystem) 
VALUES('ORG1', 'SOFTWARE PARA TI.COM', 'http://golyat.cloud/imagenes/fondo.png', 'Unificar, Simplificar, Optimizar', 'INGRESA TUS DATOS', 'SW42', 'PROCESS');

----------
-- INSERTS for public.mensajeplantillacorreo_mplp
-- -------------------

INSERT INTO servidor_serp (cser_llave, cser_nombre, cser_url, cser_usuario, cser_clave,  cser_tipo, nser_orden, cser_estado)
	select 'smtp.gmail.com', 'smtp.gmail.com', 'smtp.gmail.com',  'contacto@colombiansofture.com', '$ofture123***',  'E', 1, 'A'
	WHERE NOT EXISTS (SELECT 1 FROM servidor_serp WHERE cser_llave='smtp.gmail.com');

