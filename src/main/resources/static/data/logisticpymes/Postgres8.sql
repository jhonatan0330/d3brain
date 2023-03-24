 
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
        ctra_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_documentotransaccion_trap PRIMARY KEY (ctra_llave)
    );
 
CREATE TABLE procesoestado_pesp(
        cpes_llave character varying(32) NOT NULL,
        cpes_tipo character varying(1) NOT NULL,
        cpes_estadodocumento character varying(1) NOT NULL,
        npes_avance int NOT NULL DEFAULT 0,
        cpes_nombre character varying(100) NOT NULL,
        cpes_codigo character varying(50),
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
        cdpl_codigo character varying(16) NOT NULL UNIQUE,
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

	
CREATE TABLE task_task_tsk (
        ctsk_llave character varying(32) NOT NULL,
        ctsk_user character varying(32)  NOT NULL,
        ctsk_title character varying(200) NOT NULL,
        ctsk_notes character varying(4000),
        dtsk_completed timestamp with time zone,
        dtsk_duedate timestamp with time zone,
        ntsk_priority int4 NOT NULL DEFAULT 0,
        ntsk_order int4 NOT NULL DEFAULT 0,
        dtsk_createdAt timestamp with time zone  NOT NULL,
        dtsk_updatedAt timestamp with time zone,
        ctsk_state character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_task_task_tsk PRIMARY KEY (ctsk_llave)
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