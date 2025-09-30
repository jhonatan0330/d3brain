COMMENT ON TABLE usuario_usrp IS '2025-09-24';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol, bpvd_pideusuario)
    SELECT 'PROP_296' , 'O', 'PERMISO DE ADMINISTRADOR', 'APP_ADMIN', 'REQUISITO', true, true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_296');
    
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo,  bpvd_piderol, bpvd_pideusuario)
    SELECT 'PROP_297' , 'O', 'MODULOS GENERALES', 'APP_MODULES', 'REQUISITO', true,  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_297');
    
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_piderol, bpvd_pideusuario)
    SELECT 'PROP_298' , 'O', 'TIEMPO DE SESION ACTIVA', 'APP_SESSION_TIME', 'REQUISITO', true,  true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_298');


INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20250924',  'SC_20250924',  'Cambio el esquema de permisos y modulos',  now());
	
INSERT INTO public.propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion,  cppd_rol)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	'PROP_296', 'O', (select oo.corg_llave  from organizacion_orgp oo where oo.corg_estado = 'A' and oo.corg_principal is null),
	'1', now(), now(), 'SC_20250924', pp.cper_rolacceso 
from modulo_modp mm 
	inner join permiso_perp pp on (pp.cper_modulo = mm.cmod_llave and pp.cper_estado = 'A')
where mm.cmod_llave = 'AdministracionLogisticpymes';

INSERT INTO public.propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion,  cppd_rol)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	'PROP_297', 'O', (select oo.corg_llave  from organizacion_orgp oo where oo.corg_estado = 'A' and oo.corg_principal is null),
	replace(cmod_url, '/', ''), cmod_nombre,  now(), now(), 'SC_20250924', pp.cper_rolacceso 
from modulo_modp mm 
	inner join permiso_perp pp on (pp.cper_modulo = mm.cmod_llave and pp.cper_estado = 'A')
where mm.cmod_llave != 'AdministracionLogisticpymes';

INSERT INTO public.propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion,  cppd_rol)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	'PROP_298', 'O', (select oo.corg_llave  from organizacion_orgp oo where oo.corg_estado = 'A' and oo.corg_principal is null),
	rr.nrac_minutossesion , now(), now(), 'SC_20250924', rr.crac_llave  
from rolacceso_racp rr where rr.nrac_minutossesion != 0 and crac_estado = 'A';

ALTER TABLE permiso_perp RENAME TO permiso_perp_old;

ALTER TABLE modulo_modp  RENAME TO modulo_modp_old;

