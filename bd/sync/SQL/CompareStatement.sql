WbDataDiff -referenceProfile="New"
           -targetProfile="Older"
           -targetSchema="public"
           -referenceSchema="public"
           -file=cambios_generados_cambiar_nombre_por_sc.sql
           -singleFile=true
           -sqlDateLiterals=ansi
           -excludeTables=actividad_actp, auditoria_audp, bodega_bodp, cargaarchivo_carp, consecutivo_conp, cuenta_cuep, detallepedidoventa_dpvp, 
             detallecaracteristicaproducto_dcpp, 
             deduccionproducto_dprp, documentorelacionexpediente_dexp, documentorelaciongestor_drgp, documentotransaccion_trap, encuesta_encp, 
             encuestagrupo_egrp, encuestaopcionrespuesta_eorp, encuestapregunta_eprp ,encuestarespuesta_ersp, mensaje_msjp, 
             movimiento_movp, pedidoventa_pdvp, pedidoventaajuste_pvap, pedidoventacaracteristica_pvcp, pedidoventadinero_pvdp, plantillaconsecutivo_pcnp,
             procesotransicionautomatica_ptap, producto_prop, productoinventario_pinp,  productoinventariodescuento_pidp, puesto_puep, 
             reporteejecucion_rejp, tarifa_tarp, trazabilidadproductoinventario_tpip, turno_turp,
             usuario_usrp, usuarioautenticacion_uaup, usuariorol_erlp, usuariorolproducto_urpp , usuariosesion_ussp, procesotransicionautomatica_ptap, 
             webserviceejecucion_wsep, z_dex_documentorelacionexpediente, z_drg_documentorelaciongestor, z_pvc_pedidoventacaracteristica, z_pvd_pedidoventadinero,
             z_rej_reporteejecucion
          -ignoreColumns=cdpl_consecutivo,mcue_sobregiro, dcue_fechaconciliacion;

WbSchemaDiff -referenceProfile="New"
             -targetProfile="Older"
             -targetSchema="public"
             -referenceSchema="public"
             -includeProcedures=true
             -file=cambios_esquema.xml
             -styleSheet=wbdiff2pg.xslt
             -xsltOutput=cambios_esquema.sql
