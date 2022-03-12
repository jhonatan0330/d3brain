COMMENT ON TABLE usuario_usrp IS '2020-08-03';

update propiedad_ppdp set cppd_estado  ='I' where cppd_estado  = 'A' and cppd_tipo = 'C' and 
cppd_campo not in (select cdpc_llave from documentoplantillacaracteristica_dpcp  where cdpc_llave = cppd_campo );

update propiedad_ppdp prop_crud set cppd_valor = (select prop_fuente.cppd_valor from propiedad_ppdp prop_fuente where prop_fuente.cppd_propiedadvalor ='PROP_19' and prop_fuente.cppd_estado = 'A' and prop_fuente.cppd_campo  = prop_crud.cppd_campo )
where prop_crud.cppd_propiedadvalor ='PROP_36' and prop_crud.cppd_estado = 'A';

update propiedadvalordefinido_pvdp set bpvd_multiple = true, bpvd_propiedadboolean = false where cpvd_llave = 'PROP_36';

update propiedad_ppdp set cppd_texto = (select cdpc_nombre from documentoplantillacaracteristica_dpcp where cdpc_llave = cppd_campo )
where cppd_propiedadvalor ='PROP_36' and cppd_estado = 'A';