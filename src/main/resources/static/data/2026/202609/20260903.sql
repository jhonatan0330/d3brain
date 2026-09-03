COMMENT ON TABLE usuario_usrp IS '2026-09-03';


/*
U = Modificacion
R = Rol
T = Reporte
I = Anulaciones
A = Activar
P = Principal
*/

update documentoplantilla_dplp dd 
set cdpl_tipo = 'A'
where dd.cdpl_llave in (select cppd_valor from propiedad_ppdp pp  where pp.cppd_propiedadvalor = 'PROP_283' and pp.cppd_estado ='A');

update documentoplantilla_dplp dd 
set cdpl_padre = (select cppd_campo from propiedad_ppdp pp  where pp.cppd_propiedadvalor = 'PROP_283' and pp.cppd_estado ='A' and cppd_valor = dd.cdpl_llave limit 1)
where cdpl_tipo = 'A';

update documentoplantilla_dplp dd 
set cdpl_tipo = 'I'
where dd.cdpl_llave in (select cppd_valor from propiedad_ppdp pp  where pp.cppd_propiedadvalor = 'PROP_132' and pp.cppd_estado ='A');

update documentoplantilla_dplp dd 
set cdpl_padre = (select cppd_campo from propiedad_ppdp pp  where pp.cppd_propiedadvalor = 'PROP_132' and pp.cppd_estado ='A' and cppd_valor = dd.cdpl_llave limit 1)
where cdpl_tipo = 'I';

update documentoplantilla_dplp dd 
set cdpl_tipo = 'P', cdpl_padre = dd.cdpl_proceso
where dd.cdpl_llave in (select cppd_campo from propiedad_ppdp pp  where pp.cppd_propiedadvalor = 'PROP_142' and pp.cppd_estado ='A');


update documentoplantilla_dplp dd 
set cdpl_tipo = 'T',  cdpl_padre = dd.cdpl_proceso 
where dd.cdpl_llave in (select cppd_campo from propiedad_ppdp pp  where pp.cppd_propiedadvalor = 'PROP_141' and pp.cppd_estado ='A');


update documentoplantilla_dplp dd 
set cdpl_tipo = 'U'
where dd.cdpl_llave in (select cppd_valor from propiedad_ppdp pp  where pp.cppd_propiedadvalor = 'PROP_242' and pp.cppd_estado ='A');

update documentoplantilla_dplp dd 
set cdpl_padre = (select cppd_campo from propiedad_ppdp pp  where pp.cppd_propiedadvalor = 'PROP_242' and pp.cppd_estado ='A' and cppd_valor = dd.cdpl_llave limit 1)
where cdpl_tipo = 'U';

update documentoplantilla_dplp dd 
set cdpl_tipo = 'P',  cdpl_padre = dd.cdpl_proceso 
where cdpl_tipo is null;
