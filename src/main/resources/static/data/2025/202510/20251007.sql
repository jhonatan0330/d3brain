COMMENT ON TABLE usuario_usrp IS '2025-10-07';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo,  bpvd_textoculto)
    SELECT 'PROP_299' , 'W', 'API_URL', 'API_URL', 'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_299');
    
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave,  cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo,  bpvd_textoculto)
    SELECT 'PROP_300' , 'W', 'API_TEMPLATE', 'API_TEMPLATE', 'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_300');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo, cpvd_origencategoria)
    SELECT 'PROP_301' , 'A', 'API', 'API', 'REQUISITO', 'E'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_301');
        
INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20251007',  'SC_20251007',  'Cambio de webservices los templates y als url',  now());
	

INSERT INTO public.propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	'PROP_299', 'W', mm.cwbs_llave ,
	mm.cwbs_url , now(), now(), 'SC_20251007'
from webservice_wbsp mm;


INSERT INTO public.propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	'PROP_300', 'W', mm.cwbs_llave ,
	mm.cwbs_template , now(), now(), 'SC_20251007'
from webservice_wbsp mm;

ALTER TABLE public.webservice_wbsp DROP COLUMN cwbs_template;

ALTER TABLE public.webservice_wbsp DROP COLUMN cwbs_url;

ALTER TABLE public.webservice_wbsp ADD cwbs_proceso varchar(32);

update webservice_wbsp  
set cwbs_proceso = (select pp2.cpes_proceso  from propiedad_ppdp pp 
		inner join procesoestado_pesp pp2 on (pp2.cpes_llave = pp.cppd_campo)
	where pp.cppd_valor = cwbs_llave and pp.cppd_estado = 'A' limit 1);
