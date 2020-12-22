delete from mensaje_msjp where cmsj_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from detallecaracteristicaproducto_dcpp where cdcp_entidad in (select cdpv_llave from detallepedidoventa_dpvp where cdpv_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I')));
delete from detallecaracteristicaproducto_dcpp where cdcp_campo in (select cpvc_llave from pedidoventacaracteristica_pvcp where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I')));
delete from detallepedidoventa_dpvp where cdpv_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from documentorelacionexpediente_dexp where cdex_campomaestro in (select cpvc_llave from pedidoventacaracteristica_pvcp where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I')));
delete from relacioninterna_ritp where crit_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from pedidoventacaracteristica_pvcp where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from plantillaconsecutivo_pcnp where cpcn_caracteristica  in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from documentoplantillacaracteristica_dpcp where cdpc_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I');
delete from procesotransicion_ptrp where cptr_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I');
delete from documentorelaciongestor_drgp where cdrg_documentoprincipal in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from documentorelacionexpediente_dexp where cdex_expedientedetalle in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from documentorelaciongestor_drgp where cdrg_documentomodificador in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from pedidoventadinero_pvdp where cpvd_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from plantillaconsecutivo_pcnp where cpcn_valoropcion in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from pedidoventacaracteristica_pvcp where cpvc_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from actividad_actp where cact_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from reportebase_rpbp where crpb_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I');
delete from usuariorol_erlp where cerl_rolacceso in (select crac_llave from rolacceso_racp where crac_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from propiedad_ppdp where cppd_rol in (select crac_llave from rolacceso_racp where crac_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from rolacceso_racp where crac_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I');
delete from pedidoventaajuste_pvap where cpva_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from cuenta_cuep where ccue_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from bodega_bodp where cbod_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I');
delete from documentoplantilla_dplp where cdpl_estado = 'I';
delete from consecutivo_conp where ccon_llave not in (select cdpl_consecutivo from documentoplantilla_dplp) and ccon_llave not in (select cpcn_consecutivo from plantillaconsecutivo_pcnp);
select * from documentoplantilla_dplp where cdpl_estado = 'I' order by cdpl_nombre;

select * from pedidoventa_pdvp where cpdv_estadoexpediente in (
	select cpes_llave from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I'));

update pedidoventa_pdvp set cpdv_estadoexpediente = null where cpdv_estadoexpediente in (
	select cpes_llave from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I'));

delete from procesotransicion_ptrp where cptr_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I');
delete from documentorelaciongestor_drgp where cdrg_estadofinal in (select cpes_llave from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I'));
delete from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I');
delete from proceso_prcp where cprc_estado = 'I';

select * from proceso_prcp;

