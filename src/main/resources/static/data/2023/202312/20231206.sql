COMMENT ON TABLE usuario_usrp IS '2023-12-06';

ALTER TABLE documentoplantilla_dplp ALTER COLUMN cdpl_codigo TYPE varchar(32);

DROP VIEW campo_documento;

ALTER TABLE documentoplantillacaracteristica_dpcp ALTER COLUMN cdpc_codigo TYPE varchar(32);

CREATE OR REPLACE VIEW campo_documento
AS SELECT pvc.cpvc_llave,
    pvc.dpvc_valorfecha,
    pvc.mpvc_valornumero,
    pvc.cpvc_valortext,
    pvc.cpvc_valoropcion,
    pvc.cpvc_valorauxiliar,
    pvc.cpvc_campo,
    dpc.cdpc_codigo AS cdpf_codigo,
    dpc.cdpc_nombre AS cdpf_nombre,
    pvc.cpvc_documento AS cdrc_documento
   FROM pedidoventacaracteristica_pvcp pvc,
    documentoplantillacaracteristica_dpcp dpc
  WHERE pvc.cpvc_campo::text = dpc.cdpc_llave::text AND pvc.cpvc_estado::text = 'A'::text;