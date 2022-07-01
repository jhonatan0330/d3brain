COMMENT ON TABLE usuario_usrp IS '2020-02-23';

UPDATE reportebase_rpbp
   SET crpb_jaspertext = '<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.9.0.final using JasperReports Library version 6.9.0-cb8f9004be492ccc537180b49c026951f4220bf3  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="Blank_A4_8" columnCount="2" printOrder="Horizontal" pageWidth="572" pageHeight="752" whenNoDataType="AllSectionsNoDetail" columnWidth="286" leftMargin="0" rightMargin="0" topMargin="0" bottomMargin="0" uuid="1aeb0a4c-fbb5-4d25-8128-4801ff105230">
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
	<style name="Row" mode="Transparent">
		<conditionalStyle>
			<conditionExpression><![CDATA[$V{REPORT_COUNT}%2 == 0]]></conditionExpression>
			<style backcolor="#E3E1DE"/>
		</conditionalStyle>
	</style>
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
		<band height="40">
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="416" y="0" width="61" height="8" uuid="13c76879-dff0-41a6-b7a1-3b286677c290"/>
				<text><![CDATA[CODIGO]]></text>
			</staticText>
			<textField evaluationTime="Report" pattern="yyyy.MM.dd hh:mm:ss aaa" isBlankWhenNull="true">
				<reportElement style="BoxHeaderStyle" x="477" y="8" width="95" height="12" uuid="b273d0c2-e388-46b4-bf2f-09ff743bce7f"/>
				<textFieldExpression><![CDATA[new java.util.Date()]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="477" y="20" width="95" height="8" uuid="4d98498b-4698-42d1-8b9e-2ce3df094e13"/>
				<text><![CDATA[PAGINA]]></text>
			</staticText>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="477" y="0" width="95" height="8" uuid="35adff1a-afcf-4143-a3e3-00a31985d393"/>
				<text><![CDATA[FECHA DE IMPRESION]]></text>
			</staticText>
			<textField>
				<reportElement style="BoxHeaderStyle" x="48" y="28" width="368" height="12" uuid="80e5c5d9-7cae-48cc-a886-68671cfa2430"/>
				<textElement textAlignment="Left"/>
				<textFieldExpression><![CDATA[$F{descripcion}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement style="BoxHeaderStyle" x="416" y="8" width="61" height="12" uuid="1312fe4f-376d-4206-810a-c0bfa7bee8c3"/>
				<textFieldExpression><![CDATA[$F{codigo}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="416" y="20" width="61" height="8" uuid="ec5ed981-002e-43cb-908c-6214e0bd308f"/>
				<text><![CDATA[VERSION]]></text>
			</staticText>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="48" y="0" width="368" height="8" uuid="b307d8ab-f57f-4e46-a590-50c2b6240761"/>
				<text><![CDATA[TITULO DEL DOCUMENTO]]></text>
			</staticText>
			<textField>
				<reportElement style="BoxStyle" x="48" y="8" width="368" height="20" uuid="87e36295-223c-41b1-936d-69e901cc675a"/>
				<textFieldExpression><![CDATA[$F{nombre}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement style="BoxHeaderStyle" x="416" y="28" width="61" height="12" uuid="db8b347e-7c52-4642-b1f7-55f5da2ecb3c"/>
				<textFieldExpression><![CDATA[$F{reporte_version}]]></textFieldExpression>
			</textField>
			<textField>
				<reportElement style="BoxHeaderStyle" x="477" y="28" width="95" height="12" uuid="87fd1afe-9eee-4033-82ec-56db911b2603"/>
				<textFieldExpression><![CDATA["Pagina " + $P{P_NUMBER_PAGE}]]></textFieldExpression>
			</textField>
			<rectangle>
				<reportElement mode="Opaque" x="0" y="0" width="48" height="40" uuid="94c1218b-4dab-4b3f-89c4-74153ead55aa"/>
				<graphicElement>
					<pen lineStyle="Solid" lineColor="#CCCCCC"/>
				</graphicElement>
			</rectangle>
			<image scaleImage="RealSize" hAlign="Center" vAlign="Middle" onErrorType="Blank">
				<reportElement x="0" y="0" width="48" height="40" uuid="87da1418-2f4b-4c98-babc-4c373ebe5c92"/>
				<imageExpression><![CDATA[$F{imagen}]]></imageExpression>
			</image>
		</band>
	</pageHeader>
	<columnHeader>
		<band height="8">
			<printWhenExpression><![CDATA[$F{valor_parametro}!=null]]></printWhenExpression>
			<staticText>
				<reportElement style="BoxGrayHeaderStyle" x="0" y="0" width="286" height="8" uuid="591cb485-88a9-4802-beb2-d0c34749320c"/>
				<text><![CDATA[PARAMETROS DEL INFORME]]></text>
			</staticText>
		</band>
	</columnHeader>
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
				<reportElement style="BoxStyle" x="0" y="0" width="572" height="1" uuid="c320d774-9fb6-49d9-aa76-d3971c71f146"/>
				<graphicElement>
					<pen lineStyle="Double" lineColor="#CCCCCC"/>
				</graphicElement>
			</line>
		</band>
	</summary>
</jasperReport>'
WHERE crpb_codigo = 'ENC_LETTER';

UPDATE reportebase_rpbp
   SET crpb_jaspertext = '<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.8.0.final using JasperReports Library version 6.8.0-2ed8dfabb690ff337a5797129f2cd92902b0c87b  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="Blank_Letter_2" pageWidth="612" pageHeight="792" columnWidth="555" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="f7ab0719-c778-4a80-858e-6625ad528dd8">
	<style name="normal" isDefault="true" hTextAlign="Justified" vTextAlign="Middle" fontName="Arial" fontSize="9">
		<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
	</style>
	<style name="Titulo" style="normal" mode="Opaque" backcolor="#828282" fontSize="14" isBold="true"/>
	<style name="Objetivo" style="normal" vTextAlign="Middle"/>
	<style name="H1" style="normal" mode="Opaque" backcolor="#CCCCCC" fontSize="11" isBold="true">
		<box padding="1"/>
	</style>
	<style name="h2" style="H1" hTextAlign="Left" fontSize="8" isBold="true">
		<box padding="1"/>
	</style>
	<style name="Inactivo" vTextAlign="Middle" fontSize="8">
		<conditionalStyle>
			<conditionExpression><![CDATA[($F{propiedad_estado}!=null && $F{propiedad_estado}.compareTo("I")==0)]]></conditionExpression>
			<style isStrikeThrough="true"/>
		</conditionalStyle>
	</style>
	<parameter name="P_PLANTILLA" class="java.lang.String"/>
	<parameter name="P_CAMBIO" class="java.lang.String"/>
	<parameter name="P_PROCESO" class="java.lang.String"/>
	<parameter name="P_ROL" class="java.lang.String"/>
	<queryString language="SQL">
		<![CDATA[select 
	''FORMULARIO'' as tipo_plantilla
	,dpl.cdpl_nombre as nombre
	,dpl.cdpl_codigo as codigo
	,dpl.cdpl_objetivo as objetivo
	,dpl.cdpl_imagen as imagen
	,cpvd_grupo as propiedad_grupo
	,cpvd_nombre as propiedad_valor
	,cppd_valor as propiedad_valor_final
	,null as propiedad_motivo
	,(select cprc_nombre from proceso_prcp where cprc_llave = cdpl_proceso) as proceso
	,(select nprc_prioridad from proceso_prcp where cprc_llave = cdpl_proceso) as proceso_orden
	,dpl.cdpl_nombre as plantilla_nombre
	,(select max(npes_avance) from procesoestado_pesp where cpes_llave in (select cptr_estadollegada from procesotransicion_ptrp where cptr_plantilla = dpl.cdpl_llave and cptr_proceso = dpl.cdpl_proceso and cptr_estado = ''A'')) as plantilla_orden
	,null as subpropiedad_grupo
	,coalesce((select r.cdpl_nombre from documentoplantilla_dplp r where r.cdpl_llave = (select crac_plantilla from rolacceso_racp where crac_llave = cppd_rol)),'''') as subpropiedad_valor
	,cppd_motivo as subpropiedad_motivo
	,null as relacion_valor
	,null as relacion_motivo
	,cppd_estado as propiedad_estado
	,ccmb_nombre as cambio
	,ccmb_motivo as cambio_motivo
	,dcmb_fecha as cambio_fecha
from documentoplantilla_dplp dpl 
inner join propiedad_ppdp on (cppd_campo = dpl.cdpl_llave and (cppd_estado = ''A'' or  $P{P_CAMBIO} is not null))
inner join propiedadvalordefinido_pvdp on (cpvd_llave = cppd_propiedadvalor and cpvd_estado = ''A'')
left join cambio_cmbp on (ccmb_llave = cppd_cambiocreacion or ccmb_llave = cppd_cambioeliminacion)
where cdpl_estado = ''A'' and (cdpl_llave = $P{P_PLANTILLA} or $P{P_PLANTILLA} is null)
and (cdpl_proceso = $P{P_PROCESO} or $P{P_PROCESO} is null)
and ($P{P_CAMBIO} is null or ccmb_llave = $P{P_CAMBIO})
and ($P{P_ROL} is null or (cppd_rol is null or cppd_rol = $P{P_ROL}))
union
select 
	''REPORTE'' as tipo_plantilla
	,rpb.crpb_nombre as nombre
	,rpb.crpb_codigo as codigo
	,rpb.crpb_descripcion as objetivo
	,cdpl_imagen as imagen
	,cpvd_grupo as propiedad_grupo
	,coalesce(cppd_texto, cppd_valor) as propiedad_valor
	,cppd_valor as propiedad_valor_final
	,cppd_motivo as propiedad_motivo
	,(select cprc_nombre from proceso_prcp where cprc_llave = cdpl_proceso) as proceso
	,(select nprc_prioridad from proceso_prcp where cprc_llave = cdpl_proceso) as proceso_orden
	,cdpl_nombre as plantilla_nombre
	,(select max(npes_avance) from procesoestado_pesp where cpes_llave in (select cptr_estadollegada from procesotransicion_ptrp where cptr_plantilla = cdpl_llave and cptr_proceso = cdpl_proceso and cptr_estado = ''A'')) as plantilla_orden
	,null as subpropiedad_grupo
	,null as subpropiedad_valor
	,null as subpropiedad_motivo
	,(select pl_rit.cdpl_nombre from documentoplantilla_dplp pl_rit where pl_rit.cdpl_llave = crit_plantilla) as relacion_valor
	,(select cp_rit.cdpc_nombre from documentoplantillacaracteristica_dpcp cp_rit where cp_rit.cdpc_llave = crit_campo) as relacion_motivo
	,cppd_estado as propiedad_estado
	,ccmb_nombre as cambio
	,ccmb_motivo as cambio_motivo
	,dcmb_fecha as cambio_fecha
from reportebase_rpbp rpb 
inner join documentoplantilla_dplp on (cdpl_llave = rpb.crpb_plantilla)
left join propiedad_ppdp on (cppd_campo = rpb.crpb_llave and (cppd_estado = ''A'' or  $P{P_CAMBIO} is not null))
left join cambio_cmbp on (ccmb_llave = cppd_cambiocreacion or ccmb_llave = cppd_cambioeliminacion)
left join propiedadvalordefinido_pvdp on (cpvd_llave = cppd_propiedadvalor and cpvd_estado = ''A'')
left join relacioninterna_ritp on (crit_propiedad = cppd_llave and crit_estado = ''A'')
where crpb_estado = ''A'' and (crpb_plantilla = $P{P_PLANTILLA} or $P{P_PLANTILLA} is null)
and (cdpl_proceso = $P{P_PROCESO} or $P{P_PROCESO} is null)
and ($P{P_CAMBIO} is null or ccmb_llave = $P{P_CAMBIO})
and ($P{P_ROL} is null or (cppd_rol is null or cppd_rol = $P{P_ROL}))
union
select 
	''FORMULARIO'' as tipo_plantilla
	,dpl.cdpl_nombre as nombre
	,dpl.cdpl_codigo as codigo
	,dpl.cdpl_objetivo as objetivo
	,dpl.cdpl_imagen as imagen
	,''PASOS O CAMPOS PARA DILIGENCIAR EL FORMULARIO'' as propiedad_grupo
	,lpad(dpc.ndpc_orden::text, 2,''0'') || ''. '' || dpc.cdpc_nombre as propiedad_valor
	,null as propiedad_valor_final
	,dpc.cdpc_objetivo as propiedad_motivo
	,(select cprc_nombre from proceso_prcp where cprc_llave = cdpl_proceso) as proceso
	,(select nprc_prioridad from proceso_prcp where cprc_llave = cdpl_proceso) as proceso_orden
	,dpl.cdpl_nombre as plantilla_nombre
	,(select max(npes_avance) from procesoestado_pesp where cpes_llave in (select cptr_estadollegada from procesotransicion_ptrp where cptr_plantilla = cdpl_llave and cptr_proceso = cdpl_proceso and cptr_estado = ''A'')) as plantilla_orden
	,cpvd_grupo as subpropiedad_grupo
	,cpvd_nombre || coalesce((select '' ('' || r.cdpl_nombre || '')'' from documentoplantilla_dplp r where r.cdpl_llave = (select crac_plantilla from rolacceso_racp where crac_llave = cppd_rol)),'''') as subpropiedad_valor
	,cppd_motivo as subpropiedad_motivo
	,(select pl_rit.cdpl_nombre from documentoplantilla_dplp pl_rit where pl_rit.cdpl_llave = crit_plantilla) as relacion_valor
	,(select cp_rit.cdpc_nombre from documentoplantillacaracteristica_dpcp cp_rit where cp_rit.cdpc_llave = crit_campo) as relacion_motivo
	,cppd_estado as propiedad_estado
	,ccmb_nombre as cambio
	,ccmb_motivo as cambio_motivo
	,dcmb_fecha as cambio_fecha
from documentoplantilla_dplp dpl 
inner join documentoplantillacaracteristica_dpcp dpc on (dpc.cdpc_plantilla = dpl.cdpl_llave and dpc.cdpc_estado = ''A'')
left join propiedad_ppdp on (cppd_campo = cdpc_llave and (cppd_estado = ''A'' or  $P{P_CAMBIO} is not null))
left join cambio_cmbp on (ccmb_llave = cppd_cambiocreacion or ccmb_llave = cppd_cambioeliminacion)
left join propiedadvalordefinido_pvdp on (cppd_propiedadvalor = cpvd_llave and cpvd_origen = ''C'')
left join relacioninterna_ritp on (crit_propiedad = cppd_llave and crit_estado = ''A'')
where cdpl_estado = ''A'' and (cdpl_llave = $P{P_PLANTILLA} or $P{P_PLANTILLA} is null)
and (cdpl_proceso = $P{P_PROCESO} or $P{P_PROCESO} is null)
and ($P{P_CAMBIO} is null or ccmb_llave = $P{P_CAMBIO})
and ($P{P_ROL} is null or (cppd_rol is null or cppd_rol = $P{P_ROL}))
order by proceso_orden, plantilla_orden, plantilla_nombre, tipo_plantilla, propiedad_grupo, propiedad_valor, subpropiedad_valor]]>
	</queryString>
	<field name="tipo_plantilla" class="java.lang.String"/>
	<field name="nombre" class="java.lang.String"/>
	<field name="codigo" class="java.lang.String"/>
	<field name="objetivo" class="java.lang.String"/>
	<field name="imagen" class="java.lang.String"/>
	<field name="propiedad_grupo" class="java.lang.String"/>
	<field name="propiedad_valor" class="java.lang.String"/>
	<field name="propiedad_valor_final" class="java.lang.String"/>
	<field name="propiedad_motivo" class="java.lang.String"/>
	<field name="proceso" class="java.lang.String"/>
	<field name="proceso_orden" class="java.lang.Integer"/>
	<field name="plantilla_nombre" class="java.lang.String"/>
	<field name="plantilla_orden" class="java.lang.Integer"/>
	<field name="subpropiedad_grupo" class="java.lang.String"/>
	<field name="subpropiedad_valor" class="java.lang.String"/>
	<field name="subpropiedad_motivo" class="java.lang.String"/>
	<field name="relacion_valor" class="java.lang.String"/>
	<field name="relacion_motivo" class="java.lang.String"/>
	<field name="propiedad_estado" class="java.lang.String"/>
	<field name="cambio" class="java.lang.String"/>
	<field name="cambio_motivo" class="java.lang.String"/>
	<field name="cambio_fecha" class="java.sql.Timestamp"/>
	<group name="G_PROCESO" isStartNewPage="true">
		<groupExpression><![CDATA[$F{proceso}]]></groupExpression>
		<groupHeader>
			<band height="30">
				<textField>
					<reportElement style="Titulo" x="0" y="0" width="572" height="30" uuid="8762761e-31bc-4fa9-b0b7-76c8003dbb9e"/>
					<textFieldExpression><![CDATA["PROCESO : " + $F{proceso}]]></textFieldExpression>
				</textField>
			</band>
		</groupHeader>
	</group>
	<group name="G_PLANTILLA_NOMBRE" isStartNewPage="true">
		<groupExpression><![CDATA[$F{plantilla_nombre}]]></groupExpression>
	</group>
	<group name="G_PLANTILLA">
		<groupExpression><![CDATA[$F{tipo_plantilla}]]></groupExpression>
		<groupHeader>
			<band height="49">
				<textField>
					<reportElement style="Titulo" x="0" y="3" width="521" height="27" backcolor="#A3A3A3" uuid="5822a6af-aa32-45d5-b307-391042e8dc42"/>
					<box>
						<topPen lineStyle="Solid" lineColor="#000000"/>
						<leftPen lineStyle="Solid" lineColor="#000000"/>
						<bottomPen lineStyle="Solid" lineColor="#000000"/>
						<rightPen lineStyle="Solid" lineColor="#000000"/>
					</box>
					<textFieldExpression><![CDATA[$F{tipo_plantilla}+ " : " +$F{nombre}]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="true">
					<reportElement style="Objetivo" x="0" y="30" width="572" height="19" uuid="9715aab3-b5ef-4da3-aa0f-d0f5650c35dd"/>
					<box>
						<topPen lineStyle="Solid" lineColor="#000000"/>
						<leftPen lineStyle="Solid" lineColor="#000000"/>
						<bottomPen lineStyle="Solid" lineColor="#000000"/>
						<rightPen lineStyle="Solid" lineColor="#000000"/>
					</box>
					<textFieldExpression><![CDATA[$F{objetivo}]]></textFieldExpression>
				</textField>
				<image hAlign="Center" vAlign="Middle" onErrorType="Blank">
					<reportElement x="524" y="1" width="48" height="48" uuid="a63b0bd9-c749-45f0-8ec4-9efb691c681c"/>
					<imageExpression><![CDATA[$F{imagen}]]></imageExpression>
				</image>
			</band>
		</groupHeader>
	</group>
	<group name="G_GRUPO" isReprintHeaderOnEachPage="true">
		<groupExpression><![CDATA[$F{propiedad_grupo}]]></groupExpression>
		<groupHeader>
			<band height="18">
				<textField>
					<reportElement style="H1" x="0" y="2" width="572" height="16" backcolor="#BABABA" uuid="04d67306-9162-4ee6-b10d-acf53489746c"/>
					<textFieldExpression><![CDATA[$F{propiedad_grupo}]]></textFieldExpression>
				</textField>
			</band>
		</groupHeader>
	</group>
	<group name="G_PROPIEDAD" keepTogether="true">
		<groupExpression><![CDATA[$F{propiedad_valor}]]></groupExpression>
		<groupHeader>
			<band height="13">
				<printWhenExpression><![CDATA[$F{propiedad_motivo}!=null]]></printWhenExpression>
				<textField isStretchWithOverflow="true">
					<reportElement style="h2" stretchType="ContainerHeight" x="3" y="0" width="139" height="13" uuid="636f3019-35ce-4c28-851d-00e00b4c0ff2"/>
					<box>
						<bottomPen lineStyle="Dashed"/>
					</box>
					<textFieldExpression><![CDATA[$F{propiedad_valor}.toUpperCase()]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="true" isBlankWhenNull="true">
					<reportElement stretchType="ContainerHeight" x="142" y="1" width="430" height="12" uuid="800fe33e-9504-43b6-a2ba-006933752715"/>
					<box topPadding="1" leftPadding="3" bottomPadding="1" rightPadding="3">
						<topPen lineWidth="0.5" lineColor="#CCCCCC"/>
					</box>
					<textElement>
						<font size="8"/>
					</textElement>
					<textFieldExpression><![CDATA[$F{propiedad_motivo}]]></textFieldExpression>
				</textField>
			</band>
		</groupHeader>
	</group>
	<group name="G_SUBPROPIEDAD">
		<groupExpression><![CDATA[$F{subpropiedad_motivo} +$F{subpropiedad_valor}]]></groupExpression>
		<groupHeader>
			<band height="13">
				<printWhenExpression><![CDATA[$F{subpropiedad_valor}!=null]]></printWhenExpression>
				<staticText>
					<reportElement style="h2" stretchType="ContainerHeight" x="3" y="0" width="139" height="13" uuid="fc389c3b-1617-48bc-9c6e-a7bd2b57ec71"/>
					<text><![CDATA[]]></text>
				</staticText>
				<textField isStretchWithOverflow="true" isBlankWhenNull="true">
					<reportElement style="Inactivo" stretchType="ContainerHeight" x="289" y="1" width="268" height="12" uuid="3db0bb5f-6610-483d-bc73-ab2c6cd31b3b"/>
					<box>
						<topPen lineWidth="0.25" lineColor="#DBDBDB"/>
						<bottomPen lineStyle="Dashed" lineColor="#CCCCCC"/>
					</box>
					<textFieldExpression><![CDATA[$F{subpropiedad_motivo}]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="true">
					<reportElement style="h2" stretchType="ContainerHeight" x="145" y="1" width="142" height="12" backcolor="#DBDBDB" uuid="ba1cc910-7ada-47ee-a70e-f3dc6836b2ed"/>
					<box>
						<bottomPen lineStyle="Dashed"/>
					</box>
					<textElement>
						<font isBold="false"/>
					</textElement>
					<textFieldExpression><![CDATA[$F{subpropiedad_valor}.toUpperCase()]]></textFieldExpression>
				</textField>
				<rectangle>
					<reportElement x="562" y="2" width="8" height="8" uuid="cde198a7-9593-4601-9a9c-93cbf96da129"/>
				</rectangle>
				<textField isStretchWithOverflow="true">
					<reportElement style="h2" stretchType="ContainerHeight" isPrintRepeatedValues="false" x="3" y="0" width="139" height="13" isRemoveLineWhenBlank="true" uuid="18806371-fa8b-4d49-b6d9-e1982c63c82d">
						<printWhenExpression><![CDATA[$F{propiedad_motivo}==null &&$V{G_PROPIEDAD_COUNT}==0]]></printWhenExpression>
					</reportElement>
					<box>
						<bottomPen lineStyle="Dashed"/>
					</box>
					<textFieldExpression><![CDATA[$F{propiedad_valor}.toUpperCase()]]></textFieldExpression>
				</textField>
			</band>
		</groupHeader>
	</group>
	<title>
		<band height="54">
			<printWhenExpression><![CDATA[$P{P_CAMBIO}!=null]]></printWhenExpression>
			<textField>
				<reportElement style="H1" x="0" y="2" width="572" height="18" backcolor="#BABABA" uuid="8fc39690-f011-4822-bbf1-9f2d91b3bfb6"/>
				<textFieldExpression><![CDATA[$F{cambio}]]></textFieldExpression>
			</textField>
			<textField isStretchWithOverflow="true">
				<reportElement style="Titulo" x="0" y="22" width="572" height="30" uuid="07b4c1ff-c46f-45d6-93aa-727f0c967900"/>
				<textFieldExpression><![CDATA[$F{cambio_motivo}]]></textFieldExpression>
			</textField>
		</band>
	</title>
	<pageHeader>
		<band/>
	</pageHeader>
	<detail>
		<band height="12" splitType="Stretch">
			<printWhenExpression><![CDATA[$F{relacion_motivo}!=null]]></printWhenExpression>
			<staticText>
				<reportElement style="h2" stretchType="ContainerHeight" x="3" y="0" width="139" height="12" uuid="45fe83cc-50a3-40bb-b609-e1575f390c1f"/>
				<text><![CDATA[]]></text>
			</staticText>
			<staticText>
				<reportElement style="h2" stretchType="ContainerHeight" x="145" y="0" width="142" height="12" backcolor="#DBDBDB" uuid="9eb8b9dc-9ab1-44a4-bafd-204c81209c88"/>
				<box>
					<bottomPen lineStyle="Dashed"/>
				</box>
				<textElement textAlignment="Right">
					<font size="7" isBold="false"/>
				</textElement>
				<text><![CDATA[RELACIONES ->]]></text>
			</staticText>
			<textField isStretchWithOverflow="true">
				<reportElement style="Inactivo" stretchType="ContainerHeight" x="315" y="0" width="134" height="12" uuid="5ceb3ebf-bee4-4e1f-82e8-1b90fe931b83"/>
				<box>
					<bottomPen lineStyle="Dashed" lineColor="#CCCCCC"/>
				</box>
				<textElement textAlignment="Left">
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{relacion_valor}]]></textFieldExpression>
			</textField>
			<textField isStretchWithOverflow="true">
				<reportElement style="Inactivo" stretchType="ContainerHeight" x="479" y="0" width="93" height="12" uuid="e4630287-5fa4-452c-8873-8789925725cb"/>
				<box topPadding="0" leftPadding="2" bottomPadding="0" rightPadding="2">
					<bottomPen lineStyle="Dashed" lineColor="#CCCCCC"/>
				</box>
				<textElement>
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{relacion_motivo}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement style="h2" stretchType="ContainerHeight" x="289" y="0" width="26" height="12" backcolor="#DBDBDB" uuid="81033896-9175-40d7-b3e4-6012091646d2"/>
				<box>
					<bottomPen lineStyle="Dashed"/>
				</box>
				<textElement>
					<font size="7" isBold="false"/>
				</textElement>
				<text><![CDATA[FORM]]></text>
			</staticText>
			<staticText>
				<reportElement style="h2" stretchType="ContainerHeight" x="449" y="0" width="30" height="12" backcolor="#DBDBDB" uuid="561f8980-62cd-4ddc-aa62-37668f2829fe"/>
				<box>
					<bottomPen lineStyle="Dashed"/>
				</box>
				<textElement>
					<font size="7" isBold="false"/>
				</textElement>
				<text><![CDATA[CAMPO]]></text>
			</staticText>
		</band>
		<band height="49">
			<printWhenExpression><![CDATA[$F{propiedad_grupo}.compareTo( "IMAGEN" )==0]]></printWhenExpression>
			<image scaleImage="RealSize" hAlign="Center" vAlign="Middle" onErrorType="Blank">
				<reportElement x="145" y="0" width="427" height="49" uuid="3b7040d4-887c-4946-beb0-f92665fe04ea">
					<printWhenExpression><![CDATA[$F{propiedad_valor_final}!=null]]></printWhenExpression>
				</reportElement>
				<imageExpression><![CDATA[$F{propiedad_valor_final}]]></imageExpression>
			</image>
			<staticText>
				<reportElement style="h2" stretchType="ContainerHeight" x="3" y="0" width="139" height="49" uuid="a88b9fc4-f071-42bb-9865-048458866e9a"/>
				<text><![CDATA[]]></text>
			</staticText>
		</band>
	</detail>
	<pageFooter>
		<band/>
	</pageFooter>
</jasperReport>',crpb_codigo = 'CHECK SW42'
WHERE crpb_nombre = 'REQUERIMIENTOS DEL SISTEMA';

update documentoplantilla_dplp set cdpl_imagen = 'http://golyat.cloud/imagenes/check.png' where cdpl_llave = 'DPL_REP';

INSERT INTO cambio_cmbp( ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
VALUES('SC_20200223',  'SC_20200223',  'Colocar el footer al reporte de requerimientos',  now());

INSERT INTO propiedad_ppdp(
  cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto,  cppd_propiedadvalor,  dppd_fechadefinicion,  cppd_motivo,  cppd_cambiocreacion,  
  cppd_tipo,  cppd_codigo)
VALUES(
  'f28bf65134684909a939eb362e36dead',  '55ca8eb82203432a9ae0ddd3b2485d87',  'a8368eef8d2e4383b8d52a1bcf9c1e23',  'PIE DE PAGINA CARTA', 
  'PROP_100',  now(), 'El reporte tendra el pie de pagina general del sistema',  'SC_20200223',  'E',  '2220');
  
update usuario_usrp set cusr_correo = lower(cusr_correo) where cusr_correo is not null;
update mensaje_msjp set cmsj_correo =lower(cmsj_correo) where cmsj_correo is not null; 

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_multiple) 
	VALUES('PROP_115' , 'T', 'MENSAJE DESTINATARIO', 'MENSAJE_DESTINATARIO', 'www.softwareparati.com', 'REQUISITO', 'Se envia un mensaje al siguiente usuario', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_multiple) 
	VALUES('PROP_116' , 'P', 'MENSAJE DESTINATARIO', 'MENSAJE_DESTINATARIO', 'www.softwareparati.com', 'REQUISITO', 'Se envia un mensaje al siguiente usuario', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_multiple) 
	VALUES('PROP_117' , 'L', 'MENSAJE DESTINATARIO', 'MENSAJE_DESTINATARIO', 'www.softwareparati.com', 'REQUISITO', 'Se envia un mensaje al siguiente usuario', true);