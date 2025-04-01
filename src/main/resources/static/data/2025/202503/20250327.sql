COMMENT ON TABLE usuario_usrp IS '2025-03-27';

update propiedadvalordefinido_pvdp
set bpvd_propiedadboolean = false
where cpvd_llave = 'PROP_107';


update propiedad_ppdp pp 
set cppd_valor = (coalesce((select cdpl_proceso from documentoplantilla_dplp where cdpl_llave = cppd_campo), 'CONFIGURACION'))
,cppd_texto = (coalesce((select cprc_nombre from proceso_prcp pc where pc.cprc_llave = (select cdpl_proceso from documentoplantilla_dplp where cdpl_llave = cppd_campo) ), 'CONFIGURACION'))
where pp.cppd_estado = 'A' and pp.cppd_propiedadvalor = 'PROP_107';
