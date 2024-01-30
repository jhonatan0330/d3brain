 
CREATE TABLE plantillaconsecutivo_pcnp(
        cpcn_llave character varying(32) NOT NULL,
        cpcn_caracteristica character varying(32) NOT NULL,
        cpcn_valoropcion character varying(32) NOT NULL,
        cpcn_consecutivo character varying(32) NOT NULL,
        cpcn_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_plantillaconsecutivo_pcnp PRIMARY KEY (cpcn_llave)
    );
 
CREATE TABLE proceso_prcp(
        cprc_llave character varying(32) NOT NULL,
        cprc_tipo character varying(1) NOT NULL,
        cprc_objetivo character varying(4000) NOT NULL,
        cprc_imagen character varying(2000),
        nprc_prioridad int NOT NULL DEFAULT 0,
        cprc_macroproceso character varying(32),
        cprc_nombre character varying(100) NOT NULL,
        cprc_codigo character varying(50) NOT NULL,
        cprc_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_proceso_prcp PRIMARY KEY (cprc_llave)
    );
 
CREATE TABLE documentotransaccion_trap(
        ctra_llave character varying(32) NOT NULL,
        dtra_fecha timestamp with time zone NOT NULL,
        ctra_usuario character varying(32) NOT NULL,
        ctra_sesion character varying(32),
        ctra_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_documentotransaccion_trap PRIMARY KEY (ctra_llave)
    );
 
CREATE TABLE procesoestado_pesp(
        cpes_llave character varying(32) NOT NULL,
        cpes_tipo character varying(1) NOT NULL,
        cpes_estadodocumento character varying(1) NOT NULL,
        npes_avance int NOT NULL DEFAULT 0,
        cpes_nombre character varying(100) NOT NULL,
        cpes_codigo character varying(50) NOT NULL,
        cpes_proceso character varying(32) NOT NULL,
        cpes_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_procesoestado_pesp PRIMARY KEY (cpes_llave)
    );
 
CREATE TABLE procesotransicionautomatica_ptap(
        cpta_llave character varying(32) NOT NULL,
        dpta_fecha timestamp with time zone NOT NULL,
        cpta_transicion character varying(32),
        cpta_propiedad character varying(32) NOT NULL,
        dpta_ejecucion timestamp with time zone,
        cpta_mensaje character varying(4000) NOT NULL,
        cpta_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_procesotransicionautomatica_ptap PRIMARY KEY (cpta_llave)
    );
 
CREATE TABLE documentorelacionexpediente_dexp(
        cdex_llave character varying(32) NOT NULL,
        cdex_campomaestro character varying(32) NOT NULL,
        cdex_expedientedetalle character varying(32) NOT NULL,
        cdex_transaccionregistro character varying(32) NOT NULL,
        cdex_transaccioninactivo character varying(32),
        mdex_valor NUMERIC(18,6) NOT NULL DEFAULT 0,
        cdex_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_documentorelacionexpediente_dexp PRIMARY KEY (cdex_llave)
    );
 
CREATE TABLE documentorelaciongestor_drgp(
        cdrg_llave character varying(32) NOT NULL,
        cdrg_documentoprincipal character varying(32) NOT NULL,
        cdrg_documentomodificador character varying(32),
        ddrg_fecha timestamp with time zone NOT NULL,
        cdrg_estadoinicial character varying(32),
        cdrg_estadofinal character varying(32),
        cdrg_usuario character varying(32) NOT NULL,
        cdrg_ubicacion character varying(32),
        cdrg_valores character varying(32),
        cdrg_transaccion character varying(32),
        ddrg_cierre timestamp with time zone,
        cdrg_nombre character varying(100) NOT NULL,
        cdrg_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_documentorelaciongestor_drgp PRIMARY KEY (cdrg_llave)
    );
 
CREATE TABLE documentoplantillacaracteristica_dpcp(
        cdpc_llave character varying(32) NOT NULL,
        cdpc_objetivo character varying(4000) NOT NULL,
        cdpc_plantilla character varying(32) NOT NULL,
        cdpc_formato character varying(1) NOT NULL,
        cdpc_nombre character varying(100) NOT NULL,
        cdpc_codigo character varying(20) NOT NULL,
        ndpc_orden int NOT NULL DEFAULT 0,
        cdpc_imagen character varying(2000),
        cdpc_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_documentoplantillacaracteristica_dpcp PRIMARY KEY (cdpc_llave)
    );
 
CREATE TABLE pedidoventa_pdvp(
        cpdv_llave character varying(32) NOT NULL,
        dpdv_fecharegistro timestamp with time zone NOT NULL,
        dpdv_fecha timestamp with time zone NOT NULL,
        cpdv_funcionario character varying(32) NOT NULL,
        cpdv_plantilla character varying(32) NOT NULL,
        mpdv_consecutivo NUMERIC(18,6) NOT NULL DEFAULT 0,
        cpdv_nombre character varying(32) NOT NULL,
        cpdv_estadoexpediente character varying(32),
        cpdv_textofiltro character varying(4000),
        npdv_historico int NOT NULL DEFAULT 0,
        cpdv_transaccion character varying(32) NOT NULL,
        cpdv_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_pedidoventa_pdvp PRIMARY KEY (cpdv_llave)
    );
 
CREATE TABLE pedidoventacaracteristica_pvcp(
        cpvc_llave character varying(32) NOT NULL,
        cpvc_documento character varying(32) NOT NULL,
        cpvc_campo character varying(32) NOT NULL,
        cpvc_valortext character varying(4000) NOT NULL,
        dpvc_valorfecha timestamp with time zone,
        cpvc_valoropcion character varying(32),
        cpvc_valorauxiliar character varying(32),
        mpvc_valornumero NUMERIC(18,6) NOT NULL DEFAULT 0,
        cpvc_transaccionregistro character varying(32) NOT NULL,
        cpvc_transaccioninactivo character varying(32),
        cpvc_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_pedidoventacaracteristica_pvcp PRIMARY KEY (cpvc_llave)
    );
 
CREATE TABLE procesotransicion_ptrp(
        cptr_llave character varying(32) NOT NULL,
        cptr_nombre character varying(100) NOT NULL,
        cptr_codigo character varying(50) NOT NULL,
        cptr_proceso character varying(32) NOT NULL,
        cptr_estadopartida character varying(32),
        cptr_plantilla character varying(32),
        bptr_documentador boolean NOT NULL DEFAULT false,
        cptr_afectasaldo character varying(1),
        bptr_rapida boolean NOT NULL DEFAULT false,
        cptr_estadollegada character varying(32),
        cptr_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_procesotransicion_ptrp PRIMARY KEY (cptr_llave)
    );
 
CREATE TABLE pedidoventaajuste_pvap(
        cpva_llave character varying(32) NOT NULL,
        cpva_documento character varying(32) NOT NULL,
        dpva_fecha timestamp with time zone NOT NULL,
        cpva_estadoinicial character varying(32) NOT NULL,
        cpva_estadofinal character varying(32) NOT NULL,
        cpva_motivo character varying(4000) NOT NULL,
        cpva_responsable character varying(32) NOT NULL,
        cpva_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_pedidoventaajuste_pvap PRIMARY KEY (cpva_llave)
    );
 
CREATE TABLE documentoplantilla_dplp(
        cdpl_llave character varying(32) NOT NULL,
        cdpl_objetivo character varying(4000),
        cdpl_nombre character varying(100) NOT NULL,
        cdpl_consecutivo character varying(32),
        cdpl_imagen character varying(2000),
        cdpl_codigo character varying(32) NOT NULL UNIQUE,
        cdpl_proceso character varying(32) NOT NULL,
        cdpl_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_documentoplantilla_dplp PRIMARY KEY (cdpl_llave)
    );
 
CREATE TABLE movimiento_movp(
        cmov_llave character varying(32) NOT NULL,
        cmov_tipo character varying(1) NOT NULL,
        dmov_fecharegistro timestamp with time zone NOT NULL,
        dmov_fechaevento timestamp with time zone NOT NULL,
        cmov_cuenta character varying(32) NOT NULL,
        mmov_monto NUMERIC(18,6) NOT NULL DEFAULT 0,
        cmov_turno character varying(32),
        mmov_montoaplicado NUMERIC(18,6) NOT NULL DEFAULT 0,
        mmov_saldoinicial NUMERIC(18,6) NOT NULL DEFAULT 0,
        mmov_saldofinal NUMERIC(18,6) NOT NULL DEFAULT 0,
        cmov_anterior character varying(32),
        cmov_siguiente character varying(32),
        cmov_relacionado character varying(32),
        cmov_documento character varying(32),
        cmov_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_movimiento_movp PRIMARY KEY (cmov_llave)
    );
 
CREATE TABLE turno_turp(
        ctur_llave character varying(32) NOT NULL,
        dtur_fechaapertura timestamp with time zone,
        dtur_fechaentrega timestamp with time zone,
        mtur_montoinicial NUMERIC(18,6) NOT NULL DEFAULT 0,
        mtur_montofinal NUMERIC(18,6) NOT NULL DEFAULT 0,
        ctur_documento character varying(32) NOT NULL,
        ctur_usuario character varying(32) NOT NULL,
        ctur_cuenta character varying(32) NOT NULL,
        ctur_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_turno_turp PRIMARY KEY (ctur_llave)
    );
 
CREATE TABLE tarifa_tarp(
        ctar_llave character varying(32) NOT NULL,
        ctar_tarifario character varying(32) NOT NULL,
        ctar_producto character varying(32),
        ctar_recurso character varying(32),
        btar_rangoprecios boolean NOT NULL DEFAULT false,
        mtar_valorminimo NUMERIC(18,6) NOT NULL DEFAULT 0,
        mtar_valor NUMERIC(18,6) NOT NULL DEFAULT 0,
        mtar_valormaximo NUMERIC(18,6) NOT NULL DEFAULT 0,
        ntar_cantidadminima int NOT NULL DEFAULT 0,
        ntar_cantidadmaxima int NOT NULL DEFAULT 0,
        mtar_totalminimo NUMERIC(18,6) NOT NULL DEFAULT 0,
        ctar_dimension2 character varying(32),
        ctar_dimension3 character varying(32),
        ctar_dimension4 character varying(32),
        ctar_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_tarifa_tarp PRIMARY KEY (ctar_llave)
    );
 
CREATE TABLE pedidoventadinero_pvdp(
        cpvd_llave character varying(32) NOT NULL,
        cpvd_documento character varying(32) NOT NULL,
        dpvd_fecha timestamp with time zone NOT NULL,
        mpvd_valortotal NUMERIC(18,6) NOT NULL DEFAULT 0,
        mpvd_saldo NUMERIC(18,6) NOT NULL DEFAULT 0,
        bpvd_controlarsaldo boolean NOT NULL DEFAULT false,
        cpvd_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_pedidoventadinero_pvdp PRIMARY KEY (cpvd_llave)
    );
 
CREATE TABLE cuenta_cuep(
        ccue_llave character varying(32) NOT NULL,
        ccue_codigo character varying(50) NOT NULL,
        ccue_nombre character varying(100) NOT NULL,
        ccue_documento character varying(32) NOT NULL,
        dcue_fechaconciliacion timestamp with time zone,
        bcue_validarturno boolean NOT NULL DEFAULT false,
        ccue_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_cuenta_cuep PRIMARY KEY (ccue_llave)
    );
 
CREATE TABLE tarifario_trfp(
        ctrf_llave character varying(32) NOT NULL,
        ctrf_nombre character varying(100) NOT NULL UNIQUE,
        ctrf_tiporecurso character varying(32),
        ctrf_tipodimension2 character varying(32),
        ctrf_tipodimension3 character varying(32),
        ctrf_tipodimension4 character varying(32),
        btrf_productoopcional boolean NOT NULL DEFAULT false,
        btrf_rangovalores boolean NOT NULL DEFAULT false,
        btrf_rangocantidad boolean NOT NULL DEFAULT false,
        ctrf_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_tarifario_trfp PRIMARY KEY (ctrf_llave)
    );
 
CREATE TABLE actividad_actp(
        cact_llave character varying(32) NOT NULL,
        cact_responsable character varying(32) NOT NULL,
        cact_documento character varying(32) NOT NULL,
        cact_comentario character varying(4000) NOT NULL,
        dact_fechaarrancar timestamp with time zone,
        dact_fecharegistro timestamp with time zone NOT NULL,
        cact_usuarioregistro character varying(32) NOT NULL,
        dact_fechainactivo timestamp with time zone,
        dact_fechaterminar timestamp with time zone,
        dact_fechalimite timestamp with time zone,
        cact_usuarioinactivo character varying(32),
        nact_duracion int NOT NULL DEFAULT 0,
        cact_actividadprevia character varying(32),
        cact_actividadsiguiente character varying(32),
        dact_fechaleido timestamp with time zone,
        cact_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_actividad_actp PRIMARY KEY (cact_llave)
    );
 
CREATE TABLE pedidoventatiempo_pvtp(
        cpvt_llave character varying(32) NOT NULL,
        cpvt_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_pedidoventatiempo_pvtp PRIMARY KEY (cpvt_llave)
    );
 
CREATE TABLE propiedad_ppdp(
        cppd_llave character varying(32) NOT NULL,
        cppd_propiedadvalor character varying(32) NOT NULL,
        cppd_tipo character varying(1) NOT NULL,
        cppd_campo character varying(32) NOT NULL,
        cppd_valor character varying(180000) NOT NULL,
        cppd_texto character varying(100),
        dppd_fechadefinicion timestamp with time zone NOT NULL,
        dppd_fechaimplementacion timestamp with time zone,
        cppd_cambiocreacion character varying(32) NOT NULL,
        cppd_cambioeliminacion character varying(32),
        cppd_rol character varying(32),
        cppd_rolexcluyente character varying(32),
        dppd_fechainicial timestamp with time zone,
        dppd_fechafinal timestamp with time zone,
        cppd_usuario character varying(32),
        cppd_usuarioexcluyente character varying(32),
        cppd_motivo character varying(4000),
        cppd_bloqueo character varying(200),
        cppd_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_propiedad_ppdp PRIMARY KEY (cppd_llave)
    );
 
CREATE TABLE relacioninterna_ritp(
        crit_llave character varying(32) NOT NULL,
        crit_propiedad character varying(32) NOT NULL,
        crit_plantilla character varying(32) NOT NULL,
        crit_campo character varying(32) NOT NULL,
        crit_auxiliar character varying(4000),
        drit_fechainicio timestamp with time zone,
        crit_cambiocreacion character varying(32) NOT NULL,
        crit_cambioeliminacion character varying(32),
        crit_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_relacioninterna_ritp PRIMARY KEY (crit_llave)
    );
 
CREATE TABLE encuestarespuesta_ersp(
        cers_llave character varying(32) NOT NULL,
        cers_pregunta character varying(32) NOT NULL,
        ders_fecha timestamp with time zone NOT NULL,
        cers_usuario character varying(32) NOT NULL,
        bers_respuestaboolean boolean NOT NULL DEFAULT false,
        cers_respuestaopcion character varying(32),
        cers_comentario character varying(4000),
        cers_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_encuestarespuesta_ersp PRIMARY KEY (cers_llave)
    );
 
CREATE TABLE cambio_cmbp(
        ccmb_llave character varying(32) NOT NULL,
        ccmb_nombre character varying(100) NOT NULL,
        ccmb_motivo character varying(4000) NOT NULL,
        dcmb_fecha timestamp with time zone NOT NULL,
        dcmb_fechaaplicacion timestamp with time zone,
        ccmb_sesionactiva character varying(32),
        ccmb_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_cambio_cmbp PRIMARY KEY (ccmb_llave)
    );
 
CREATE TABLE encuestaopcionrespuesta_eorp(
        ceor_llave character varying(32) NOT NULL,
        ceor_codigo character varying(20) NOT NULL,
        ceor_nombre character varying(100) NOT NULL,
        ceor_imagen character varying(2000),
        ceor_pregunta character varying(32) NOT NULL,
        ceor_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_encuestaopcionrespuesta_eorp PRIMARY KEY (ceor_llave)
    );
 
CREATE TABLE propiedadvalordefinido_pvdp(
        cpvd_llave character varying(32) NOT NULL,
        cpvd_origen character varying(1) NOT NULL,
        cpvd_origencategoria character varying(1),
        cpvd_codigo character varying(100) NOT NULL,
        cpvd_nombre character varying(100) NOT NULL,
        cpvd_grupo character varying(100) NOT NULL,
        bpvd_textoculto boolean NOT NULL DEFAULT false,
        bpvd_necesitadesarrollo boolean NOT NULL DEFAULT false,
        bpvd_incluirpreloadorigen boolean NOT NULL DEFAULT false,
        bpvd_multiple boolean NOT NULL DEFAULT false,
        bpvd_piderol boolean NOT NULL DEFAULT false,
        bpvd_pidetiempobloqueo boolean NOT NULL DEFAULT false,
        bpvd_propiedadboolean boolean NOT NULL DEFAULT false,
        bpvd_pideusuario boolean NOT NULL DEFAULT false,
        bpvd_solicitamotivo boolean NOT NULL DEFAULT false,
        bpvd_pidefechas boolean NOT NULL DEFAULT false,
        cpvd_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_propiedadvalordefinido_pvdp PRIMARY KEY (cpvd_llave)
    );
 
