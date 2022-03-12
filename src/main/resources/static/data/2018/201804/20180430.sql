
COMMENT ON TABLE usuario_usrp IS '2018-04-30';

update pedidoventadinero_pvdp set mpvd_saldo = mpvd_valortotal where mpvd_saldo = 0 and cpvd_estado = 'A'
and cpvd_documento in (select cpdv_llave from pedidoventa_pdvp  where cpdv_estado  = 'A');

DROP TABLE productodisponibilidadtiempo_pdtp;

ALTER TABLE cuenta_cuep
	DROP COLUMN bcue_permiteproyecciones;

ALTER TABLE cuentapermisousuario_cpup
	DROP COLUMN ccpu_tipo;

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor) select cppr_llave, cdpc_llave, 'DETALLE_TARIFARIO', cppr_tarifario from plantillaparametro_pprp , documentoplantillacaracteristica_dpcp
where cdpc_plantilla = cppr_plantilla and cppr_tarifario is not null and cdpc_formato = 'J';
ALTER TABLE plantillaparametro_pprp
	DROP COLUMN cppr_tarifario;

update tarifa_tarp set  ctar_recurso = ctar_caracteristica1 where ctar_caracteristica1 is not null;
ALTER TABLE tarifa_tarp
	DROP COLUMN ctar_caracteristica1,
	DROP COLUMN ctar_caracteristica2,
	DROP COLUMN ctar_caracteristica3;

ALTER TABLE tarifario_trfp
	DROP COLUMN ctrf_caracteristica1,
	DROP COLUMN ctrf_caracteristica2,
	DROP COLUMN ctrf_caracteristica3,
	DROP COLUMN ctrf_rolrecurso;

update idiomareemplazo_irep set cire_clave = 'LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIProducto.categoriaList.titulo.HGroup._MVCList_MVCLabelnull' where cire_llave = 'CATEGORIA PRODUCTO';

update idiomareemplazo_irep set cire_clave = 'LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIProducto.productoList.titulo.HGroup._MVCList_MVCLabelnull' where cire_llave = 'PROD_PROD';

update idiomareemplazo_irep set cire_clave = 'LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIProducto._UIProducto_VGroup.tarifaList.titulo.HGroup._MVCList_MVCLabelnull' where cire_llave = 'VALOR PRODUCTO';

update idiomareemplazo_irep set cire_clave = 'LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIProducto._UIProducto_VGroup.productoCaracteristicaList.titulo.HGroup._MVCList_MVCLabelnull' where cire_llave = 'CARACTERISTICAS';

update idiomareemplazo_irep set cire_clave = 'LOGISTICPYMES.ApplicationSkin._ApplicationSkin_Group.contentGroup.Group.HGroup.servicesViewer.BorderContainerSkin.Group.serviceCvs.UIProducto._UIProducto_VGroup.productoInventarioDescuentoList.titulo.HGroup._MVCList_MVCLabelnull' where cire_llave = 'PRD_composicion';

delete from idiomareemplazo_irep where cire_llave  in ('CARACTERISTICAS PRODUCTOS', 'MODULOS', 'OPCION CARACTERISTICA', 'PRD_OPCIONES');

update modulocontratado_mdcp set cmdc_identificador = 'SEDE' where cmdc_modulo = 'Inventarios';

INSERT INTO reportebase_rpbp (crpb_llave, crpb_nombre, crpb_jaspertext, crpb_plantilla, brpb_soloexistente, crpb_codigo)
    SELECT 'POS029', 'PREARQUEO DE CAJA', '', 'CAJA', TRUE, 'POS029' FROM documentoplantilla_dplp WHERE cdpl_llave = 'CAJA'