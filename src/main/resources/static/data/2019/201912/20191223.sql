COMMENT ON TABLE usuario_usrp IS '2019-12-23';

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha,  dcmb_fechaaplicacion,  ccmb_estado,  ccmb_sesionactiva)VALUES('SC_REP',  'SC REPORTES DOCUMENTACION',  'Ingresar los reportes de documentaciOn',  now(),  NULL,  'A',  NULL);

INSERT INTO consecutivo_conp(ccon_llave,  ccon_nombre,  ccon_prefijo,  ccon_sufijo,  ccon_estado,  bcon_manual,  mcon_numeroinicial,  mcon_numerofinal,  mcon_numeroactual)VALUES('CON_REP',  'INFORMES GENERALES',  'DOC-',  NULL,  'A',  FALSE,  100.00,  0.00,  100.00);

INSERT INTO documentoplantilla_dplp(cdpl_llave,  cdpl_nombre,  cdpl_consecutivo,  cdpl_estado,  cdpl_imagen,  cdpl_color,  cdpl_codigo,  cdpl_tipo,  cdpl_proceso,  cdpl_objetivo)VALUES('DPL_REP',  'INFORMES GENERALES',  'CON_REP',  'A',  'http://golyat.cloud/imagenes/modulo.png',  NULL,  'DOCUMENTACION',  'R',  'RRHH',  'Permite imprimir los reportes que muestran la documentacion del sistema');

INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave,  cdpc_plantilla,  bdpc_obligatorio,  cdpc_estado,  ndpc_orden,  bdpc_visiblerender,  bdpc_editable,  cdpc_imagen,  bdpc_modificable,  cdpc_nombre,  cdpc_codigo,  cdpc_formato,  cdpc_objetivo)VALUES('ce1318a457124f0fba0894f171ba675f',  'DPL_REP',  FALSE,  'A',  1,  FALSE,  TRUE,  NULL,  FALSE,  'PLANTILLA',  'PLANTILLA',  'G',  'Permite seleccionar una plantilla para filtrar la documentacion');
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave,  cdpc_plantilla,  bdpc_obligatorio,  cdpc_estado,  ndpc_orden,  bdpc_visiblerender,  bdpc_editable,  cdpc_imagen,  bdpc_modificable,  cdpc_nombre,  cdpc_codigo,  cdpc_formato,  cdpc_objetivo)VALUES('671e153b061b40b48e69cb21c49b1357',  'DPL_REP',  FALSE,  'A',  2,  FALSE,  TRUE,  NULL,  FALSE,  'PROCESO',  'PROCESO',  'G',  'Seleccionar un proceso para conocer la documentacion del proceso');
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave,  cdpc_plantilla,  bdpc_obligatorio,  cdpc_estado,  ndpc_orden,  bdpc_visiblerender,  bdpc_editable,  cdpc_imagen,  bdpc_modificable,  cdpc_nombre,  cdpc_codigo,  cdpc_formato,  cdpc_objetivo)VALUES('671e153b061b40b48e69cb21c49b1358',  'DPL_REP',  FALSE,  'A',  3,  FALSE,  TRUE,  NULL,  FALSE,  'CAMBIO',  'CAMBIO',  'G',  'Seleccionar un cambio para conocer la documentacion del proceso');

INSERT INTO documentoplantillarol_dprp(cdpr_llave,  cdpr_plantilla,  cdpr_rol, bdpr_crear,  bdpr_vertodos, ndpr_orden)
select substring('REP_' || crac_llave, 0, 32 ),'DPL_REP', crac_llave, TRUE, true, 200 from rolacceso_racp where crac_estado = 'A' and brac_permisoscompletos = true;

INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto,  cppd_estado,  cppd_propiedadvalor,  dppd_fechadefinicion,  dppd_fechaimplementacion,  cppd_motivo,  cppd_cambiocreacion,  cppd_cambioeliminacion,  cppd_tipo,  cppd_codigo,  bppd_necesario)VALUES('8fa382ef18874d3fb225a9ca0ef1d791',  'ce1318a457124f0fba0894f171ba675f',  'PLANTILLAS',  NULL,  'A',  'PROP_08',  now(),  NULL,  'Permite escoger entre las plantillas activas',  'SC_REP',  NULL,  'C',  '860',  FALSE);
INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto,  cppd_estado,  cppd_propiedadvalor,  dppd_fechadefinicion,  dppd_fechaimplementacion,  cppd_motivo,  cppd_cambiocreacion,  cppd_cambioeliminacion,  cppd_tipo,  cppd_codigo,  bppd_necesario)VALUES('b592559b6aa0426b8e5dd58a9009d364',  '671e153b061b40b48e69cb21c49b1357',  'PROCESO',  NULL,  'A',  'PROP_08',  now(),  NULL,  'Permite escoger entre los procesos activos del sistema',  'SC_REP',  NULL,  'C',  '861',  FALSE);
INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto,  cppd_estado,  cppd_propiedadvalor,  dppd_fechadefinicion,  dppd_fechaimplementacion,  cppd_motivo,  cppd_cambiocreacion,  cppd_cambioeliminacion,  cppd_tipo,  cppd_codigo,  bppd_necesario)VALUES('b592559b6aa0426b8e5dd58a9009d365',  '671e153b061b40b48e69cb21c49b1358',  'CAMBIO',  NULL,  'A',  'PROP_08',  now(),  NULL,  'Permite escoger entre los cambios activos del sistema',  'SC_REP',  NULL,  'C',  '861',  FALSE);
INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto,  cppd_estado,  cppd_propiedadvalor,  dppd_fechadefinicion,  dppd_fechaimplementacion,  cppd_motivo,  cppd_cambiocreacion,  cppd_cambioeliminacion,  cppd_tipo,  cppd_codigo,  bppd_necesario)VALUES('c8144ca461484756bac2a1fc4dea58e7',  '55ca8eb82203432a9ae0ddd3b2485d87',  'a8368eef8d2e4383b8d52a1bcf9c1e2e',  'ENCABEZADO CARTA',  'A',  'PROP_70', now(),  NULL,  'El reporte tendra el encabezado general del sistema',  'SC_REP',  NULL,  'E',  '862',  FALSE);

