/*
	Arreglo los titulos del modulo de administracion
*/
COMMENT ON TABLE usuario_usrp IS '2018-02-05';

update idiomareemplazo_irep set cire_clave = 'LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.ExpedienteTransicionList.titulo.HGroup._MVCList_MVCLabelnull' where cire_llave = 'ADMIN_D_EXPEDIENTE';
update idiomareemplazo_irep set cire_clave = 'LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.estadosList.titulo.HGroup._MVCList_MVCLabelnull' where cire_llave = 'ADMIN_D_ESTADO';
update idiomareemplazo_irep set cire_clave = 'LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.ReporteBaseList.titulo.HGroup._MVCList_MVCLabelnull' where cire_llave = 'ADMIN_REPORTE';

/*
INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'ADMIN_D_EXPEDIENTE','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.ExpedienteTransicionList.titulo.HGroup._MVCList_MVCLabelnull','TRANSICIONES' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'ADMIN_D_EXPEDIENTE');
INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'ADMIN_D_ESTADO','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.estadosList.titulo.HGroup._MVCList_MVCLabelnull','ESTADO' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'ADMIN_D_ESTADO');
INSERT INTO idiomareemplazo_irep(cire_llave, cire_clave,cire_nombre)SELECT 'ADMIN_REPORTE','LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIAdministracion.TabNavigator.HBox.VBox.ReporteBaseList.titulo.HGroup._MVCList_MVCLabelnull','REPORTES' WHERE NOT EXISTS (SELECT cire_llave FROM idiomareemplazo_irep WHERE cire_llave = 'ADMIN_REPORTE');
*/