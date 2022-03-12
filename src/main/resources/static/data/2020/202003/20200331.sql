COMMENT ON TABLE usuario_usrp IS '2020-03-31';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_128' , 'C', 'REQUERIMIENTO_CAMPO', 'REQUERIMIENTO_CAMPO', 'www.softwareparati.com', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_126' , 'T', 'REQUERIMIENTO_TRANSICION', 'REQUERIMIENTO_TRANSICION', 'www.softwareparati.com', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_130' , 'P', 'REQUERIMIENTO_PROCESO', 'REQUERIMIENTO PROCESO', 'www.softwareparati.com', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_131' , 'L', 'REQUERIMIENTO_PLANTILLA', 'REQUERIMIENTO PLANTILLA', 'www.softwareparati.com', 'REQUISITO', true);
	
UPDATE propiedadvalordefinido_pvdp SET bpvd_necesitadesarrollo = TRUE WHERE cpvd_llave IN ('PROP_102', 'PROP_86', 'PROP_88');

CREATE OR REPLACE VIEW public.vi_valores
AS SELECT pedidoventadinero_pvdp.cpvd_documento AS vi_vlr_documento,
    pedidoventadinero_pvdp.mpvd_valorsubtotal AS vi_vlr_subtotal,
    pedidoventadinero_pvdp.mpvd_valortotal AS vi_vlr_total,
    pedidoventadinero_pvdp.mpvd_saldo AS vi_vlr_saldo,
    pedidoventadinero_pvdp.dpvd_fecha AS vi_vlr_fecha
   FROM pedidoventadinero_pvdp
  WHERE pedidoventadinero_pvdp.cpvd_estado::text = 'A'::text;
