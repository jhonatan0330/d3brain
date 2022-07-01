
INSERT INTO usuario_usrp(cusr_llave, cusr_identificacion, cusr_nombre, cusr_imagen) VALUES ('SYSTEM', 'SYSTEM', 'SISTEMA', 'http://golyat.cloud/imagenes/avatar.png');
INSERT INTO usuario_usrp(cusr_llave, cusr_identificacion, cusr_nombre, cusr_imagen) VALUES ('PROCESS', 'PROCESS', 'PROCESS', 'http://golyat.cloud/imagenes/avatar.png');
INSERT INTO usuarioautenticacion_uaup(cuau_llave, cuau_usuario, cuau_sesion, cuau_clave)VALUES ('SYSTEM', 'SYSTEM', '1', '1');
--Modulos
INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_url, cmod_estado)
    VALUES ('AdministracionLogisticpymes', 'Administracion', 'com.softure.logisticpymes.view.ui.UIAdministracion', 'A');
INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_url, cmod_estado)
    VALUES ('Inventarios', 'INVENTARIOS', 'com.softure.logisticpymes.view.ui.UIInventario', 'A');
INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
    VALUES ('Productos', 'PRODUCTOS', 'Control de los productos de una empresa', 'com.softure.logisticpymes.view.ui.UIProducto', 'A');
INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
    VALUES ('UITarifario', 'UITarifario', 'UITarifario', 'com.softure.logisticpymes.view.ui.UITarifario', 'A');
INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
    VALUES ('UIVotacion', 'UIVotacion', 'PLantilla para entrar a votar', 'com.softure.logisticpymes.view.ui.UIVotacion', 'A');
INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
    VALUES ('UIVotantes', 'UIVotantes', 'Configurar encuestas', 'com.softure.logisticpymes.view.ui.UIVotantes', 'A');

--
insert into pg_description (objoid, classoid, objsubid, description) select oid, 1259, 0, to_char(now(), 'yyyy-MM-dd') from pg_class where relname = 'usuario_usrp';
insert into pg_description (objoid, classoid, objsubid, description) select oid, 1259, 0, to_char(CURRENT_TIMESTAMP + CAST('1 Month' AS INTERVAL),'yyyy-MM-dd') from pg_class where relname = 'usuarioautenticacion_uaup';

INSERT INTO categoriaproducto_cprp (ccpr_llave, ccpr_nombre) VALUES('GENERAL', 'GENERAL');
INSERT INTO tarifario_trfp(ctrf_llave, ctrf_nombre)VALUES ('GENERAL', 'GENERAL');
INSERT INTO cambio_cmbp (ccmb_llave, ccmb_nombre, ccmb_motivo, dcmb_fecha, dcmb_fechaaplicacion) 
	VALUES('SC-1', 'SC-1', 'CONFIGURACION INICIAL DEL SISTEMA', now(), now());

INSERT INTO proceso_prcp ( cprc_llave, cprc_nombre, cprc_codigo, cprc_objetivo, nprc_prioridad, cprc_tipo, cprc_imagen)
	VALUES ('SOPORTE', 'PROCESOS DE SOPORTE', 'SOPORTE', 'Agrupar los procesos que permiten realizar la mision de la empresa',100, 'A', 'http://golyat.cloud/imagenes/modulo.png');

INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_codigo, cdpl_nombre, cdpl_imagen, cdpl_objetivo, cdpl_proceso)
    VALUES ('ADMINISTRADOR', 'ADM', 'BPM ADMINISTRADOR', 'http://golyat.cloud/imagenes/modulo.png', 'Rol inicial para configurar el aplicativo', 'SOPORTE');

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
	values ('49e5294011124e138dd7c661621866de','4e3014ec945e4c718dc50481220fcf80','E',NULL,'A','PROP_75',TIMESTAMP '2021-10-02 16:21:33.150000 -0500',TIMESTAMP '2021-10-02 16:21:33.125000 -0500', 'SC-1','C');
	
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto,  dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo) 
	VALUES( 'DESC_ADMINISTRADOR' , 'PROP_44', 'ADMINISTRADOR', 'ADMINISTRADOR-NM', 'NOMBRE', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'CONSE_ADMINISTRADOR' , 'PROP_48', 'ADMINISTRADOR', 'ADMINISTRADOR-ID', 'ID', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'ORDE_ADMINISTRADOR' , 'PROP_51', 'ADMINISTRADOR', 'N', 'NOMBRE', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'ADMINISTRADOR_TIPO_ROL' , 'PROP_141', 'ADMINISTRADOR', '1', now(), now(), 'SC-1', 'L');
	
