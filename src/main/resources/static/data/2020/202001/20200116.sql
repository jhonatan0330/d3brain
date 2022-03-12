COMMENT ON TABLE usuario_usrp IS '2020-01-16';

update reportebase_rpbp set crpb_jaspertext = '<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.8.0.final using JasperReports Library version 6.8.0-2ed8dfabb690ff337a5797129f2cd92902b0c87b  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="Blank_Letter_2" pageWidth="612" pageHeight="792" columnWidth="555" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="f7ab0719-c778-4a80-858e-6625ad528dd8">
	<style name="normal" isDefault="true" hTextAlign="Justified" vTextAlign="Middle" fontName="Arial" fontSize="9">
		<box topPadding="2" leftPadding="2" bottomPadding="2" rightPadding="2"/>
	</style>
	<style name="Titulo" style="normal" mode="Opaque" forecolor="#FFFFFF" backcolor="#828282" fontSize="14" isBold="true"/>
	<style name="Objetivo" style="normal" vTextAlign="Middle"/>
	<style name="H1" style="normal" mode="Opaque" forecolor="#FFFFFF" backcolor="#CCCCCC" fontSize="11" isBold="true"/>
	<style name="h2" style="H1" fontSize="9" isBold="true"/>
	<style name="Inactivo">
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
	,''PASO A PASO'' as propiedad_grupo
	,lpad(dpc.ndpc_orden::text, 2,''0'') || ''. '' || dpc.cdpc_nombre as propiedad_valor
	,null as propiedad_valor_final
	,dpc.cdpc_objetivo as propiedad_motivo
	,(select cprc_nombre from proceso_prcp where cprc_llave = cdpl_proceso) as proceso
	,(select nprc_prioridad from proceso_prcp where cprc_llave = cdpl_proceso) as proceso_orden
	,dpl.cdpl_nombre as plantilla_nombre
	,(select max(npes_avance) from procesoestado_pesp where cpes_llave in (select cptr_estadollegada from procesotransicion_ptrp where cptr_plantilla = cdpl_llave and cptr_proceso = cdpl_proceso and cptr_estado = ''A'')) as plantilla_orden
	,cpvd_grupo as subpropiedad_grupo
	,cpvd_nombre as subpropiedad_valor
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
	<group name="G_PROCESO" isStartNewPage="true" isReprintHeaderOnEachPage="true">
		<groupExpression><![CDATA[$F{proceso}]]></groupExpression>
		<groupHeader>
			<band height="30">
				<textField>
					<reportElement style="Titulo" x="0" y="0" width="571" height="30" uuid="8762761e-31bc-4fa9-b0b7-76c8003dbb9e"/>
					<textFieldExpression><![CDATA[$F{proceso}]]></textFieldExpression>
				</textField>
			</band>
		</groupHeader>
	</group>
	<group name="G_PLANTILLA_NOMBRE" isStartNewPage="true">
		<groupExpression><![CDATA[$F{plantilla_nombre}]]></groupExpression>
	</group>
	<group name="G_PLANTILLA" isReprintHeaderOnEachPage="true">
		<groupExpression><![CDATA[$F{tipo_plantilla}]]></groupExpression>
		<groupHeader>
			<band height="80">
				<textField>
					<reportElement style="Titulo" x="0" y="6" width="521" height="27" uuid="5822a6af-aa32-45d5-b307-391042e8dc42"/>
					<box>
						<topPen lineStyle="Solid" lineColor="#000000"/>
						<leftPen lineStyle="Solid" lineColor="#000000"/>
						<bottomPen lineStyle="Solid" lineColor="#000000"/>
						<rightPen lineStyle="Solid" lineColor="#000000"/>
					</box>
					<textFieldExpression><![CDATA[$F{nombre}]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="true">
					<reportElement style="Objetivo" x="0" y="33" width="572" height="47" uuid="9715aab3-b5ef-4da3-aa0f-d0f5650c35dd"/>
					<box>
						<topPen lineStyle="Solid" lineColor="#000000"/>
						<leftPen lineStyle="Solid" lineColor="#000000"/>
						<bottomPen lineStyle="Solid" lineColor="#000000"/>
						<rightPen lineStyle="Solid" lineColor="#000000"/>
					</box>
					<textFieldExpression><![CDATA[$F{objetivo}]]></textFieldExpression>
				</textField>
				<image hAlign="Center" vAlign="Middle" onErrorType="Blank">
					<reportElement x="521" y="4" width="48" height="48" uuid="a63b0bd9-c749-45f0-8ec4-9efb691c681c"/>
					<imageExpression><![CDATA[$F{imagen}]]></imageExpression>
				</image>
				<textField>
					<reportElement style="normal" mode="Transparent" x="0" y="6" width="521" height="13" forecolor="#FFFFFF" uuid="169292b6-e811-4fa4-9f43-10391cbaa5cf"/>
					<box>
						<topPen lineStyle="Solid" lineColor="#000000"/>
						<leftPen lineStyle="Solid" lineColor="#000000"/>
						<bottomPen lineStyle="Solid" lineColor="#000000"/>
						<rightPen lineStyle="Solid" lineColor="#000000"/>
					</box>
					<textElement textAlignment="Right">
						<font size="7"/>
					</textElement>
					<textFieldExpression><![CDATA[$F{tipo_plantilla}]]></textFieldExpression>
				</textField>
			</band>
		</groupHeader>
	</group>
	<group name="G_GRUPO" isReprintHeaderOnEachPage="true">
		<groupExpression><![CDATA[$F{propiedad_grupo}]]></groupExpression>
		<groupHeader>
			<band height="22">
				<textField>
					<reportElement style="H1" x="0" y="2" width="572" height="18" forecolor="#FFFFFF" backcolor="#BABABA" uuid="04d67306-9162-4ee6-b10d-acf53489746c"/>
					<textFieldExpression><![CDATA[$F{propiedad_grupo}]]></textFieldExpression>
				</textField>
			</band>
		</groupHeader>
	</group>
	<group name="G_PROPIEDAD" keepTogether="true">
		<groupExpression><![CDATA[$F{propiedad_valor}]]></groupExpression>
		<groupHeader>
			<band height="17">
				<textField isStretchWithOverflow="true">
					<reportElement style="h2" stretchType="ContainerHeight" x="3" y="1" width="139" height="16" uuid="636f3019-35ce-4c28-851d-00e00b4c0ff2"/>
					<box>
						<bottomPen lineStyle="Dashed"/>
					</box>
					<textFieldExpression><![CDATA[$F{propiedad_valor}.toUpperCase()]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="true" isBlankWhenNull="true">
					<reportElement stretchType="ContainerHeight" x="142" y="1" width="427" height="16" uuid="800fe33e-9504-43b6-a2ba-006933752715"/>
					<box>
						<topPen lineWidth="0.5" lineColor="#CCCCCC"/>
					</box>
					<textFieldExpression><![CDATA[$F{propiedad_motivo}]]></textFieldExpression>
				</textField>
			</band>
		</groupHeader>
	</group>
	<group name="G_SUBPROPIEDAD">
		<groupExpression><![CDATA[$F{subpropiedad_motivo} +$F{subpropiedad_valor}]]></groupExpression>
		<groupHeader>
			<band height="17">
				<printWhenExpression><![CDATA[$F{subpropiedad_valor}!=null]]></printWhenExpression>
				<staticText>
					<reportElement style="h2" stretchType="ContainerHeight" x="3" y="0" width="139" height="17" uuid="fc389c3b-1617-48bc-9c6e-a7bd2b57ec71"/>
					<text><![CDATA[]]></text>
				</staticText>
				<textField isStretchWithOverflow="true" isBlankWhenNull="true">
					<reportElement style="Inactivo" stretchType="ContainerHeight" x="289" y="1" width="268" height="16" uuid="3db0bb5f-6610-483d-bc73-ab2c6cd31b3b"/>
					<box>
						<topPen lineWidth="0.25" lineColor="#DBDBDB"/>
						<bottomPen lineStyle="Dashed" lineColor="#CCCCCC"/>
					</box>
					<textFieldExpression><![CDATA[$F{subpropiedad_motivo}]]></textFieldExpression>
				</textField>
				<textField isStretchWithOverflow="true">
					<reportElement style="h2" stretchType="ContainerHeight" x="145" y="1" width="142" height="16" backcolor="#DBDBDB" uuid="ba1cc910-7ada-47ee-a70e-f3dc6836b2ed"/>
					<box>
						<bottomPen lineStyle="Dashed"/>
					</box>
					<textElement>
						<font isBold="false"/>
					</textElement>
					<textFieldExpression><![CDATA[$F{subpropiedad_valor}.toUpperCase()]]></textFieldExpression>
				</textField>
				<rectangle>
					<reportElement x="557" y="3" width="12" height="12" uuid="cde198a7-9593-4601-9a9c-93cbf96da129"/>
				</rectangle>
			</band>
		</groupHeader>
	</group>
	<title>
		<band height="54">
			<printWhenExpression><![CDATA[$P{P_CAMBIO}!=null]]></printWhenExpression>
			<textField>
				<reportElement style="H1" x="0" y="2" width="572" height="18" forecolor="#FFFFFF" backcolor="#BABABA" uuid="8fc39690-f011-4822-bbf1-9f2d91b3bfb6"/>
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
		<band height="14" splitType="Stretch">
			<printWhenExpression><![CDATA[$F{relacion_motivo}!=null]]></printWhenExpression>
			<staticText>
				<reportElement style="h2" stretchType="ContainerHeight" x="3" y="0" width="139" height="14" uuid="45fe83cc-50a3-40bb-b609-e1575f390c1f"/>
				<text><![CDATA[]]></text>
			</staticText>
			<staticText>
				<reportElement style="h2" stretchType="ContainerHeight" x="145" y="0" width="142" height="14" backcolor="#DBDBDB" uuid="9eb8b9dc-9ab1-44a4-bafd-204c81209c88"/>
				<box>
					<bottomPen lineStyle="Dashed"/>
				</box>
				<textElement textAlignment="Right">
					<font size="7" isBold="false"/>
				</textElement>
				<text><![CDATA[RELACIONES ->]]></text>
			</staticText>
			<textField isStretchWithOverflow="true">
				<reportElement style="Inactivo" stretchType="ContainerHeight" x="315" y="0" width="134" height="14" uuid="5ceb3ebf-bee4-4e1f-82e8-1b90fe931b83"/>
				<box>
					<bottomPen lineStyle="Dashed" lineColor="#CCCCCC"/>
				</box>
				<textElement textAlignment="Left">
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{relacion_valor}]]></textFieldExpression>
			</textField>
			<textField isStretchWithOverflow="true">
				<reportElement style="Inactivo" stretchType="ContainerHeight" x="479" y="0" width="90" height="14" uuid="e4630287-5fa4-452c-8873-8789925725cb"/>
				<box>
					<bottomPen lineStyle="Dashed" lineColor="#CCCCCC"/>
				</box>
				<textElement>
					<font size="7"/>
				</textElement>
				<textFieldExpression><![CDATA[$F{relacion_motivo}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement style="h2" stretchType="ContainerHeight" x="289" y="0" width="26" height="14" backcolor="#DBDBDB" uuid="81033896-9175-40d7-b3e4-6012091646d2"/>
				<box>
					<bottomPen lineStyle="Dashed"/>
				</box>
				<textElement>
					<font size="7" isBold="false"/>
				</textElement>
				<text><![CDATA[FORM]]></text>
			</staticText>
			<staticText>
				<reportElement style="h2" stretchType="ContainerHeight" x="449" y="0" width="30" height="14" backcolor="#DBDBDB" uuid="561f8980-62cd-4ddc-aa62-37668f2829fe"/>
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
				<reportElement x="3" y="0" width="566" height="49" uuid="3b7040d4-887c-4946-beb0-f92665fe04ea">
					<printWhenExpression><![CDATA[$F{propiedad_valor_final}!=null]]></printWhenExpression>
				</reportElement>
				<imageExpression><![CDATA[$F{propiedad_valor_final}]]></imageExpression>
			</image>
		</band>
	</detail>
</jasperReport>' where crpb_codigo = 'DOCUMENTACION';
