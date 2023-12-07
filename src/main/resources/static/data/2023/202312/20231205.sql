COMMENT ON TABLE usuario_usrp IS '2023-12-05';

update reportebase_rpbp set crpb_nombre = crpb_nombre || ' ' || crpb_codigo
where crpb_nombre in (
select crpb_nombre from reportebase_rpbp
where crpb_estado = 'A'
group by crpb_nombre
having count(crpb_nombre) > 1);

update propiedad_ppdp set cppd_texto = (select cwbs_nombre from webservice_wbsp where cwbs_llave = cppd_valor)
where cppd_valor in (select cwbs_llave from webservice_wbsp);

update propiedad_ppdp set cppd_texto = (select cmpl_nombre from mensajeplantillacorreo_mplp where cmpl_llave = cppd_valor)
where cppd_valor in (select cmpl_llave from mensajeplantillacorreo_mplp);

update propiedad_ppdp set cppd_texto = (select cmpl_nombre from mensajeplantillacorreo_mplp where cmpl_llave = cppd_valor)
where cppd_valor in (select cmpl_llave from mensajeplantillacorreo_mplp);

update propiedad_ppdp set cppd_texto = (select crpb_nombre from reportebase_rpbp where crpb_llave = cppd_valor)
where cppd_valor in (select crpb_llave from reportebase_rpbp);

update permiso_perp set cper_modulo = 'AdministracionLogisticpymes'	where cper_modulo = '2';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	select 'PROP_121' , 'C', 'MODIFICAR CAMPO PRINCIPAL', 'MODIFICAR_CAMPO', 'REQUISITO', 'Z', true
	WHERE NOT EXISTS (SELECT 1 FROM propiedadvalordefinido_pvdp WHERE cpvd_llave='PROP_121');

