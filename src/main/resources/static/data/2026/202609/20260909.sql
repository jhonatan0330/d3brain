COMMENT ON TABLE usuario_usrp IS '2026-09-09';

CREATE OR REPLACE FUNCTION public.migrar_campos(_plantilla character varying, _fecha_maxima timestamp with time zone) RETURNS numeric
AS '
declare 
	documentos character varying[];
	campos character varying[];
	items_documento character varying[];
	v_cnt numeric;
begin
	--1. Reviso si es un proceso o un formulario normal
	--2. Obtengo los documentos
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
	--3. Obtengo los campos de esos documento
	select array (
		select cpvc_llave from pedidoventacaracteristica_pvcp 
			where cpvc_documento = any(documentos)) 
		into campos;
	--4. Obtengo los detallepedidoventa de esos documento
	select array (
		select cdpv_llave from detallepedidoventa_dpvp 
			where cdpv_documento = any(documentos))
		into items_documento;
	--5. Inserto la informacion en historicos
	INSERT INTO historic.z_pvc_pedidoventacaracteristica (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado) 
		select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado
	 		from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	 		
	INSERT INTO historic.z_dex_documentorelacionexpediente (cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor, cdex_documentoregistro, cdex_documentoinactivo)
		SELECT cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor, cdex_documentoregistro, cdex_documentoinactivo
			FROM documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
			
	INSERT INTO historic.z_pvd_pedidoventadinero(cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha, bpvd_controlarsaldo, cpvd_modificador)
		SELECT cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha, bpvd_controlarsaldo, cpvd_modificador
			FROM pedidoventadinero_pvdp where cpvd_documento = any(documentos);
			
			
	INSERT INTO historic.z_drg_documentorelaciongestor (cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido)
		SELECT cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido
			FROM documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
			
	INSERT INTO historic.z_pvu_pedidoventaubicacion (cpvu_llave, cpvu_documento, dpvu_fecha, cpvu_ubicacion, cpvu_modificador, cpvu_estado)
		SELECT cpvu_llave, cpvu_documento, dpvu_fecha, cpvu_ubicacion, cpvu_modificador, cpvu_estado
			FROM pedidoventaubicacion_pvup where cpvu_documento = any(documentos);
			
	INSERT INTO historic.z_rej_reporteejecucion (crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado, crej_url)
		SELECT crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado , crej_url
			FROM reporteejecucion_rejp where crej_documento = any(documentos);
			
	INSERT INTO historic.z_dpv_detallepedidoventa (cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo, cdpv_campo, cdpv_nombre, cdpv_detalleid)
		SELECT cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo, cdpv_campo, cdpv_nombre, cdpv_detalleid
			FROM detallepedidoventa_dpvp where cdpv_llave = any(items_documento);
			
	INSERT INTO historic.z_msj_mensaje (cmsj_llave, dmsj_fecha, cmsj_titulo, cmsj_usuario, cmsj_documento, cmsj_template, cmsj_parametros, dmsj_leido, dmsj_correoenviado, cmsj_correoerror, cmsj_correo, cmsj_reporte, cmsj_transaccion, cmsj_estado, cmsj_adjuntourl)	
		SELECT cmsj_llave, dmsj_fecha, cmsj_titulo, cmsj_usuario, cmsj_documento, cmsj_template, cmsj_parametros, dmsj_leido, dmsj_correoenviado, cmsj_correoerror, cmsj_correo, cmsj_reporte, cmsj_transaccion, cmsj_estado, cmsj_adjuntourl
			FROM public.mensaje_msjp where cmsj_documento = any(documentos);

	INSERT INTO historic.z_wse_webserviceejecucion (cwse_llave, cwse_servicio, cwse_usuario, dwse_fecha, cwse_documento, cwse_modificador, cwse_transaccion, cwse_parametros, dwse_fechaejecucion, cwse_entrada, cwse_salida, cwse_error, cwse_masivo, cwse_extracciones, cwse_sincrona, cwse_estado)
		SELECT cwse_llave, cwse_servicio, cwse_usuario, dwse_fecha, cwse_documento, cwse_modificador, cwse_transaccion, cwse_parametros, dwse_fechaejecucion, cwse_entrada, cwse_salida, cwse_error, cwse_masivo, cwse_extracciones, cwse_sincrona, cwse_estado
			FROM public.webserviceejecucion_wsep where cwse_documento = any(documentos);

	-- 6. Elimino la información de la fuente principal
	delete from detallepedidoventa_dpvp where cdpv_llave = any(items_documento);
	delete from webserviceejecucion_wsep where cwse_documento = any(documentos);
	delete from mensaje_msjp where cmsj_documento = any(documentos);
	delete from reporteejecucion_rejp where crej_documento = any(documentos);
	delete from pedidoventaubicacion_pvup where cpvu_documento = any(documentos);
	delete from documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	delete from pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	delete from documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	delete from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	update pedidoventa_pdvp set npdv_historico = 3 where cpdv_llave = any(documentos);
	GET DIAGNOSTICS v_cnt = ROW_COUNT;
	return v_cnt;
END;
' LANGUAGE plpgsql STRICT;


CREATE OR REPLACE FUNCTION historic.mover_datos_historico()
RETURNS TABLE (
    tabla text,
    registros_movidos bigint
)
AS '
DECLARE
    v_fecha_limite timestamptz;
    v_cantidad bigint;
