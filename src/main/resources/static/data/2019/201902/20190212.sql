
COMMENT ON TABLE usuario_usrp IS '2019-02-12';
COMMENT ON TABLE usuariosesion_ussp IS '2019.02.12.00';

ALTER TABLE documentoplantillarol_dprp
	DROP COLUMN bdpr_sinrangofechas;
	
	
--REPORTE DE PERMISOS

select upsert_reporte('PER001', 'PERMISOS DE USUARIO', 'PEDIDO_VENTA_ROLES', '1',null, '<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="PER001" language="groovy" pageWidth="792" pageHeight="612" orientation="Landscape" whenNoDataType="AllSectionsNoDetail" columnWidth="752" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="8b4572b9-8cf1-421d-a4d5-c7887f914a3d">
	<parameter name="P_KEY" class="java.lang.String"/>
	<queryString>
		<![CDATA[SELECT
	CASE cdpl_tipo
		WHEN ''P'' THEN ''1. FORMULARIOS PRINCIPALES''
		WHEN ''F'' THEN ''2. FORMULARIOS AUXILIARES''
		WHEN ''L'' THEN ''3. LISTAS DE SELECCION''
		WHEN ''R'' THEN ''4. REPORTES''
	END as tipo,
	cdpl_nombre as plantilla,
	(select crac_nombre from rolacceso_racp where crac_llave = cdpr_rol) as rol,
	bdpr_listable as listable,
	ndpr_orden as orden,
	bdpr_crear as crear,
	bdpr_modificar as modificar,
	bdpr_eliminar as eliminar,
	bdpr_vertodos as ver_todos,
	bdpr_totalvisiblerender as total_visible,
	bdpr_iniciorapido as inicio_rapido
FROM documentoplantillarol_dprp, documentoplantilla_dplp
WHERE cdpl_llave = cdpr_plantilla and cdpl_estado = ''A''
	and cdpr_rol in (select usr.cerl_rolacceso from usuariorol_erlp usr where usr.cerl_usuario  = (select rol.cerl_usuario from usuariorol_erlp rol where rol.cerl_documento = $P{P_KEY}) and cerl_estado = ''A'') and cdpr_estado = ''A''
order by tipo, plantilla, orden, rol]]>
	</queryString>
	<field name="tipo" class="java.lang.String"/>
	<field name="plantilla" class="java.lang.String"/>
	<field name="rol" class="java.lang.String"/>
	<field name="listable" class="java.lang.Boolean"/>
	<field name="orden" class="java.lang.Integer"/>
	<field name="crear" class="java.lang.Boolean"/>
	<field name="modificar" class="java.lang.Boolean"/>
	<field name="eliminar" class="java.lang.Boolean"/>
	<field name="ver_todos" class="java.lang.Boolean"/>
	<field name="total_visible" class="java.lang.Boolean"/>
	<field name="inicio_rapido" class="java.lang.Boolean"/>
	<group name="G_TIPO">
		<groupExpression><![CDATA[$F{tipo}]]></groupExpression>
		<groupHeader>
			<band height="18">
				<textField>
					<reportElement mode="Opaque" x="0" y="0" width="752" height="18" backcolor="#CCCCCC" uuid="bfc2af7a-b62e-4309-9f82-0bdcb3eb3519"/>
					<box>
						<pen lineWidth="0.5"/>
						<topPen lineWidth="0.5"/>
						<leftPen lineWidth="0.5"/>
						<bottomPen lineWidth="0.5"/>
						<rightPen lineWidth="0.5"/>
					</box>
					<textElement textAlignment="Center" verticalAlignment="Middle">
						<font size="8" isBold="true"/>
					</textElement>
					<textFieldExpression><![CDATA[$F{tipo}]]></textFieldExpression>
				</textField>
			</band>
		</groupHeader>
	</group>
	<pageHeader>
		<band height="62">
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="482" y="0" width="90" height="9" backcolor="#CCCCCC" uuid="0a7e63db-f0b3-4365-b3d3-2bfbf6650160"/>
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
				<reportElement key="staticText-9" x="482" y="29" width="90" height="12" uuid="8d23e7fd-8274-4b04-96b1-2cb694920bcf"/>
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
				<text><![CDATA[1]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="662" y="21" width="90" height="8" backcolor="#CCCCCC" uuid="2cf3aa5b-ca02-41d5-8b4f-5109936b4695"/>
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
				<reportElement key="staticText-9" x="482" y="9" width="90" height="12" uuid="5a453772-99f1-437b-96d0-2608b721ba38"/>
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
				<text><![CDATA[PER001]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="0" y="0" width="482" height="9" backcolor="#CCCCCC" uuid="780611b4-4b93-4a86-a9ed-98cb765a52aa"/>
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
				<reportElement key="staticText-9" mode="Opaque" x="482" y="21" width="90" height="8" backcolor="#CCCCCC" uuid="0f23550b-b2c8-4e3f-a1e9-46d2c800f7fe"/>
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
			<textField isBlankWhenNull="false">
				<reportElement key="textField" x="572" y="29" width="62" height="12" uuid="27a5414f-5f02-4a88-bdd8-5f1219b4a98d"/>
				<box>
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.0"/>
				</box>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA["Pagina " + $V{PAGE_NUMBER} + " de "]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="662" y="41" width="45" height="21" backcolor="#CCCCCC" uuid="8de509cf-6517-4221-9c72-88614328c6cb"/>
				<box leftPadding="1" rightPadding="0">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[TOTAL VISIBLE]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="662" y="0" width="90" height="9" backcolor="#CCCCCC" uuid="a3e17be0-0286-4554-9757-10e58efb1afc"/>
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
				<reportElement key="staticText-9" mode="Opaque" x="572" y="21" width="90" height="8" backcolor="#CCCCCC" uuid="4c509d51-f768-47aa-8865-f9efff7b83a2"/>
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
				<text><![CDATA[PAGINACIÓN]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" x="0" y="29" width="482" height="12" uuid="80e610a7-64ab-48b0-a36c-07dfc262d5c8"/>
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
				<text><![CDATA[PERMISOS QUE TIENE CADA USUARIO PARA INTERACTUAR CON UN DOCUMENTO SEGÚN LOS ROLES A LOS CUALES SE ENCUENTRA ASIGNADO]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="572" y="0" width="90" height="9" backcolor="#CCCCCC" uuid="481bcd0e-fec2-4e06-afd1-efadf35cc882"/>
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
			<textField evaluationTime="Report" pattern="yyyy.MM.dd hh:mm:ss aaa" isBlankWhenNull="true">
				<reportElement key="textField" x="572" y="9" width="90" height="12" uuid="2485ce52-7d23-4843-a996-596c01798635"/>
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
			<staticText>
				<reportElement key="staticText-10" x="0" y="9" width="482" height="20" uuid="b985d9a0-4de8-4f23-9901-d553e522dddf"/>
				<box leftPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle" markup="none">
					<font size="12" isBold="true" pdfFontName="Helvetica-Bold"/>
				</textElement>
				<text><![CDATA[PERMISOS DE USUARIO]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="222" y="41" width="170" height="21" backcolor="#CCCCCC" uuid="d3ccf76d-70d3-4a10-bcf3-336cb752d8a8"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[ROL]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="0" y="41" width="222" height="21" backcolor="#CCCCCC" uuid="453c1448-8eba-414d-8cab-3540d2028674"/>
				<box leftPadding="3" rightPadding="3">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[DOCUMENTO]]></text>
			</staticText>
			<textField evaluationTime="Report">
				<reportElement x="634" y="29" width="28" height="12" uuid="c55a00c6-97db-48b3-870c-a2281be9386f"/>
				<box>
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.0"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA[" " + $V{PAGE_NUMBER}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="527" y="41" width="45" height="21" backcolor="#CCCCCC" uuid="88ee2ef6-09d3-4d9c-b580-f8cb96babe1d"/>
				<box leftPadding="1" rightPadding="0">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="7" isBold="false"/>
				</textElement>
				<text><![CDATA[MODIFICAR]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="707" y="41" width="45" height="21" backcolor="#CCCCCC" uuid="70d02066-dc4c-444e-b828-65e73a8efe44"/>
				<box leftPadding="1" rightPadding="0">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[INICIO RAPIDO]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" x="662" y="29" width="90" height="12" uuid="8155e2ef-a2c5-4b6e-bfb4-db969fd32265"/>
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
				<text><![CDATA[]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" x="662" y="9" width="90" height="12" uuid="6a25d83c-23fb-426b-a692-dd81b7a486cf"/>
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
				<text><![CDATA[]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="617" y="41" width="45" height="21" backcolor="#CCCCCC" uuid="bdf61608-97a1-4046-8f16-10ed45c50d74"/>
				<box leftPadding="1" rightPadding="0">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[VER TODOS]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="572" y="41" width="45" height="21" backcolor="#CCCCCC" uuid="cb70df2e-9d5c-4a6b-b9c4-fa1e056a7077"/>
				<box leftPadding="1" rightPadding="0">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[ELIMINAR]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="482" y="41" width="45" height="21" backcolor="#CCCCCC" uuid="ed4f1d58-80ea-420f-8ed2-61104af73e17"/>
				<box leftPadding="1" rightPadding="0">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[CREAR]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="437" y="41" width="45" height="21" backcolor="#CCCCCC" uuid="0112150e-7488-4b41-954c-df2988ebad68"/>
				<box leftPadding="1" rightPadding="0">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[ORDEN]]></text>
			</staticText>
			<staticText>
				<reportElement key="staticText-9" mode="Opaque" x="392" y="41" width="45" height="21" backcolor="#CCCCCC" uuid="53f54464-a6c2-4844-a31b-716cf7c33f0e"/>
				<box leftPadding="1" rightPadding="0">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[LISTABLE]]></text>
			</staticText>
		</band>
	</pageHeader>
	<detail>
		<band height="15" splitType="Stretch">
			<textField isStretchWithOverflow="true">
				<reportElement positionType="Float" stretchType="RelativeToBandHeight" x="0" y="0" width="222" height="15" isPrintInFirstWholeBand="true" isPrintWhenDetailOverflows="true" uuid="cfc73d48-2342-47c0-9389-c0d070979e0e"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{plantilla}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement stretchType="RelativeToBandHeight" x="222" y="0" width="170" height="15" uuid="58d9a830-8e88-4941-b31d-c7b89c82de64"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{rol}]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="392" y="0" width="45" height="15" uuid="a0022e20-fec2-41b6-bb2f-fdd7f922f579"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[($F{listable})?"X":null]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="437" y="0" width="45" height="15" uuid="b92dacc2-1362-4295-ade2-8d05cca5b659"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[($F{listable})?$F{orden}:null]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="482" y="0" width="45" height="15" uuid="66def48b-3988-46ac-8447-f23640adde3e"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[($F{crear})?"X":null]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="527" y="0" width="45" height="15" uuid="6dd8ef53-6296-469c-a3e1-3624a68993f8"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[($F{modificar})?"X":null]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="572" y="0" width="45" height="15" uuid="9b2c12ad-d410-49f9-a801-a7ae469f95d5"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[($F{eliminar})?"X":null]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="617" y="0" width="45" height="15" uuid="821623d0-4424-4043-9c1a-69477303bc2b"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[($F{ver_todos})?"X":null]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="662" y="0" width="45" height="15" uuid="e8e22783-894b-4405-8888-e47a6a5c24c7"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[($F{total_visible})?"X":null]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="707" y="0" width="45" height="15" uuid="c37b52f9-2591-461e-aa7a-ae33972da0ea"/>
				<box leftPadding="2" rightPadding="2">
					<pen lineWidth="0.5"/>
					<topPen lineWidth="0.5"/>
					<leftPen lineWidth="0.5"/>
					<bottomPen lineWidth="0.5"/>
					<rightPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[($F{inicio_rapido})?"X":null]]></textFieldExpression>
			</textField>
		</band>
	</detail>
</jasperReport>
');

-- REPORTE EXPORTAR REGISTROS

select upsert_reporte('CST003', 'EXPORTAR REGISTROS', null, '2',null, '<?xml version="1.0" encoding="UTF-8"?>
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="CST003" language="groovy" pageWidth="7920" pageHeight="612" orientation="Landscape" whenNoDataType="BlankPage" columnWidth="7880" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" isIgnorePagination="true" uuid="3361b6b7-6272-491e-b9a8-e7ba4f685eff">
	<style name="Crosstab Data Text" hAlign="Center"/>
	<parameter name="P_FECHA_INICIO" class="java.sql.Timestamp"/>
	<parameter name="P_FECHA_FIN" class="java.sql.Timestamp"/>
	<parameter name="P_PLANTILLA" class="java.lang.String"/>
	<queryString>
		<![CDATA[select
	cpdv_nombre
	,cdpc_nombre
	,(select cpvc_valortext from pedidoventacaracteristica_pvcp where cpdv_llave = cpvc_documento and cdpc_llave = cpvc_campo and cpvc_estado = ''A'') as cpvc_valortext
	,ndpc_orden
	,(select cusr_nombre from usuario_usrp where cusr_llave = cpdv_funcionario)
	,dpdv_fecharegistro
	,dpdv_fecha
	,CASE WHEN cpdv_estadoexpediente is null THEN
		CASE cpdv_estado WHEN ''A'' THEN ''ACTIVO''
		WHEN ''I'' THEN ''INACTIVO''
		WHEN ''C'' THEN ''FINALIZADO'' END
	ELSE (select cpes_nombre from procesoestado_pesp where cpes_llave = cpdv_estadoexpediente)
	END AS cpdv_estadoexpediente
	,(select mpvd_valortotal from pedidoventadinero_pvdp where cpvd_documento = cpdv_llave and cpvd_estado= ''A'') as total
from
	pedidoventa_pdvp, documentoplantillacaracteristica_dpcp
where
	cdpc_plantilla = $P{P_PLANTILLA} and cdpc_estado = ''A'' and cpdv_plantilla = $P{P_PLANTILLA}
	and ((select cdpl_tipo from documentoplantilla_dplp  where cdpl_llave = $P{P_PLANTILLA} and cdpl_tipo in (''O'',''L'')) is not null or (dpdv_fecha >= $P{P_FECHA_INICIO} and dpdv_fecha < $P{P_FECHA_FIN}))]]>
	</queryString>
	<field name="cpdv_nombre" class="java.lang.String"/>
	<field name="cdpc_nombre" class="java.lang.String"/>
	<field name="cpvc_valortext" class="java.lang.String"/>
	<field name="ndpc_orden" class="java.lang.Integer"/>
	<field name="cusr_nombre" class="java.lang.String"/>
	<field name="dpdv_fecharegistro" class="java.sql.Timestamp"/>
	<field name="dpdv_fecha" class="java.sql.Timestamp"/>
	<field name="cpdv_estadoexpediente" class="java.lang.String"/>
	<field name="total" class="java.math.BigDecimal"/>
	<summary>
		<band height="125" splitType="Stretch">
			<crosstab>
				<reportElement x="0" y="0" width="7520" height="125" uuid="fe0d2220-6c3b-4496-a0c0-aaed9e397e42"/>
				<crosstabHeaderCell>
					<cellContents>
						<staticText>
							<reportElement style="Crosstab Data Text" x="210" y="0" width="100" height="16" uuid="798adf8d-83ba-4d2b-9820-6f261eebd39b"/>
							<box>
								<pen lineWidth="0.5"/>
								<topPen lineWidth="0.5"/>
								<leftPen lineWidth="0.5"/>
								<bottomPen lineWidth="0.5"/>
								<rightPen lineWidth="0.5"/>
							</box>
							<textElement verticalAlignment="Middle"/>
							<text><![CDATA[FECHA REGISTRO]]></text>
						</staticText>
						<staticText>
							<reportElement style="Crosstab Data Text" x="110" y="0" width="100" height="16" uuid="af28f47f-3e7c-46b9-b2a7-7dd969886512"/>
							<box>
								<pen lineWidth="0.5"/>
								<topPen lineWidth="0.5"/>
								<leftPen lineWidth="0.5"/>
								<bottomPen lineWidth="0.5"/>
								<rightPen lineWidth="0.5"/>
							</box>
							<textElement verticalAlignment="Middle"/>
							<text><![CDATA[ESTADO]]></text>
						</staticText>
						<staticText>
							<reportElement style="Crosstab Data Text" x="0" y="0" width="110" height="16" uuid="e931c4dd-e1c5-4b01-92dc-f979aa759fe2"/>
							<box>
								<pen lineWidth="0.5"/>
								<topPen lineWidth="0.5"/>
								<leftPen lineWidth="0.5"/>
								<bottomPen lineWidth="0.5"/>
								<rightPen lineWidth="0.5"/>
							</box>
							<textElement verticalAlignment="Middle"/>
							<text><![CDATA[CODIGO]]></text>
						</staticText>
						<staticText>
							<reportElement style="Crosstab Data Text" x="310" y="0" width="100" height="16" uuid="b1c6ed3d-43b5-4b3b-9155-3e79fe1104c1"/>
							<box>
								<pen lineWidth="0.5"/>
								<topPen lineWidth="0.5"/>
								<leftPen lineWidth="0.5"/>
								<bottomPen lineWidth="0.5"/>
								<rightPen lineWidth="0.5"/>
							</box>
							<textElement verticalAlignment="Middle"/>
							<text><![CDATA[FECHA]]></text>
						</staticText>
						<staticText>
							<reportElement style="Crosstab Data Text" x="410" y="0" width="200" height="16" uuid="4114254b-bd21-44ea-9bf1-aa6ffaa121c9"/>
							<box>
								<pen lineWidth="0.5"/>
								<topPen lineWidth="0.5"/>
								<leftPen lineWidth="0.5"/>
								<bottomPen lineWidth="0.5"/>
								<rightPen lineWidth="0.5"/>
							</box>
							<textElement verticalAlignment="Middle"/>
							<text><![CDATA[REGISTRADO POR]]></text>
						</staticText>
						<staticText>
							<reportElement style="Crosstab Data Text" x="610" y="0" width="80" height="16" uuid="4114254b-bd21-44ea-9bf1-aa6ffaa121c5"/>
							<box>
								<pen lineWidth="0.5"/>
								<topPen lineWidth="0.5"/>
								<leftPen lineWidth="0.5"/>
								<bottomPen lineWidth="0.5"/>
								<rightPen lineWidth="0.5"/>
							</box>
							<textElement verticalAlignment="Middle"/>
							<text><![CDATA[VALOR]]></text>
						</staticText>
					</cellContents>
				</crosstabHeaderCell>
				<rowGroup name="cpdv_nombre" width="110">
					<bucket class="java.lang.String">
						<bucketExpression><![CDATA[$F{cpdv_nombre}]]></bucketExpression>
					</bucket>
					<crosstabRowHeader>
						<cellContents backcolor="#F0F8FF" mode="Opaque">
							<box>
								<pen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
							</box>
							<textField>
								<reportElement style="Crosstab Data Text" stretchType="RelativeToBandHeight" x="0" y="0" width="110" height="20" uuid="b55b0f9d-a608-4d54-ab51-662266d43a98"/>
								<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
								<textElement textAlignment="Left" verticalAlignment="Middle"/>
								<textFieldExpression><![CDATA[$V{cpdv_nombre}]]></textFieldExpression>
							</textField>
						</cellContents>
					</crosstabRowHeader>
					<crosstabTotalRowHeader>
						<cellContents backcolor="#BFE1FF" mode="Opaque">
							<box>
								<pen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
							</box>
							<staticText>
								<reportElement x="0" y="0" width="110" height="25" uuid="5abbe2ad-da4e-4382-a537-bc5ed79af0eb"/>
								<textElement textAlignment="Center" verticalAlignment="Middle"/>
								<text><![CDATA[Total cpdv_nombre]]></text>
							</staticText>
						</cellContents>
					</crosstabTotalRowHeader>
				</rowGroup>
				<rowGroup name="cpdv_estadoexpediente" width="100">
					<bucket class="java.lang.String">
						<bucketExpression><![CDATA[$F{cpdv_estadoexpediente}]]></bucketExpression>
					</bucket>
					<crosstabRowHeader>
						<cellContents>
							<textField isStretchWithOverflow="true">
								<reportElement style="Crosstab Data Text" stretchType="RelativeToBandHeight" x="0" y="0" width="100" height="20" isPrintWhenDetailOverflows="true" uuid="6ae7598e-20e7-45c7-94f6-bff91c48ab61"/>
								<box leftPadding="2" rightPadding="2">
									<pen lineWidth="0.5"/>
									<topPen lineWidth="0.5"/>
									<leftPen lineWidth="0.5"/>
									<bottomPen lineWidth="0.5"/>
									<rightPen lineWidth="0.5"/>
								</box>
								<textElement textAlignment="Left" verticalAlignment="Middle"/>
								<textFieldExpression><![CDATA[$V{cpdv_estadoexpediente}]]></textFieldExpression>
							</textField>
						</cellContents>
					</crosstabRowHeader>
					<crosstabTotalRowHeader>
						<cellContents/>
					</crosstabTotalRowHeader>
				</rowGroup>
				<rowGroup name="dpdv_fecharegistro" width="100">
					<bucket class="java.sql.Timestamp">
						<bucketExpression><![CDATA[$F{dpdv_fecharegistro}]]></bucketExpression>
					</bucket>
					<crosstabRowHeader>
						<cellContents>
							<textField>
								<reportElement style="Crosstab Data Text" stretchType="RelativeToBandHeight" x="0" y="0" width="100" height="20" uuid="02d86ba8-06a2-4e69-a9ea-9f31c9d8c6af"/>
								<box leftPadding="2" rightPadding="2">
									<pen lineWidth="0.5"/>
									<topPen lineWidth="0.5"/>
									<leftPen lineWidth="0.5"/>
									<bottomPen lineWidth="0.5"/>
									<rightPen lineWidth="0.5"/>
								</box>
								<textElement textAlignment="Left" verticalAlignment="Middle"/>
								<textFieldExpression><![CDATA[$V{dpdv_fecharegistro}]]></textFieldExpression>
							</textField>
						</cellContents>
					</crosstabRowHeader>
					<crosstabTotalRowHeader>
						<cellContents/>
					</crosstabTotalRowHeader>
				</rowGroup>
				<rowGroup name="dpdv_fecha" width="100">
					<bucket class="java.sql.Timestamp">
						<bucketExpression><![CDATA[$F{dpdv_fecha}]]></bucketExpression>
					</bucket>
					<crosstabRowHeader>
						<cellContents>
							<textField isStretchWithOverflow="true" isBlankWhenNull="true">
								<reportElement style="Crosstab Data Text" stretchType="RelativeToBandHeight" x="0" y="0" width="100" height="20" isPrintWhenDetailOverflows="true" uuid="653e6108-684a-45ed-bf08-021faf08f509"/>
								<box leftPadding="2" rightPadding="2">
									<pen lineWidth="0.5"/>
									<topPen lineWidth="0.5"/>
									<leftPen lineWidth="0.5"/>
									<bottomPen lineWidth="0.5"/>
									<rightPen lineWidth="0.5"/>
								</box>
								<textElement textAlignment="Left" verticalAlignment="Middle"/>
								<textFieldExpression><![CDATA[$V{dpdv_fecha}]]></textFieldExpression>
							</textField>
						</cellContents>
					</crosstabRowHeader>
					<crosstabTotalRowHeader>
						<cellContents/>
					</crosstabTotalRowHeader>
				</rowGroup>
				<rowGroup name="cusr_nombre" width="200">
					<bucket class="java.lang.String">
						<bucketExpression><![CDATA[$F{cusr_nombre}]]></bucketExpression>
					</bucket>
					<crosstabRowHeader>
						<cellContents>
							<textField isStretchWithOverflow="true">
								<reportElement style="Crosstab Data Text" stretchType="RelativeToBandHeight" x="0" y="0" width="200" height="20" isPrintWhenDetailOverflows="true" uuid="bc5aa87b-0fea-4fd5-b67d-0bf00511d82a"/>
								<box leftPadding="2" rightPadding="2">
									<pen lineWidth="0.5"/>
									<topPen lineWidth="0.5"/>
									<leftPen lineWidth="0.5"/>
									<bottomPen lineWidth="0.5"/>
									<rightPen lineWidth="0.5"/>
								</box>
								<textElement textAlignment="Left" verticalAlignment="Middle"/>
								<textFieldExpression><![CDATA[$V{cusr_nombre}]]></textFieldExpression>
							</textField>
						</cellContents>
					</crosstabRowHeader>
					<crosstabTotalRowHeader>
						<cellContents/>
					</crosstabTotalRowHeader>
				</rowGroup>
				<rowGroup name="valor" width="80">
					<bucket class="java.math.BigDecimal">
						<bucketExpression><![CDATA[$F{total}]]></bucketExpression>
					</bucket>
					<crosstabRowHeader>
						<cellContents>
							<textField isStretchWithOverflow="true" pattern="###0.00" isBlankWhenNull="true">
								<reportElement style="Crosstab Data Text" stretchType="RelativeToBandHeight" x="0" y="0" width="80" height="20" isPrintWhenDetailOverflows="true" uuid="653e6108-684a-45ed-bf08-021faf08f509"/>
								<box leftPadding="2" rightPadding="2">
									<pen lineWidth="0.5"/>
									<topPen lineWidth="0.5"/>
									<leftPen lineWidth="0.5"/>
									<bottomPen lineWidth="0.5"/>
									<rightPen lineWidth="0.5"/>
								</box>
								<textElement textAlignment="Right" verticalAlignment="Middle"/>
								<textFieldExpression><![CDATA[$V{valor}]]></textFieldExpression>
							</textField>
						</cellContents>
					</crosstabRowHeader>
					<crosstabTotalRowHeader>
						<cellContents/>
					</crosstabTotalRowHeader>
				</rowGroup>
				<columnGroup name="cdpc_nombre" height="16">
					<bucket class="java.lang.String">
						<bucketExpression><![CDATA[$F{cdpc_nombre}]]></bucketExpression>
					</bucket>
					<crosstabColumnHeader>
						<cellContents backcolor="#F0F8FF" mode="Opaque">
							<box>
								<pen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
							</box>
							<textField>
								<reportElement style="Crosstab Data Text" x="0" y="0" width="273" height="16" uuid="03947224-ceee-4065-88cd-68bb12c77426"/>
								<textElement verticalAlignment="Middle"/>
								<textFieldExpression><![CDATA[$V{cdpc_nombre}]]></textFieldExpression>
							</textField>
						</cellContents>
					</crosstabColumnHeader>
					<crosstabTotalColumnHeader>
						<cellContents backcolor="#BFE1FF" mode="Opaque">
							<box>
								<pen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
							</box>
						</cellContents>
					</crosstabTotalColumnHeader>
				</columnGroup>
				<measure name="cpvc_valortextMeasure" class="java.lang.String">
					<measureExpression><![CDATA[$F{cpvc_valortext}]]></measureExpression>
				</measure>
				<crosstabCell width="273" height="20">
					<cellContents>
						<box>
							<pen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
						</box>
						<textField isStretchWithOverflow="true" isBlankWhenNull="true">
							<reportElement style="Crosstab Data Text" stretchType="RelativeToBandHeight" x="0" y="0" width="273" height="20" isPrintWhenDetailOverflows="true" uuid="b072cb52-2e20-4838-84bd-fc6ccb278cd5"/>
							<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
							<textElement textAlignment="Left" verticalAlignment="Middle"/>
							<textFieldExpression><![CDATA[$V{cpvc_valortextMeasure}]]></textFieldExpression>
						</textField>
					</cellContents>
				</crosstabCell>
				<crosstabCell width="176" height="25" rowTotalGroup="cpdv_nombre">
					<cellContents backcolor="#BFE1FF" mode="Opaque">
						<box>
							<pen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
						</box>
						<textField>
							<reportElement style="Crosstab Data Text" x="0" y="0" width="126" height="25" uuid="8e83fa71-ba7c-48a1-a79e-f584b9a2ca2b"/>
							<textFieldExpression><![CDATA[$V{cpvc_valortextMeasure}]]></textFieldExpression>
						</textField>
					</cellContents>
				</crosstabCell>
				<crosstabCell width="98" columnTotalGroup="cdpc_nombre">
					<cellContents backcolor="#BFE1FF" mode="Opaque">
						<box>
							<pen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
						</box>
					</cellContents>
				</crosstabCell>
				<crosstabCell width="98" rowTotalGroup="cpdv_nombre" columnTotalGroup="cdpc_nombre">
					<cellContents backcolor="#BFE1FF" mode="Opaque">
						<box>
							<pen lineWidth="0.5" lineStyle="Solid" lineColor="#000000"/>
						</box>
					</cellContents>
				</crosstabCell>
				<crosstabCell rowTotalGroup="cpdv_estadoexpediente">
					<cellContents/>
				</crosstabCell>
				<crosstabCell rowTotalGroup="cpdv_estadoexpediente" columnTotalGroup="cdpc_nombre">
					<cellContents/>
				</crosstabCell>
				<crosstabCell rowTotalGroup="dpdv_fecharegistro">
					<cellContents/>
				</crosstabCell>
				<crosstabCell rowTotalGroup="dpdv_fecharegistro" columnTotalGroup="cdpc_nombre">
					<cellContents/>
				</crosstabCell>
				<crosstabCell rowTotalGroup="dpdv_fecha">
					<cellContents/>
				</crosstabCell>
				<crosstabCell rowTotalGroup="dpdv_fecha" columnTotalGroup="cdpc_nombre">
					<cellContents/>
				</crosstabCell>
				<crosstabCell rowTotalGroup="cusr_nombre">
					<cellContents/>
				</crosstabCell>
				<crosstabCell rowTotalGroup="cusr_nombre" columnTotalGroup="cdpc_nombre">
					<cellContents/>
				</crosstabCell>
			</crosstab>
		</band>
	</summary>
</jasperReport>
');
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, bdpc_obligatorio, ndpc_orden, bdpc_editable, cdpc_nombre, cdpc_codigo, cdpc_formato, bdpc_visiblerender)
    SELECT 'CST003-1', 'CST003', true, 2, true, 'FECHAS', 'FECHA', 'F', true WHERE  EXISTS (SELECT cdpl_llave FROM documentoplantilla_dplp  WHERE cdpl_llave = 'CST003') and NOT EXISTS(SELECT cdpc_llave FROM documentoplantillacaracteristica_dpcp  WHERE cdpc_llave = 'CST003-1');
INSERT INTO plantillacampoparametro_pcpp(cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
    SELECT 'CST003-1', 'CST003-1', 'FECHA_RANGO', 'TRUE' WHERE  EXISTS (SELECT cdpl_llave FROM documentoplantilla_dplp  WHERE cdpl_llave = 'CST003') and NOT EXISTS(SELECT cpcp_llave FROM plantillacampoparametro_pcpp  WHERE cpcp_llave = 'CST003-1');

--Campo plantilla
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, bdpc_obligatorio, ndpc_orden, bdpc_editable, cdpc_nombre, cdpc_codigo, cdpc_formato, bdpc_visiblerender)
    SELECT 'CST003-2', 'CST003', true, 1, true, 'PLANTILLA', 'PLANTILLA', 'G', true WHERE  EXISTS (SELECT cdpl_llave FROM documentoplantilla_dplp  WHERE cdpl_llave = 'CST003') and NOT EXISTS(SELECT cdpc_llave FROM documentoplantillacaracteristica_dpcp  WHERE cdpc_llave = 'CST003-2');
INSERT INTO plantillacampoparametro_pcpp(cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
    SELECT 'CST003-2', 'CST003-2', 'CONFIGURACION_ENTIDAD', 'PLANTILLAS' WHERE  EXISTS (SELECT cdpl_llave FROM documentoplantilla_dplp  WHERE cdpl_llave = 'CST003') and NOT EXISTS(SELECT cpcp_llave FROM plantillacampoparametro_pcpp  WHERE cpcp_llave = 'CST003-2');
