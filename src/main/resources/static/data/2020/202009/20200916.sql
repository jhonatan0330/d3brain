COMMENT ON TABLE usuario_usrp IS '2020-09-16';

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20200916',  'SC_20200916',  'Permisos de ver todos los productos y filtrar por nombre',  now());

--VEr todos
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, 
	dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	cdpl_llave, '1', 'PROP_108', now(), now(), 'SC_20200916', 'L'
from documentoplantilla_dplp 
inner join propiedad_ppdp p_prod on (cdpl_llave = p_prod.cppd_campo and p_prod.cppd_propiedadvalor = 'PROP_144' and p_prod.cppd_estado ='A');

--Campos filtro
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, 
	dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	cdpl_llave, '1', 'PROP_94', now(), now(), 'SC_20200916', 'C'
from documentoplantilla_dplp 
inner join propiedad_ppdp on (cdpl_llave = cppd_campo and cppd_propiedadvalor = 'PROP_144' and cppd_estado ='A')
inner join documentoplantillacaracteristica_dpcp on (cdpc_plantilla = cdpl_llave);

--actualizar filtros
update  pedidoventa_pdvp set cpdv_textofiltro = movimiento_descripcion(cpdv_llave) where cpdv_plantilla in (select cdpl_llave
	from documentoplantilla_dplp 
	inner join propiedad_ppdp on (cdpl_llave = cppd_campo and cppd_propiedadvalor = 'PROP_144' and cppd_estado ='A'));
	
update propiedad_ppdp set cppd_valor = replace (cppd_valor, '.replace(".00"', '.replace(".000000"') where cppd_llave in (
	select cppd_llave from reportebase_rpbp 
	inner join propiedad_ppdp on (cppd_campo = crpb_llave and cppd_propiedadvalor = 'PROP_138' and cppd_estado = 'A')
	where cppd_valor like '%.replace(".00"%');

update propiedad_ppdp set cppd_valor = replace (cppd_valor, 'mcue_sobregiro', '0') where cppd_llave in (
	select cppd_llave from reportebase_rpbp 
	inner join propiedad_ppdp on (cppd_campo = crpb_llave and cppd_propiedadvalor = 'PROP_138' and cppd_estado = 'A')
	where cppd_valor like '%mcue_sobregiro%');