CREATE TABLE encuestagrupo_egrp(
        cegr_llave character varying(32) NOT NULL,
        cegr_codigo character varying(20),
        cegr_nombre character varying(50) NOT NULL,
        cegr_encuesta character varying(32) NOT NULL,
        cegr_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_encuestagrupo_egrp PRIMARY KEY (cegr_llave)
    );
 
CREATE TABLE encuestapregunta_eprp(
        cepr_llave character varying(32) NOT NULL,
        cepr_codigo character varying(20) NOT NULL,
        cepr_nombre character varying(4000) NOT NULL,
        cepr_grupo character varying(32) NOT NULL,
        cepr_tipo character varying(1) NOT NULL,
        cepr_descripcion character varying(4000),
        cepr_restriccion character varying(32),
        cepr_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_encuestapregunta_eprp PRIMARY KEY (cepr_llave)
    );
 
CREATE TABLE encuesta_encp(
        cenc_llave character varying(32) NOT NULL,
        cenc_nombre character varying(100) NOT NULL,
        denc_fechainicio timestamp with time zone NOT NULL,
        denc_fechafin timestamp with time zone NOT NULL,
        denc_fechaejecucion timestamp with time zone,
        benc_colaborativa boolean NOT NULL DEFAULT false,
        cenc_rol character varying(32),
        cenc_cliente character varying(32),
        cenc_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_encuesta_encp PRIMARY KEY (cenc_llave)
    );
 
CREATE TABLE usuariorol_erlp(
        cerl_llave character varying(32) NOT NULL,
        cerl_usuario character varying(32) NOT NULL,
        cerl_rolacceso character varying(32) NOT NULL,
        cerl_documento character varying(32) NOT NULL,
        derl_fechainicial timestamp with time zone NOT NULL,
        derl_fechafinal timestamp with time zone,
        cerl_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_usuariorol_erlp PRIMARY KEY (cerl_llave)
    );
 
CREATE TABLE puesto_puep(
        cpue_llave character varying(32) NOT NULL,
        cpue_campo character varying(32) NOT NULL,
        npue_fila int NOT NULL DEFAULT 0,
        npue_columna int NOT NULL DEFAULT 0,
        cpue_imagen character varying(2000),
        cpue_nombre character varying(50),
        cpue_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_puesto_puep PRIMARY KEY (cpue_llave)
    );
 
CREATE TABLE rolacceso_racp(
        crac_llave character varying(32) NOT NULL,
        crac_plantilla character varying(32) NOT NULL,
        brac_permisoscompletos boolean NOT NULL DEFAULT false,
        nrac_minutossesion int NOT NULL DEFAULT 0,
        crac_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_rolacceso_racp PRIMARY KEY (crac_llave)
    );
 
CREATE TABLE usuario_usrp(
        cusr_llave character varying(32) NOT NULL,
        cusr_identificacion character varying(32) NOT NULL UNIQUE,
        cusr_nombre character varying(200) NOT NULL,
        cusr_imagen character varying(2000) NOT NULL,
        cusr_correo character varying(50),
        cusr_telefono character varying(50),
        cusr_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_usuario_usrp PRIMARY KEY (cusr_llave)
    );
 
CREATE TABLE webservice_wbsp(
        cwbs_llave character varying(32) NOT NULL,
        cwbs_nombre character varying(50) NOT NULL,
        cwbs_codigo character varying(50),
        cwbs_template character varying(120000) NOT NULL,
        cwbs_url character varying(2000) NOT NULL,
        cwbs_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_webservice_wbsp PRIMARY KEY (cwbs_llave)
    );
 
CREATE TABLE postrespuesta_prsp(
        cprs_llave character varying(32) NOT NULL,
        dprs_fecha timestamp with time zone NOT NULL,
        cprs_autor character varying(32) NOT NULL,
        cprs_pregunta character varying(32) NOT NULL,
        cprs_respuesta character varying(4000) NOT NULL,
        cprs_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_postrespuesta_prsp PRIMARY KEY (cprs_llave)
    );
 
CREATE TABLE gpslocalizacion_gplp(
        cgpl_llave character varying(32) NOT NULL,
        cgpl_dispositivo character varying(32) NOT NULL,
        dgpl_fecha timestamp with time zone NOT NULL,
        mgpl_longitud NUMERIC(18,6) NOT NULL DEFAULT 0,
        mgpl_latitud NUMERIC(18,6) NOT NULL DEFAULT 0,
        cgpl_documento character varying(32),
        cgpl_codigo character varying(32),
        dgpl_fechareporte timestamp with time zone NOT NULL,
        cgpl_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_gpslocalizacion_gplp PRIMARY KEY (cgpl_llave)
    );
    
 
CREATE TABLE mensaje_msjp(
        cmsj_llave character varying(32) NOT NULL,
        dmsj_fecha timestamp with time zone NOT NULL,
        cmsj_titulo character varying(200) NOT NULL,
        cmsj_usuario character varying(32),
        cmsj_documento character varying(32) NOT NULL,
        cmsj_template character varying(32) NOT NULL,
        cmsj_parametros character varying(4000) NOT NULL,
        dmsj_leido timestamp with time zone,
        dmsj_correoenviado timestamp with time zone,
        cmsj_correoerror character varying(4000),
        cmsj_correo character varying(2000),
        cmsj_reporte character varying(32),
        cmsj_transaccion character varying(32),
        cmsj_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_mensaje_msjp PRIMARY KEY (cmsj_llave)
    );
 
CREATE TABLE postcalificacion_pclp(
        cpcl_llave character varying(32) NOT NULL,
        cpcl_usuario character varying(32) NOT NULL,
        dpcl_fecha timestamp with time zone NOT NULL,
        cpcl_respuesta character varying(32) NOT NULL,
        bpcl_positiva boolean NOT NULL DEFAULT false,
        cpcl_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_postcalificacion_pclp PRIMARY KEY (cpcl_llave)
    );
 
CREATE TABLE webserviceejecucion_wsep(
        cwse_llave character varying(32) NOT NULL,
        cwse_servicio character varying(32) NOT NULL,
        cwse_usuario character varying(32) NOT NULL,
        dwse_fecha timestamp with time zone NOT NULL,
        cwse_documento character varying(32) NOT NULL,
        cwse_modificador character varying(32),
        cwse_transaccion character varying(32),
        cwse_parametros character varying(4000),
        dwse_fechaejecucion timestamp with time zone,
        cwse_entrada character varying(2000),
        cwse_salida character varying(2000),
        cwse_error character varying(4000),
        cwse_masivo character varying(2000),
        cwse_extracciones character varying(4000),
        cwse_sincrona character varying(1),
        cwse_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_webserviceejecucion_wsep PRIMARY KEY (cwse_llave)
    );
 
CREATE TABLE postpregunta_pprp(
        cppr_llave character varying(32) NOT NULL,
        cppr_campo character varying(32) NOT NULL,
        cppr_tipo character varying(1) NOT NULL,
        dppr_fecha timestamp with time zone NOT NULL,
        cppr_autor character varying(32) NOT NULL,
        cppr_pregunta character varying(4000) NOT NULL,
        cppr_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_postpregunta_pprp PRIMARY KEY (cppr_llave)
    );
 
CREATE TABLE servidor_serp(
        cser_llave character varying(32) NOT NULL,
        cser_tipo character varying(1) NOT NULL,
        nser_orden int NOT NULL DEFAULT 0,
        cser_nombre character varying(100) NOT NULL,
        cser_url character varying(4000) NOT NULL,
        cser_puerto character varying(10),
        cser_usuario character varying(4000),
        cser_clave character varying(4000),
        cser_base character varying(4000),
        cser_urlconexion character varying(4000),
        cser_servidorrespaldo character varying(32),
        cser_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_servidor_serp PRIMARY KEY (cser_llave)
    );
 
CREATE TABLE mensajeplantillacorreo_mplp(
        cmpl_llave character varying(32) NOT NULL,
        cmpl_nombre character varying(100) NOT NULL,
        cmpl_titulo character varying(100) NOT NULL,
        cmpl_texto character varying(120000) NOT NULL,
        cmpl_servidor character varying(32),
        cmpl_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_mensajeplantillacorreo_mplp PRIMARY KEY (cmpl_llave)
    );
 
CREATE TABLE gpsdispositivo_gpsp(
        cgps_llave character varying(32) NOT NULL,
        cgps_usuario character varying(32) NOT NULL,
        cgps_nombre character varying(100),
        dgps_ultimaconexion timestamp with time zone,
        ngps_intervalo int NOT NULL DEFAULT 0,
        ngps_distancia int NOT NULL DEFAULT 0,
        ngps_acercamiento int NOT NULL DEFAULT 0,
        cgps_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_gpsdispositivo_gpsp PRIMARY KEY (cgps_llave)
    );
 
CREATE TABLE trazabilidadproductoinventario_tpip(
        ctpi_llave character varying(32) NOT NULL,
        dtpi_fecha timestamp with time zone NOT NULL,
        ctpi_bodega character varying(32) NOT NULL,
        ctpi_producto character varying(32) NOT NULL,
        mtpi_cantidadinicial NUMERIC(18,6) NOT NULL DEFAULT 0,
        mtpi_cantidadfinal NUMERIC(18,6) NOT NULL DEFAULT 0,
        mtpi_cantidad NUMERIC(18,6) NOT NULL DEFAULT 0,
        ctpi_deduccionproducto character varying(32) NOT NULL,
        ctpi_responsable character varying(32) NOT NULL,
        ctpi_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_trazabilidadproductoinventario_tpip PRIMARY KEY (ctpi_llave)
    );
 
CREATE TABLE detallepedidoventa_dpvp(
        cdpv_llave character varying(32) NOT NULL,
        cdpv_documento character varying(32) NOT NULL,
        cdpv_producto character varying(32) NOT NULL,
        cdpv_nombre character varying(200),
        cdpv_productotercero character varying(32),
        mdpv_cantidad NUMERIC(18,6) NOT NULL DEFAULT 0,
        ndpv_cantidadpromocion int NOT NULL DEFAULT 0,
        ndpv_cantidadpromocionbase int NOT NULL DEFAULT 0,
        mdpv_cantidadtotal NUMERIC(18,6) NOT NULL DEFAULT 0,
        mdpv_valorminimo NUMERIC(18,6) NOT NULL DEFAULT 0,
        mdpv_valortotal NUMERIC(18,6) NOT NULL DEFAULT 0,
        mdpv_valorunitario NUMERIC(18,6) NOT NULL DEFAULT 0,
        mdpv_valormaximo NUMERIC(18,6) NOT NULL DEFAULT 0,
        cdpv_plantilla character varying(32) NOT NULL,
        mdpv_valorsubtotal NUMERIC(18,6) NOT NULL DEFAULT 0,
        cdpv_transaccionregistro character varying(32) NOT NULL,
        cdpv_transaccioninactivo character varying(32),
        cdpv_campo character varying(32),
        cdpv_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_detallepedidoventa_dpvp PRIMARY KEY (cdpv_llave)
    );
 
CREATE TABLE categoriaproducto_cprp(
        ccpr_llave character varying(32) NOT NULL,
        ccpr_nombre character varying(100) NOT NULL,
        ccpr_imagen character varying(2000),
        mcpr_cantidadmaxima NUMERIC(18,6) NOT NULL DEFAULT 0,
        ccpr_nodosuperior character varying(32),
        bcpr_inventarios boolean NOT NULL DEFAULT false,
        bcpr_camposadicionales boolean NOT NULL DEFAULT false,
        bcpr_composicion boolean NOT NULL DEFAULT false,
        ncpr_promocionbase int NOT NULL DEFAULT 0,
        ccpr_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_categoriaproducto_cprp PRIMARY KEY (ccpr_llave)
    );
 
CREATE TABLE productoinventario_pinp(
        cpin_llave character varying(32) NOT NULL,
        cpin_producto character varying(32) NOT NULL,
        cpin_bodega character varying(32) NOT NULL,
        mpin_cantidadactual NUMERIC(18,6) NOT NULL DEFAULT 0,
        mpin_cantidadminima NUMERIC(18,6) NOT NULL DEFAULT 0,
        mpin_cantidadmaxima NUMERIC(18,6) NOT NULL DEFAULT 0,
        dpin_fechainicial timestamp with time zone NOT NULL,
        cpin_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_productoinventario_pinp PRIMARY KEY (cpin_llave)
    );
 
CREATE TABLE productocaracteristica_pcrp(
        cpcr_llave character varying(32) NOT NULL,
        cpcr_objetivo character varying(4000) NOT NULL,
        cpcr_base character varying(32) NOT NULL,
        cpcr_formato character varying(1) NOT NULL,
        cpcr_nombre character varying(100) NOT NULL,
        cpcr_codigo character varying(20) NOT NULL,
        npcr_orden int NOT NULL DEFAULT 0,
        cpcr_imagen character varying(2000),
        cpcr_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_productocaracteristica_pcrp PRIMARY KEY (cpcr_llave)
    );
 
CREATE TABLE usuariorolproducto_urpp(
        curp_llave character varying(32) NOT NULL,
        curp_documento character varying(32) NOT NULL,
        curp_producto character varying(32) NOT NULL,
        curp_nombre character varying(50),
        curp_modificador character varying(32),
        nurp_cantidadpromocion int NOT NULL DEFAULT 0,
        nurp_cantidadpromocionbase int NOT NULL DEFAULT 0,
        curp_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_usuariorolproducto_urpp PRIMARY KEY (curp_llave)
    );
 
CREATE TABLE detallecaracteristicaproducto_dcpp(
        cdcp_llave character varying(32) NOT NULL,
        cdcp_entidad character varying(32) NOT NULL,
        cdcp_campo character varying(32) NOT NULL,
        ddcp_valorfecha timestamp with time zone,
        cdcp_valortext character varying(4000),
        mdcp_valornumero NUMERIC(18,6) NOT NULL DEFAULT 0,
        cdcp_valoropcion character varying(32),
        cdcp_transaccionregistro character varying(32) NOT NULL,
        cdcp_transaccioninactivo character varying(32),
        cdcp_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_detallecaracteristicaproducto_dcpp PRIMARY KEY (cdcp_llave)
    );
 
CREATE TABLE bodega_bodp(
        cbod_llave character varying(32) NOT NULL,
        cbod_documento character varying(32) NOT NULL,
        cbod_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_bodega_bodp PRIMARY KEY (cbod_llave)
    );
 
CREATE TABLE producto_prop(
        cpro_llave character varying(32) NOT NULL,
        cpro_imagen character varying(2000),
        cpro_descripcion character varying(4000),
        cpro_categoria character varying(32) NOT NULL,
        cpro_documento character varying(32) NOT NULL,
        cpro_productobase character varying(32),
        cpro_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_producto_prop PRIMARY KEY (cpro_llave)
    );
 
CREATE TABLE productoinventariodescuento_pidp(
        cpid_llave character varying(32) NOT NULL,
        cpid_producto character varying(32) NOT NULL,
        cpid_productodescontar character varying(32) NOT NULL,
        mpid_cantidadproductodescontar NUMERIC(18,6) NOT NULL DEFAULT 0,
        cpid_caracteristica character varying(32),
        cpid_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_productoinventariodescuento_pidp PRIMARY KEY (cpid_llave)
    );
 
CREATE TABLE deduccionproducto_dprp(
        cdpr_llave character varying(32) NOT NULL,
        cdpr_documento character varying(32) NOT NULL,
        cdpr_producto character varying(32) NOT NULL,
        ddpr_fecha timestamp with time zone NOT NULL,
        mdpr_cantidad NUMERIC(18,6) NOT NULL DEFAULT 0,
        cdpr_responsable character varying(32),
        cdpr_bodega character varying(32) NOT NULL,
        cdpr_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_deduccionproducto_dprp PRIMARY KEY (cdpr_llave)
    );
 
CREATE TABLE modulocontratado_mdcp(
        cmdc_llave character varying(32) NOT NULL,
        cmdc_modulo character varying(32) NOT NULL,
        cmdc_nombre character varying(100) NOT NULL,
        cmdc_identificador character varying(4000),
        cmdc_imagen character varying(2000),
        cmdc_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_modulocontratado_mdcp PRIMARY KEY (cmdc_llave)
    );
 
CREATE TABLE modulo_modp(
        cmod_llave character varying(32) NOT NULL,
        cmod_nombre character varying(100) NOT NULL,
        cmod_url character varying(2000) NOT NULL,
        cmod_descripcion character varying(4000),
        bmod_movil boolean NOT NULL DEFAULT false,
        cmod_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_modulo_modp PRIMARY KEY (cmod_llave)
    );
 