BEGIN

    -- =========================================================
    -- FECHA DE CORTE
    -- =========================================================

    v_fecha_limite := CURRENT_TIMESTAMP - INTERVAL ''1 year'';


    -- =========================================================
    -- 1. CARGA ARCHIVO
    -- =========================================================

    WITH registros_insertados AS (
        INSERT INTO historic.z_car_cargaarchivo (
            ccar_llave,
            ccar_servidor,
            ncar_size,
            ccar_url,
            dcar_fechainicio,
            dcar_fechafin,
            ccar_error,
            ccar_usuario,
            ccar_estado
        )
        SELECT
            ccar_llave,
            ccar_servidor,
            ncar_size,
            ccar_url,
            dcar_fechainicio,
            dcar_fechafin,
            ccar_error,
            ccar_usuario,
            ccar_estado
        FROM public.cargaarchivo_carp
        WHERE dcar_fechafin < v_fecha_limite
        ORDER BY dcar_fechafin
        LIMIT 500
        RETURNING ccar_llave
    )
    DELETE FROM public.cargaarchivo_carp p
    WHERE p.ccar_llave IN (
        SELECT ccar_llave
        FROM registros_insertados
    );

    GET DIAGNOSTICS v_cantidad = ROW_COUNT;

    tabla := ''cargaarchivo_carp'';
    registros_movidos := v_cantidad;
    RETURN NEXT;


    -- =========================================================
    -- 2. USUARIO SESION
    -- =========================================================

    WITH registros_insertados AS (
        INSERT INTO historic.z_uss_usuariosesion (
            cuss_llave,
            cuss_usuario,
            duss_fecha,
            duss_fechacierre,
            cuss_ip,
            cuss_estado,
            buss_privada
        )
        SELECT
            cuss_llave,
            cuss_usuario,
            duss_fecha,
            duss_fechacierre,
            cuss_ip,
            cuss_estado,
            buss_privada
        FROM public.usuariosesion_ussp
        WHERE duss_fecha < v_fecha_limite
        ORDER BY duss_fecha
        LIMIT 500
        RETURNING cuss_llave
    )
    DELETE FROM public.usuariosesion_ussp p
    WHERE p.cuss_llave IN (
        SELECT cuss_llave
        FROM registros_insertados
    );

    GET DIAGNOSTICS v_cantidad = ROW_COUNT;

    tabla := ''usuariosesion_ussp'';
    registros_movidos := v_cantidad;
    RETURN NEXT;


    -- =========================================================
    -- 3. TRANSACCION LOG
    -- =========================================================

    WITH registros_insertados AS (
        INSERT INTO historic.z_tlg_transaccionlog (
            ctlg_llave,
            dtlg_fechainicio,
            dtlg_fechafin,
            ctlg_transaccion,
            ctlg_estado,
            ctlg_sesion,
            ctlg_usuario,
            ctlg_entrada,
            ctlg_salida
        )
        SELECT
            ctlg_llave,
            dtlg_fechainicio,
            dtlg_fechafin,
            ctlg_transaccion,
            ctlg_estado,
            ctlg_sesion,
            ctlg_usuario,
            ctlg_entrada,
            ctlg_salida
        FROM public.transaccionlog_tlgp
        WHERE dtlg_fechafin < v_fecha_limite
        ORDER BY dtlg_fechafin
        LIMIT 500
        RETURNING ctlg_llave
    )
    DELETE FROM public.transaccionlog_tlgp p
    WHERE p.ctlg_llave IN (
        SELECT ctlg_llave
        FROM registros_insertados
    );

    GET DIAGNOSTICS v_cantidad = ROW_COUNT;

    tabla := ''transaccionlog_tlgp'';
    registros_movidos := v_cantidad;
    RETURN NEXT;


    -- =========================================================
    -- 4. PROCESO TRANSICION AUTOMATICA
    -- =========================================================

    WITH registros_insertados AS (
        INSERT INTO historic.z_pta_procesotransicionautomatica (
            cpta_llave,
            dpta_fecha,
            cpta_transicion,
            cpta_propiedad,
            dpta_ejecucion,
            cpta_mensaje,
            cpta_estado
        )
        SELECT
            cpta_llave,
            dpta_fecha,
            cpta_transicion,
            cpta_propiedad,
            dpta_ejecucion,
            cpta_mensaje,
            cpta_estado
        FROM public.procesotransicionautomatica_ptap
        WHERE dpta_fecha < v_fecha_limite
        ORDER BY dpta_fecha
        LIMIT 500
        RETURNING cpta_llave
    )
    DELETE FROM public.procesotransicionautomatica_ptap p
    WHERE p.cpta_llave IN (
        SELECT cpta_llave
        FROM registros_insertados
    );

    GET DIAGNOSTICS v_cantidad = ROW_COUNT;

    tabla := ''procesotransicionautomatica_ptap'';
    registros_movidos := v_cantidad;
    RETURN NEXT;


    -- =========================================================
    -- 5. TRANSACCION ERROR
    -- =========================================================

    WITH registros_insertados AS (
        INSERT INTO historic.z_ter_transaccionerror (
            cter_llave,
            dter_fechainicio,
            dter_fechafin,
            cter_error,
            cter_usuario,
            cter_estado,
            cter_entrada
        )
        SELECT
            cter_llave,
            dter_fechainicio,
            dter_fechafin,
            cter_error,
            cter_usuario,
            cter_estado,
            cter_entrada
        FROM public.transaccionerror_terp
        WHERE dter_fechafin < v_fecha_limite
        ORDER BY dter_fechafin
        LIMIT 500
        RETURNING cter_llave
    )
    DELETE FROM public.transaccionerror_terp p
    WHERE p.cter_llave IN (
        SELECT cter_llave
        FROM registros_insertados
    );

    GET DIAGNOSTICS v_cantidad = ROW_COUNT;

    tabla := ''transaccionerror_terp'';
    registros_movidos := v_cantidad;
    RETURN NEXT;

END;
' LANGUAGE plpgsql STRICT;


