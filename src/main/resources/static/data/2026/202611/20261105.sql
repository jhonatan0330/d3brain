COMMENT ON TABLE usuario_usrp IS '2026-11-05';



--REPORT_QUERY
--select * from propiedadvalordefinido_pvdp pp  where pp.cpvd_llave  = 'PROP_250';
--select * from propiedad_ppdp pp where pp.cppd_propiedadvalor = 'PROP_250';

update propiedadvalordefinido_pvdp
set cpvd_uso_motivo = 'Nombre del Grupo de SQL para freemarker(DATOS)', cpvd_nombre = 'QUERY REPORTE MULTIPLES REGISTROS'
where cpvd_llave in ('PROP_250');

update propiedad_ppdp pp 
set cppd_motivo = 'DATOS'
where cppd_propiedadvalor = 'PROP_250';

--REPORT_QUERY_ENCABEZADO
--select * from propiedadvalordefinido_pvdp pp  where pp.cpvd_llave  = 'PROP_309';
--select * from propiedad_ppdp pp where pp.cppd_propiedadvalor = 'PROP_309';

update propiedadvalordefinido_pvdp
set cpvd_uso_motivo = 'Nombre del Grupo de SQL para freemarker(ENCABEZADO)', cpvd_nombre = 'QUERY REPORTE UN SOLO REGISTRO'
where cpvd_llave in ('PROP_309');

update propiedad_ppdp pp 
set cppd_motivo = 'ENCABEZADO'
where pp.cppd_propiedadvalor = 'PROP_309';