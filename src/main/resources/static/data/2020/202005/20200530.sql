COMMENT ON TABLE usuario_usrp IS '2020-05-30';

update propiedadvalordefinido_pvdp set cpvd_nombre = 'ENCABEZADO DOCUMENTO' where cpvd_llave = 'PROP_70';
update propiedadvalordefinido_pvdp set cpvd_nombre = 'OPCION EN EXCEL' where cpvd_llave = 'PROP_71';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo) 
	VALUES('PROP_137' , 'E', 'ENCABEZADO EN EXCEL', 'REPORTE_ENCABEZADO_EXCEL', 'www.softwareparati.com', 'REQUISITO', 'El reporte tendra el encabezado cuando se genere en excel');


INSERT INTO reportebase_rpbp(crpb_llave,  crpb_nombre,  crpb_jaspertext,  crpb_plantilla,  crpb_codigo,  nrpb_version,  crpb_descripcion, brpb_soloexistente)
VALUES(
  'DPL_ENC_EXCEL',  'ENCABEZADO EXCEL',  '<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.12.2.final using JasperReports Library version 6.12.2-75c5e90a222ab406e416cbf590a5397028a52de3  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="HeaderExcel" pageWidth="10" pageHeight="572" whenNoDataType="AllSectionsNoDetail" columnWidth="10" leftMargin="0" rightMargin="0" topMargin="0" bottomMargin="0" uuid="1aeb0a4c-fbb5-4d25-8128-4801ff105230">
	<parameter name="P_REPORTE" class="java.lang.String"/>
	<parameter name="P_KEY" class="java.lang.String"/>
	<parameter name="P_NUMBER_PAGE" class="java.lang.Integer"/>
	<queryString>
		<![CDATA[select 
	crpb_nombre as nombre
	,crpb_codigo as codigo
	,1 as reporte_version
	,crpb_descripcion as descripcion
	,cdpf_nombre as parametro
	,cpvc_valortext as valor_parametro
	,(select cdpl_imagen from documentoplantilla_dplp where cdpl_llave = crpb_plantilla) as imagen
from reportebase_rpbp
left join campo_documento on (cdrc_documento =  $P{P_KEY} )
where
crpb_llave =  $P{P_REPORTE}]]>
	</queryString>
	<field name="nombre" class="java.lang.String"/>
	<field name="codigo" class="java.lang.String"/>
	<field name="reporte_version" class="java.lang.Integer"/>
	<field name="descripcion" class="java.lang.String"/>
	<field name="parametro" class="java.lang.String"/>
	<field name="valor_parametro" class="java.lang.String"/>
	<field name="imagen" class="java.lang.String"/>
	<pageHeader>
		<band height="76">
			<textField>
				<reportElement x="0" y="0" width="10" height="20" isPrintWhenDetailOverflows="true" uuid="b6f748f1-514e-497c-9008-b320c94fbf2c"/>
				<textElement verticalAlignment="Middle">
					<font size="12" isBold="true"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{nombre}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement x="0" y="34" width="10" height="14" isPrintWhenDetailOverflows="true" uuid="2eea2fdc-2e19-41b0-a067-0c68a3bd69d3"/>
				<textElement verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{codigo}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement x="0" y="48" width="10" height="14" isPrintWhenDetailOverflows="true" uuid="2dd9253f-3d20-462d-8c28-7c7bf5798bfa"/>
				<textElement verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{reporte_version}]]></textFieldExpression>
			</textField>
			<textField evaluationTime="Report" pattern="yyyy.MM.dd hh:mm:ss aaa" isBlankWhenNull="true">
				<reportElement key="" x="0" y="62" width="10" height="14" isPrintWhenDetailOverflows="true" uuid="ac7bf66e-6e91-40aa-9fbe-bc9473c1cefa"/>
				<textElement verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[new java.util.Date()]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement x="0" y="20" width="10" height="14" isPrintWhenDetailOverflows="true" uuid="a409c1f4-498a-4215-9179-8c693e247690"/>
				<textElement verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{descripcion}]]></textFieldExpression>
			</textField>
		</band>
	</pageHeader>
	<detail>
		<band height="14" splitType="Stretch">
			<printWhenExpression><![CDATA[$F{valor_parametro}!=null]]></printWhenExpression>
			<textField>
				<reportElement x="0" y="0" width="10" height="14" isPrintWhenDetailOverflows="true" uuid="59e3b5da-4775-4e8f-a542-8190717d05de"/>
				<textFieldExpression><![CDATA[$F{parametro} + " : " + $F{valor_parametro}]]></textFieldExpression>
			</textField>
		</band>
	</detail>
</jasperReport>',  'DPL_REP',  'ENC_EXCEL',  1,  'Servir como plantilla para todos los reportes de excel', true);