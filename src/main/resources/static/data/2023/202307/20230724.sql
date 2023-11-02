COMMENT ON TABLE usuario_usrp IS '2023-07-24';

ALTER TABLE documentorelaciongestor_drgp ADD bdrg_estadorepetido bool NOT NULL DEFAULT false;

ALTER TABLE z_drg_documentorelaciongestor ADD bdrg_estadorepetido bool NOT NULL DEFAULT false;

CREATE OR REPLACE FUNCTION public.ultima_gestion(_documento character varying, _estado character varying)
 RETURNS TABLE(cdrg_llave character varying, cdrg_documentoprincipal character varying, cdrg_documentomodificador character varying, ddrg_fecha timestamp with time zone, cdrg_estadoinicial character varying, cdrg_estadofinal character varying, cdrg_estado character varying, cdrg_ubicacion character varying, cdrg_valores character varying, cdrg_usuario character varying, ddrg_cierre timestamp with time zone, cdrg_nombre character varying, cdrg_transaccion character varying)
 LANGUAGE plpgsql
AS '
begin
	return query select 
		drg.cdrg_llave,
		drg.cdrg_documentoprincipal,
		drg.cdrg_documentomodificador,
		drg.ddrg_fecha,
		drg.cdrg_estadoinicial,
		drg.cdrg_estadofinal,
		drg.cdrg_estado,
		drg.cdrg_ubicacion,
		drg.cdrg_valores,
		drg.cdrg_usuario,
		drg.ddrg_cierre,
		drg.cdrg_nombre,
		drg.cdrg_transaccion
	from documentorelaciongestor_drgp drg 
	where drg.cdrg_documentoprincipal = _documento and drg.cdrg_estado = ''A'' and drg.cdrg_estadofinal = _estado and bdrg_estadorepetido = true;	
END;'
;

CREATE OR REPLACE FUNCTION migrar_campos(_plantilla character varying, _fecha_maxima timestamp with time zone)
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
	INSERT INTO z_pvc_pedidoventacaracteristica (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado) 
		select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado
	 		from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	INSERT INTO z_dex_documentorelacionexpediente (cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor)
		SELECT cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor
			FROM documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	INSERT INTO z_pvd_pedidoventadinero(cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha)
		SELECT cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha
			FROM pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	INSERT INTO z_drg_documentorelaciongestor (cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido)
		SELECT cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido
			FROM documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	INSERT INTO z_rej_reporteejecucion (crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado)
		SELECT crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado 
			FROM reporteejecucion_rejp where crej_documento = any(documentos);
	INSERT INTO z_dpv_detallepedidoventa (cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo)
		SELECT cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo
			FROM detallepedidoventa_dpvp where cdpv_llave = any(items_documento);
	INSERT INTO z_dcp_detallecaracteristicaproducto (cdcp_llave, cdcp_entidad, cdcp_estado, ddcp_valorfecha, cdcp_valortext, mdcp_valornumero, cdcp_valoropcion, cdcp_campo, cdcp_transaccionregistro, cdcp_transaccioninactivo)
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
END;';



CREATE OR REPLACE FUNCTION organizar_ultima_gestion()
 RETURNS void
 LANGUAGE plpgsql
AS '
declare
    ele record;
begin
		drop table if exists tbl_ultima;
	create temp table tbl_ultima as		
	select 
		cdrg_documentoprincipal as principal,
		cdrg_estadofinal as estadofinal
	from documentorelaciongestor_drgp dd 
	where bdrg_estadorepetido = false
	group by cdrg_documentoprincipal , cdrg_estadofinal
	having count(*)>1
	limit 1000;

	for ele in select principal, estadofinal from tbl_ultima
    loop 
	    update documentorelaciongestor_drgp
	    set bdrg_estadorepetido = true
	    where cdrg_llave in (select g.cdrg_llave from documentorelaciongestor_drgp g
	    					where g.cdrg_documentoprincipal = ele.principal
	    					and g.cdrg_estadofinal = ele.estadofinal
	    					and bdrg_estadorepetido = false
	    					order by g.ddrg_fecha
	    					limit 1);
    end loop;
   
   	drop table if exists tbl_ultima;
    
   	drop table if exists tbl_ultima_z;
	create temp table tbl_ultima_z as		
	select 
		cdrg_documentoprincipal as principal,
		cdrg_estadofinal as estadofinal
	from z_drg_documentorelaciongestor dd 
	group by cdrg_documentoprincipal , cdrg_estadofinal
	having count(*)>1
	limit 1000;
	
	for ele in select principal, estadofinal 
           from tbl_ultima_z
    loop 
	    update z_drg_documentorelaciongestor
	    set bdrg_estadorepetido = true
	    where cdrg_llave in (select g.cdrg_llave from z_drg_documentorelaciongestor g
	    					where g.cdrg_documentoprincipal = ele.principal
	    					and g.cdrg_estadofinal = ele.estadofinal
	    					and bdrg_estadorepetido = false
	    					order by g.ddrg_fecha
	    					limit 1);
    end loop;
   	drop table if exists tbl_ultima_z;	
END;';

select * from organizar_ultima_gestion();
