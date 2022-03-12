
COMMENT ON TABLE usuario_usrp IS '2018-02-27';


COMMENT ON TABLE usuariosesion_ussp IS '2018.02.27.17';

DROP VIEW public.campo_documento;

ALTER TABLE pedidoventacaracteristica_pvcp
	ALTER COLUMN mpvc_valornumero TYPE numeric(18,6) /* TYPE change - table: pedidoventacaracteristica_pvcp original: numeric(16,2) new: numeric(18,6) */;

ALTER TABLE encuesta_encp
	DROP COLUMN benc_creamodulo;


CREATE OR REPLACE VIEW public.campo_documento AS 
 SELECT pvc.cpvc_llave,
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

ALTER TABLE public.campo_documento
  OWNER TO postgres;

ALTER TABLE documentoplantillacosto_dpcp
	ADD COLUMN cdpc_campoafectasaldo character varying(32);

update documentoplantillacosto_dpcp set cdpc_campoafectasaldo = cdpc_valortotal 
where cdpc_valortotal is not null and cdpc_valortotal in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato  = 'Z' and cdpc_estado = 'A');

