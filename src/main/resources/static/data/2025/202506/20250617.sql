COMMENT ON TABLE usuario_usrp IS '2025-06-17';

update webservice_wbsp
set cwbs_url = 'http://localhost:8080/fe/signWithZip'
where cwbs_llave in (
	select ww.cwbs_llave from propiedad_ppdp pp 
		inner join webservice_wbsp ww on (ww.cwbs_llave = pp.cppd_campo)
	where pp.cppd_estado = 'A' and pp.cppd_texto = 'FE_CONTENT_FILE'
);

update mensajeplantillacorreo_mplp mm 
set cmpl_nombre = cmpl_nombre || ' (ZIP)'
where cmpl_llave in (
	select p2.cppd_valor from propiedad_ppdp p2
	where p2.cppd_estado = 'A' and p2.cppd_propiedadvalor = 'PROP_57' and p2.cppd_campo in (
	select pp.cppd_campo from propiedad_ppdp pp 
		where pp.cppd_estado = 'A' and pp.cppd_propiedadvalor in ('PROP_241','PROP_245','PROP_240')
	)
)