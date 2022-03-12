COMMENT ON TABLE usuario_usrp IS '2018-09-29';

COMMENT ON TABLE usuariosesion_ussp IS '2018.09.29.00';



CREATE TABLE procesodecision_pdcp (
	cpdc_llave character varying(32) NOT NULL,
	cpdc_proceso character varying(32) NOT NULL,
	cpdc_pregunta character varying(100) NOT NULL,
	cpdc_codigo character varying(20) NOT NULL,
	cpdc_funcion character varying(100) NOT NULL,
	cpdc_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE procesodecisionrespuesta_pdrp (
	cpdr_llave character varying(32) NOT NULL,
	cpdr_decision character varying(32) NOT NULL,
	cpdr_respuesta character varying(20) NOT NULL,
	cpdr_estadollegada character varying(32) NOT NULL,
	cpdr_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE procesotransicion_ptrp
	ADD COLUMN cptr_decision character varying(32);



ALTER TABLE procesodecision_pdcp
	ADD CONSTRAINT pk_procesodecision_pdcp PRIMARY KEY (cpdc_llave);

ALTER TABLE procesodecisionrespuesta_pdrp
	ADD CONSTRAINT pk_procesodecisionrespuesta_pdrp PRIMARY KEY (cpdr_llave);

ALTER TABLE procesodecision_pdcp
	ADD CONSTRAINT fk_procesodecisionproceso FOREIGN KEY (cpdc_proceso) REFERENCES public.proceso_prcp(cprc_llave);

ALTER TABLE procesodecisionrespuesta_pdrp
	ADD CONSTRAINT fk_procesodecisionrespuestadecision FOREIGN KEY (cpdr_decision) REFERENCES public.procesodecision_pdcp(cpdc_llave);

ALTER TABLE procesotransicion_ptrp
	ADD CONSTRAINT fk_procesotransiciondecision FOREIGN KEY (cptr_decision) REFERENCES public.procesodecision_pdcp(cpdc_llave);
	
ALTER TABLE comprobanteconfiguraciondetalle_ccdp
	ADD COLUMN cccd_valordebe character varying(20),
	ADD COLUMN cccd_valorhaber character varying(20);

INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'CPB_COMPROBANTE','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.VBox.HBox.VBox.ComprobanteConfiguracionDetalleList.titulo.HGroup._MVCList_MVCLabelnull','COMPROBANTE DETALLE' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'CPB_COMPROBANTE');
INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'PRO_PROCESO','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.procesosList.titulo.HGroup._MVCList_MVCLabelnull','PROCESOS' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'PRO_PROCESO');
INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'PRO_PROCESO_TRANSICION','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.ProcesoTransicionList.titulo.HGroup._MVCList_MVCLabelnull','TRANSICION' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'PRO_PROCESO_TRANSICION');
INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'DESC_DECISION','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.decisionList.titulo.HGroup._MVCList_MVCLabelnull','DESICION' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'DESC_DECISION');
INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'COMPR_COMPROBANTE','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.VBox.HBox.VBox.ComprobanteConfiguracionList.titulo.HGroup._MVCList_MVCLabelnull','COMPROBANTES' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'COMPR_COMPROBANTE');
INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'CUENTA_CONTABLE','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.VBox.HBox.VBox.CuentaContableList.titulo.HGroup._MVCList_MVCLabelnull','CUENTAS' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'CUENTA_CONTABLE');
INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'CATAG_CONTABLE','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.VBox.HBox.VBox.CatalogoContableList.titulo.HGroup._MVCList_MVCLabelnull','CATALOGOS' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'CATAG_CONTABLE');

