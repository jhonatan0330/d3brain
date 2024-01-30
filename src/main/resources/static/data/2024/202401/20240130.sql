COMMENT ON TABLE usuario_usrp IS '2024-01-30';

CREATE SCHEMA historic AUTHORIZATION postgres;

ALTER TABLE public.z_dcp_detallecaracteristicaproducto SET SCHEMA historic;
ALTER TABLE public.z_dex_documentorelacionexpediente SET SCHEMA historic;
ALTER TABLE public.z_dpv_detallepedidoventa SET SCHEMA historic;
ALTER TABLE public.z_drg_documentorelaciongestor SET SCHEMA historic;
ALTER TABLE public.z_pvc_pedidoventacaracteristica SET SCHEMA historic;
ALTER TABLE public.z_pvd_pedidoventadinero SET SCHEMA historic;
ALTER TABLE public.z_rej_reporteejecucion SET SCHEMA historic;


CREATE OR REPLACE FUNCTION public.migrar_campos(_plantilla character varying, _fecha_maxima timestamp with time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS '
declare 
	documentos character varying[];
	campos character varying[];
	items_documento character varying[];
	v_cnt numeric;
begin
	if
		(select count(*) from procesotransicion_ptrp where cptr_estado = ''A'' and cptr_estadopartida is null and cptr_plantilla = _plantilla) = 0
	then
		select array (
			select cpdv_llave from pedidoventa_pdvp 
				where cpdv_plantilla = _plantilla and dpdv_fecha < _fecha_maxima 
				and npdv_historico is null 
				limit 500) 
			into documentos;
	else
		select array (
			select cpdv_llave from pedidoventa_pdvp 
				where cpdv_plantilla = _plantilla and dpdv_fecha < _fecha_maxima 
				and npdv_historico is null and cpdv_estado != ''A''
				limit 500) 
			into documentos;
	end if;	
	select array (
		select cpvc_llave from pedidoventacaracteristica_pvcp 
			where cpvc_documento = any(documentos)) 
		into campos;
	select array (
		select cdpv_llave from detallepedidoventa_dpvp 
			where cdpv_documento = any(documentos))
		into items_documento;
	INSERT INTO historic.z_pvc_pedidoventacaracteristica (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado) 
		select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado
	 		from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	INSERT INTO historic.z_dex_documentorelacionexpediente (cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor)
		SELECT cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor
			FROM documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	INSERT INTO historic.z_pvd_pedidoventadinero(cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha, bpvd_controlarsaldo)
		SELECT cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha, bpvd_controlarsaldo
			FROM pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	INSERT INTO historic.z_drg_documentorelaciongestor (cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido)
		SELECT cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido
			FROM documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	INSERT INTO historic.z_rej_reporteejecucion (crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado, crej_url)
		SELECT crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado , crej_url
			FROM reporteejecucion_rejp where crej_documento = any(documentos);
	INSERT INTO historic.z_dpv_detallepedidoventa (cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo, cdpv_campo, cdpv_nombre)
		SELECT cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo, cdpv_campo, cdpv_nombre
			FROM detallepedidoventa_dpvp where cdpv_llave = any(items_documento);
	INSERT INTO historic.z_dcp_detallecaracteristicaproducto (cdcp_llave, cdcp_entidad, cdcp_estado, ddcp_valorfecha, cdcp_valortext, mdcp_valornumero, cdcp_valoropcion, cdcp_campo, cdcp_transaccionregistro, cdcp_transaccioninactivo)
		SELECT  cdcp_llave, cdcp_entidad, cdcp_estado, ddcp_valorfecha, cdcp_valortext, mdcp_valornumero, cdcp_valoropcion, cdcp_campo, cdcp_transaccionregistro, cdcp_transaccioninactivo
			FROM detallecaracteristicaproducto_dcpp where cdcp_entidad = any(items_documento);
	delete from detallecaracteristicaproducto_dcpp where cdcp_entidad = any(items_documento);
	delete from detallepedidoventa_dpvp where cdpv_llave = any(items_documento);
	delete from reporteejecucion_rejp where crej_documento = any(documentos);
	delete from documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	delete from pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	delete from documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	delete from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	update pedidoventa_pdvp set npdv_historico = 3 where cpdv_llave = any(documentos);
	GET DIAGNOSTICS v_cnt = ROW_COUNT;
	return v_cnt;
END;'
;