CREATE TABLE reportebase_rpbp(
        crpb_llave character varying(32) NOT NULL,
        crpb_plantilla character varying(32) NOT NULL,
        crpb_nombre character varying(100) NOT NULL,
        crpb_codigo character varying(16) NOT NULL,
        brpb_soloexistente boolean NOT NULL DEFAULT false,
        crpb_variables character varying(4000),
        nrpb_version int NOT NULL DEFAULT 0,
        crpb_descripcion character varying(4000) NOT NULL,
        crpb_servidor character varying(32),
        brpb_publico boolean NOT NULL DEFAULT false,
        crpb_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_reportebase_rpbp PRIMARY KEY (crpb_llave)
    );
 
CREATE TABLE usuarioorganizacion_uorp(
        cuor_llave character varying(32) NOT NULL,
        cuor_usuario character varying(32) NOT NULL,
        cuor_organizacion character varying(32) NOT NULL,
        cuor_tokenserver character varying(32) NOT NULL,
        cuor_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_usuarioorganizacion_uorp PRIMARY KEY (cuor_llave)
    );
 
CREATE TABLE permiso_perp(
        cper_llave character varying(32) NOT NULL,
        cper_rolacceso character varying(32) NOT NULL,
        cper_modulo character varying(32) NOT NULL,
        cper_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_permiso_perp PRIMARY KEY (cper_llave)
    );
 
CREATE TABLE usuarioautenticacionautorizacion_uaap(
        cuaa_llave character varying(32) NOT NULL,
        cuaa_usuario character varying(32) NOT NULL,
        duaa_fechamaxima timestamp with time zone NOT NULL,
        duaa_fechasolicitud timestamp with time zone NOT NULL,
        cuaa_correo character varying(100),
        cuaa_ipsolicitud character varying(100) NOT NULL,
        cuaa_codigo character varying(100),
        duaa_fecharedencion timestamp with time zone,
        cuaa_key character varying(100),
        cuaa_ipredencion character varying(100),
        cuaa_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_usuarioautenticacionautorizacion_uaap PRIMARY KEY (cuaa_llave)
    );
 
CREATE TABLE usuariosesion_ussp(
        cuss_llave character varying(32) NOT NULL,
        cuss_usuario character varying(32) NOT NULL,
        duss_fecha timestamp with time zone NOT NULL,
        duss_fechacierre timestamp with time zone,
        cuss_ip character varying(100),
        cuss_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_usuariosesion_ussp PRIMARY KEY (cuss_llave)
    );
 
CREATE TABLE cargaarchivo_carp(
        ccar_llave character varying(32) NOT NULL,
        ccar_servidor character varying(32),
        ncar_size int NOT NULL DEFAULT 0,
        ccar_url character varying(4000),
        dcar_fechainicio timestamp with time zone NOT NULL,
        dcar_fechafin timestamp with time zone NOT NULL,
        ccar_error character varying(4000),
        ccar_usuario character varying(32),
        ccar_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_cargaarchivo_carp PRIMARY KEY (ccar_llave)
    );
 
CREATE TABLE reporteejecucion_rejp(
        crej_llave character varying(32) NOT NULL,
        crej_reporte character varying(32) NOT NULL,
        crej_documento character varying(32),
        drej_fechainicio timestamp with time zone NOT NULL,
        drej_fechafin timestamp with time zone NOT NULL,
        crej_error character varying(4000),
        crej_url character varying(4000),
        crej_usuario character varying(32),
        crej_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_reporteejecucion_rejp PRIMARY KEY (crej_llave)
    );
 
CREATE TABLE consecutivo_conp(
        ccon_llave character varying(32) NOT NULL,
        ccon_nombre character varying(100) NOT NULL,
        ccon_prefijo character varying(50) UNIQUE,
        ccon_sufijo character varying(50),
        mcon_numeroinicial NUMERIC(18,6) NOT NULL DEFAULT 0,
        mcon_numerofinal NUMERIC(18,6) NOT NULL DEFAULT 0,
        mcon_numeroactual NUMERIC(18,6) NOT NULL DEFAULT 0,
        bcon_manual boolean NOT NULL DEFAULT false,
        ccon_padding character varying(20),
        ccon_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_consecutivo_conp PRIMARY KEY (ccon_llave)
    );
 
CREATE TABLE transaccionlog_tlgp(
        ctlg_llave character varying(32) NOT NULL,
        dtlg_fechainicio timestamp with time zone NOT NULL,
        dtlg_fechafin timestamp with time zone NOT NULL,
        ctlg_transaccion character varying(32),
        ctlg_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_transaccionlog_tlgp PRIMARY KEY (ctlg_llave)
    );
 
CREATE TABLE usuariosesionerror_usep(
        cuse_llave character varying(32) NOT NULL,
        cuse_sesion character varying(100),
        cuse_clave character varying(100),
        cuse_ip character varying(100),
        duse_fecha timestamp with time zone NOT NULL,
        cuse_error character varying(4000) NOT NULL,
        cuse_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_usuariosesionerror_usep PRIMARY KEY (cuse_llave)
    );
 
CREATE TABLE usuarioautenticacion_uaup(
        cuau_llave character varying(32) NOT NULL,
        cuau_usuario character varying(32) NOT NULL,
        cuau_sesion character varying(50) NOT NULL,
        cuau_clave character varying(50) NOT NULL,
        duau_fechamaxima timestamp with time zone,
        cuau_autorizacioncrea character varying(32),
        cuau_autorizacionelimina character varying(32),
        cuau_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_usuarioautenticacion_uaup PRIMARY KEY (cuau_llave)
    );
 
 
CREATE TABLE transaccionerror_terp(
        cter_llave character varying(32) NOT NULL,
        dter_fechainicio timestamp with time zone NOT NULL,
        dter_fechafin timestamp with time zone NOT NULL,
        cter_error character varying(4000),
        cter_usuario character varying(32) NOT NULL,
        cter_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_transaccionerror_terp PRIMARY KEY (cter_llave)
    );
 
CREATE TABLE organizacion_orgp(
        corg_llave character varying(32) NOT NULL,
        corg_nombre character varying(100) NOT NULL,
        corg_principal character varying(32),
        corg_servidor character varying(32),
        corg_usuariosystem character varying(32),
        corg_imagen character varying(2000),
        corg_slogan character varying(4000),
        borg_sincronizacion boolean NOT NULL DEFAULT false,
        corg_mensajeingreso character varying(4000),
        corg_codigo character varying(20) NOT NULL,
        corg_servidorcorreo character varying(32),
        corg_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_organizacion_orgp PRIMARY KEY (corg_llave)
    ); 

