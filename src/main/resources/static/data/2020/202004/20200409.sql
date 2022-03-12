
COMMENT ON TABLE usuario_usrp IS '2020-04-09';

COMMENT ON TABLE usuariosesion_ussp IS '2020.04.09.00';

ALTER TABLE documentorelaciongestor_drgp
	ADD COLUMN cdrg_usuario character varying(32);

ALTER TABLE organizacion_orgp
	ADD COLUMN corg_codigo character varying(20);

update organizacion_orgp set corg_codigo = 'NIT';
	
ALTER TABLE organizacion_orgp
	ALTER COLUMN corg_codigo set not null;

ALTER TABLE documentorelaciongestor_drgp
	ADD CONSTRAINT fk_documentorelaciongestorusuario FOREIGN KEY (cdrg_usuario) REFERENCES public.usuario_usrp(cusr_llave);

update documentorelaciongestor_drgp set cdrg_usuario =  (select cpdv_funcionario from pedidoventa_pdvp where cpdv_llave = cdrg_documentomodificador);

update documentorelaciongestor_drgp set cdrg_usuario =  (select cpdv_funcionario from pedidoventa_pdvp where cpdv_llave = cdrg_documentoprincipal) where cdrg_usuario is null;

ALTER TABLE documentorelaciongestor_drgp
	ALTER COLUMN cdrg_usuario set not null;

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo) 
	VALUES('PROP_133' , 'T', 'UBICACION', 'UBICACION', 'www.softwareparati.com', 'REQUISITO', 'Este campo define bajo la responsabilidad de quien se encuentra fisicamente el documento');

ALTER TABLE documentorelaciongestor_drgp
	ADD COLUMN ddrg_cierre timestamp with time zone;
	
	

