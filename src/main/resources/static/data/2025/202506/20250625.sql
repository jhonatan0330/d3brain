COMMENT ON TABLE usuario_usrp IS '2025-06-25';

CREATE OR REPLACE VIEW public.campo_documento AS
SELECT
    pvc.cpvc_llave,
    pvc.dpvc_valorfecha,
    pvc.mpvc_valornumero,
    pvc.cpvc_valortext,
    pvc.cpvc_valoropcion,
    pvc.cpvc_valorauxiliar,
    pvc.cpvc_campo,
    dpc.cdpc_codigo AS cdpf_codigo,
    dpc.cdpc_nombre AS cdpf_nombre,
    pvc.cpvc_documento AS cdrc_documento
FROM
    pedidoventacaracteristica_pvcp pvc
JOIN
    documentoplantillacaracteristica_dpcp dpc
    ON pvc.cpvc_campo = dpc.cdpc_llave
WHERE
    pvc.cpvc_estado = 'A';
    
    
CREATE INDEX ix_pedidoventacaracteristica_campoestado ON pedidoventacaracteristica_pvcp(cpvc_campo, cpvc_estado);

CREATE INDEX ix_z_pedidoventacaracteristica_campoestado ON historic.z_pvc_pedidoventacaracteristica(cpvc_campo, cpvc_estado);