INSERT INTO rolacceso_racp(crac_llave, brac_permisoscompletos, crac_plantilla)
    VALUES ('ADMINISTRADOR',  true, 'ADMINISTRADOR');

INSERT INTO modulocontratado_mdcp(cmdc_llave, cmdc_modulo, cmdc_nombre, cmdc_imagen)
    VALUES ('ADMINISTRACION', 'AdministracionLogisticpymes', 'ADMINISTRACION', 'http://golyat.cloud/imagenes/modulo.png');
    
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



insert into documentoplantilla_dplp (cdpl_llave,cdpl_nombre,cdpl_imagen,cdpl_codigo,cdpl_objetivo,cdpl_proceso) 
values ('57c7788e671b4bc192fe2700ad71378d','COMPORTAMIENTO GENERAL DEL SISTEMA','http://golyat.cloud/imagenes/modulo.png','D228','.','SOPORTE');

-- -------------------
-- INSERTS for public.documentoplantillacaracteristica_dpcp
-- -------------------
insert into documentoplantillacaracteristica_dpcp (cdpc_llave,cdpc_plantilla,ndpc_orden,cdpc_nombre,cdpc_codigo,cdpc_formato,cdpc_objetivo) values ('147cac472c604deaa943c7a6cb5d3893','57c7788e671b4bc192fe2700ad71378d',1,'FECHA','FECHA','F','Contiene las fechas del reporte');
insert into documentoplantillacaracteristica_dpcp (cdpc_llave,cdpc_plantilla,ndpc_orden,cdpc_nombre,cdpc_codigo,cdpc_formato,cdpc_objetivo) values ('d1c9f83727ea43f2ad97d90b0f4eb0f8','57c7788e671b4bc192fe2700ad71378d',2,'INTERESADOS','INTERESADOS','Z','.');

-- -------------------
-- INSERTS for public.mensajeplantillacorreo_mplp
-- -------------------

INSERT INTO servidor_serp (cser_llave, cser_nombre, cser_url, cser_usuario, cser_clave,  cser_tipo, nser_orden, cser_estado)
	select 'smtp.gmail.com', 'smtp.gmail.com', 'smtp.gmail.com',  'contacto@golyat.cloud', 'conde123',  'E', 1, 'A'
	WHERE NOT EXISTS (SELECT 1 FROM servidor_serp WHERE cser_llave='smtp.gmail.com');

insert into mensajeplantillacorreo_mplp (cmpl_llave,cmpl_texto,cmpl_estado,cmpl_nombre,cmpl_titulo,cmpl_servidor) values ('ea45ec023c754313a0714687efb633fb','seguimiento de sistema','A','SEGUIMIENTO DE SISTEMA','seguimiento de sistema','smtp.gmail.com');

insert into reportebase_rpbp (crpb_llave,crpb_nombre,crpb_estado,crpb_variables,crpb_plantilla,brpb_soloexistente,crpb_codigo,nrpb_version,crpb_descripcion,crpb_servidor,brpb_publico) values ('80ad573d0df04b358732e18834b532fb','COMPORTAMIENTO GENERAL DEL SISTEMA','A',NULL,'57c7788e671b4bc192fe2700ad71378d',false,'D228',0,'PENDIENTE',NULL,false);

insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('f25e50668a944633ab3476ffff6ec291','57c7788e671b4bc192fe2700ad71378d','1',NULL,'A','PROP_142',TIMESTAMP '2021-10-02 07:11:35.573000 -0500',TIMESTAMP '2021-10-02 07:11:35.570000 -0500',NULL,'SC-1',NULL,'L',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('e185c5a0fa144aff8ae8fe9d59310c16','147cac472c604deaa943c7a6cb5d3893','*',NULL,'A','PROP_25',TIMESTAMP '2021-10-02 07:11:35.699000 -0500',TIMESTAMP '2021-10-02 07:11:35.696000 -0500',NULL,'SC-1',NULL,'C',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('b3c95a486d1f40acbc317dbb668a4d1f','57c7788e671b4bc192fe2700ad71378d','15','00:00:07:00:00','A','PROP_185',TIMESTAMP '2021-10-02 07:11:35.730000 -0500',TIMESTAMP '2021-10-02 07:11:35.727000 -0500','Pasar a tabla historico','SC-1',NULL,'L',NULL,TIMESTAMP '2021-10-02 07:11:35.722000 -0500',NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('607904404ea94ef89e486945563bb843','147cac472c604deaa943c7a6cb5d3893','1',NULL,'A','PROP_134',TIMESTAMP '2021-10-02 07:11:49.687000 -0500',TIMESTAMP '2021-10-02 07:11:49.685000 -0500',NULL,'SC-1',NULL,'C',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('0ef9a69c966a488681689637ba18b2da','d1c9f83727ea43f2ad97d90b0f4eb0f8','1',NULL,'A','PROP_32',TIMESTAMP '2021-10-02 07:32:49.426000 -0500',TIMESTAMP '2021-10-02 07:32:49.423000 -0500',NULL,'SC-1',NULL,'C',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('27bceb25b46e4de7bd10aae3454e3457','57c7788e671b4bc192fe2700ad71378d','begin
return query
	select administrador.* from pedidoventa_pdvp administrador
	where ''ADMINISTRADOR'' = cpdv_plantilla and cpdv_estado = ''A'';
end;','00:00:01:00:00','A','PROP_189',TIMESTAMP '2021-10-02 07:33:12.388000 -0500',TIMESTAMP '2021-10-02 07:33:12.388000 -0500','Consulta los usuarios de soporte','SC-1',NULL,'L',NULL,TIMESTAMP '2021-10-02 04:00:00.000000 -0500',NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('99edc409f74d43418f18dfbc9497eccc','d1c9f83727ea43f2ad97d90b0f4eb0f8','1',NULL,'A','PROP_134',TIMESTAMP '2021-10-02 08:28:42.609000 -0500',TIMESTAMP '2021-10-02 08:28:42.606000 -0500',NULL,'SC-1',NULL,'C',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('31525f362deb45e4b4fe64c8871b4af0','80ad573d0df04b358732e18834b532fb','<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.14.0.final using JasperReports Library version 6.14.0-2ab0d8625be255bf609c78e1181801213e51db8f  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="CS010" pageWidth="792" pageHeight="612" orientation="Landscape" whenNoDataType="AllSectionsNoDetail" columnWidth="752" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="3fed3734-da88-4606-b5e1-825d03b1e6fe">
	<style name="BorderChartStyle">
		<box padding="4">
			<pen lineWidth="5.0" lineColor="#CCCCCC"/>
		</box>
	</style>
	<style name="BoxStyle" vTextAlign="Middle">
		<box leftPadding="3" rightPadding="3">
			<pen lineWidth="0.5" lineStyle="Solid"/>
		</box>
	</style>
	<style name="BoxGrayStyle" style="BoxStyle" mode="Opaque" backcolor="#CCCCCC" isBold="true"/>
	<style name="BoxHeaderStyle" style="BoxStyle" hTextAlign="Center" fontSize="8"/>
	<style name="BoxGrayHeaderStyle" style="BoxGrayStyle" fontSize="5"/>
	<subDataset name="D_PLANTILLA" uuid="5fc41db9-2d9f-467e-a451-14f79e3e0a01">
		<parameter name="P_FECHA_INICIO_PLANTILLA" class="java.util.Date"/>
		<parameter name="P_FECHA_FIN_PLANTILLA" class="java.util.Date"/>
		<queryString language="SQL">
			<![CDATA[select cdpl_nombre as plantilla, count(*) as cantidad from pedidoventa_pdvp inner join documentoplantilla_dplp on (cdpl_llave = cpdv_plantilla) where dpdv_fecharegistro >= $P{P_FECHA_INICIO_PLANTILLA} and dpdv_fecharegistro < $P{P_FECHA_FIN_PLANTILLA} group by cdpl_nombre order by 2 desc]]>
		</queryString>
		<field name="plantilla" class="java.lang.String"/>
		<field name="cantidad" class="java.lang.Long"/>
	</subDataset>
	<subDataset name="D_LAST_DAYS" uuid="69cc7dcf-5357-4462-be0c-553bfecb5a1a">
		<parameter name="P_FECHA_INICIO_LAST" class="java.util.Date"/>
		<parameter name="P_FECHA_START_LAST" class="java.util.Date">
			<defaultValueExpression><![CDATA[new Date($P{P_FECHA_INICIO_LAST}.getTime() - 30L*24*60*60*1000)]]></defaultValueExpression>
		</parameter>
		<queryString language="SQL">
			<![CDATA[select ''SOFT'' as name ,dpdv_fecharegistro::date as fecha ,count(*) as cantidad from pedidoventa_pdvp inner join documentoplantilla_dplp on (cdpl_llave = cpdv_plantilla) where dpdv_fecharegistro >= $P{P_FECHA_START_LAST} and dpdv_fecharegistro < $P{P_FECHA_INICIO_LAST} group by 1,2 order by 2]]>
		</queryString>
		<field name="name" class="java.lang.String"/>
		<field name="fecha" class="java.sql.Date"/>
		<field name="cantidad" class="java.lang.Long"/>
	</subDataset>
	<subDataset name="D_USERS" uuid="2cb5712c-9370-4e0c-b891-fa5ae9ae04fc">
		<parameter name="P_FECHA_INICIO_USER" class="java.util.Date"/>
		<parameter name="P_FECHA_FIN_USER" class="java.util.Date"/>
		<queryString language="SQL">
			<![CDATA[select cusr_nombre as usuario, count(*) as cantidad from pedidoventa_pdvp inner join usuario_usrp on (cusr_llave = cpdv_funcionario) where dpdv_fecharegistro >= $P{P_FECHA_INICIO_USER} and dpdv_fecharegistro < $P{P_FECHA_FIN_USER} group by cusr_nombre order by 2 desc limit 10]]>
		</queryString>
		<field name="usuario" class="java.lang.String"/>
		<field name="cantidad" class="java.lang.Long"/>
	</subDataset>
	<subDataset name="D_HOURS" uuid="0a357fa4-de58-4875-b0fd-75921b8f053e">
		<parameter name="P_FECHA_INICIO_HORAS" class="java.util.Date"/>
		<parameter name="P_FECHA_FIN_HORAS" class="java.util.Date"/>
		<queryString language="SQL">
			<![CDATA[select hour_it as hora 
,(select count(*) from pedidoventa_pdvp 
	where dpdv_fecharegistro >= $P{P_FECHA_INICIO_HORAS} 
	and dpdv_fecharegistro < $P{P_FECHA_FIN_HORAS} 
	and extract(hour from dpdv_fecharegistro) = hour_it
	) as cantidad 
from generate_series( 0, 23 ) hour_it 
order by 1]]>
		</queryString>
		<field name="hora" class="java.lang.Integer"/>
		<field name="cantidad" class="java.lang.Long"/>
	</subDataset>
	<parameter name="P_FECHA_INICIO" class="java.util.Date" isForPrompting="false">
		<defaultValueExpression><![CDATA[new Date(new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(),0,0,0).getTime() - (60 * 60 * 24 * 1000))]]></defaultValueExpression>
	</parameter>
	<parameter name="P_FECHA_FIN" class="java.util.Date" isForPrompting="false">
		<defaultValueExpression><![CDATA[new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(),0,0,0)]]></defaultValueExpression>
	</parameter>
	<queryString>
		<![CDATA[select corg_nombre as nombre from organizacion_orgp where corg_principal is null and corg_estado = ''A'']]>
	</queryString>
	<field name="nombre" class="java.lang.String"/>
	<pageHeader>
		<band height="40" splitType="Stretch">
			<staticText>
				<reportElement style="BoxStyle" x="0" y="8" width="368" height="20" uuid="45df7ac7-e697-4efa-93d4-1e43af611d1e"/>
				<text><![CDATA[COMPORTAMIENTO GENERAL DEL SISTEMA ]]></text>
			</staticText>
			<staticText>
				<reportElement style="BoxHeaderStyle" x="0" y="28" width="368" height="12" uuid="e2a2eb1c-9b7b-454c-8ae5-9e9f2e6356b6"/>
				<text><![CDATA[CANTIDAD DE REGISTROS CONSUMIDOS EN EL SISTEMA ]]></text>
			</staticText>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="0" y="0" width="368" height="8" uuid="a55d307c-b398-420f-b738-9cbb59bbe04e"/>
				<text><![CDATA[NOMBRE DEL REPORTE ]]></text>
			</staticText>
			<textField evaluationTime="Report" pattern="yyyy.MM.dd hh:mm:ss aaa" isBlankWhenNull="true">
				<reportElement style="BoxHeaderStyle" x="368" y="8" width="146" height="12" uuid="a4b4488c-f731-4d7e-b138-f820a5f9d02a"/>
				<textFieldExpression><![CDATA[new java.util.Date()]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="false">
				<reportElement style="BoxHeaderStyle" x="368" y="28" width="73" height="12" uuid="6125b249-08e5-43c2-ac7b-f2a35325a910"/>
				<box>
					<rightPen lineWidth="0.0"/>
				</box>
				<textElement textAlignment="Right"/>
				<textFieldExpression><![CDATA["Pagina " + $V{PAGE_NUMBER} + " de "]]></textFieldExpression>
			</textField>
			<textField evaluationTime="Report">
				<reportElement style="BoxHeaderStyle" x="441" y="28" width="73" height="12" uuid="62276392-ec15-4727-927f-b53c10c632d2"/>
				<box>
					<leftPen lineWidth="0.0"/>
				</box>
				<textElement textAlignment="Left"/>
				<textFieldExpression><![CDATA[" " + $V{PAGE_NUMBER}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="368" y="0" width="146" height="8" uuid="31b0be73-4eb5-442f-a40c-6f6126bac9b1"/>
				<text><![CDATA[FECHA DE IMPRESION ]]></text>
			</staticText>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="368" y="20" width="146" height="8" uuid="061b1ff1-4e06-4e2a-b4aa-497c779d4093"/>
				<text><![CDATA[PAGINACIÓN ]]></text>
			</staticText>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="514" y="0" width="238" height="8" uuid="64286def-980f-4130-9eec-f354ca78c09a"/>
				<text><![CDATA[PARAMETROS DEL INFORME ]]></text>
			</staticText>
			<staticText>
				<reportElement style="BoxGrayStyle" x="514" y="24" width="80" height="16" uuid="9736a5fe-64d6-4440-bded-cf15848b88ac"/>
				<textElement>
					<font size="8"/>
				</textElement>
				<text><![CDATA[FECHA FIN ]]></text>
			</staticText>
			<staticText>
				<reportElement style="BoxGrayStyle" x="514" y="8" width="80" height="16" uuid="80fc04ca-63a6-4555-846a-a6985889deb7"/>
				<textElement>
					<font size="8"/>
				</textElement>
				<text><![CDATA[FECHA DE INICIO ]]></text>
			</staticText>
			<textField pattern="MMM d, yyyy h:mm:ss a">
				<reportElement style="BoxStyle" x="594" y="8" width="158" height="16" uuid="a813d9fb-f318-495c-823f-d633dd8a7eb4"/>
				<textElement textAlignment="Right"/>
				<textFieldExpression><![CDATA[$P{P_FECHA_INICIO}]]></textFieldExpression>
			</textField>
			<textField pattern="MMM d, yyyy h:mm:ss a">
				<reportElement style="BoxStyle" x="594" y="24" width="158" height="16" uuid="d0748856-dd6e-48d2-b430-d461c0f61bd4"/>
				<textElement textAlignment="Right"/>
				<textFieldExpression><![CDATA[$P{P_FECHA_FIN}]]></textFieldExpression>
			</textField>
		</band>
	</pageHeader>
	<detail>
		<band height="532">
			<frame>
				<reportElement x="2" y="30" width="511" height="480" isPrintInFirstWholeBand="true" isPrintWhenDetailOverflows="true" uuid="cd278f0f-7891-4d4f-8009-e64930a9bf11"/>
				<frame>
					<reportElement x="0" y="0" width="511" height="160" uuid="af78cc27-ee1c-4e4c-b57b-46524642af73">
						<property name="ShowOutOfBoundContent" value="false"/>
					</reportElement>
					<barChart>
						<chart isShowLegend="false" evaluationTime="Report">
							<reportElement style="BorderChartStyle" x="0" y="0" width="511" height="160" uuid="c45bb1f2-8515-44e3-81a4-e30511557e60"/>
							<chartTitle>
								<font size="14" isBold="true"/>
								<titleExpression><![CDATA["COMPORTAMIENTO DEL SISTEMA POR HORAS"]]></titleExpression>
							</chartTitle>
							<chartSubtitle/>
							<chartLegend/>
						</chart>
						<categoryDataset>
							<dataset resetType="Report">
								<datasetRun subDataset="D_HOURS" uuid="59608f2f-fcb0-487f-9e6f-7f8dd8ce2d9c">
									<datasetParameter name="P_FECHA_FIN_HORAS">
										<datasetParameterExpression><![CDATA[$P{P_FECHA_FIN}]]></datasetParameterExpression>
									</datasetParameter>
									<datasetParameter name="P_FECHA_INICIO_HORAS">
										<datasetParameterExpression><![CDATA[$P{P_FECHA_INICIO}]]></datasetParameterExpression>
									</datasetParameter>
									<connectionExpression><![CDATA[$P{REPORT_CONNECTION}]]></connectionExpression>
								</datasetRun>
							</dataset>
							<categorySeries>
								<seriesExpression><![CDATA[$F{hora}]]></seriesExpression>
								<categoryExpression><![CDATA[$F{hora}.intValue()]]></categoryExpression>
								<valueExpression><![CDATA[$F{cantidad}]]></valueExpression>
								<labelExpression><![CDATA[$F{cantidad}.toString()]]></labelExpression>
							</categorySeries>
						</categoryDataset>
						<barPlot isShowLabels="true" isShowTickLabels="true" isShowTickMarks="false">
							<plot/>
							<itemLabel/>
							<categoryAxisFormat>
								<axisFormat labelColor="#000000" tickLabelColor="#000000" verticalTickLabels="false" axisLineColor="#000000"/>
							</categoryAxisFormat>
							<valueAxisFormat>
								<axisFormat labelColor="#000000" tickLabelColor="#000000" verticalTickLabels="true" axisLineColor="#000000"/>
							</valueAxisFormat>
						</barPlot>
					</barChart>
				</frame>
				<frame>
					<reportElement x="0" y="160" width="511" height="160" uuid="9dcb1545-f5c4-4717-9672-e7f0b47fb1f7"/>
					<barChart>
						<chart isShowLegend="true">
							<reportElement style="BorderChartStyle" x="0" y="0" width="511" height="160" uuid="a1dc9d41-5d62-4a5a-9075-7b5298d9d5b2"/>
							<chartTitle>
								<font size="14" isBold="true"/>
								<titleExpression><![CDATA["TOP 10 PERSONAS CREADORES"]]></titleExpression>
							</chartTitle>
							<chartSubtitle/>
							<chartLegend position="Right">
								<font size="6"/>
							</chartLegend>
						</chart>
						<categoryDataset>
							<dataset resetType="Report">
								<datasetRun subDataset="D_USERS" uuid="0cd092b3-3fa3-4506-a846-19f8635f4712">
									<datasetParameter name="P_FECHA_FIN_USER">
										<datasetParameterExpression><![CDATA[$P{P_FECHA_FIN}]]></datasetParameterExpression>
									</datasetParameter>
									<datasetParameter name="P_FECHA_INICIO_USER">
										<datasetParameterExpression><![CDATA[$P{P_FECHA_INICIO}]]></datasetParameterExpression>
									</datasetParameter>
								</datasetRun>
							</dataset>
							<categorySeries>
								<seriesExpression><![CDATA[$F{usuario}]]></seriesExpression>
								<categoryExpression><![CDATA[0]]></categoryExpression>
								<valueExpression><![CDATA[$F{cantidad}]]></valueExpression>
							</categorySeries>
						</categoryDataset>
						<barPlot isShowLabels="true" isShowTickLabels="false" isShowTickMarks="false">
							<plot/>
							<itemLabel/>
							<categoryAxisFormat>
								<axisFormat labelColor="#000000" tickLabelColor="#000000" axisLineColor="#000000"/>
							</categoryAxisFormat>
							<valueAxisFormat>
								<axisFormat labelColor="#000000" tickLabelColor="#000000" axisLineColor="#000000"/>
							</valueAxisFormat>
						</barPlot>
					</barChart>
				</frame>
				<timeSeriesChart>
					<chart isShowLegend="false" evaluationTime="Report" theme="aegean">
						<reportElement style="BorderChartStyle" x="0" y="320" width="510" height="160" uuid="d6407d2b-20eb-4b75-97a5-1b1780f2fedb"/>
						<chartTitle>
							<titleExpression><![CDATA["COMPORTAMIENTO ULTIMOS 30 DIAS "]]></titleExpression>
						</chartTitle>
						<chartSubtitle/>
						<chartLegend/>
					</chart>
					<timeSeriesDataset>
						<dataset resetType="Report">
							<datasetRun subDataset="D_LAST_DAYS" uuid="a9a5b3fe-d4a5-4cd7-8f9c-5ca613196cce">
								<datasetParameter name="P_FECHA_INICIO_LAST">
									<datasetParameterExpression><![CDATA[$P{P_FECHA_FIN}]]></datasetParameterExpression>
								</datasetParameter>
								<connectionExpression><![CDATA[$P{REPORT_CONNECTION}]]></connectionExpression>
							</datasetRun>
						</dataset>
						<timeSeries>
							<seriesExpression><![CDATA[$F{name}]]></seriesExpression>
							<timePeriodExpression><![CDATA[$F{fecha}]]></timePeriodExpression>
							<valueExpression><![CDATA[$F{cantidad}]]></valueExpression>
							<labelExpression><![CDATA[$F{cantidad}.toString()]]></labelExpression>
						</timeSeries>
					</timeSeriesDataset>
					<timeSeriesPlot isShowLines="true" isShowShapes="false">
						<plot/>
						<timeAxisFormat>
							<axisFormat labelColor="#000000" tickLabelColor="#000000" verticalTickLabels="true" axisLineColor="#000000"/>
						</timeAxisFormat>
						<valueAxisFormat>
							<axisFormat labelColor="#000000" tickLabelColor="#000000" verticalTickLabels="false" axisLineColor="#000000"/>
						</valueAxisFormat>
					</timeSeriesPlot>
				</timeSeriesChart>
			</frame>
			<frame>
				<reportElement style="BorderChartStyle" x="513" y="30" width="239" height="480" isPrintWhenDetailOverflows="true" uuid="407af32d-9eff-4560-b99d-efc0f5b6cf14">
					<property name="ShowOutOfBoundContent" value="false"/>
				</reportElement>
				<componentElement>
					<reportElement stretchType="ContainerHeight" x="0" y="0" width="231" height="472" isPrintWhenDetailOverflows="true" uuid="81efbad1-2c5f-466d-8a30-81f9331c4328"/>
					<jr:table xmlns:jr="http://jasperreports.sourceforge.net/jasperreports/components" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports/components http://jasperreports.sourceforge.net/xsd/components.xsd">
						<datasetRun subDataset="D_PLANTILLA" uuid="1491f024-ea3a-4226-81ed-9291eeb8b8b6">
							<datasetParameter name="P_FECHA_INICIO_PLANTILLA">
								<datasetParameterExpression><![CDATA[$P{P_FECHA_INICIO}]]></datasetParameterExpression>
							</datasetParameter>
							<datasetParameter name="P_FECHA_FIN_PLANTILLA">
								<datasetParameterExpression><![CDATA[$P{P_FECHA_FIN}]]></datasetParameterExpression>
							</datasetParameter>
							<connectionExpression><![CDATA[$P{REPORT_CONNECTION}]]></connectionExpression>
						</datasetRun>
						<jr:column width="172" uuid="1a4d1e00-9f21-449c-a2a1-cc1941308195">
							<jr:columnHeader height="14" rowSpan="1">
								<staticText>
									<reportElement x="0" y="0" width="172" height="14" uuid="3f068aab-3676-4c94-b6a0-3f4fb2d7fef7"/>
									<text><![CDATA[PLANTILLA ]]></text>
								</staticText>
							</jr:columnHeader>
							<jr:detailCell height="16">
								<textField pattern="yyyy-MM-dd">
									<reportElement x="0" y="0" width="172" height="16" uuid="0c587a60-fcd8-43ef-9125-aa04553a49b2"/>
									<box leftPadding="3" rightPadding="3"/>
									<textElement verticalAlignment="Middle">
										<font size="9"/>
									</textElement>
									<textFieldExpression><![CDATA[$F{plantilla}]]></textFieldExpression>
								</textField>
							</jr:detailCell>
						</jr:column>
						<jr:column width="54" uuid="e79cc443-14b9-490a-ad80-3ff60e072fca">
							<jr:columnHeader height="14" rowSpan="1">
								<staticText>
									<reportElement x="0" y="0" width="54" height="14" uuid="bfcf9eca-2cc1-4dee-a25e-a0e0bd72174a"/>
									<textElement textAlignment="Right"/>
									<text><![CDATA[CANT.]]></text>
								</staticText>
							</jr:columnHeader>
							<jr:detailCell height="16">
								<textField>
									<reportElement x="0" y="0" width="54" height="16" uuid="82bdd796-5b44-4456-98e7-459de73cedc9"/>
									<box leftPadding="3" rightPadding="3"/>
									<textElement textAlignment="Right" verticalAlignment="Middle">
										<font size="9"/>
									</textElement>
									<textFieldExpression><![CDATA[$F{cantidad}]]></textFieldExpression>
								</textField>
							</jr:detailCell>
						</jr:column>
					</jr:table>
				</componentElement>
			</frame>
			<textField>
				<reportElement style="BoxGrayStyle" x="0" y="0" width="752" height="30" isPrintWhenDetailOverflows="true" uuid="3d8d4fdf-473a-42ed-8cb5-39c7010e484b"/>
				<textElement textAlignment="Center">
					<font size="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{nombre}]]></textFieldExpression>
			</textField>
		</band>
	</detail>
</jasperReport>
',NULL,'A','PROP_138',TIMESTAMP '2021-10-02 08:47:11.903000 -0500',TIMESTAMP '2021-10-02 08:47:11.903000 -0500',NULL,'SC-1',NULL,'E',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('ca3c8d97bba8468a846057f5af6922f9','57c7788e671b4bc192fe2700ad71378d','ea45ec023c754313a0714687efb633fb','SEGUIMIENTO DE SISTEMA','A','PROP_57',TIMESTAMP '2021-10-02 08:51:04.226000 -0500',TIMESTAMP '2021-10-02 08:51:04.221000 -0500',NULL,'SC-1',NULL,'L',NULL,NULL,NULL,NULL,NULL);
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion,cppd_motivo,cppd_cambiocreacion,cppd_cambioeliminacion,cppd_tipo,cppd_rol,dppd_fechainicial,dppd_fechafinal,cppd_usuario,cppd_bloqueo) values ('ef5ebb5f3c784a9dab34c03e8142e6d6','57c7788e671b4bc192fe2700ad71378d','*',NULL,'A','PROP_117',TIMESTAMP '2021-10-02 08:58:29.489000 -0500',TIMESTAMP '2021-10-02 08:58:29.489000 -0500',NULL,'SC-1',NULL,'L',NULL,NULL,NULL,NULL,NULL);
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_estado, cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo) 
	VALUES('80553c75b0f44b2ca8179347e3ff031d', '57c7788e671b4bc192fe2700ad71378d', '80ad573d0df04b358732e18834b532fb', 'COMPORTAMIENTO GENERAL DEL SISTEMA', 'A', 'PROP_113', '2021-10-02 16:52:41.261', '2021-10-02 16:52:41.152', 'SC-1', 'L');

insert into relacioninterna_ritp (crit_llave,crit_estado,crit_propiedad,crit_plantilla,crit_campo,crit_auxiliar) values ('e151cf6ad9d84088a7faf265bdfe6a0e','A','27bceb25b46e4de7bd10aae3454e3457','57c7788e671b4bc192fe2700ad71378d','d1c9f83727ea43f2ad97d90b0f4eb0f8',NULL);
insert into relacioninterna_ritp (crit_llave,crit_estado,crit_propiedad,crit_plantilla,crit_campo,crit_auxiliar) 
	values ('43a64e5a45da420d8d421a9a5c35c421','A','ef5ebb5f3c784a9dab34c03e8142e6d6','ADMINISTRADOR','4e3014ec945e4c718dc50481220fcf80',NULL);
INSERT INTO relacioninterna_ritp (crit_llave, crit_estado, crit_propiedad, crit_plantilla, crit_campo, crit_auxiliar) VALUES('04c7354220e045a997b06baead97cf96', 'A', 'ef5ebb5f3c784a9dab34c03e8142e6d6', '57c7788e671b4bc192fe2700ad71378d', 'd1c9f83727ea43f2ad97d90b0f4eb0f8', NULL);

CREATE OR REPLACE FUNCTION propiedad_27bceb25b46e4de7bd10aae3454e3457(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[])
  RETURNS SETOF pedidoventa_pdvp
  LANGUAGE plpgsql
AS
$body$
begin
return query
select administrador.* from pedidoventa_pdvp administrador
	where 'ADMINISTRADOR' = cpdv_plantilla and cpdv_estado = 'A';
end;
$body$
  VOLATILE
  COST 100
  ROWS 1000;