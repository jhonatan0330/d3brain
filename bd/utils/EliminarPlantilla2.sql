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
delete from reporteejecucion_rejp where crej_reporte in (select crpb_llave from  reportebase_rpbp where crpb_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));

delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'E' and cppd_campo in (select crpb_llave from reportebase_rpbp where crpb_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I')));
delete from propiedad_ppdp where cppd_tipo = 'E' and cppd_campo in (select crpb_llave from reportebase_rpbp where crpb_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from reportebase_rpbp where crpb_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I');
delete from usuariorol_erlp where cerl_rolacceso in (select crac_llave from rolacceso_racp where crac_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from propiedad_ppdp where cppd_rol in (select crac_llave from rolacceso_racp where crac_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from rolacceso_racp where crac_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I');
delete from pedidoventaajuste_pvap where cpva_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from cuenta_cuep where ccue_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from bodega_bodp where cbod_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from tarifa_tarp where ctar_producto in (select cpro_llave from producto_prop where cpro_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I')));
delete from detallepedidoventa_dpvp where cdpv_producto in (select cpro_llave from producto_prop where cpro_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I')));
delete from productocaracteristica_pcrp where cpcr_base in (select cpro_llave from producto_prop where cpro_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I')));
delete from producto_prop where cpro_documento in (select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from pedidoventa_pdvp where cpdv_plantilla in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I');

select * from documentoplantilla_dplp where cdpl_estado = 'I' order by cdpl_nombre;
delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'L' and cppd_campo in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I'));
delete from propiedad_ppdp where cppd_tipo = 'L' and cppd_campo in (select cdpl_llave from documentoplantilla_dplp where cdpl_estado = 'I');
delete from documentoplantilla_dplp where cdpl_estado = 'I';
delete from consecutivo_conp where ccon_llave not in (select cdpl_consecutivo from documentoplantilla_dplp) and ccon_llave not in (select cpcn_consecutivo from plantillaconsecutivo_pcnp);

select * from pedidoventa_pdvp where cpdv_estadoexpediente in (
	select cpes_llave from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I'));

update pedidoventa_pdvp set cpdv_estadoexpediente = null where cpdv_estadoexpediente in (
	select cpes_llave from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I'));

delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'T' and cppd_campo in (select cptr_llave from procesotransicion_ptrp where cptr_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I')));
delete from propiedad_ppdp where cppd_tipo = 'T' and cppd_campo in (select cptr_llave from procesotransicion_ptrp where cptr_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I'));
delete from procesotransicion_ptrp where cptr_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I');
delete from documentorelaciongestor_drgp where cdrg_estadofinal in (select cpes_llave from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I'));

delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'A' and cppd_campo in (select cpes_llave from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I')));
delete from propiedad_ppdp where cppd_tipo = 'A' and cppd_campo in (select cpes_llave from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I'));
delete from procesoestado_pesp  where cpes_proceso in (select cprc_llave from proceso_prcp where cprc_estado = 'I');

select * from proceso_prcp where cprc_estado = 'I';
delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'P' and cppd_campo in (select cprc_llave from proceso_prcp where cprc_estado = 'I'));
delete from propiedad_ppdp where cppd_tipo = 'P' and cppd_campo in (select cprc_llave from proceso_prcp where cprc_estado = 'I');
delete from proceso_prcp where cprc_estado = 'I';

/*
delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_estado = 'I');
delete from propiedad_ppdp where cppd_estado = 'I';

delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'P' and cppd_campo not in (select cprc_llave from proceso_prcp ));
delete from propiedad_ppdp where cppd_tipo = 'P' and cppd_campo not in (select cprc_llave from proceso_prcp );
delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'A' and cppd_campo not in (select cpes_llave from procesoestado_pesp));
delete from propiedad_ppdp where cppd_tipo = 'A' and cppd_campo not in (select cpes_llave from procesoestado_pesp);
delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'T' and cppd_campo not in (select cptr_llave from procesotransicion_ptrp));
delete from propiedad_ppdp where cppd_tipo = 'T' and cppd_campo not in (select cptr_llave from procesotransicion_ptrp);
delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'L' and cppd_campo not in (select cdpl_llave from documentoplantilla_dplp));
delete from propiedad_ppdp where cppd_tipo = 'L' and cppd_campo not in (select cdpl_llave from documentoplantilla_dplp);
delete from relacioninterna_ritp where crit_propiedad in (select cppd_llave from propiedad_ppdp where cppd_tipo = 'E' and cppd_campo not in (select crpb_llave from reportebase_rpbp));
delete from propiedad_ppdp where cppd_tipo = 'E' and cppd_campo not in (select crpb_llave from reportebase_rpbp);

select * from propiedad_ppdp where cppd_tipo = 'E' and cppd_campo in (select crpb_llave from reportebase_rpbp);

select * from propiedad_ppdp where cppd_tipo = 'E'

*/
/*
delete from auditoria_audp;
update documentotransaccion_trap set ctra_usuario = 'PROCESS' where ctra_usuario in (select cusr_llave from usuario_usrp where cusr_estado = 'I');
delete from usuarioautenticacion_uaup where cuau_usuario in (select cusr_llave from usuario_usrp where cusr_estado = 'I');
delete from documentorelaciongestor_drgp where cdrg_usuario in (select cusr_llave from usuario_usrp where cusr_estado = 'I');
delete from usuariosesion_ussp where cuss_usuario in (select cusr_llave from usuario_usrp where cusr_estado = 'I');
update pedidoventa_pdvp set cpdv_funcionario = 'PROCESS' where cpdv_funcionario in (select cusr_llave from usuario_usrp where cusr_estado = 'I');
delete from usuariorol_erlp where  cerl_usuario in (select cusr_llave from usuario_usrp where cusr_estado = 'I');
delete from mensaje_msjp where cmsj_usuario in (select cusr_llave from usuario_usrp where cusr_estado = 'I');
delete from usuario_usrp where cusr_estado = 'I';
*/
