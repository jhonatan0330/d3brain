COMMENT ON TABLE usuario_usrp IS '2020-09-05';

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20200905',  'SC_20200905',  'Permisos a productos',  now());

--Permisos de ver listable
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, 
	dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo, cppd_rol)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	cdpl_llave, '1', 'PROP_107', now(), now(), 'SC_20200905', 'L', crac_llave
from rolacceso_racp, permiso_perp , modulocontratado_mdcp,
	documentoplantilla_dplp, propiedad_ppdp, categoriaproducto_cprp
where crac_llave = cper_rolacceso and cper_estado = 'A' and brac_permisoscompletos = false
	and cmdc_modulo = 'AdministracionLogisticpymes' and cppd_valor =  ccpr_llave and ccpr_estado = 'A' 
	and cdpl_llave = cppd_campo and cppd_estado = 'A' and cppd_propiedadvalor = 'PROP_144';

--Permisos de crear
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, 
	dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo, cppd_rol)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	cdpl_llave, '1', 'PROP_77', now(), now(), 'SC_20200905', 'L', crac_llave
from rolacceso_racp, permiso_perp , modulocontratado_mdcp,
	documentoplantilla_dplp, propiedad_ppdp, categoriaproducto_cprp
where crac_llave = cper_rolacceso and cper_estado = 'A' and brac_permisoscompletos = false
	and cmdc_modulo = 'AdministracionLogisticpymes' and cppd_valor =  ccpr_llave and ccpr_estado = 'A' 
	and cdpl_llave = cppd_campo and cppd_estado = 'A' and cppd_propiedadvalor = 'PROP_144';

--Permisos de modificar
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, 
	dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo, cppd_rol)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	cdpl_llave, '1', 'PROP_78', now(), now(), 'SC_20200905', 'L', crac_llave
from rolacceso_racp, permiso_perp , modulocontratado_mdcp,
	documentoplantilla_dplp, propiedad_ppdp, categoriaproducto_cprp
where crac_llave = cper_rolacceso and cper_estado = 'A' and brac_permisoscompletos = false
	and cmdc_modulo = 'AdministracionLogisticpymes' and cppd_valor =  ccpr_llave and ccpr_estado = 'A' 
	and cdpl_llave = cppd_campo and cppd_estado = 'A' and cppd_propiedadvalor = 'PROP_144';
--copiar imagen
update documentoplantilla_dplp set cdpl_imagen = (SELECT ccpr_imagen
	FROM propiedad_ppdp, categoriaproducto_cprp 
	where cdpl_llave = cppd_campo and cppd_estado = 'A' and cppd_valor = ccpr_llave and ccpr_estado = 'A'
	and cppd_propiedadvalor = 'PROP_144' and ccpr_imagen is not null )
where cdpl_llave in (SELECT cdpl_llave 
	FROM documentoplantilla_dplp, propiedad_ppdp, categoriaproducto_cprp 
	where cdpl_llave = cppd_campo and cppd_estado = 'A'
	and cppd_propiedadvalor = 'PROP_144' and cppd_valor =  ccpr_llave and ccpr_estado = 'A' 
	and ccpr_imagen is not null);