INSERT INTO reportebase_rpbp(crpb_llave,  crpb_nombre,  crpb_jaspertext, crpb_plantilla,  crpb_codigo,  nrpb_version,  crpb_descripcion)VALUES('55ca8eb82203432a9ae0ddd3b2485d87',  'REQUERIMIENTOS DEL SISTEMA',  
'<?xml version="1.0" encoding="UTF-8"?>
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
	<queryString language="SQL">
		<![CDATA[select 
	''FORMULARIO'' as tipo_plantilla
	,dpl.cdpl_nombre as nombre
	,dpl.cdpl_codigo as codigo
	,dpl.cdpl_objetivo as objetivo
	,dpl.cdpl_imagen as imagen
	,cpvd_grupo as propiedad_grupo
	,cpvd_nombre as propiedad_valor
	,cppd_motivo as propiedad_motivo
	,(select cprc_nombre from proceso_prcp where cprc_llave = cdpl_proceso) as proceso
	,(select nprc_prioridad from proceso_prcp where cprc_llave = cdpl_proceso) as proceso_orden
	,dpl.cdpl_nombre as plantilla_nombre
	,(select max(npes_avance) from procesoestado_pesp where cpes_llave in (select cptr_estadollegada from procesotransicion_ptrp where cptr_plantilla = dpl.cdpl_llave and cptr_proceso = dpl.cdpl_proceso and cptr_estado = ''A'')) as plantilla_orden
	,null as subpropiedad_grupo
	,null as subpropiedad_valor
	,null as subpropiedad_motivo
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
union
select 
	''REPORTE'' as tipo_plantilla
	,rpb.crpb_nombre as nombre
	,rpb.crpb_codigo as codigo
	,rpb.crpb_descripcion as objetivo
	,cdpl_imagen as imagen
	,cpvd_grupo as propiedad_grupo
	,coalesce(cppd_texto, cppd_valor) as propiedad_valor
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
union
select 
	''FORMULARIO'' as tipo_plantilla
	,dpl.cdpl_nombre as nombre
	,dpl.cdpl_codigo as codigo
	,dpl.cdpl_objetivo as objetivo
	,dpl.cdpl_imagen as imagen
	,''PASO A PASO'' as propiedad_grupo
	,lpad(dpc.ndpc_orden::text, 2,''0'') || ''. '' || dpc.cdpc_nombre as propiedad_valor
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
order by proceso_orden, plantilla_orden, plantilla_nombre, tipo_plantilla, propiedad_grupo, propiedad_valor]]>
	</queryString>
	<field name="tipo_plantilla" class="java.lang.String"/>
	<field name="nombre" class="java.lang.String"/>
	<field name="codigo" class="java.lang.String"/>
	<field name="objetivo" class="java.lang.String"/>
	<field name="imagen" class="java.lang.String"/>
	<field name="propiedad_grupo" class="java.lang.String"/>
	<field name="propiedad_valor" class="java.lang.String"/>
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
	<group name="G_PLANTILLA">
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
	<group name="G_GRUPO">
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
	<group name="G_PROPIEDAD">
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
				<textField isStretchWithOverflow="true">
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
		<groupExpression><![CDATA[$F{subpropiedad_motivo}]]></groupExpression>
		<groupHeader>
			<band height="17">
				<printWhenExpression><![CDATA[$F{subpropiedad_valor}!=null]]></printWhenExpression>
				<staticText>
					<reportElement style="h2" stretchType="ContainerHeight" x="3" y="0" width="139" height="17" uuid="fc389c3b-1617-48bc-9c6e-a7bd2b57ec71"/>
					<text><![CDATA[]]></text>
				</staticText>
				<textField isStretchWithOverflow="true">
					<reportElement style="Inactivo" stretchType="ContainerHeight" x="289" y="1" width="280" height="16" uuid="3db0bb5f-6610-483d-bc73-ab2c6cd31b3b"/>
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
	</detail>
</jasperReport>',  'DPL_REP',  'DOCUMENTACION',  1,  'Muestra los requerimientos del sistema');

INSERT INTO reportebase_rpbp(crpb_llave,  crpb_nombre,  crpb_jaspertext, crpb_plantilla,  crpb_codigo,  nrpb_version,  crpb_descripcion, brpb_soloexistente)VALUES('a8368eef8d2e4383b8d52a1bcf9c1e2e',  'ENCABEZADO CARTA',  
'<?xml version="1.0" encoding="UTF-8"?>
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
				<textFieldExpression><![CDATA["PÃ¡gina " + $P{P_NUMBER_PAGE}]]></textFieldExpression>
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
</jasperReport>', 'DPL_REP', 'ENC_LETTER',  1,  'Servir como plantilla para todos los reportes tamano carta', true);