INSERT INTO reportebase_rpbp(crpb_llave,  crpb_nombre,  crpb_jaspertext,  crpb_plantilla,  crpb_codigo,  nrpb_version,  crpb_descripcion, brpb_soloexistente)
VALUES(
  'DPL_REP_PORTRAIT',  'ENCABEZADO CARTA HORIZONTAL',  '<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.8.0.final using JasperReports Library version 6.8.0-2ed8dfabb690ff337a5797129f2cd92902b0c87b  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="HeaderP" columnCount="3" printOrder="Horizontal" pageWidth="752" pageHeight="572" orientation="Landscape" whenNoDataType="AllSectionsNoDetail" columnWidth="250" leftMargin="0" rightMargin="0" topMargin="0" bottomMargin="0" uuid="1aeb0a4c-fbb5-4d25-8128-4801ff105230">
	<style name="BoxStyle" vTextAlign="Middle">
		<box leftPadding="3" rightPadding="3">
			<pen lineWidth="1.0" lineStyle="Solid" lineColor="#CCCCCC"/>
		</box>
	</style>
	<style name="BoxGrayStyle" style="BoxStyle" mode="Opaque" backcolor="#CCCCCC" fontSize="8" isBold="true"/>
	<style name="BoxHeaderStyle" style="BoxStyle" hTextAlign="Center" fontSize="7"/>
	<style name="BoxGrayHeaderStyle" style="BoxGrayStyle" fontSize="5"/>
	<style name="DetailStyle" vTextAlign="Middle" fontSize="8">
		<box leftPadding="2" rightPadding="2"/>
	</style>
	<style name="DetailMoneyStyle" style="DetailStyle" hTextAlign="Right" pattern="$ #,##0"/>
	<style name="DetailNumberStyle" style="DetailStyle" hTextAlign="Right" pattern="#,##0.###"/>
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
		<band height="48">
			<textField>
				<reportElement x="0" y="8" width="341" height="20" uuid="b6f748f1-514e-497c-9008-b320c94fbf2c"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle" markup="none">
					<font size="12" isBold="true"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{nombre}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement mode="Opaque" x="0" y="0" width="341" height="8" backcolor="#CCCCCC" uuid="7cd4a348-1db6-4ca6-97f6-1533ad927e7b"/>
				<box leftPadding="3" rightPadding="4">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="5" isBold="true"/>
				</textElement>
				<text><![CDATA[TITULO DEL DOCUMENTO]]></text>
			</staticText>
			<staticText>
				<reportElement mode="Opaque" x="341" y="0" width="159" height="8" backcolor="#CCCCCC" uuid="04dae6f7-c69e-4de6-9e61-1b957c1eab98"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="5" isBold="true"/>
				</textElement>
				<text><![CDATA[CODIGO  DE FORMATO]]></text>
			</staticText>
			<staticText>
				<reportElement mode="Opaque" x="341" y="20" width="159" height="8" backcolor="#CCCCCC" uuid="e0f92130-1a4a-426c-9ab5-03c48b753d10"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="5" isBold="true"/>
				</textElement>
				<text><![CDATA[VERSION]]></text>
			</staticText>
			<textField>
				<reportElement x="341" y="8" width="159" height="12" uuid="2eea2fdc-2e19-41b0-a067-0c68a3bd69d3"/>
				<box>
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{codigo}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement x="341" y="28" width="159" height="12" uuid="2dd9253f-3d20-462d-8c28-7c7bf5798bfa"/>
				<box>
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{reporte_version}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement mode="Opaque" x="653" y="0" width="99" height="8" backcolor="#CCCCCC" uuid="33b3a9bc-a4b2-4788-89a6-b4447c881f49"/>
				<box leftPadding="3" rightPadding="4">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="5" isBold="true"/>
				</textElement>
				<text><![CDATA[ELABORADO POR]]></text>
			</staticText>
			<staticText>
				<reportElement mode="Opaque" x="653" y="20" width="99" height="8" backcolor="#CCCCCC" uuid="e5c3100a-6b4a-42e6-95b4-527f3f8b0662"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="5" isBold="true"/>
				</textElement>
				<text><![CDATA[APROBADO POR]]></text>
			</staticText>
			<staticText>
				<reportElement mode="Opaque" x="500" y="0" width="153" height="8" backcolor="#CCCCCC" uuid="e23a20f7-e1c6-4912-ada1-4f9ea7323fef"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="5" isBold="true"/>
				</textElement>
				<text><![CDATA[FECHA DE IMPRESION]]></text>
			</staticText>
			<staticText>
				<reportElement mode="Opaque" x="500" y="20" width="153" height="8" backcolor="#CCCCCC" uuid="06de9364-9684-4e19-ae2c-850d1d579610"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="5" isBold="true"/>
				</textElement>
				<text><![CDATA[PAGINACION]]></text>
			</staticText>
			<textField evaluationTime="Report" pattern="yyyy.MM.dd hh:mm:ss aaa" isBlankWhenNull="true">
				<reportElement key="textField" x="500" y="8" width="153" height="12" uuid="ac7bf66e-6e91-40aa-9fbe-bc9473c1cefa"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA[new java.util.Date()]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="false">
				<reportElementx="500" y="28" width="153" height="12" uuid="98fafd38-9296-40df-b3f0-922628758833"/>
				<box>
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.0"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA["Pagina " + $P{P_NUMBER_PAGE}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="653" y="28" width="99" height="12" uuid="fa585f14-bea2-45aa-8456-e948f0707da7"/>
				<box>
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement>
					<font size="8"/>
				</textElement>
			</staticText>
			<staticText>
				<reportElement x="653" y="8" width="99" height="12" uuid="a939d8f9-1f6f-4d3b-8524-c4195aa9b566"/>
				<box>
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement>
					<font size="8"/>
				</textElement>
			</staticText>
			<textField>
				<reportElement x="0" y="28" width="341" height="12" uuid="a409c1f4-498a-4215-9179-8c693e247690"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="0.5" lineColor="#000000"/>
					<rightPen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="6"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{descripcion}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="0" y="40" width="752" height="8" uuid="591cb485-88a9-4802-beb2-d0c34749320c"/>
				<text><![CDATA[PARAMETROS DEL INFORME]]></text>
			</staticText>
		</band>
	</pageHeader>
	<detail>
		<band height="14" splitType="Stretch">
			<printWhenExpression><![CDATA[$F{valor_parametro}!=null]]></printWhenExpression>
			<textField>
				<reportElement style="BoxGrayStyle" x="0" y="0" width="82" height="14" uuid="59e3b5da-4775-4e8f-a542-8190717d05de"/>
				<textFieldExpression><![CDATA[$F{parametro}]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement style="BoxStyle" x="82" y="0" width="204" height="14" uuid="2ec10bc6-cf24-4ebf-bdaa-63f2bc150070"/>
				<textFieldExpression><![CDATA[$F{valor_parametro}]]></textFieldExpression>
			</textField>
		</band>
	</detail>
	<columnFooter>
		<band height="1"/>
	</columnFooter>
	<summary>
		<band height="1">
			<line>
				<reportElement style="BoxStyle" x="0" y="0" width="752" height="1" uuid="c320d774-9fb6-49d9-aa76-d3971c71f146"/>
				<graphicElement>
					<pen lineStyle="Double" lineColor="#CCCCCC"/>
				</graphicElement>
			</line>
		</band>
	</summary>
</jasperReport>',  'DPL_REP',  'ENC_LETTER_P',  1,  'Servir como plantilla para todos los reportes tamano carta orientacion vertical', true);