ALTER TABLE PlantillaConsecutivo_pcnp ADD CONSTRAINT FK_PlantillaConsecutivovalorOpcion FOREIGN KEY (cpcn_valorOpcion) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE PlantillaConsecutivo_pcnp ADD CONSTRAINT FK_PlantillaConsecutivocaracteristica FOREIGN KEY (cpcn_caracteristica) REFERENCES DocumentoPlantillaCaracteristica_dpcp(cdpc_llave);
ALTER TABLE PlantillaConsecutivo_pcnp ADD CONSTRAINT FK_PlantillaConsecutivoconsecutivo FOREIGN KEY (cpcn_consecutivo) REFERENCES Consecutivo_conp(ccon_llave);
ALTER TABLE ProcesoTransicion_ptrp ADD CONSTRAINT FK_ProcesoTransicionproceso FOREIGN KEY (cptr_proceso) REFERENCES Proceso_prcp(cprc_llave);
ALTER TABLE ProcesoEstado_pesp ADD CONSTRAINT FK_ProcesoEstadoproceso FOREIGN KEY (cpes_proceso) REFERENCES Proceso_prcp(cprc_llave);
ALTER TABLE DocumentoTransaccion_trap ADD CONSTRAINT FK_DocumentoTransaccionusuario FOREIGN KEY (ctra_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE DocumentoRelacionGestor_drgp ADD CONSTRAINT FK_DocumentoRelacionGestorestadoFinal FOREIGN KEY (cdrg_estadoFinal) REFERENCES ProcesoEstado_pesp(cpes_llave);
ALTER TABLE DocumentoRelacionGestor_drgp ADD CONSTRAINT FK_DocumentoRelacionGestorestadoInicial FOREIGN KEY (cdrg_estadoInicial) REFERENCES ProcesoEstado_pesp(cpes_llave);
ALTER TABLE ProcesoTransicion_ptrp ADD CONSTRAINT FK_ProcesoTransicionestadoLLegada FOREIGN KEY (cptr_estadoLLegada) REFERENCES ProcesoEstado_pesp(cpes_llave);
ALTER TABLE PedidoVentaAjuste_pvap ADD CONSTRAINT FK_PedidoVentaAjusteestadoInicial FOREIGN KEY (cpva_estadoInicial) REFERENCES ProcesoEstado_pesp(cpes_llave);
ALTER TABLE ProcesoTransicion_ptrp ADD CONSTRAINT FK_ProcesoTransicionestadoPartida FOREIGN KEY (cptr_estadoPartida) REFERENCES ProcesoEstado_pesp(cpes_llave);
ALTER TABLE PedidoVentaAjuste_pvap ADD CONSTRAINT FK_PedidoVentaAjusteestadoFinal FOREIGN KEY (cpva_estadoFinal) REFERENCES ProcesoEstado_pesp(cpes_llave);
ALTER TABLE PedidoVenta_pdvp ADD CONSTRAINT FK_PedidoVentaestadoExpediente FOREIGN KEY (cpdv_estadoExpediente) REFERENCES ProcesoEstado_pesp(cpes_llave);
ALTER TABLE ProcesoTransicionAutomatica_ptap ADD CONSTRAINT FK_ProcesoTransicionAutomaticatransicion FOREIGN KEY (cpta_transicion) REFERENCES ProcesoTransicion_ptrp(cptr_llave);
ALTER TABLE ProcesoTransicionAutomatica_ptap ADD CONSTRAINT FK_ProcesoTransicionAutomaticapropiedad FOREIGN KEY (cpta_propiedad) REFERENCES Propiedad_ppdp(cppd_llave);
ALTER TABLE DocumentoRelacionExpediente_dexp ADD CONSTRAINT FK_DocumentoRelacionExpedientecampoMaestro FOREIGN KEY (cdex_campoMaestro) REFERENCES PedidoVentaCaracteristica_pvcp(cpvc_llave);
ALTER TABLE DocumentoRelacionExpediente_dexp ADD CONSTRAINT FK_DocumentoRelacionExpedienteexpedienteDetalle FOREIGN KEY (cdex_expedienteDetalle) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE DocumentoRelacionGestor_drgp ADD CONSTRAINT FK_DocumentoRelacionGestordocumentoModificador FOREIGN KEY (cdrg_documentoModificador) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE DocumentoRelacionGestor_drgp ADD CONSTRAINT FK_DocumentoRelacionGestordocumentoPrincipal FOREIGN KEY (cdrg_documentoPrincipal) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE DocumentoRelacionGestor_drgp ADD CONSTRAINT FK_DocumentoRelacionGestorusuario FOREIGN KEY (cdrg_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE DocumentoRelacionGestor_drgp ADD CONSTRAINT FK_DocumentoRelacionGestorvalores FOREIGN KEY (cdrg_valores) REFERENCES PedidoVentaDinero_pvdp(cpvd_llave);
ALTER TABLE DocumentoPlantillaCaracteristica_dpcp ADD CONSTRAINT FK_DocumentoPlantillaCaracteristicaplantilla FOREIGN KEY (cdpc_plantilla) REFERENCES DocumentoPlantilla_dplp(cdpl_llave);
ALTER TABLE PedidoVentaCaracteristica_pvcp ADD CONSTRAINT FK_PedidoVentaCaracteristicacampo FOREIGN KEY (cpvc_campo) REFERENCES DocumentoPlantillaCaracteristica_dpcp(cdpc_llave);
ALTER TABLE RelacionInterna_ritp ADD CONSTRAINT FK_RelacionInternacampo FOREIGN KEY (crit_campo) REFERENCES DocumentoPlantillaCaracteristica_dpcp(cdpc_llave);
ALTER TABLE Actividad_actp ADD CONSTRAINT FK_Actividaddocumento FOREIGN KEY (cact_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE DeduccionProducto_dprp ADD CONSTRAINT FK_DeduccionProductodocumento FOREIGN KEY (cdpr_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE PedidoVenta_pdvp ADD CONSTRAINT FK_PedidoVentafuncionario FOREIGN KEY (cpdv_funcionario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE PedidoVentaDinero_pvdp ADD CONSTRAINT FK_PedidoVentaDinerodocumento FOREIGN KEY (cpvd_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE PedidoVenta_pdvp ADD CONSTRAINT FK_PedidoVentaplantilla FOREIGN KEY (cpdv_plantilla) REFERENCES DocumentoPlantilla_dplp(cdpl_llave);
ALTER TABLE UsuarioRol_erlp ADD CONSTRAINT FK_UsuarioRoldocumento FOREIGN KEY (cerl_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE Turno_turp ADD CONSTRAINT FK_Turnodocumento FOREIGN KEY (ctur_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE Bodega_bodp ADD CONSTRAINT FK_Bodegadocumento FOREIGN KEY (cbod_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE DetallePedidoVenta_dpvp ADD CONSTRAINT FK_DetallePedidoVentadocumento FOREIGN KEY (cdpv_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE Tarifa_tarp ADD CONSTRAINT FK_Tarifarecurso FOREIGN KEY (ctar_recurso) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE WebServiceEjecucion_wsep ADD CONSTRAINT FK_WebServiceEjecuciondocumento FOREIGN KEY (cwse_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE PedidoVentaCaracteristica_pvcp ADD CONSTRAINT FK_PedidoVentaCaracteristicadocumento FOREIGN KEY (cpvc_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE ReporteEjecucion_rejp ADD CONSTRAINT FK_ReporteEjecuciondocumento FOREIGN KEY (crej_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE ProductoInventarioDescuento_pidp ADD CONSTRAINT FK_ProductoInventarioDescuentocaracteristica FOREIGN KEY (cpid_caracteristica) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE UsuarioRolProducto_urpp ADD CONSTRAINT FK_UsuarioRolProductodocumento FOREIGN KEY (curp_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE PedidoVentaAjuste_pvap ADD CONSTRAINT FK_PedidoVentaAjustedocumento FOREIGN KEY (cpva_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE Cuenta_cuep ADD CONSTRAINT FK_Cuentadocumento FOREIGN KEY (ccue_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE Producto_prop ADD CONSTRAINT FK_Productodocumento FOREIGN KEY (cpro_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE Mensaje_msjp ADD CONSTRAINT FK_Mensajedocumento FOREIGN KEY (cmsj_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE Movimiento_movp ADD CONSTRAINT FK_Movimientodocumento FOREIGN KEY (cmov_documento) REFERENCES PedidoVenta_pdvp(cpdv_llave);
ALTER TABLE ProcesoTransicion_ptrp ADD CONSTRAINT FK_ProcesoTransicionplantilla FOREIGN KEY (cptr_plantilla) REFERENCES DocumentoPlantilla_dplp(cdpl_llave);
ALTER TABLE PedidoVentaAjuste_pvap ADD CONSTRAINT FK_PedidoVentaAjusteresponsable FOREIGN KEY (cpva_responsable) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE DocumentoPlantilla_dplp ADD CONSTRAINT FK_DocumentoPlantillaconsecutivo FOREIGN KEY (cdpl_consecutivo) REFERENCES Consecutivo_conp(ccon_llave);
ALTER TABLE RelacionInterna_ritp ADD CONSTRAINT FK_RelacionInternaplantilla FOREIGN KEY (crit_plantilla) REFERENCES DocumentoPlantilla_dplp(cdpl_llave);
ALTER TABLE ReporteBase_rpbp ADD CONSTRAINT FK_ReporteBaseplantilla FOREIGN KEY (crpb_plantilla) REFERENCES DocumentoPlantilla_dplp(cdpl_llave);
ALTER TABLE RolAcceso_racp ADD CONSTRAINT FK_RolAccesoplantilla FOREIGN KEY (crac_plantilla) REFERENCES DocumentoPlantilla_dplp(cdpl_llave);
ALTER TABLE Movimiento_movp ADD CONSTRAINT FK_Movimientoturno FOREIGN KEY (cmov_turno) REFERENCES Turno_turp(ctur_llave);
ALTER TABLE Movimiento_movp ADD CONSTRAINT FK_Movimientorelacionado FOREIGN KEY (cmov_relacionado) REFERENCES Movimiento_movp(cmov_llave);
ALTER TABLE Movimiento_movp ADD CONSTRAINT FK_Movimientocuenta FOREIGN KEY (cmov_cuenta) REFERENCES Cuenta_cuep(ccue_llave);
ALTER TABLE Movimiento_movp ADD CONSTRAINT FK_Movimientoanterior FOREIGN KEY (cmov_anterior) REFERENCES Movimiento_movp(cmov_llave);
ALTER TABLE Movimiento_movp ADD CONSTRAINT FK_Movimientosiguiente FOREIGN KEY (cmov_siguiente) REFERENCES Movimiento_movp(cmov_llave);
ALTER TABLE Turno_turp ADD CONSTRAINT FK_Turnocuenta FOREIGN KEY (ctur_cuenta) REFERENCES Cuenta_cuep(ccue_llave);
ALTER TABLE Tarifa_tarp ADD CONSTRAINT FK_Tarifaproducto FOREIGN KEY (ctar_producto) REFERENCES Producto_prop(cpro_llave);
ALTER TABLE Tarifa_tarp ADD CONSTRAINT FK_Tarifatarifario FOREIGN KEY (ctar_tarifario) REFERENCES Tarifario_trfp(ctrf_llave);
ALTER TABLE Actividad_actp ADD CONSTRAINT FK_ActividadusuarioRegistro FOREIGN KEY (cact_usuarioRegistro) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE Actividad_actp ADD CONSTRAINT FK_ActividadusuarioInactivo FOREIGN KEY (cact_usuarioInactivo) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE Actividad_actp ADD CONSTRAINT FK_Actividadresponsable FOREIGN KEY (cact_responsable) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE Propiedad_ppdp ADD CONSTRAINT FK_PropiedadpropiedadValor FOREIGN KEY (cppd_propiedadValor) REFERENCES PropiedadValorDefinido_pvdp(cpvd_llave);
ALTER TABLE Propiedad_ppdp ADD CONSTRAINT FK_PropiedadcambioCreacion FOREIGN KEY (cppd_cambioCreacion) REFERENCES Cambio_cmbp(ccmb_llave);
ALTER TABLE Propiedad_ppdp ADD CONSTRAINT FK_PropiedadcambioEliminacion FOREIGN KEY (cppd_cambioEliminacion) REFERENCES Cambio_cmbp(ccmb_llave);
ALTER TABLE RelacionInterna_ritp ADD CONSTRAINT FK_RelacionInternapropiedad FOREIGN KEY (crit_propiedad) REFERENCES Propiedad_ppdp(cppd_llave);
ALTER TABLE Propiedad_ppdp ADD CONSTRAINT FK_Propiedadrol FOREIGN KEY (cppd_rol) REFERENCES RolAcceso_racp(crac_llave);
ALTER TABLE RelacionInterna_ritp ADD CONSTRAINT FK_RelacionInternacambioEliminacion FOREIGN KEY (crit_cambioEliminacion) REFERENCES Cambio_cmbp(ccmb_llave);
ALTER TABLE RelacionInterna_ritp ADD CONSTRAINT FK_RelacionInternacambioCreacion FOREIGN KEY (crit_cambioCreacion) REFERENCES Cambio_cmbp(ccmb_llave);
ALTER TABLE EncuestaRespuesta_ersp ADD CONSTRAINT FK_EncuestaRespuestausuario FOREIGN KEY (cers_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE EncuestaRespuesta_ersp ADD CONSTRAINT FK_EncuestaRespuestapregunta FOREIGN KEY (cers_pregunta) REFERENCES EncuestaPregunta_eprp(cepr_llave);
ALTER TABLE EncuestaRespuesta_ersp ADD CONSTRAINT FK_EncuestaRespuestarespuestaOpcion FOREIGN KEY (cers_respuestaOpcion) REFERENCES EncuestaOpcionRespuesta_eorp(ceor_llave);
ALTER TABLE EncuestaOpcionRespuesta_eorp ADD CONSTRAINT FK_EncuestaOpcionRespuestapregunta FOREIGN KEY (ceor_pregunta) REFERENCES EncuestaPregunta_eprp(cepr_llave);
ALTER TABLE EncuestaGrupo_egrp ADD CONSTRAINT FK_EncuestaGrupoencuesta FOREIGN KEY (cegr_encuesta) REFERENCES Encuesta_encp(cenc_llave);
ALTER TABLE EncuestaPregunta_eprp ADD CONSTRAINT FK_EncuestaPreguntagrupo FOREIGN KEY (cepr_grupo) REFERENCES EncuestaGrupo_egrp(cegr_llave);
ALTER TABLE Encuesta_encp ADD CONSTRAINT FK_Encuestarol FOREIGN KEY (cenc_rol) REFERENCES RolAcceso_racp(crac_llave);
ALTER TABLE UsuarioRol_erlp ADD CONSTRAINT FK_UsuarioRolrolAcceso FOREIGN KEY (cerl_rolAcceso) REFERENCES RolAcceso_racp(crac_llave);
ALTER TABLE UsuarioRol_erlp ADD CONSTRAINT FK_UsuarioRolusuario FOREIGN KEY (cerl_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE Permiso_perp ADD CONSTRAINT FK_PermisorolAcceso FOREIGN KEY (cper_rolAcceso) REFERENCES RolAcceso_racp(crac_llave);
ALTER TABLE UsuarioOrganizacion_uorp ADD CONSTRAINT FK_UsuarioOrganizacionusuario FOREIGN KEY (cuor_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE CargaArchivo_carp ADD CONSTRAINT FK_CargaArchivousuario FOREIGN KEY (ccar_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE UsuarioAutenticacion_uaup ADD CONSTRAINT FK_UsuarioAutenticacionusuario FOREIGN KEY (cuau_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE UsuarioAutenticacionAutorizacion_uaap ADD CONSTRAINT FK_UsuarioAutenticacionAutorizacionusuario FOREIGN KEY (cuaa_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE PostPregunta_pprp ADD CONSTRAINT FK_PostPreguntaautor FOREIGN KEY (cppr_autor) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE PostCalificacion_pclp ADD CONSTRAINT FK_PostCalificacionusuario FOREIGN KEY (cpcl_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE TrazabilidadProductoInventario_tpip ADD CONSTRAINT FK_TrazabilidadProductoInventarioresponsable FOREIGN KEY (ctpi_responsable) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE UsuarioSesion_ussp ADD CONSTRAINT FK_UsuarioSesionusuario FOREIGN KEY (cuss_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE PostRespuesta_prsp ADD CONSTRAINT FK_PostRespuestaautor FOREIGN KEY (cprs_autor) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE Mensaje_msjp ADD CONSTRAINT FK_Mensajeusuario FOREIGN KEY (cmsj_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE DeduccionProducto_dprp ADD CONSTRAINT FK_DeduccionProductoresponsable FOREIGN KEY (cdpr_responsable) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE GPSDispositivo_gpsp ADD CONSTRAINT FK_GPSDispositivousuario FOREIGN KEY (cgps_usuario) REFERENCES Usuario_usrp(cusr_llave);
ALTER TABLE WebServiceEjecucion_wsep ADD CONSTRAINT FK_WebServiceEjecucionservicio FOREIGN KEY (cwse_servicio) REFERENCES WebService_wbsp(cwbs_llave);
ALTER TABLE PostCalificacion_pclp ADD CONSTRAINT FK_PostCalificacionrespuesta FOREIGN KEY (cpcl_respuesta) REFERENCES PostRespuesta_prsp(cprs_llave);
ALTER TABLE PostRespuesta_prsp ADD CONSTRAINT FK_PostRespuestapregunta FOREIGN KEY (cprs_pregunta) REFERENCES PostPregunta_pprp(cppr_llave);
ALTER TABLE GPSLocalizacion_gplp ADD CONSTRAINT FK_GPSLocalizaciondispositivo FOREIGN KEY (cgpl_dispositivo) REFERENCES GPSDispositivo_gpsp(cgps_llave);
ALTER TABLE Mensaje_msjp ADD CONSTRAINT FK_Mensajetemplate FOREIGN KEY (cmsj_template) REFERENCES MensajePlantillaCorreo_mplp(cmpl_llave);
ALTER TABLE CargaArchivo_carp ADD CONSTRAINT FK_CargaArchivoservidor FOREIGN KEY (ccar_servidor) REFERENCES Servidor_serp(cser_llave);
ALTER TABLE MensajePlantillaCorreo_mplp ADD CONSTRAINT FK_MensajePlantillaCorreoservidor FOREIGN KEY (cmpl_servidor) REFERENCES Servidor_serp(cser_llave);
ALTER TABLE ReporteBase_rpbp ADD CONSTRAINT FK_ReporteBaseservidor FOREIGN KEY (crpb_servidor) REFERENCES Servidor_serp(cser_llave);
ALTER TABLE Organizacion_orgp ADD CONSTRAINT FK_Organizacionservidor FOREIGN KEY (corg_servidor) REFERENCES Servidor_serp(cser_llave);
ALTER TABLE TrazabilidadProductoInventario_tpip ADD CONSTRAINT FK_TrazabilidadProductoInventariobodega FOREIGN KEY (ctpi_bodega) REFERENCES Bodega_bodp(cbod_llave);
ALTER TABLE TrazabilidadProductoInventario_tpip ADD CONSTRAINT FK_TrazabilidadProductoInventariodeduccionProducto FOREIGN KEY (ctpi_deduccionProducto) REFERENCES DeduccionProducto_dprp(cdpr_llave);
ALTER TABLE TrazabilidadProductoInventario_tpip ADD CONSTRAINT FK_TrazabilidadProductoInventarioproducto FOREIGN KEY (ctpi_producto) REFERENCES Producto_prop(cpro_llave);
ALTER TABLE DetallePedidoVenta_dpvp ADD CONSTRAINT FK_DetallePedidoVentaproductoTercero FOREIGN KEY (cdpv_productoTercero) REFERENCES UsuarioRolProducto_urpp(curp_llave);
ALTER TABLE DetallePedidoVenta_dpvp ADD CONSTRAINT FK_DetallePedidoVentaproducto FOREIGN KEY (cdpv_producto) REFERENCES Producto_prop(cpro_llave);
ALTER TABLE DetalleCaracteristicaProducto_dcpp ADD CONSTRAINT FK_DetalleCaracteristicaProductoentidad FOREIGN KEY (cdcp_entidad) REFERENCES DetallePedidoVenta_dpvp(cdpv_llave);
ALTER TABLE CategoriaProducto_cprp ADD CONSTRAINT FK_CategoriaProductonodoSuperior FOREIGN KEY (ccpr_nodoSuperior) REFERENCES CategoriaProducto_cprp(ccpr_llave);
ALTER TABLE Producto_prop ADD CONSTRAINT FK_Productocategoria FOREIGN KEY (cpro_categoria) REFERENCES CategoriaProducto_cprp(ccpr_llave);
ALTER TABLE ProductoInventario_pinp ADD CONSTRAINT FK_ProductoInventariobodega FOREIGN KEY (cpin_bodega) REFERENCES Bodega_bodp(cbod_llave);
ALTER TABLE ProductoInventario_pinp ADD CONSTRAINT FK_ProductoInventarioproducto FOREIGN KEY (cpin_producto) REFERENCES Producto_prop(cpro_llave);
ALTER TABLE ProductoCaracteristica_pcrp ADD CONSTRAINT FK_ProductoCaracteristicabase FOREIGN KEY (cpcr_base) REFERENCES Producto_prop(cpro_llave);
ALTER TABLE UsuarioRolProducto_urpp ADD CONSTRAINT FK_UsuarioRolProductoproducto FOREIGN KEY (curp_producto) REFERENCES Producto_prop(cpro_llave);
ALTER TABLE DeduccionProducto_dprp ADD CONSTRAINT FK_DeduccionProductobodega FOREIGN KEY (cdpr_bodega) REFERENCES Bodega_bodp(cbod_llave);
ALTER TABLE DeduccionProducto_dprp ADD CONSTRAINT FK_DeduccionProductoproducto FOREIGN KEY (cdpr_producto) REFERENCES Producto_prop(cpro_llave);
ALTER TABLE ProductoInventarioDescuento_pidp ADD CONSTRAINT FK_ProductoInventarioDescuentoproducto FOREIGN KEY (cpid_producto) REFERENCES Producto_prop(cpro_llave);
ALTER TABLE ProductoInventarioDescuento_pidp ADD CONSTRAINT FK_ProductoInventarioDescuentoproductoDescontar FOREIGN KEY (cpid_productoDescontar) REFERENCES Producto_prop(cpro_llave);
ALTER TABLE Permiso_perp ADD CONSTRAINT FK_Permisomodulo FOREIGN KEY (cper_modulo) REFERENCES ModuloContratado_mdcp(cmdc_llave);
ALTER TABLE ModuloContratado_mdcp ADD CONSTRAINT FK_ModuloContratadomodulo FOREIGN KEY (cmdc_modulo) REFERENCES Modulo_modp(cmod_llave);
ALTER TABLE ReporteEjecucion_rejp ADD CONSTRAINT FK_ReporteEjecucionreporte FOREIGN KEY (crej_reporte) REFERENCES ReporteBase_rpbp(crpb_llave);
ALTER TABLE UsuarioOrganizacion_uorp ADD CONSTRAINT FK_UsuarioOrganizacionorganizacion FOREIGN KEY (cuor_organizacion) REFERENCES Organizacion_orgp(corg_llave);
ALTER TABLE UsuarioAutenticacion_uaup ADD CONSTRAINT FK_UsuarioAutenticacionautorizacionCrea FOREIGN KEY (cuau_autorizacionCrea) REFERENCES UsuarioAutenticacionAutorizacion_uaap(cuaa_llave);

insert into pg_description (objoid, classoid, objsubid, description) select oid, 1259, 0, '2022.11.29.00' from pg_class where relname = 'usuariosesion_ussp';

CREATE SCHEMA task AUTHORIZATION postgres;

CREATE TABLE task.task_tsk (
	ctsk_llave varchar(32) NOT NULL,
	ctsk_user varchar(32) NOT NULL,
	ctsk_title varchar(200) NOT NULL,
	ctsk_notes varchar(4000) NULL,
	dtsk_completed timestamptz NULL,
	dtsk_duedate timestamptz NULL,
	ntsk_priority int4 NOT NULL DEFAULT 0,
	ntsk_order int4 NOT NULL DEFAULT 0,
	dtsk_createdat timestamptz NOT NULL,
	ctsk_createduser varchar(32) NOT NULL,
	dtsk_updatedat timestamptz NULL,
	ctsk_updateduser varchar(32) NULL,
	ctsk_state varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_task_task_tsk PRIMARY KEY (ctsk_llave)
);


--Valida que no existan 2 modulos iguales para un usaurio
ALTER TABLE permiso_perp
  ADD CONSTRAINT uk_permiso_rolaccesomodulo UNIQUE(cper_rolacceso , cper_modulo);
--Validacion de roles que no se repitan de un mismo usuario
--ALTER TABLE usuariorol_erlp
--  ADD CONSTRAINT uk_usuariorol_rolacceso_usuario UNIQUE(cerl_rolacceso, cerl_usuario);
--Validar preguntas y grupos de los cdigos de encuesta
ALTER TABLE encuestagrupo_egrp
  ADD CONSTRAINT uk_encuestagrupo_codigo_encuesta UNIQUE(cegr_codigo, cegr_encuesta);
ALTER TABLE encuestapregunta_eprp 
  ADD CONSTRAINT uk_encuestapregunta_codigo_grupo UNIQUE(cepr_codigo, cepr_grupo);

ALTER TABLE documentoplantillacaracteristica_dpcp    
  ADD CONSTRAINT uk_documentoplantillacaracteristica_plantillacodigo UNIQUE(cdpc_plantilla , cdpc_codigo);
ALTER TABLE productocaracteristica_pcrp    
  ADD CONSTRAINT uk_productocaracteristica_basecodigo UNIQUE(cpcr_base , cpcr_codigo);
  
CREATE INDEX ix_pedidoventacaracteristica_documento
  ON pedidoventacaracteristica_pvcp
  USING btree
  (cpvc_documento);
  
CREATE INDEX ix_pedidoventadinero_documento
  ON pedidoventadinero_pvdp
  USING btree
  (cpvd_documento);
  
CREATE INDEX ix_documentorelacionexpediente_campomaestro
  ON documentorelacionexpediente_dexp
  USING btree
  (cdex_campomaestro);

CREATE INDEX ix_pedidoventacaracteristica_valoropcion
	ON pedidoventacaracteristica_pvcp
	  USING btree
	  (cpvc_valoropcion);
	  
CREATE INDEX ix_documentorelaciongestor_documentoprincipal
	ON documentorelaciongestor_drgp
  USING btree
  (cdrg_documentoprincipal);
  
CREATE INDEX ix_pedidoventa_nombre
	ON pedidoventa_pdvp
  USING btree
  (cpdv_nombre);

CREATE INDEX ix_pedidoventa_plantillafecha
	ON pedidoventa_pdvp
  USING btree
  (cpdv_plantilla, dpdv_fecha);

CREATE INDEX ix_propiedad_ppdp_campoestado 
  ON propiedad_ppdp USING btree (cppd_campo, cppd_estado);
  
ALTER TABLE pedidoventacaracteristica_pvcp ALTER COLUMN mpvc_valornumero DROP NOT NULL;
ALTER TABLE pedidoventacaracteristica_pvcp ALTER COLUMN mpvc_valornumero DROP DEFAULT;

ALTER TABLE pedidoventa_pdvp ALTER COLUMN npdv_historico DROP NOT NULL;
ALTER TABLE pedidoventa_pdvp ALTER COLUMN npdv_historico DROP DEFAULT;

CREATE TABLE z_pvc_pedidoventacaracteristica (
	cpvc_llave varchar(32) NOT NULL,
	cpvc_documento varchar(32) NOT NULL,
	cpvc_campo varchar(32) NOT NULL,
	cpvc_valortext varchar(4000) NOT NULL,
	dpvc_valorfecha timestamptz NULL,
	cpvc_valoropcion varchar(32) NULL,
	cpvc_valorauxiliar varchar(32) NULL,
	mpvc_valornumero numeric(18,6) NULL,
	cpvc_transaccionregistro varchar(32) NOT NULL,
	cpvc_transaccioninactivo varchar(32) NULL,
	cpvc_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_z_pvc_pedidoventacaracteristica PRIMARY KEY (cpvc_llave)
);
CREATE INDEX ix_z_pvc_pedidoventacaracteristica_documento ON z_pvc_pedidoventacaracteristica USING btree (cpvc_documento);
CREATE INDEX ix_z_pvc_pedidoventacaracteristica_valoropcion ON z_pvc_pedidoventacaracteristica USING btree (cpvc_valoropcion);


ALTER TABLE z_pvc_pedidoventacaracteristica ADD CONSTRAINT fk_z_pvc_pedidoventacaracteristicacampo FOREIGN KEY (cpvc_campo) REFERENCES documentoplantillacaracteristica_dpcp(cdpc_llave);
ALTER TABLE z_pvc_pedidoventacaracteristica ADD CONSTRAINT fk_z_pvc_pedidoventacaracteristicadocumento FOREIGN KEY (cpvc_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);


CREATE TABLE z_dex_documentorelacionexpediente (
	cdex_llave varchar(32) NOT NULL,
	cdex_campomaestro varchar(32) NOT NULL,
	cdex_expedientedetalle varchar(32) NOT NULL,
	cdex_transaccionregistro varchar(32) NOT NULL,
	cdex_transaccioninactivo varchar(32) NULL,
	cdex_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	mdex_valor numeric(18,6) NOT NULL DEFAULT 0,
	CONSTRAINT pk_z_dex_documentorelacionexpediente PRIMARY KEY (cdex_llave)
);

CREATE INDEX ix_z_dex_documentorelacionexpediente_campomaestro ON z_dex_documentorelacionexpediente USING btree (cdex_campomaestro);

ALTER TABLE z_dex_documentorelacionexpediente ADD CONSTRAINT fk_z_dex_documentorelacionexpedientecampomaestro FOREIGN KEY (cdex_campomaestro) REFERENCES z_pvc_pedidoventacaracteristica(cpvc_llave);
ALTER TABLE z_dex_documentorelacionexpediente ADD CONSTRAINT fk_z_dex_documentorelacionexpedienteexpedientedetalle FOREIGN KEY (cdex_expedientedetalle) REFERENCES pedidoventa_pdvp(cpdv_llave);


CREATE TABLE z_pvd_pedidoventadinero (
	cpvd_llave varchar(32) NOT NULL,
	cpvd_documento varchar(32) NOT NULL,
	dpvd_fecha timestamptz NOT NULL,
	mpvd_valortotal numeric(18,6) NOT NULL DEFAULT 0,
	mpvd_saldo numeric(18,6) NOT NULL DEFAULT 0,
	bpvd_controlarsaldo boolean NOT NULL DEFAULT false,
	cpvd_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_z_pvd_pedidoventadinero_pvdp PRIMARY KEY (cpvd_llave),
	CONSTRAINT fk_z_pvd_pedidoventadinerodocumento FOREIGN KEY (cpvd_documento) REFERENCES pedidoventa_pdvp(cpdv_llave)
);
CREATE INDEX ix_z_pvd_pedidoventadinero_documento ON z_pvd_pedidoventadinero USING btree (cpvd_documento);

CREATE TABLE Z_drg_documentorelaciongestor (
	cdrg_llave varchar(32) NOT NULL,
	cdrg_documentoprincipal varchar(32) NOT NULL,
	cdrg_documentomodificador varchar(32) NULL,
	cdrg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	ddrg_fecha timestamptz NOT NULL,
	cdrg_estadoinicial varchar(32) NULL,
	cdrg_estadofinal varchar(32) NULL,
	cdrg_ubicacion varchar(32) NULL,
	cdrg_valores varchar(32) NULL,
	cdrg_usuario varchar(32) NOT NULL,
	ddrg_cierre timestamptz NULL,
	cdrg_nombre varchar(100) NOT NULL,
	cdrg_transaccion varchar(32) NULL,
	CONSTRAINT pk_z_drg_documentorelaciongestor_drgp PRIMARY KEY (cdrg_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestordocumentomodificador FOREIGN KEY (cdrg_documentomodificador) REFERENCES pedidoventa_pdvp(cpdv_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestordocumentoprincipal FOREIGN KEY (cdrg_documentoprincipal) REFERENCES pedidoventa_pdvp(cpdv_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorestadofinal FOREIGN KEY (cdrg_estadofinal) REFERENCES procesoestado_pesp(cpes_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorestadoinicial FOREIGN KEY (cdrg_estadoinicial) REFERENCES procesoestado_pesp(cpes_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorusuario FOREIGN KEY (cdrg_usuario) REFERENCES usuario_usrp(cusr_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorvalores FOREIGN KEY (cdrg_valores) REFERENCES z_pvd_pedidoventadinero(cpvd_llave)
);
CREATE INDEX ix_z_drg_documentorelaciongestor_documentoprincipal ON Z_drg_documentorelaciongestor USING btree (cdrg_documentoprincipal);

CREATE TABLE z_rej_reporteejecucion (
	crej_llave varchar(32) NOT NULL,
	crej_reporte varchar(32) NOT NULL,
	crej_documento varchar(32) NULL,
	drej_fechainicio timestamptz NOT NULL,
	drej_fechafin timestamptz NOT NULL,
	crej_error varchar(4000) NULL,
	crej_url character varying(4000),
	crej_usuario varchar(32) NULL,
	crej_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_z_rej_reporteejecucion_rejp PRIMARY KEY (crej_llave),
	CONSTRAINT fk_z_rej_reporteejecuciondocumento FOREIGN KEY (crej_documento) REFERENCES pedidoventa_pdvp(cpdv_llave),
	CONSTRAINT fk_z_rej_reporteejecucionreporte FOREIGN KEY (crej_reporte) REFERENCES reportebase_rpbp(crpb_llave)
);

CREATE TABLE z_dpv_detallepedidoventa (
	cdpv_llave varchar(32) NOT NULL,
	cdpv_producto varchar(32) NOT NULL,
	cdpv_nombre character varying(200),
    cdpv_campo character varying(32),
	mdpv_cantidad numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_valorunitario numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_valorsubtotal numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_valortotal numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_cantidadtotal numeric(18, 6) NOT NULL DEFAULT 0,
	cdpv_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	cdpv_productotercero varchar(32) NULL,
	ndpv_cantidadpromocion int4 NOT NULL DEFAULT 0,
	ndpv_cantidadpromocionbase int4 NOT NULL DEFAULT 0,
	mdpv_valorminimo numeric(18, 6) NOT NULL DEFAULT 0,
	mdpv_valormaximo numeric(18, 6) NOT NULL DEFAULT 0,
	cdpv_plantilla varchar(32) NOT NULL,
	cdpv_documento varchar(32) NOT NULL,
	cdpv_transaccionregistro varchar(32) NOT NULL,
	cdpv_transaccioninactivo varchar(32) NULL,
	CONSTRAINT pk_z_dpv_detallepedidoventa PRIMARY KEY (cdpv_llave)
);

ALTER TABLE detallepedidoventa_dpvp ADD CONSTRAINT fk_z_dpv_detallepedidoventadocumento FOREIGN KEY (cdpv_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);
ALTER TABLE detallepedidoventa_dpvp ADD CONSTRAINT fk_z_dpv_detallepedidoventaproducto FOREIGN KEY (cdpv_producto) REFERENCES producto_prop(cpro_llave);
ALTER TABLE detallepedidoventa_dpvp ADD CONSTRAINT fk_z_dpv_detallepedidoventaproductotercero FOREIGN KEY (cdpv_productotercero) REFERENCES usuariorolproducto_urpp(curp_llave);

CREATE TABLE z_dcp_detallecaracteristicaproducto (
	cdcp_llave varchar(32) NOT NULL,
	cdcp_entidad varchar(32) NOT NULL,
	cdcp_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	ddcp_valorfecha timestamptz NULL,
	cdcp_valortext varchar(4000) NULL,
	mdcp_valornumero numeric(18, 6) NOT NULL DEFAULT 0,
	cdcp_valoropcion varchar(32) NULL,
	cdcp_campo varchar(32) NOT NULL,
	cdcp_transaccionregistro varchar(32) NOT NULL,
	cdcp_transaccioninactivo varchar(32) NULL,
	CONSTRAINT pk_z_dcp_detallecaracteristicaproducto PRIMARY KEY (cdcp_llave)
);

CREATE INDEX IF NOT EXISTS ix_procesotransicionautomatica_ejecucion ON procesotransicionautomatica_ptap USING btree (dpta_ejecucion);
CREATE INDEX IF NOT EXISTS ix_procesotransicionautomatica_transicion ON procesotransicionautomatica_ptap USING btree (cpta_transicion);

--Funciones iniciales
CREATE OR REPLACE FUNCTION f_convnl(num numeric)
  RETURNS character varying AS
$BODY$
DECLARE	
	d VARCHAR[];f VARCHAR[];g VARCHAR[];numt VARCHAR;txt VARCHAR;a INTEGER;a1 INTEGER;a2 INTEGER;
	n INTEGER;
	p INTEGER;
	negativo BOOLEAN;
BEGIN
	-- Maximo 999.999.999,99
	IF num > 999999999.99 THEN
		RETURN '---';
	END IF;
	txt = '';
	d = ARRAY[' un',' dos',' tres',' cuatro',' cinco',' seis',' siete',' ocho',' nueve',' diez',' once',' doce',' trece',' catorce',' quince',
		' dieciseis',' diecisiete',' dieciocho',' diecinueve',' veinte',' veintiun',' veintidos', ' veintitres', ' veinticuatro', ' veinticinco',
		' veintiseis',' veintisiete',' veintiocho',' veintinueve'];
	f = ARRAY ['','',' treinta',' cuarenta',' cincuenta',' sesenta',' setenta',' ochenta', ' noventa'];
	g= ARRAY [' ciento',' doscientos',' trescientos',' cuatrocientos',' quinientos',' seiscientos',' setecientos',' ochocientos',' novecientos'];
	numt = LPAD((num::numeric(12,2))::text,12,'0');
	IF strpos(numt,'-') > 0 THEN
	   negativo = TRUE;
	ELSE
	   negativo = FALSE;
	END IF;
	numt = TRANSLATE(numt,'-','0');
	numt = TRANSLATE(numt,'.,','');
	-- Trato 4 grupos: millones, miles, unidades y decimales
	p = 1;
	FOR i IN 1..4 LOOP
		IF i < 4 THEN
			n = substring(numt::text FROM p FOR 3);
		ELSE
			n = substring(numt::text FROM p FOR 2);
		END IF;
		p = p + 3;
		IF i = 4 THEN
			IF txt = '' THEN
				txt = ' cero';
			END IF;
			IF n > 0 THEN
			-- Empieza con los decimales
				txt = txt || ' con';
			END IF;
		END IF;
		-- Centenas 
		IF n > 99 THEN
			a = substring(n::text FROM 1 FOR 1);
			a1 = substring(n::text FROM 2 FOR 2);
			IF a = 1 THEN
				IF a1 = 0 THEN
					txt = txt || ' cien';
				ELSE
					txt = txt || ' ciento';
				END IF;
			ELSE
				txt = txt || g[a];
			END IF;
		ELSE
			a1 = n;
		END IF;
		-- Decenas
		a = a1;
		IF a > 0 THEN
			IF a < 30 THEN
				IF a = 21 AND (i = 3 OR i = 4) THEN
					txt = txt || ' veintiuno';
				ELSIF n = 1 AND i = 2 THEN
					txt = txt; 
				ELSIF a = 1 AND (i = 3 OR i = 4)THEN
					txt = txt || ' uno';
				ELSE
					txt = txt || d[a];
				END IF;
			ELSE
				a1 = substring(a::text FROM 1 FOR 1);
				a2 = substring(a::text FROM 2 FOR 1);
				IF a2 = 1 AND (i = 3 OR i = 4) THEN
						txt = txt || f[a1] || ' y' || ' uno';
				ELSE
					IF a2 <> 0 THEN
						txt = txt || f[a1] || ' y' || d[a2];
					ELSE
						txt = txt || f[a1];
					END IF;
				END IF;
			END IF;
		END IF;
		IF n > 0 THEN
			IF i = 1 THEN
				IF n = 1 THEN
					txt = txt || ' millon';
				ELSE
					txt = txt || ' millones';
				END IF;
			ELSIF i = 2 THEN
				txt = txt || ' mil';
			END IF;		
		END IF;
	END LOOP;
	txt = LTRIM(txt);
	IF negativo = TRUE THEN
	   txt= '-' || txt;
	END IF;
    RETURN txt;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION f_convnl(numeric)
  OWNER TO postgres;

CREATE OR REPLACE VIEW campo_documento AS 
 SELECT pvc.cpvc_llave as cpvc_llave,
    pvc.dpvc_valorfecha as dpvc_valorfecha,
    pvc.mpvc_valornumero as mpvc_valornumero,
    pvc.cpvc_valortext as cpvc_valortext,
    pvc.cpvc_valoropcion as cpvc_valoropcion,
    pvc.cpvc_valorauxiliar as cpvc_valorauxiliar,
    pvc.cpvc_campo as cpvc_campo,
    dpc.cdpc_codigo as cdpf_codigo,
    dpc.cdpc_nombre as cdpf_nombre,
    pvc.cpvc_documento as cdrc_documento
   FROM pedidoventacaracteristica_pvcp pvc,
    documentoplantillacaracteristica_dpcp dpc
  WHERE pvc.cpvc_campo =dpc.cdpc_llave AND pvc.cpvc_estado::text = 'A'::text;

CREATE OR REPLACE FUNCTION movimiento_descripcion(id_documento character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
BEGIN 
    return descripcion(id_documento);
END; 
$function$
;

CREATE OR REPLACE FUNCTION dcs_saldo_cero(character varying)
  RETURNS character varying AS
$BODY$
BEGIN
	IF EXISTS (SELECT mpvd_saldo FROM pedidoventadinero_pvdp WHERE cpvd_documento  = $1 AND cpvd_estado = 'A' AND mpvd_saldo !=0) THEN
		RETURN 'N';
	ELSE
		RETURN 'S';
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION descripcion(id_documento character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
	DECLARE _documento_actual pedidoventa_pdvp; 
	DECLARE plantilla_campo_descripcion character varying;
	DECLARE plantilla_campo_descripcion_nivel2 character varying;
	DECLARE id_documento_principal character varying;
	DECLARE descripcion_anidada character varying;
BEGIN 
    if id_documento IS NULL THEN 
        RETURN NULL;
    END IF;
    SELECT * INTO _documento_actual FROM pedidoventa_pdvp where cpdv_llave = id_documento;
    SELECT cppd_valor INTO plantilla_campo_descripcion FROM propiedad_ppdp where cppd_campo = _documento_actual.cpdv_plantilla and cppd_estado = 'A' and cppd_propiedadvalor = 'PROP_44';
    IF plantilla_campo_descripcion IS NOT NULL THEN 
		RETURN (select cpvc_valortext from campo4id ( id_documento , plantilla_campo_descripcion, _documento_actual.npdv_historico));
    ELSE
		SELECT cppd_valor INTO plantilla_campo_descripcion_nivel2 FROM propiedad_ppdp where cppd_campo = _documento_actual.cpdv_plantilla and cppd_estado = 'A' and cppd_propiedadvalor = 'PROP_45';
		IF plantilla_campo_descripcion_nivel2 IS NOT NULL THEN
			SELECT cpvc_valoropcion INTO id_documento_principal FROM campo4id (id_documento, plantilla_campo_descripcion_nivel2, _documento_actual.npdv_historico);
			CASE WHEN id_documento_principal IS  NULL THEN 
			    RETURN NULL;
			ELSE
			    SELECT descripcion(id_documento_principal) INTO descripcion_anidada;
			    IF descripcion_anidada IS NULL THEN
					RETURN (select cpdv_nombre from pedidoventa_pdvp pcd where cpdv_llave = id_documento_principal);
			    ELSE
					RETURN '(' || (select cpdv_nombre from pedidoventa_pdvp pcd where cpdv_llave = id_documento_principal) ||') '|| descripcion_anidada;
			    END IF;
			END CASE;
		ELSE
			RETURN NULL;
		END IF;
    END IF;
END; 
$function$
;

CREATE OR REPLACE VIEW vi_valores
AS SELECT pedidoventadinero_pvdp.cpvd_documento AS vi_vlr_documento,
    pedidoventadinero_pvdp.mpvd_valortotal AS vi_vlr_total,
    pedidoventadinero_pvdp.mpvd_saldo AS vi_vlr_saldo,
    pedidoventadinero_pvdp.dpvd_fecha AS vi_vlr_fecha
   FROM pedidoventadinero_pvdp
  WHERE pedidoventadinero_pvdp.cpvd_estado::text = 'A'::text;


CREATE OR REPLACE FUNCTION migrar_campos(_plantilla character varying, _fecha_maxima timestamp with time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare 
	documentos character varying[];
	campos character varying[];
	items_documento character varying[];
	v_cnt numeric;
begin
	if
		(select count(*) from procesotransicion_ptrp where cptr_estado = 'A' and cptr_estadopartida is null and cptr_plantilla = _plantilla) = 0
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
				and npdv_historico is null and cpdv_estado != 'A'
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
	INSERT INTO z_pvd_pedidoventadinero(cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha, bpvd_controlarsaldo)
		SELECT cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha, bpvd_controlarsaldo
			FROM pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	INSERT INTO z_drg_documentorelaciongestor (cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido)
		SELECT cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre, cdrg_transaccion, bdrg_estadorepetido
			FROM documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	INSERT INTO z_rej_reporteejecucion (crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado, crej_url)
		SELECT crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado , crej_url
			FROM reporteejecucion_rejp where crej_documento = any(documentos);
	INSERT INTO z_dpv_detallepedidoventa (cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo, cdpv_campo, cdpv_nombre)
		SELECT cdpv_llave, cdpv_producto, mdpv_cantidad, mdpv_valorunitario, mdpv_valorsubtotal, mdpv_valortotal, mdpv_cantidadtotal, cdpv_estado, cdpv_productotercero, ndpv_cantidadpromocion, ndpv_cantidadpromocionbase, mdpv_valorminimo, mdpv_valormaximo, cdpv_plantilla, cdpv_documento, cdpv_transaccionregistro, cdpv_transaccioninactivo, cdpv_campo, cdpv_nombre
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
END;$function$
;

CREATE OR REPLACE FUNCTION campo4code(_documento character varying, _code character varying)
 returns table (
	cpvc_llave varchar(32),
	cpvc_documento varchar(32),
	dpvc_valorfecha timestamptz,
	mpvc_valornumero numeric(24, 6),
	cpvc_valortext varchar(4000),
	cpvc_valoropcion varchar(32),
	cpvc_estado varchar(1),
	cpvc_campo varchar(32),
	cpvc_valorauxiliar varchar(32),
	cpvc_transaccionregistro varchar(32),
	cpvc_transaccioninactivo varchar(32)
	) 
 LANGUAGE plpgsql
AS $function$
declare 
	_documento_actual pedidoventa_pdvp;
begin
	select * into _documento_actual from pedidoventa_pdvp where cpdv_llave = _documento;
	if found then
		return query select
			t.cpvc_llave,
			t.cpvc_documento,
			t.dpvc_valorfecha,
			t.mpvc_valornumero,
			t.cpvc_valortext,
			t.cpvc_valoropcion,
			t.cpvc_estado,
			t.cpvc_campo,
			t.cpvc_valorauxiliar,
			t.cpvc_transaccionregistro,
			t.cpvc_transaccioninactivo
		from campo4code(_documento, _code, _documento_actual.cpdv_plantilla, _documento_actual.npdv_historico) t;
	end if;
	
END;$function$
;

CREATE OR REPLACE FUNCTION campo4code(_documento character varying, _code character varying, _plantilla character varying, _historico int)
 returns table (
	cpvc_llave varchar(32),
	cpvc_documento varchar(32),
	dpvc_valorfecha timestamptz,
	mpvc_valornumero numeric(24, 6),
	cpvc_valortext varchar(4000),
	cpvc_valoropcion varchar(32),
	cpvc_estado varchar(1),
	cpvc_campo varchar(32),
	cpvc_valorauxiliar varchar(32),
	cpvc_transaccionregistro varchar(32),
	cpvc_transaccioninactivo varchar(32)
	) 
 LANGUAGE plpgsql
AS $function$
declare 
	_campo documentoplantillacaracteristica_dpcp;
begin
	select * into _campo from documentoplantillacaracteristica_dpcp where cdpc_plantilla = _plantilla and cdpc_estado = 'A' and cdpc_codigo = _code;
	if found then
		return query select
				tb.cpvc_llave,
				tb.cpvc_documento,
				tb.dpvc_valorfecha,
				tb.mpvc_valornumero,
				tb.cpvc_valortext,
				tb.cpvc_valoropcion,
				tb.cpvc_estado,
				tb.cpvc_campo,
				tb.cpvc_valorauxiliar,
				tb.cpvc_transaccionregistro,
				tb.cpvc_transaccioninactivo
			from campo4id(_documento, _campo.cdpc_llave, _historico) tb;
	end if;
END;$function$
;

CREATE OR REPLACE FUNCTION campo4id(_documento character varying, _id_campo character varying, _historico int)
 returns table (
	cpvc_llave varchar(32),
	cpvc_documento varchar(32),
	dpvc_valorfecha timestamptz,
	mpvc_valornumero numeric(24, 6),
	cpvc_valortext varchar(4000),
	cpvc_valoropcion varchar(32),
	cpvc_estado varchar(1),
	cpvc_campo varchar(32),
	cpvc_valorauxiliar varchar(32),
	cpvc_transaccionregistro varchar(32),
	cpvc_transaccioninactivo varchar(32)
	) 
 LANGUAGE plpgsql
AS $function$
begin
	if _historico = 0 then
		select npdv_historico into _historico from pedidoventa_pdvp where cpdv_llave = _documento;
	end if;
	if _historico is null then
		return query select
				t.cpvc_llave,
				t.cpvc_documento,
				t.dpvc_valorfecha,
				t.mpvc_valornumero,
				t.cpvc_valortext,
				t.cpvc_valoropcion,
				t.cpvc_estado,
				t.cpvc_campo,
				t.cpvc_valorauxiliar,
				t.cpvc_transaccionregistro,
				t.cpvc_transaccioninactivo
			from pedidoventacaracteristica_pvcp t where t.cpvc_documento = _documento and t.cpvc_campo = _id_campo and t.cpvc_estado = 'A';
	else
		return query select
			z.cpvc_llave,
			z.cpvc_documento,
			z.dpvc_valorfecha,
			z.mpvc_valornumero,
			z.cpvc_valortext,
			z.cpvc_valoropcion,
			z.cpvc_estado,
			z.cpvc_campo,
			z.cpvc_valorauxiliar,
			z.cpvc_transaccionregistro,
			z.cpvc_transaccioninactivo
		from z_pvc_pedidoventacaracteristica z where z.cpvc_documento = _documento and z.cpvc_campo = _id_campo and z.cpvc_estado = 'A';
	end if;	
END;$function$
;

CREATE OR REPLACE FUNCTION campo4documento(_documento character varying, _historico int) 
 returns table (
	cpvc_llave varchar(32),
	cpvc_documento varchar(32),
	dpvc_valorfecha timestamptz,
	mpvc_valornumero numeric(24, 6),
	cpvc_valortext varchar(4000),
	cpvc_valoropcion varchar(32),
	cpvc_estado varchar(1),
	cpvc_campo varchar(32),
	cpvc_valorauxiliar varchar(32),
	cpvc_transaccionregistro varchar(32),
	cpvc_transaccioninactivo varchar(32)
	) 
 LANGUAGE plpgsql
AS $function$
begin
	if _historico = 0 then
		select npdv_historico into _historico from pedidoventa_pdvp where cpdv_llave = _documento;
	end if;
	if _historico is null then
		return query select
				t.cpvc_llave,
				t.cpvc_documento,
				t.dpvc_valorfecha,
				t.mpvc_valornumero,
				t.cpvc_valortext,
				t.cpvc_valoropcion,
				t.cpvc_estado,
				t.cpvc_campo,
				t.cpvc_valorauxiliar,
				t.cpvc_transaccionregistro,
				t.cpvc_transaccioninactivo
			from pedidoventacaracteristica_pvcp t where t.cpvc_documento = _documento and t.cpvc_estado = 'A';
	else
		return query select
			z.cpvc_llave,
			z.cpvc_documento,
			z.dpvc_valorfecha,
			z.mpvc_valornumero,
			z.cpvc_valortext,
			z.cpvc_valoropcion,
			z.cpvc_estado,
			z.cpvc_campo,
			z.cpvc_valorauxiliar,
			z.cpvc_transaccionregistro,
			z.cpvc_transaccioninactivo
		from z_pvc_pedidoventacaracteristica z where z.cpvc_documento = _documento and z.cpvc_estado = 'A';
	end if;
END;$function$
;

CREATE OR REPLACE FUNCTION saldo4documento(_documento character varying, _historico int) 
 returns table (
	cpvd_llave varchar(32), 
	cpvd_documento varchar(32), 
	mpvd_valortotal numeric(24, 6), 
	mpvd_saldo numeric(24, 6), 
	cpvd_estado varchar(1), 
	dpvd_fecha timestamptz
	) 
 LANGUAGE plpgsql
AS $function$
begin
	if _historico = 0 then
		select npdv_historico into _historico from pedidoventa_pdvp where cpdv_llave = _documento;
	end if;
	if _historico is null then
		return query select
				t.cpvd_llave, 
				t.cpvd_documento, 
				t.mpvd_valortotal, 
				t.mpvd_saldo, 
				t.cpvd_estado, 
				t.dpvd_fecha 
			from pedidoventadinero_pvdp t where t.cpvd_documento = _documento and t.cpvd_estado = 'A';
	else
		return query select
				z.cpvd_llave, 
				z.cpvd_documento, 
				z.mpvd_valortotal, 
				z.mpvd_saldo, 
				z.cpvd_estado, 
				z.dpvd_fecha 
			from pedidoventadinero_pvdp z where z.cpvd_documento = _documento and z.cpvd_estado = 'A';
	end if;
END;$function$
;

CREATE OR REPLACE FUNCTION public.ultima_gestion(_documento character varying, _estado character varying)
 RETURNS TABLE(cdrg_llave character varying, cdrg_documentoprincipal character varying, cdrg_documentomodificador character varying, ddrg_fecha timestamp with time zone, cdrg_estadoinicial character varying, cdrg_estadofinal character varying, cdrg_estado character varying, cdrg_ubicacion character varying, cdrg_valores character varying, cdrg_usuario character varying, ddrg_cierre timestamp with time zone, cdrg_nombre character varying, cdrg_transaccion character varying)
 LANGUAGE plpgsql
AS $function$
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
	where drg.cdrg_documentoprincipal = _documento and drg.cdrg_estado = 'A' and drg.cdrg_estadofinal = _estado and bdrg_ultima = true;	
END;$function$
;

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean)
	VALUES('PROP_01' , 'C', 'TEXTO LARGO', 'BASICA', 'FORMATO', 'T', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_02' , 'C', 'ARCHIVO_TAMANO_MAXIMO', 'ARCHIVO_TAMANO_MAXIMO', 'REQUISITO', 'A');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_03' , 'C', 'BINARIO_VERDADERO', 'BINARIO_VERDADERO', 'FORMATO', 'I');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_04' , 'C', 'BINARIO_FALSO', 'BINARIO_FALSO', 'FORMATO', 'I');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_05' , 'C', 'BODEGA_FIJA', 'BODEGA_FIJA', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_06' , 'C', 'BODEGA_MOVIMIENTO', 'BODEGA_MOVIMIENTO', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_07' , 'C', 'CONFIGURACION_PLANTILLA_TIPO', 'CONFIGURACION_PLANTILLA_TIPO', 'REQUISITO', 'G');	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_08' , 'C', 'CONFIGURACION_ENTIDAD', 'CONFIGURACION_ENTIDAD', 'REQUISITO' ,'G', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_09' , 'C', 'TIPO DE MOVIMIENTO', 'CUENTA_MOVIMIENTO', 'REQUISITO', 'Z', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_11' , 'C', 'CUENTA_ABRIR_CAJA', 'CUENTA_ABRIR_CAJA', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_12' , 'C', 'CUENTA_CERRAR_CAJA', 'CUENTA_CERRAR_CAJA', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_13' , 'C', 'DETALLE_NUMERO_COLUMNAS', 'DETALLE_NUMERO_COLUMNAS', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_14' , 'C', 'DETALLE_TECLADO', 'DETALLE_TECLADO', 'REQUISITO', 'J', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_15' , 'C', 'DETALLE_TARIFARIO', 'DETALLE_TARIFARIO', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_16' , 'C', 'DETALLE_OCULTAR_IMAGENES', 'DETALLE_OCULTAR_IMAGENES', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_17' , 'C', 'UNICO_PRODUCTO', 'UNICO_PRODUCTO', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_18' , 'C', 'DETALLE_FORMULA', 'DETALLE_FORMULA', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_19' , 'C', 'FUENTE DE DATOS', 'PLANTILLA_AUXILIAR', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_20' , 'C', 'AUTOLOAD', 'AUTOLOAD', 'REQUISITO', 'Z', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_22' , 'C', 'FECHA_SIN_CALENDAR', 'FECHA_SIN_CALENDAR', 'REQUISITO', 'F', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_23' , 'C', 'FECHA_MAXIMA', 'FECHA_MAXIMA', 'REQUISITO', 'F');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_24' , 'C', 'FECHA_MINIMA', 'FECHA_MINIMA', 'REQUISITO', 'F');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_25' , 'C', 'FECHA_RANGO', 'FECHA_RANGO', 'REQUISITO', 'F');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_26' , 'C', 'FECHA_RANGO_MAXIMO', 'FECHA_RANGO_MAXIMO', 'REQUISITO', 'F');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_27' , 'C', 'FORMATO MONEDA', 'NUMERO_MONEDA', 'REQUISITO', 'N', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_28' , 'C', 'NUMERO_FORMULA', 'NUMERO_FORMULA', 'REQUISITO', 'N', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_29' , 'C', 'NUMERO_FUNCION', 'NUMERO_FUNCION_SQL', 'REQUISITO', 'N', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_30' , 'C', 'NUMERO_STEP', 'NUMERO_STEP', 'REQUISITO', 'N');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_31' , 'C', 'NUMERO_REDONDEO', 'NUMERO_REDONDEO', 'REQUISITO', 'N');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_32' , 'C', 'MULTIPLE', 'MULTIPLE', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_33' , 'C', 'CAMPO_HEREDADO', 'CAMPO_HEREDADO', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_34' , 'C', 'FORMATO', 'FORMATO', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_35' , 'C', 'MOSTRAR EN POP UP', 'PROCESO_POP', 'REQUISITO', 'Z', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple)
	VALUES('PROP_36' , 'C', 'ACCIONES CRUD', 'PROCESO_ACCIONES', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_37' , 'C', 'RUTA BPM GESTION', 'PROCESO_GESTIONAR_ESTADOS', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_38' , 'C', 'PROCESO_DIVISION', 'PROCESO_DIVISION', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_39' , 'C', 'PROCESO_VALOR', 'PROCESO_VALOR', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_40' , 'C', 'SOLICITAR FECHAS EN CONSULTA', 'SOLICITAR_FECHAS', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_41' , 'C', 'FUNCION CONSULTA DATOS', 'PROCESO_FUNCION_SQL', 'REQUISITO', 'Z', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_42' , 'L', 'TERCERO', 'TERCERO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_43' , 'L', 'ENCABEZADO', 'ENCABEZADO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_44' , 'L', 'DESCRIPCION', 'DESCRIPCION', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_45' , 'L', 'DESCRIPCION_NIVEL2', 'DESCRIPCION_NIVEL2', 'REQUISITO');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_47' , 'L', 'TOTAL', 'TOTAL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_48' , 'L', 'CONSECUTIVO', 'CONSECUTIVO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_49' , 'L', 'FECHA', 'FECHA', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_50' , 'L', 'RESPONSABLE', 'RESPONSABLE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_51' , 'L', 'ORDEN', 'ORDEN', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_52' , 'L', 'ORDEN_DESCENDENTE', 'ORDEN_DESCENDENTE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_53' , 'L', 'AYUDA', 'AYUDA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple) 
	VALUES('PROP_54' , 'L', 'FUNCION_SQL_VALIDAR', 'FUNCION_SQL_VALIDAR', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_55' , 'L', 'SOLICITAR FECHAS EN CONSULTA', 'SOLICITAR_FECHAS', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_56' , 'L', 'COPY_TEXT', 'COPY_TEXT', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_57' , 'L', 'MENSAJE', 'MENSAJE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_58' , 'L', 'FUNCION DESTINATARIOS DEL MENSAJE', 'MENSAJE_DESTINATARIOS_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple) 
	VALUES('PROP_59' , 'T', 'FUNCION_SQL_VALIDAR', 'FUNCION_SQL_VALIDAR', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_60' , 'T', 'MENSAJE', 'MENSAJE', 'REQUISITO');
--Borradas las propiedades de sistema
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_69' , 'T', 'FUNCION DESTINATARIOS DEL MENSAJE', 'MENSAJE_DESTINATARIOS_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_70' , 'E', 'ENCABEZADO DOCUMENTO', 'REPORTE_ENCABEZADO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_71' , 'E', 'OPCION EN EXCEL', 'REPORTE_EXCEL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_72' , 'E', 'P_SUBREPORT_', 'P_SUBREPORT_', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_73' , 'P', 'MENSAJE', 'MENSAJE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_74' , 'P', 'FUNCION DESTINATARIOS DEL MENSAJE', 'MENSAJE_DESTINATARIOS_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_75' , 'C', 'FORMATO', 'FORMATO', 'REQUISITO', 'T');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_76' , 'C', 'TEXTO_FORMULA', 'TEXTO_FORMULA', 'REQUISITO', 'T', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_77' , 'L', 'PERMISO CREAR', 'PERMISO_PLANTILLA_CREAR', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_78' , 'L', 'PERMISO MODIFICAR', 'PERMISO_PLANTILLA_MODIFICAR', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_79' , 'L', 'PERMISO ELIMINAR', 'PERMISO_PLANTILLA_ELIMINAR', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_piderol, bpvd_multiple) 
	VALUES('PROP_80' , 'L', 'INICIO_RAPIDO', 'PERMISO_PLANTILLA_INICIO_RAPIDO', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_81' , 'L', 'OCULTAR_TOTAL', 'PERMISO_PLANTILLA_OCULTAR_TOTAL', 'PERMISOS', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_82' , 'L', 'FILTRO POR CAMPO', 'PERMISO_PLANTILLA_CAMPO_FILTRO', 'PERMISOS', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_83' , 'L', 'ESTADOS POR DEFECTO CONSULTA', 'PERMISO_PLANTILLA_FILTROS_BASE', 'PERMISOS');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_84' , 'L', 'CARGAS MASIVAS', 'PERMISO_PLANTILLA_CARGA_MASIVA', 'PERMISOS', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_85' , 'L', 'CAMBIAR ESTADOS', 'PERMISO_PLANTILLA_CAMBIAR_ESTADO', 'PERMISOS', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_87' , 'L', 'BENEFICIO', 'BENEFICIO', 'BENEFICIO');	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo, bpvd_multiple) 
	VALUES('PROP_88' , 'E', 'COLUMNA', 'COLUMNA', 'COLUMNAS DEL REPORTE', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_89', 'A', 'ROL', 'ROL', 'REQUISITO', 'E');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, cpvd_origencategoria) 
	VALUES('PROP_90', 'A', 'FUNCION_SQL_ESTADO_ASIGNAR', 'FUNCION ASIGNACION', 'REQUISITO', true, true, 'E');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean, cpvd_origencategoria) 
	VALUES('PROP_91', 'A', 'MODIFICABLE', 'MODIFICABLE', 'REQUISITO', true,'E');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_92', 'A', 'COLOR', 'COLOR', 'REQUISITO', 'E');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_94' , 'C', 'FILTRO', 'FILTRO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_95' , 'C', 'VALOR POR DEFECTO', 'DEFAULT', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_96' , 'C', 'CAMPOS DEPENDENCIA', 'DEPENDE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_97' , 'C', 'FECHA CON HORA', 'FECHA_CON_HORA', 'REQUISITO', 'F', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_98' , 'C', 'TIPOS SOPORTADOS', 'ARCHIVO_TIPO', 'REQUISITO', 'A', true);
INSERT INTO propiedadvalordefinido_pvdp(cpvd_llave,  cpvd_origen,  cpvd_codigo,  cpvd_nombre,  cpvd_grupo)
	VALUES('PROP_99',  'L',  'CUENTA_SOBREGIRO',  'CUENTA SOBREGIRO',  'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_100' , 'E', 'PIE DE PAGINA', 'REPORTE_PIE_PAGINA', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_multiple) 
	VALUES('PROP_101' , 'L', 'IMAGEN DOCUMENTACION', 'IMAGEN', 'IMAGEN', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_102' , 'E', 'REQUERIMIENTO', 'REQUERIMIENTO', 'REQUISITO', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_104' , 'C', 'VISIBLE EN EL RENDER', 'PERMISO_CAMPO_RENDER', 'PERMISOS', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_105' , 'C', 'MODIFICABLE', 'PERMISO_CAMPO_MODIFICABLE', 'PERMISOS', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_107' , 'L', 'LISTABLE EN MENU', 'PERMISO_PLANTILLA_LISTAR_MENU', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_108' , 'L', 'VER TODOS', 'PERMISO_PLANTILLA_VER_TODOS', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_109' , 'L', 'FUNCION CALCULA TOTAL', 'TOTAL_FUNCION', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_110' , 'C', 'DETALLE_TARIFARIO_PRODUCTO', 'DETALLE_TARIFARIO_PRODUCTO', 'REQUISITO', 'N');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_111' , 'T', 'MENSAJE REPORTE', 'MENSAJE_REPORTE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_112' , 'P', 'MENSAJE REPORTE', 'MENSAJE_REPORTE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_113' , 'L', 'MENSAJE REPORTE', 'MENSAJE_REPORTE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_114' , 'L', 'PERMISO VER FORMULARIOS', 'PERMISO_PLANTILLA_VER', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_115' , 'T', 'MENSAJE DESTINATARIO', 'MENSAJE_DESTINATARIO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_116' , 'P', 'MENSAJE DESTINATARIO', 'MENSAJE_DESTINATARIO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_117' , 'L', 'MENSAJE DESTINATARIO', 'MENSAJE_DESTINATARIO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_118' , 'C', 'OPCIONES', 'OPCIONES', 'REQUISITO', 'G', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_120' , 'L', 'FUNCION CONSULTA DATOS', 'PROCESO_FUNCION_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_121' , 'C', 'MODIFICAR CAMPO PRINCIPAL', 'MODIFICAR_CAMPO', 'REQUISITO', 'Z', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_122' , 'C', 'CONSULTA PRODUCTOS FUNCION ', 'PRODUCTOS_FUNCION_SQL', 'REQUISITO', 'J', TRUE, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_123' , 'C', 'CONSULTA PRODUCTOS CAMPO', 'PRODUCTOS_FUNCION_CAMPO', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_124' , 'C', 'CONSULTA PRODUCTOS TERCERO', 'PRODUCTOS_TERCERO', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_125' , 'L', 'GENERAR DOCUMENTOS', 'GENERAR_DOCUMENTOS_SQL', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_126' , 'T', 'REQUERIMIENTO_TRANSICION', 'REQUERIMIENTO_TRANSICION', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_127' , 'C', 'CATEGORIA PRODUCTO', 'DETALLE_CATEGORIA', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_128' , 'C', 'REQUERIMIENTO_CAMPO', 'REQUERIMIENTO_CAMPO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_129' , 'C', 'INCLUIR EN TRAZABILIDAD', 'INCLUIR_TRAZA_PRINCIPAL', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_130' , 'P', 'REQUERIMIENTO_PROCESO', 'REQUERIMIENTO PROCESO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_131' , 'L', 'REQUERIMIENTO_PLANTILLA', 'REQUERIMIENTO PLANTILLA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_132' , 'L', 'PLANTILLA ANULAR', 'PLANTILLA_ANULAR', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_133' , 'T', 'UBICACION', 'UBICACION', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_134' , 'C', 'OPCIONAL', 'PERMISO_CAMPO_OPCIONAL', 'PERMISOS', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_135' , 'C', 'BLOQUEAR', 'PERMISO_CAMPO_BLOQUEAR', 'PERMISOS', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_136' , 'A', 'ESTADO_ASIGNAR', 'ASIGNACION DE USUARIO', 'REQUISITO', 'E');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_137' , 'E', 'ENCABEZADO EN EXCEL', 'REPORTE_ENCABEZADO_EXCEL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo,  bpvd_textoculto) 
	VALUES('PROP_138' , 'E', 'JRXML', 'REPORTE_JRXML', 'REQUISITO',  true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple) 
	VALUES('PROP_139' , 'L', 'VALIDACION ANTERIOR A GENERAR EL DOCUMENTO', 'FUNCION_SQL_VALIDAR_ANTES', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_pidefechas, bpvd_solicitamotivo, bpvd_pideusuario, bpvd_pidetiempobloqueo) 
	VALUES('PROP_140' , 'T', 'TEMPORIZADOR', 'TEMPORIZADOR', 'REQUISITO', true, true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_141' , 'L', 'TIPO ROL', 'PLANTILLA_TIPO_ROL', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_142' , 'L', 'TIPO REPORTE', 'PLANTILLA_TIPO_REPORTE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_143' , 'L', 'TIPO CUENTA', 'PLANTILLA_TIPO_CUENTA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_144' , 'L', 'TIPO PRODUCTO', 'PLANTILLA_TIPO_PRODUCTO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_145' , 'L', 'TIPO BODEGA', 'PLANTILLA_TIPO_BODEGA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_textoculto, cpvd_origencategoria, bpvd_solicitamotivo) 
	VALUES('PROP_146' , 'A', 'DECISION_SQL', 'DECISION_SQL', 'REQUISITO', true, 'D', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_147' , 'C', 'FUNCION TARIFAS', 'DETALLE_TARIFARIO_SQL', 'REQUISITO', 'J', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo) 
	VALUES('PROP_148' , 'L', 'PRODUCTO_CAMPO_VALOR_UNITARIO', 'PRODUCTO CAMPO VALOR UNITARIO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo) 
	VALUES('PROP_149' , 'L', 'PRODUCTO_CAMPO_VALOR_MINIMO', 'PRODUCTO CAMPO VALOR MINIMO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo) 
	VALUES('PROP_150' , 'L', 'PRODUCTO_CAMPO_CANTIDAD', 'PRODUCTO CAMPO CANTIDAD', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_151' , 'T', 'GENERA_DOCUMENTO_CAMPO', 'CAMPO PARA GENERAR DOCUMENTO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple, cpvd_origencategoria) 
	VALUES('PROP_152' , 'C', 'RELACIONAR_DOCUMENTOS', 'RELACIONAR DOCUMENTOS', 'REQUISITO', true, 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple, cpvd_origencategoria) 
	VALUES('PROP_153' , 'C', 'RETIRAR_DOCUMENTOS', 'RETIRAR DOCUMENTOS', 'REQUISITO', true, 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_154' , 'C', 'LLENAR AL GUARDAR', 'AUTOLOAD_SAVE', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_155' , 'L', 'OCULTAR GUARDAR', 'PLANTILLA_OCULTAR_GUARDAR', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_textoculto, cpvd_origencategoria, bpvd_solicitamotivo) 
	VALUES('PROP_156' , 'A', 'ITERACION_SQL', 'ITERACION_SQL', 'REQUISITO', true, 'R', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_157' , 'C', 'MULTIPLES ADJUNTOS', 'MULTIPLE_FILE', 'REQUISITO', 'A');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_158' , 'C', 'CAMPO UNICO', 'UNIQUE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple, bpvd_solicitamotivo, bpvd_textoculto) 
	VALUES('PROP_159' , 'T', 'GENERA_DOCUMENTO_FUNCION_SQL', 'FUNCION PARA GENERAR UN CAMPO EN DOCUMENTO', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_160' , 'L', 'CAMPO RENDER ESPECIAL', 'PLANTILLA_RENDER_ESPECIAL_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_161' , 'L', 'TRANSFERIR', 'PERMISO_PLANTILLA_TRANSFERIR', 'REQUISITO',true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_162' , 'C', 'CROQUIS FUENTE', 'DISPONIBILIDAD_CROQUIS', 'REQUISITO', 'U');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_propiedadboolean) 
	VALUES('PROP_163' , 'C', 'FIRMA', 'ARCHIVO_FIRMA', 'REQUISITO', 'A', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_164' , 'C', 'CROQUIS DISPONIBILIDAD', 'DISPONIBILIDAD_FUNCION_SQL', 'REQUISITO', 'U',true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_165' , 'C', 'AUTOLOAD', 'AUTOLOAD', 'REQUISITO', 'J', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_166' , 'C', 'CUENTA REGRESICA', 'FECHA_TIMER_BACK', 'REQUISITO', 'F', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_167' , 'C', 'ESCOGER VARIOS', 'MULTIPLE_SELECCION', 'REQUISITO', 'U', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_168' , 'C', 'ORIENTACION', 'VALIDATE_ORIENTATION', 'REQUISITO', 'U');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_169' , 'L', 'API', 'API', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, bpvd_solicitamotivo, bpvd_textoculto) 
	VALUES('PROP_170' , 'W', 'API_HEADER', 'API_HEADER', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, bpvd_solicitamotivo, bpvd_textoculto) 
	VALUES('PROP_171' , 'W', 'API_NEW_DOCUMENT', 'API_NEW_DOCUMENT', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean ) 
	VALUES('PROP_173' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO FORMULARIO', 'API_CODE_DIRECT', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_multiple) 
	VALUES('PROP_174' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO REFERENCIADO', 'API_CODE_REFERENCE', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_175' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO ESPECIAL', 'API_CODE_REFERENCE', 'REQUISITO', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_177' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO GENERA ACCION', 'API_CODE_MODIFICADOR', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_178' , 'L', 'CAMPO EVIDENCIA', 'CAMPO_EVIDENCIA', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_179' , 'R', 'OCULTAR MENSAJE LICENCIA', 'OCULTAR_MENSAJE_LICENCIA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_180' , 'O', 'REITAR LA LECTURA DE NOTIFICACIONES AL ABRIR EL SISTEMA', 'FORCE_NOTIFICATION', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_181' , 'C', 'LEER CODIGO QR', 'READ_QR', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_solicitamotivo, bpvd_piderol) 
	VALUES('PROP_182' , 'O', 'TABLERO DE CONTROL', 'TABLERO_CONTROL_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_183' , 'P', 'LISTABLE EN MENU', 'PERMISO_PLANTILLA_LISTAR_MENU_PROCESO', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo) 
	VALUES('PROP_184' , 'L', 'PRODUCTO_CAMPO_TOTAL', 'PRODUCTO CAMPO TOTAL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_pidefechas, bpvd_solicitamotivo, bpvd_pideusuario)
	VALUES('PROP_185' , 'L', 'PERIODO LIMPIEZA A HISTORICO', 'PERIODO_LIMPIEZA_HISTORICO', 'REQUISITO', true, true, true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_186' , 'C', 'ALERTAR AL SELECCIONAR', 'ALERTAR_CAMPO_PROCESO', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple) 
	VALUES('PROP_187' , 'E', 'FUNCION_SQL_VALIDAR', 'FUNCION_SQL_VALIDAR', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_188' , 'C', 'CAMPO INVISIBLE U OCULTO', 'INVISIBLE', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_pidefechas, bpvd_solicitamotivo, bpvd_pideusuario, bpvd_pidetiempobloqueo) 
	VALUES('PROP_189' , 'L', 'TEMPORIZADOR', 'TEMPORIZADOR', 'REQUISITO', true, true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_190' , 'L', 'PLANTILLA_INSTRUCCION_CREAR', 'PLANTILLA_INSTRUCCION_CREAR', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, bpvd_textoculto) 
	VALUES('PROP_191' , 'W', 'API_VALIDATION', 'API_VALIDATION', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_192' , 'W', 'API_EXTRACTION', 'API_EXTRACTION', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_193' , 'A', 'API', 'API', 'REQUISITO', 'P');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo) 
	VALUES('PROP_194' , 'W', 'API_MAX_TRY', 'API MAXIMO NUMERO DE INTENTOS', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo) 
	VALUES('PROP_195' , 'W', 'API_AUTHENTICATION', 'API EJECUTAR PARA AUTENTICAR', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_196' , 'C', 'VISIBLE CAMPO SEGUN VALOR DEPENDIENTE', 'VISIBLE_VALOR_DEPENDIENTE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_197' , 'L', 'PLANTILLA CARGA MASIVA MULTIPLE', 'PLANTILLA_CARGA_MASIVA_MULTIPLE', 'PERMISOS');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_textoculto) 
	VALUES('PROP_198' , 'W', 'API ASYNCRONO AL FINALIZAR', 'API_ASYNCHRONOUS', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_199' , 'C', 'ANULAR MOVIMIENTO', 'CUENTA_ANULAR_MOVIMIENTO', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_200' , 'C', 'DETALLE OCULTAR UNIDADES NOMBRE CANTIDAD', 'DETALLE_OCULTAR_UNIDADES_NOMBRE_CANTIDAD', 'REQUISITO', 'J', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_201' , 'C', 'NUMERO MAXIMO', 'NUMERO_MAXIMO', 'REQUISITO', 'N', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple, bpvd_textoculto) 
	VALUES('PROP_202' , 'T', 'GENERA_DOCUMENTO_TEXTO', 'GENERAR UN CAMPO CON UN TEXTO', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_203' , 'C', 'NUMERO MINIMO', 'NUMERO_MINIMO', 'REQUISITO', 'N', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_204' , 'W', 'API_EXTRACTION_TO_BASE_64', 'API_EXTRACTION_TO_BASE_64', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_205' , 'E', 'REPORTE VISIBLE EN EL ESTADO', 'REP_VISIBLE_STATE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_206' , 'E', 'IMPRESION UNICA DEL REPORTE', 'REP_PRINT_ONE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_207' , 'L', 'CORREO ROL', 'CORREO_ROL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_208' , 'L', 'CELULAR ROL', 'CELULAR_ROL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_209' , 'C', 'PERMITIR LINKS DIRECTAMENTE', 'ARCHIVO_URL_USUARIO', 'REQUISITO', 'A', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_210' , 'C', 'LINKS EXTERNO', 'LINK_EXTERNO', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, cpvd_origencategoria) 
	VALUES('PROP_211' , 'A', 'API_ITERATION_ONE_EXECUTION', 'API_ITERATION_ONE_EXECUTION', 'REQUISITO', true, 'P');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_212' , 'E', 'IMAGEN EN REPORTE', 'REPORTE_IMAGEN', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo)
	VALUES('PROP_213' , 'T', 'GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR', 'CAMPO PARA GENERAR DOCUMENTO DEL FORM GENERADOR', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo)
	VALUES('PROP_214' , 'T', 'GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE', 'CAMPO PARA GENERAR DOCUMENTO DEL FORM EXPEDIENTE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_215' , 'C', 'TEXTO_LONGITUD', 'TEXTO_LONGITUD', 'REQUISITO', 'T', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_216' , 'L', 'PLANTILLA_HISTORIAL_ACTIVO', 'PLANTILLA_HISTORIAL_ACTIVO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_217' , 'W', 'CONNECT TIMEOUT', 'API_CONNECT_TIMEOUT', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_218' , 'W', 'READ TIMEOUT', 'API_READ_TIMEOUT', 'REQUISITO');
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
	VALUES('PROP_219' , 'O', 'API-KEY', 'API_KEY', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_multiple)
	VALUES('PROP_220' , 'O', 'COVERAGE_IMAGE', 'COVERAGE_IMAGE', 'REQUISITO', true, true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_221' , 'C', 'PRODUCTO APLICADO UBICACION CROQUIS', 'PRODUCTO_PUESTO', 'REQUISITO', 'U');

-- La borro y al cambio por la propiedad
--INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean) 
--	VALUES('PROP_222' , 'L', 'INVENTARIO_OBLIGATORIO', 'CREAR INVENTARIO EN BODEGA DEL PRODUCTO', 'REQUISITO', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_223' , 'W', 'API - HORAS EN QUE EL API APLAZA', 'API_SCHEDULE_TIME_BLOCK', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_multiple) 
	VALUES('PROP_224' , 'W', 'API - VALIDAR ANTES DE EJECUTAR', 'FUNCION_SQL_PREVALIDATE_API', 'REQUISITO', true,  true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_225' , 'C', 'RELACIONAR DOCUMENTO Y CAMPO', 'INFORMATIVE_DATA', 'REQUISITO', 'V');
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_226' , 'L', 'INVENTARIO_OPCIONAL', 'OMITIR CREAR INVENTARIO EN BODEGA DEL PRODUCTO', 'REQUISITO', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_227' , 'C', 'FORMATO', 'FORMATO', 'REQUISITO', 'N');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_228' , 'C', 'LEER CODIGO QR', 'READ_QR', 'REQUISITO', 'T', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_229' , 'C', 'BINARIO_PREGUNTA', 'BINARIO_PREGUNTA', 'REQUISITO', 'I', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_230' , 'E', 'OMITIR LA IMPRESION DEL REPORTE', 'REP_EXCLUDE_STORAGE_FILE', 'REQUISITO', true);
	INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_231' , 'C', 'ACTUALIZAR CAMPO INFORMATIVO', 'UPDATE_INFORMATIVE_FIELD', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_232' , 'C', 'GUARDAR AL SELECCIONAR', 'SAVE_TO_SELECT', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_233' , 'C', 'BUSQUEDA SIN TEXTO', 'BUSQUEDA_SIN_TEXTO', 'REQUISITO', 'J', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_234' , 'E', 'OCULTAR REPORTE', 'OCULTAR_REPORTE', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_235' , 'W', 'API_EXTRACTION_NO_ERROR', 'API_EXTRACTION_NO_ERROR', 'REQUISITO', true);	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_236' , 'E', 'IMPRESION INMEDIATAMENTE', 'REP_AUTOPRINT', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_237' , 'W', 'CODIGOS PARA REEMPLAZAR', 'API_CODE_REPLACE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_multiple) 
	VALUES('PROP_238' , 'W', 'REEMPLAZAR CODIGO REFERENCIADO TIPO LISTA', 'API_CODE_REFERENCE_LIST', 'REQUISITO', true, true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_239' , 'C', 'MONITOREAR', 'PLANTILLA_MONITOR', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_240' , 'T', 'MENSAJE ADJUNTO URL', 'MENSAJE_ADJUNTO_URL', 'REQUISITO', true);	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_241' , 'L', 'MENSAJE ADJUNTO URL', 'MENSAJE_ADJUNTO_URL', 'REQUISITO', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_242' , 'L', 'PLANTILLA REGISTRA DIFERENCIAS', 'PLANTILLA_DIFERENCIAS', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_243' , 'C', 'CAMPO EN PLANTILLA DE DIFERENCIAS', 'CAMPO_DIFERENCIAS', 'REQUISITO');
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo)
	VALUES('PROP_244' , 'T', 'GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION', 'CAMPO PARA GENERAR DOCUMENTO DEL RESULTADO DE LA ITERACION', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_245' , 'P', 'MENSAJE ADJUNTO URL', 'MENSAJE_ADJUNTO_URL', 'REQUISITO', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_246' , 'E', 'TIPO REPORTE', 'REP_TYPE_EXPORT', 'REQUISITO', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_247' , 'W', 'API ESTANDAR CODIFICACION', 'API_ENCODE_STANDAR', 'REQUISITO', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_248' , 'C', 'NUMERO FUNCION SIEMPRE CALCULAR AL GUARDAR', 'FUNCION_NUMBER_ALL_CALCULATE_SAVE', 'REQUISITO', 'N', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, cpvd_origencategoria) 
	VALUES('PROP_249' , 'A', 'PARAMETROS PARA EL API', 'API_PARAMETER', 'REQUISITO', true, 'P');
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_250' , 'E', 'QUERY REPORTE', 'REPORT_QUERY', 'REQUISITO', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_251' , 'W', 'API CORREO NOTIFICACION ERRORES', 'API_MAIL_NOTIFICATION', 'REQUISITO', true);
	


INSERT INTO usuario_usrp(cusr_llave, cusr_identificacion, cusr_nombre, cusr_imagen, cusr_correo) VALUES ('SYSTEM', 'SYSTEM', 'SYSTEM', 'https://fs.softwareparati.com/imagenes/avatar.png', 'jhonatan.garcia@colombiansofture.com');
INSERT INTO usuario_usrp(cusr_llave, cusr_identificacion, cusr_nombre, cusr_imagen, cusr_correo) VALUES ('PROCESS', 'PROCESS', 'PROCESS', 'https://fs.softwareparati.com/avatar.png', 'jhonatan.garcia@colombiansofture.com');
INSERT INTO usuarioautenticacion_uaup(cuau_llave, cuau_usuario, cuau_sesion, cuau_clave)VALUES ('SYSTEM', 'SYSTEM', '1', '1');
--Modulos
INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_url, cmod_estado)
    VALUES ('AdministracionLogisticpymes', 'Administracion', 'com.softure.logisticpymes.view.ui.UIAdministracion', 'A');
-- INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('Inventarios', 'INVENTARIOS', 'com.softure.logisticpymes.view.ui.UIInventario', 'A');
--INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('Productos', 'PRODUCTOS', 'Control de los productos de una empresa', 'com.softure.logisticpymes.view.ui.UIProducto', 'A');
-- INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('UITarifario', 'UITarifario', 'UITarifario', 'com.softure.logisticpymes.view.ui.UITarifario', 'A');
-- INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('UIVotacion', 'UIVotacion', 'PLantilla para entrar a votar', 'com.softure.logisticpymes.view.ui.UIVotacion', 'A');
-- INSERT INTO modulo_modp(cmod_llave, cmod_descripcion, cmod_nombre, cmod_url, cmod_estado)
--     VALUES ('UIVotantes', 'UIVotantes', 'Configurar encuestas', 'com.softure.logisticpymes.view.ui.UIVotantes', 'A');

--
insert into pg_description (objoid, classoid, objsubid, description) select oid, 1259, 0, '2023-07-13' from pg_class where relname = 'usuario_usrp';
insert into pg_description (objoid, classoid, objsubid, description) select oid, 1259, 0, to_char(CURRENT_TIMESTAMP + CAST('1 Month' AS INTERVAL),'yyyy-MM-dd') from pg_class where relname = 'usuarioautenticacion_uaup';

INSERT INTO categoriaproducto_cprp (ccpr_llave, ccpr_nombre) VALUES('GENERAL', 'GENERAL');
INSERT INTO tarifario_trfp(ctrf_llave, ctrf_nombre)VALUES ('GENERAL', 'GENERAL');
INSERT INTO cambio_cmbp (ccmb_llave, ccmb_nombre, ccmb_motivo, dcmb_fecha, dcmb_fechaaplicacion) 
	VALUES('SC-1', 'SC-1', 'CONFIGURACION INICIAL DEL SISTEMA', now(), now());

INSERT INTO proceso_prcp ( cprc_llave, cprc_nombre, cprc_codigo, cprc_objetivo, nprc_prioridad, cprc_tipo, cprc_imagen)
	VALUES ('SOPORTE', 'PROCESOS DE SOPORTE', 'SOPORTE', 'Agrupar los procesos que permiten realizar la mision de la empresa',100, 'A', 'https://fs.softwareaparati.com/imagenes/modulo.png');

INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_codigo, cdpl_nombre, cdpl_imagen, cdpl_objetivo, cdpl_proceso)
    VALUES ('ADMINISTRADOR', 'ADM', 'SOPORTE SOFTWARE PARA TI', 'https://file.softwareparati.com/softure/2019/12/28/f580bc7ca449440f8e9b581b252790c9.png', 'Rol inicial para configurar el aplicativo', 'SOPORTE');

INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, ndpc_orden, cdpc_nombre, cdpc_codigo, cdpc_formato, cdpc_objetivo)
    VALUES ('ADMINISTRADOR-ID', 'ADMINISTRADOR',  1, 'ID', 'ID', 'T', 'Contiene el numero de cedula del administrador');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
	VALUES( 'ADMINISTRADOR-ID_2', 'C', 'PROP_105', 'ADMINISTRADOR-ID', '1', 'Se tiene permisos para modificar el campo', now(), now(), 'SC-1'); 

INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, ndpc_orden, cdpc_nombre, cdpc_codigo, cdpc_formato, cdpc_objetivo)
    VALUES ('ADMINISTRADOR-NM', 'ADMINISTRADOR', 2, 'NOMBRE', 'NOMBRE', 'T', 'Contiene el nombre completo del administrador');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
	VALUES( 'ADMINISTRADOR-NM_2', 'C', 'PROP_105', 'ADMINISTRADOR-NM', '1', 'Se tiene permisos para modificar el campo', now(), now(), 'SC-1'); 

insert into documentoplantillacaracteristica_dpcp (cdpc_llave,cdpc_plantilla,cdpc_estado,ndpc_orden,cdpc_imagen,cdpc_nombre,cdpc_codigo,cdpc_formato,cdpc_objetivo) 
	values ('4e3014ec945e4c718dc50481220fcf80','ADMINISTRADOR','A',7,NULL,'CORREO','CORREO','T','.');
insert into propiedad_ppdp (cppd_llave,cppd_campo,cppd_valor,cppd_texto,cppd_estado,cppd_propiedadvalor,dppd_fechadefinicion,dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo) 
	values ('49e5294011124e138dd7c661621866de','4e3014ec945e4c718dc50481220fcf80','E',NULL,'A','PROP_75',now(),now(), 'SC-1','C');
	
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto,  dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo) 
	VALUES( 'DESC_ADMINISTRADOR' , 'PROP_44', 'ADMINISTRADOR', 'ADMINISTRADOR-NM', 'NOMBRE', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'CONSE_ADMINISTRADOR' , 'PROP_48', 'ADMINISTRADOR', 'ADMINISTRADOR-ID', 'ID', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'ORDE_ADMINISTRADOR' , 'PROP_51', 'ADMINISTRADOR', 'N', 'NOMBRE', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'ADMINISTRADOR_TIPO_ROL' , 'PROP_141', 'ADMINISTRADOR', '1', now(), now(), 'SC-1', 'L');
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
	VALUES( 'CORREO_ADMINISTRADOR' , 'PROP_207', 'ADMINISTRADOR', '4e3014ec945e4c718dc50481220fcf80', 'CORREO', now(), now(), 'SC-1', 'L');
		
INSERT INTO rolacceso_racp(crac_llave, brac_permisoscompletos, crac_plantilla)
    VALUES ('ADMINISTRADOR',  true, 'ADMINISTRADOR');

INSERT INTO modulocontratado_mdcp(cmdc_llave, cmdc_modulo, cmdc_nombre, cmdc_imagen)
    VALUES ('ADMINISTRACION', 'AdministracionLogisticpymes', 'ADMINISTRACION', 'https://fs.softwareaparati.com/imagenes/modulo.png');
    
INSERT INTO permiso_perp(cper_llave, cper_rolacceso, cper_modulo)
    VALUES ('ADMINISTRADOR', 'ADMINISTRADOR', 'ADMINISTRACION');
    
INSERT INTO documentotransaccion_trap(ctra_llave, dtra_fecha, ctra_usuario)
	VALUES('SYSTEM', now(), 'SYSTEM');
	
INSERT INTO pedidoventa_pdvp(cpdv_llave, dpdv_fecharegistro, dpdv_fecha, cpdv_nombre, cpdv_plantilla, cpdv_funcionario, cpdv_transaccion )
    VALUES ('SYSTEM', current_timestamp, current_timestamp, 'SYSTEM', 'ADMINISTRADOR', 'SYSTEM', 'SYSTEM');

INSERT INTO pedidoventacaracteristica_pvcp(cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, cpvc_transaccionregistro)
    VALUES ('SYSTEM-ID', 'SYSTEM', 'ADMINISTRADOR-ID', 'SYSTEM', 'SYSTEM');

INSERT INTO pedidoventacaracteristica_pvcp(cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, cpvc_transaccionregistro)
    VALUES ('SYSTEM-NM', 'SYSTEM', 'ADMINISTRADOR-NM', 'SISTEMA', 'SYSTEM');
        
INSERT INTO usuariorol_erlp(cerl_llave, cerl_usuario, cerl_rolacceso, cerl_documento, derl_fechainicial)
    VALUES ('ADMINISTRADOR', 'SYSTEM', 'ADMINISTRADOR', 'SYSTEM', current_timestamp);
    
INSERT INTO organizacion_orgp (corg_llave, corg_nombre, corg_imagen, corg_slogan,  corg_mensajeingreso, corg_codigo, corg_usuariosystem) 
VALUES('ORG1', 'SOFTWARE PARA TI.COM', 'https://fs.softwareparati.com/imagenes/fondo.png', 'Unificar, Simplificar, Optimizar', 'INGRESA TUS DATOS', 'SW42', 'PROCESS');

----------
-- INSERTS for public.mensajeplantillacorreo_mplp
-- -------------------

INSERT INTO servidor_serp (cser_llave, cser_nombre, cser_url, cser_usuario, cser_clave,  cser_tipo, nser_orden, cser_estado)
	select 'smtp.gmail.com', 'smtp.gmail.com', 'smtp.gmail.com',  'notificaciones@colombiansofture.com', '$ofture123',  'E', 1, 'A'
	WHERE NOT EXISTS (SELECT 1 FROM servidor_serp WHERE cser_llave='smtp.gmail.com');
	

INSERT INTO servidor_serp (cser_llave, cser_nombre, cser_url, cser_usuario, cser_clave,  cser_tipo, nser_orden, cser_estado, cser_puerto, cser_base, cser_urlconexion)
	select 'ftp', 'FILE SERVER', '192.168.2.121',  'softure', 'softure123',  'F', 2, 'A', '21', 'pruebas', 'https://fs3.softwareparati.com/'
	WHERE NOT EXISTS (SELECT 1 FROM servidor_serp WHERE cser_llave='ftp');



