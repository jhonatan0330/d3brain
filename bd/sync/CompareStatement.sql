WbDataDiff -referenceProfile="New"
           -targetProfile="Older"
           -file=cambios_generados_cambiar_nombre_por_sc.sql
           -includeDelete=true
           -singleFile=true
           -sqlDateLiterals=ansi
           -excludeTables=actividad_actp, auditoria_audp, bodega_bodp, consecutivo_conp, cuenta_cuep, detallepedidoventa_dpvp, detallecaracteristicaproducto_dcpp, 
           deduccionproducto_dprp, documentorelacionexpediente_dexp, documentorelaciongestor_drgp, documentotransaccion_trap, encuestarespuesta_ersp, mensaje_msjp, 
           movimiento_movp, pedidoventa_pdvp, pedidoventaajuste_pvap, pedidoventacaracteristica_pvcp, pedidoventadinero_pvdp, plantillaconsecutivo_pcnp,
           procesotransicionautomatica_ptap, producto_prop, productoinventario_pinp,  productoinventariodescuento_pidp, puesto_puep, tarifa_tarp, 
           trazabilidadproductoinventario_tpip, turno_turp,
           usuario_usrp, usuarioautenticacion_uaup, usuariorol_erlp, usuariorolproducto_urpp , usuariosesion_ussp, procesotransicionautomatica_ptap, reporteejecucion_rejp,
           webserviceejecucion_wsep, z_dex_documentorelacionexpediente
          -ignoreColumns=cdpl_consecutivo,mcue_sobregiro, dcue_fechaconciliacion;

WbSchemaDiff -referenceProfile="New"
             -targetProfile="Older"
             -includeProcedures=true
             -file=cambios_esquema.xml
             -styleSheet=wbdiff2pg.xslt
             -xsltOutput=cambios_esquema.sql
