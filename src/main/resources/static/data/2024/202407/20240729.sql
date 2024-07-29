COMMENT ON TABLE usuario_usrp IS '2024-07-29';

CREATE OR REPLACE FUNCTION public.regresar_campos(_documento character varying)
 RETURNS numeric
AS '
declare 
	campos character varying[];
	items_documento character varying[];
	v_cnt numeric;
begin
	select array (
		select cpvc_llave from historic.z_pvc_pedidoventacaracteristica 
			where cpvc_documento = _documento) 
		into campos;
	select array (
		select cdpv_llave from historic.z_dpv_detallepedidoventa 
			where cdpv_documento = _documento)
		into items_documento;
	
	INSERT INTO pedidoventacaracteristica_pvcp (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado) 
		select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado
	 		from historic.z_pvc_pedidoventacaracteristica where cpvc_documento = _documento;
	INSERT INTO documentorelacionexpediente_dexp (cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor)
		SELECT cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor
			FROM historic.z_dex_documentorelacionexpediente where cdex_campomaestro = any(campos);
	INSERT INTO pedidoventadinero_pvdp(cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha, bpvd_controlarsaldo)
		SELECT cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha, bpvd_controlarsaldo
			FROM historic.z_pvd_pedidoventadinero where cpvd_documento = _documento;
	INSERT INTO documentorelaciongestor_drgp(cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido)
		SELECT cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido
			FROM historic.z_drg_documentorelaciongestor where cdrg_documentoprincipal = _documento;
	INSERT INTO reporteejecucion_rejp (crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado, crej_url)
		SELECT crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado , crej_url
			FROM historic.z_rej_reporteejecucion where crej_documento = _documento;
	INSERT INTO detallepedidoventa_dpvp (cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo, cdpv_campo, cdpv_nombre)
		SELECT cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo, cdpv_campo, cdpv_nombre
			FROM historic.z_dpv_detallepedidoventa where cdpv_llave = any(items_documento);	
	INSERT INTO detallecaracteristicaproducto_dcpp (cdcp_llave, cdcp_entidad, cdcp_estado, ddcp_valorfecha, cdcp_valortext, mdcp_valornumero, cdcp_valoropcion, cdcp_campo, cdcp_transaccionregistro, cdcp_transaccioninactivo)
		SELECT  cdcp_llave, cdcp_entidad, cdcp_estado, ddcp_valorfecha, cdcp_valortext, mdcp_valornumero, cdcp_valoropcion, cdcp_campo, cdcp_transaccionregistro, cdcp_transaccioninactivo
			FROM historic.z_dcp_detallecaracteristicaproducto where cdcp_entidad = any(items_documento);


	delete from historic.z_dcp_detallecaracteristicaproducto where cdcp_entidad = any(items_documento);
	delete from historic.z_dpv_detallepedidoventa where cdpv_llave = any(items_documento);
	delete from historic.z_rej_reporteejecucion where crej_documento = _documento;
	delete from historic.z_drg_documentorelaciongestor where cdrg_documentoprincipal = _documento;
	delete from historic.z_pvd_pedidoventadinero where cpvd_documento = _documento;
	delete from historic.z_dex_documentorelacionexpediente where cdex_campomaestro = any(campos);
	delete from historic.z_pvc_pedidoventacaracteristica where cpvc_documento = _documento;
	update pedidoventa_pdvp set npdv_historico = null where cpdv_llave = _documento;
	GET DIAGNOSTICS v_cnt = ROW_COUNT;
	return v_cnt;
END;
' LANGUAGE plpgsql STRICT;
