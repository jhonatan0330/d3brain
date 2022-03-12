COMMENT ON TABLE usuario_usrp IS '2020-01-22';

update reportebase_rpbp set crpb_jaspertext = '<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.8.0.final using JasperReports Library version 6.8.0-2ed8dfabb690ff337a5797129f2cd92902b0c87b  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="POS001" pageWidth="226" pageHeight="728" whenNoDataType="AllSectionsNoDetail" columnWidth="200" leftMargin="13" rightMargin="13" topMargin="0" bottomMargin="0" whenResourceMissingType="Empty" isIgnorePagination="true" uuid="c44a4718-95e7-4c76-aa4d-2716b171cabe">
	<parameter name="P_KEY" class="java.lang.String"/>
	<queryString>
		<![CDATA[SELECT
	null as producto
	,-1 as orden
	,null as formato
	,ndpc_orden::text as producto_categoria
	,cpvc_valortext as producto_nombre
	,null::NUMERIC as producto_cantidad
	,null::NUMERIC as producto_valor_unitario
	,null::NUMERIC as producto_valor_total
	,null as producto_caracteristica
	,null as producto_tipo
FROM
	campo_documento, documentoplantillacaracteristica_dpcp
WHERE
	cpvc_campo = cdpc_llave and cdpc_formato = ''T'' and cdrc_documento = (
	select sed.cpdv_llave from propiedadvalordefinido_pvdp, propiedad_ppdp, pedidoventa_pdvp doc, documentoplantilla_dplp, pedidoventa_pdvp sed 
		where cpvd_codigo = ''ENCABEZADO''
		and cpvd_llave = cppd_propiedadvalor
		and cppd_campo = doc.cpdv_plantilla
		and cdpl_nombre = cppd_valor
		and doc.cpdv_llave = $P{P_KEY}
		and sed.cpdv_plantilla = cdpl_llave
		and sed.cpdv_nombre = cppd_texto)
UNION SELECT
	null as producto
	,0 as orden
	,null as formato
	,(select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = cpdv_plantilla) as producto_categoria
	,cpdv_nombre as producto_nombre
	,null as producto_cantidad
	,null as producto_valor_unitario
	,null as producto_valor_total
	,null as producto_caracteristica
	,null as producto_tipo
from pedidoventa_pdvp where cpdv_llave = $P{P_KEY}
UNION SELECT
	cdpc_nombre as producto
	,ndpc_orden as orden
	,cdpc_formato as formato
	,''.TEXTO'' as producto_categoria
	,cdpc_nombre as producto_nombre
	,null as producto_cantidad
	,null as producto_valor_unitario
	,null as producto_valor_total
	,null as producto_caracteristica
	,cpvc_valortext as producto_tipo
FROM
	campo_documento, documentoplantillacaracteristica_dpcp
WHERE
	cdpc_llave = cpvc_campo and  cdpc_formato in (''Z'',''U'')
	and cdrc_documento IN( (SELECT cdrg_documentoprincipal FROM documentorelaciongestor_drgp WHERE cdrg_documentomodificador = $P{P_KEY}), $P{P_KEY})
UNION SELECT
	null as producto
	,ndpc_orden as orden
	,''J'' as formato
	,''.TEXTO'' as producto_categoria
	,cdpc_nombre as producto_nombre
	,null as producto_cantidad
	,null as producto_valor_unitario
	,null as producto_valor_total
	,null as producto_caracteristica
	,null as producto_tipo
FROM
	pedidoventa_pdvp, documentoplantillacaracteristica_dpcp
WHERE
	cpdv_llave =  (SELECT cdrg_documentoprincipal FROM documentorelaciongestor_drgp WHERE cdrg_documentomodificador = $P{P_KEY})
	and cdpc_plantilla = cpdv_plantilla and cdpc_formato = ''J'' and cdpc_estado  = ''A''
UNION SELECT
	cdpv_llave as producto
	,(select ndpc_orden from documentoplantillacaracteristica_dpcp where cdpc_plantilla = cpdv_plantilla and cdpc_formato = ''J'' and cdpc_estado = ''A'') as orden
	,null as formato
	,(select ccpr_nombre from categoriaproducto_cprp where ccpr_llave =(select cpro_categoria from producto_prop where cpro_llave = cdpv_producto)) as producto_categoria
	,(select cpro_nombre from producto_prop where cpro_llave = cdpv_producto) as producto_nombre
	,mdpv_cantidad as producto_cantidad
	,mdpv_valorunitario as producto_valor_unitario
	,mdpv_valortotal as producto_valor_total
	,null as producto_caracteristica
	,null as producto_tipo
FROM
	pedidoventa_pdvp, detallepedidoventa_dpvp
WHERE
	cdpv_documento = cpdv_llave
	and cdpv_estado =''A''
	and cpdv_llave =  (SELECT cdrg_documentoprincipal FROM documentorelaciongestor_drgp WHERE cdrg_documentomodificador = $P{P_KEY})
UNION SELECT
	null as producto
	,99 as orden
	,''N'' as formato
	,''Z. SUBTOTAL'' as producto_categoria
	,null as producto_nombre
	,null as producto_cantidad
	,mpvd_valorsubtotal as producto_valor_unitario
	,null as producto_valor_total
	,null as producto_caracteristica
	,null as producto_tipo
FROM
	pedidoventadinero_pvdp
WHERE
	cpvd_documento =  (SELECT cdrg_documentoprincipal FROM documentorelaciongestor_drgp WHERE cdrg_documentomodificador = $P{P_KEY}) and cpvd_estado = ''A'' and mpvd_valorsubtotal!=0
UNION SELECT
	cdpc_nombre as producto
	,ndpc_orden+200 as orden
	,cdpc_formato
	,''Z.NUMERO'' as producto_categoria
	,cdpc_nombre as producto_nombre
	,null as producto_cantidad
	,mpvc_valornumero as producto_valor_unitario
	,null as producto_valor_total
	,null as producto_caracteristica
	,cpvc_valortext as producto_tipo
FROM
	campo_documento, documentoplantillacaracteristica_dpcp
WHERE
	cdpc_llave = cpvc_campo and  cdpc_formato = ''N''
	and cdrc_documento IN ((SELECT cdrg_documentoprincipal FROM documentorelaciongestor_drgp WHERE cdrg_documentomodificador = $P{P_KEY}), $P{P_KEY})
union
SELECT
	null as producto
	,100 as orden
	,''N'' as formato
	,''Z.Z TOTAL'' as producto_categoria
	,null as producto_nombre
	,null as producto_cantidad
	,null as producto_valor_unitario
	,mpvd_valortotal as producto_valor_total
	,null as producto_caracteristica
	,null as producto_tipo
FROM
	pedidoventadinero_pvdp
WHERE
	cpvd_documento =  (SELECT cdrg_documentoprincipal FROM documentorelaciongestor_drgp WHERE cdrg_documentomodificador = $P{P_KEY}) and cpvd_estado = ''A''
union
SELECT
	null as producto
	,300 as orden
	,''N'' as formato
	,''Z.Z SALDO'' as producto_categoria
	,null as producto_nombre
	,null as producto_cantidad
	,null as producto_valor_unitario
	,mpvd_saldo as producto_valor_total
	,null as producto_caracteristica
	,null as producto_tipo
FROM
	pedidoventadinero_pvdp
WHERE
	cpvd_documento =  (SELECT cdrg_documentoprincipal FROM documentorelaciongestor_drgp WHERE cdrg_documentomodificador = $P{P_KEY}) and cpvd_estado = ''A'' and mpvd_saldo!=0
ORDER BY
	orden, producto_categoria, producto_nombre, producto, producto_caracteristica desc]]>
	</queryString>
	<field name="producto" class="java.lang.String"/>
	<field name="orden" class="java.lang.Integer"/>
	<field name="formato" class="java.lang.String"/>
	<field name="producto_categoria" class="java.lang.String"/>
	<field name="producto_nombre" class="java.lang.String"/>
	<field name="producto_cantidad" class="java.math.BigDecimal"/>
	<field name="producto_valor_unitario" class="java.math.BigDecimal"/>
	<field name="producto_valor_total" class="java.math.BigDecimal"/>
	<field name="producto_caracteristica" class="java.lang.String"/>
	<field name="producto_tipo" class="java.lang.String"/>
	<variable name="producto_valor_total_1" class="java.math.BigDecimal" calculation="Sum">
		<variableExpression><![CDATA[$F{producto_valor_total}]]></variableExpression>
	</variable>
	<variable name="V_CANT_ARTICULOS" class="java.math.BigDecimal" calculation="Sum">
		<variableExpression><![CDATA[$F{producto_cantidad}]]></variableExpression>
		<initialValueExpression><![CDATA[new BigDecimal(0)]]></initialValueExpression>
	</variable>
	<variable name="V_CONT_TITULO" class="java.lang.Integer" resetType="None" calculation="Sum">
		<variableExpression><![CDATA[($V{V_CONT_TITULO}==null)?0:($F{producto_cantidad}==null)?$V{V_CONT_TITULO}:($V{V_CONT_TITULO}+1)]]></variableExpression>
		<initialValueExpression><![CDATA[]]></initialValueExpression>
	</variable>
	<detail>
		<band height="18">
			<printWhenExpression><![CDATA[$F{orden}==-1]]></printWhenExpression>
			<textField isBlankWhenNull="true">
				<reportElement x="0" y="0" width="200" height="18" uuid="0a156276-9dac-4e97-8daa-87b660e4c290"/>
				<textElement textAlignment="Center" markup="none">
					<font size="12" isBold="true" isUnderline="false"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_nombre}]]></textFieldExpression>
			</textField>
		</band>
		<band height="30">
			<printWhenExpression><![CDATA[$F{orden}==0]]></printWhenExpression>
			<textField>
				<reportElement x="0" y="0" width="107" height="14" uuid="e59b8a80-5a78-4f3f-8231-e6c9f5f70fac"/>
				<box>
					<topPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle" markup="none">
					<font fontName="SansSerif" size="8" isBold="true"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_categoria}]]></textFieldExpression>
			</textField>
			<textField isStretchWithOverflow="true" isBlankWhenNull="true">
				<reportElement x="0" y="14" width="107" height="16" uuid="042af019-7feb-49f8-ba08-bf2e6ab96b75"/>
				<box leftPadding="0" rightPadding="0">
					<bottomPen lineWidth="0.5"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font fontName="SansSerif" size="9" isBold="true"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_nombre}]]></textFieldExpression>
			</textField>
			<textField pattern="HH:mm" isBlankWhenNull="true">
				<reportElement x="165" y="14" width="35" height="16" uuid="72a9a660-010b-44d2-823a-e5ec4771e7f0"/>
				<box leftPadding="2" rightPadding="2">
					<bottomPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA[new Date ()]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="165" y="0" width="35" height="14" uuid="1a898196-eafd-4d35-85b5-efe33defaf7b"/>
				<box leftPadding="2" rightPadding="2">
					<topPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Bottom">
					<font fontName="SansSerif" size="6" isBold="false"/>
				</textElement>
				<text><![CDATA[HORA]]></text>
			</staticText>
			<staticText>
				<reportElement x="107" y="0" width="58" height="14" uuid="3c0108cf-2359-4e22-bba2-ee42b32c5afa"/>
				<box leftPadding="2" rightPadding="2">
					<topPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Bottom">
					<font fontName="SansSerif" size="6" isBold="false"/>
				</textElement>
				<text><![CDATA[FECHA]]></text>
			</staticText>
			<textField pattern="yyyy/MM/dd" isBlankWhenNull="true">
				<reportElement x="107" y="14" width="58" height="16" uuid="586bdaac-5030-448a-bcbd-cf9e9c812c0d"/>
				<box leftPadding="2" rightPadding="2">
					<bottomPen lineWidth="0.5"/>
				</box>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA[new Date ()]]></textFieldExpression>
			</textField>
		</band>
		<band height="12">
			<printWhenExpression><![CDATA[$F{producto_categoria}==".TEXTO" && $F{producto}!=null]]></printWhenExpression>
			<textField isBlankWhenNull="true">
				<reportElement x="0" y="0" width="57" height="12" uuid="0038c629-34c8-4b06-89c0-805738eec65a"/>
				<textElement verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_nombre}]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement x="57" y="0" width="143" height="12" uuid="0230bd07-58e1-4322-8977-df86205842c4"/>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_tipo}]]></textFieldExpression>
			</textField>
		</band>
		<band height="12">
			<printWhenExpression><![CDATA[$F{formato}.compareTo( "J" )==0]]></printWhenExpression>
			<textField>
				<reportElement x="0" y="0" width="107" height="12" uuid="26a4ae5c-335a-494d-88d7-2589987d6923"/>
				<textElement verticalAlignment="Middle" markup="none">
					<font fontName="SansSerif" size="6" isBold="false"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_nombre}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="107" y="0" width="23" height="12" uuid="a113f09c-39f2-4248-94a7-d62db4cd80d1"/>
				<box leftPadding="0" rightPadding="0">
					<topPen lineWidth="0.0"/>
				</box>
				<textElement verticalAlignment="Middle">
					<font fontName="SansSerif" size="6" isBold="false"/>
				</textElement>
				<text><![CDATA[CANT]]></text>
			</staticText>
			<staticText>
				<reportElement x="130" y="0" width="35" height="12" uuid="27389533-0740-4cda-9a81-f78bb2fcb0c9"/>
				<box leftPadding="0" rightPadding="0">
					<topPen lineWidth="0.0"/>
				</box>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font fontName="SansSerif" size="6" isBold="false"/>
				</textElement>
				<text><![CDATA[PRECIO]]></text>
			</staticText>
			<staticText>
				<reportElement x="165" y="0" width="35" height="12" uuid="69503410-cac4-41dc-ba98-2e2e5c75a42a"/>
				<box leftPadding="0" rightPadding="0">
					<topPen lineWidth="0.0"/>
				</box>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font fontName="SansSerif" size="6" isBold="false"/>
				</textElement>
				<text><![CDATA[VALOR]]></text>
			</staticText>
		</band>
		<band height="11">
			<printWhenExpression><![CDATA[$F{producto_cantidad}!=null]]></printWhenExpression>
			<textField isStretchWithOverflow="true" pattern="#,##0" isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="130" y="0" width="35" height="11" uuid="10167113-cd5b-4342-8d96-22d8e3f385a9"/>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_valor_unitario}]]></textFieldExpression>
			</textField>
			<textField isStretchWithOverflow="true" pattern="###0" isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="107" y="0" width="23" height="11" uuid="a4291338-9a2e-4025-afdd-1d73bbf1485f"/>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_cantidad}]]></textFieldExpression>
			</textField>
			<textField isStretchWithOverflow="true" pattern="#,##0" isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="165" y="0" width="35" height="11" isPrintInFirstWholeBand="true" uuid="0bbdec1c-bcb4-4df7-be34-1add07d6dd03"/>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_valor_total}]]></textFieldExpression>
			</textField>
			<textField isStretchWithOverflow="true" isBlankWhenNull="true">
				<reportElement stretchType="RelativeToTallestObject" x="0" y="0" width="107" height="11" uuid="e0da96d9-92d0-4b57-bbd8-049bfe9751d8"/>
				<textElement>
					<font size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_nombre}]]></textFieldExpression>
			</textField>
		</band>
		<band height="8">
			<printWhenExpression><![CDATA[$F{producto_caracteristica}!=null]]></printWhenExpression>
			<textField>
				<reportElement x="0" y="0" width="200" height="8" uuid="89b1e2be-b603-40a5-9d9d-280894e2a9ed"/>
				<box leftPadding="5" rightPadding="2"/>
				<textElement>
					<font size="6"/>
				</textElement>
				<textFieldExpression><![CDATA[($F{producto_caracteristica}==null)?$F{producto_tipo}:$F{producto_tipo}+ " : " + $F{producto_caracteristica}]]></textFieldExpression>
			</textField>
		</band>
		<band height="12">
			<printWhenExpression><![CDATA[$F{producto_categoria}=="Z.NUMERO"]]></printWhenExpression>
			<textField pattern="$ #,##0" isBlankWhenNull="true">
				<reportElement x="130" y="0" width="70" height="12" uuid="ae40f013-ba95-4776-844d-65834471203c"/>
				<textElement textAlignment="Right">
					<font size="9"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_valor_unitario}]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement x="0" y="0" width="130" height="12" uuid="21d3d9bf-fa2a-4fa9-9b9e-1c977dcaa0be"/>
				<textElement textAlignment="Right">
					<font size="8" isBold="true"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_nombre}]]></textFieldExpression>
			</textField>
		</band>
		<band height="15">
			<printWhenExpression><![CDATA[$F{producto_categoria} == "Z. SUBTOTAL"]]></printWhenExpression>
			<staticText>
				<reportElement x="0" y="0" width="130" height="15" uuid="c847aa36-8bba-482f-98b5-0e2f72c56eb0"/>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[SUBTOTAL]]></text>
			</staticText>
			<textField isStretchWithOverflow="true" pattern="$ #,##0" isBlankWhenNull="true">
				<reportElement x="130" y="0" width="70" height="15" uuid="e152f150-e4b6-4756-b623-1968ae175101"/>
				<box>
					<topPen lineWidth="1.0"/>
				</box>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font fontName="SansSerif" size="10"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_valor_unitario}]]></textFieldExpression>
			</textField>
		</band>
		<band height="20">
			<printWhenExpression><![CDATA[$F{producto_categoria} == "Z.Z TOTAL"]]></printWhenExpression>
			<staticText>
				<reportElement x="0" y="0" width="130" height="20" uuid="4610fb59-c5cc-4081-925d-a617d9fe08a0"/>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font size="10" isBold="true"/>
				</textElement>
				<text><![CDATA[TOTAL]]></text>
			</staticText>
			<textField isStretchWithOverflow="true" pattern="$ #,##0" isBlankWhenNull="true">
				<reportElement x="130" y="0" width="70" height="20" uuid="fc52e97b-4834-4416-9938-c8829baaca18"/>
				<box>
					<topPen lineWidth="2.0" lineStyle="Double"/>
				</box>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font fontName="SansSerif" size="10"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_valor_total}]]></textFieldExpression>
			</textField>
		</band>
		<band height="15">
			<printWhenExpression><![CDATA[$F{producto_categoria} == "Z.Z SALDO"]]></printWhenExpression>
			<staticText>
				<reportElement x="0" y="0" width="130" height="15" uuid="3b80f8a7-630f-4359-b96b-59a5b54ae0f4"/>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font size="8" isBold="true"/>
				</textElement>
				<text><![CDATA[SALDO]]></text>
			</staticText>
			<textField isStretchWithOverflow="true" pattern="$ #,##0" isBlankWhenNull="true">
				<reportElement x="130" y="0" width="70" height="15" uuid="10043344-1408-422e-ac40-d666798f747e"/>
				<box>
					<topPen lineWidth="1.0"/>
				</box>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font fontName="SansSerif" size="10"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{producto_valor_total}]]></textFieldExpression>
			</textField>
		</band>
	</detail>
	<summary>
		<band height="15">
			<printWhenExpression><![CDATA[$V{V_CANT_ARTICULOS}.compareTo( new BigDecimal(0) )!=0]]></printWhenExpression>
			<staticText>
				<reportElement x="0" y="0" width="165" height="15" uuid="70a0a593-c65c-47f1-835b-0eb221ff6d5b"/>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font size="8" isBold="false"/>
				</textElement>
				<text><![CDATA[CANTIDAD DE ARTICULOS]]></text>
			</staticText>
			<textField isStretchWithOverflow="true" pattern="###0" isBlankWhenNull="true">
				<reportElement stretchType="RelativeToBandHeight" x="165" y="0" width="35" height="15" uuid="0327747e-e7c7-45c3-a2bd-bc3552115d3a"/>
				<textElement textAlignment="Right" verticalAlignment="Middle">
					<font size="8"/>
				</textElement>
				<textFieldExpression><![CDATA[$V{V_CANT_ARTICULOS}]]></textFieldExpression>
			</textField>
		</band>
	</summary>
</jasperReport>' where crpb_codigo = 'POS001';
