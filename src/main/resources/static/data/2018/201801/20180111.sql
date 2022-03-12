
COMMENT ON TABLE usuario_usrp IS '2018-01-11';

COMMENT ON TABLE usuariosesion_ussp IS '2018.01.11.02';


delete from documentorelacionexpediente_dexp  where cdex_campomaestro  in (select cpvc_llave from pedidoventacaracteristica_pvcp  where cpvc_documento  in (select cpdv_llave from pedidoventa_pdvp  where cpdv_plantilla  in (SELECT cdpl_llave FROM documentoplantilla_dplp  where cdpl_tipo  = 'R')));
delete from documentopermisodocumento_dpdp  where cdpd_destino in (select cpdv_llave from pedidoventa_pdvp  where cpdv_plantilla  in (SELECT cdpl_llave FROM documentoplantilla_dplp  where cdpl_tipo  = 'R'));
delete from documentopermisodocumento_dpdp  where cdpd_origen in (select cpdv_llave from pedidoventa_pdvp  where cpdv_plantilla  in (SELECT cdpl_llave FROM documentoplantilla_dplp  where cdpl_tipo  = 'R'));
delete from pedidoventacaracteristica_pvcp  where cpvc_documento  in (select cpdv_llave from pedidoventa_pdvp  where cpdv_plantilla  in (SELECT cdpl_llave FROM documentoplantilla_dplp  where cdpl_tipo  = 'R'));
delete from pedidoventa_pdvp  where cpdv_plantilla  in (SELECT cdpl_llave FROM documentoplantilla_dplp  where cdpl_tipo  = 'R');
delete from plantillacampoparametro_pcpp  where cpcp_campo in (SELECT cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_plantilla in (SELECT cdpl_llave FROM documentoplantilla_dplp where cdpl_tipo  = 'R'));
delete from documentoplantillacaracteristica_dpcp where cdpc_plantilla in (SELECT cdpl_llave FROM documentoplantilla_dplp  where cdpl_tipo  = 'R');

INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, bdpc_obligatorio, ndpc_orden, bdpc_editable, cdpc_nombre, cdpc_codigo, cdpc_formato, bdpc_visiblerender)
    SELECT 'POS006-1', 'POS006', true, 1, true, 'FECHAS', 'FECHA', 'F', true WHERE  EXISTS (SELECT cdpl_llave FROM documentoplantilla_dplp  WHERE cdpl_llave = 'POS006');
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, bdpc_obligatorio, ndpc_orden, bdpc_editable, cdpc_nombre, cdpc_codigo, cdpc_formato, bdpc_visiblerender)
    SELECT 'POS006-2', 'POS006', true, 2, true, 'CUENTA', 'CUENTA', 'C', true WHERE  EXISTS (SELECT cdpl_llave FROM documentoplantilla_dplp  WHERE cdpl_llave = 'POS006');
INSERT INTO plantillacampoparametro_pcpp(cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
    SELECT 'POS006-1', 'POS006-1', 'FECHA_RANGO', 'TRUE' WHERE  EXISTS (SELECT cdpl_llave FROM documentoplantilla_dplp  WHERE cdpl_llave = 'POS006');

update idiomareemplazo_irep set cire_clave = 'LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.documentoPlantillaCaracteristicaList.titulo.HGroup._MVCList_MVCLabelnull' where cire_llave = 'ADMIN_P_CARACTERISTICA';

INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'ADMIN_P_CAR_PARAMETRO','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.plantillaCampoParametroList.titulo.HGroup._MVCList_MVCLabelnull','PARAMETROS' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'ADMIN_P_CAR_PARAMETRO');

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
	select substring('FSH' || cdpc_llave , 0, 32), cdpc_llave, 'FECHA_SIN_HORA', 'TRUE' from documentoplantillacaracteristica_dpcp where cdpc_estado = 'A' and cdpc_formato = 'F' and cdpc_valordefecto is not null;

update documentoplantillacaracteristica_dpcp set cdpc_valordefecto = null where cdpc_formato = 'F' and cdpc_valordefecto is not null;

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
	select substring('NM' || cdpc_llave , 0, 32), cdpc_llave, 'NUMERO_MONEDA', 'TRUE' from documentoplantillacaracteristica_dpcp where cdpc_estado = 'A' and cdpc_formato = 'N' and cdpc_valordefecto is not null;

update documentoplantillacaracteristica_dpcp set cdpc_valordefecto = null where cdpc_formato = 'N' and cdpc_valordefecto is not null;

UPDATE plantillacampoparametro_pcpp SET cpcp_key  = 'BASICA' where cpcp_key  = 'NUMERO_FORMULA' AND cpcp_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_estado = 'A' and cdpc_formato = 'N');