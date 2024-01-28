COMMENT ON TABLE usuario_usrp IS '2023-09-11';

update propiedadvalordefinido_pvdp SET bpvd_propiedadboolean = true where cpvd_llave = 'PROP_52';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean)
	select
	'PROP_236' , 'E', 'IMPRESION INMEDIATAMENTE', 'REP_AUTOPRINT', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_236'); 
	
INSERT INTO propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), 'PROP_236', 'E', rr.crpb_llave, '1', now(), now(), pp.cppd_cambiocreacion from reportebase_rpbp rr 
	inner join documentoplantilla_dplp dd ON (rr.crpb_plantilla =dd.cdpl_llave)
	inner join propiedad_ppdp pp on (pp.cppd_campo = dd.cdpl_llave and pp.cppd_estado = 'A' and pp.cppd_propiedadvalor = 'PROP_142');
	