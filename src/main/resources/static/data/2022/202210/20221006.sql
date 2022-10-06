COMMENT ON TABLE usuario_usrp IS '2022-10-06';


INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20221006',  'SC_20221006',  'Filtro de fechas para los reportes',  now());

--Propiedaqd de filtrar reportes
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, 
	dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	cdpl_llave, '1', 'PROP_55', now(), now(), 'SC_20221006', 'L'
from propiedad_ppdp pp
inner join documentoplantilla_dplp dd on (dd.cdpl_llave = pp.cppd_campo) 
left join  propiedad_ppdp pp2 on (pp2.cppd_campo = dd.cdpl_llave and pp2.cppd_estado='A' and pp2.cppd_propiedadvalor= 'PROP_55')
where pp.cppd_propiedadvalor = 'PROP_142'
and pp.cppd_estado = 'A'
and pp2.cppd_llave is null;