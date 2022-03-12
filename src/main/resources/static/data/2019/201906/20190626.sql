
COMMENT ON TABLE usuario_usrp IS '2019-06-26';

update pedidoventa_pdvp set cpdv_textofiltro = (SELECT array_to_string(ARRAY( SELECT upper(pvc.cpvc_valortext::text)
                   FROM pedidoventacaracteristica_pvcp pvc,documentoplantillacaracteristica_dpcp dpc_a
                  WHERE pvc.cpvc_campo = dpc_a.cdpc_llave AND pvc.cpvc_documento = cpdv_llave AND pvc.cpvc_estado = 'A' 
                  AND dpc_a.bdpc_filtro = true AND dpc_a.cdpc_estado = 'A'), ',', ''));

delete from reportebase_rpbp where crpb_codigo = 'PER001';