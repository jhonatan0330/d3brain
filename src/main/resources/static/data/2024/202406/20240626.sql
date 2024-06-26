COMMENT ON TABLE usuario_usrp IS '2024-06-26';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean)
	SELECT 'PROP_261' , 'L', 'HISTORICO_VISTA', 'VISTA ANTERIOR DE TRAZABILIDAD', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_261');

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20240627',  'SC_20240627',  'Actualizacion de trazabilidad',  now());
	
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
	select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-','')
	,'PROP_261', 'L', cdpl_llave , '1', now(), now()
	,'SC_20240627'
	from pedidoventa_pdvp, documentoplantilla_dplp
	where cpdv_plantilla =cdpl_llave
	group by cdpl_llave, cdpl_nombre
	having count(*) > 1000;