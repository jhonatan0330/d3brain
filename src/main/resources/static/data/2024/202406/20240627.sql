COMMENT ON TABLE usuario_usrp IS '2024-06-27';

update propiedad_ppdp set cppd_estado = 'I', cppd_cambioeliminacion = 'SC_20240627'
where cppd_llave in (
	select prop.cppd_llave from documentoplantilla_dplp dd 
		inner join documentoplantillacaracteristica_dpcp dc on (dc.cdpc_plantilla = dd.cdpl_llave 
							and dc.cdpc_estado= 'A' and dc.cdpc_formato= 'F')
		inner join propiedad_ppdp prop on (prop.cppd_campo = dc.cdpc_llave and prop.cppd_estado = 'A' 
							and prop.cppd_tipo = 'C' and prop.cppd_propiedadvalor= 'PROP_135')
	where dd.cdpl_estado = 'A'
	and dd.cdpl_llave in (
		select pp.cppd_valor from propiedad_ppdp pp 
		where pp.cppd_estado = 'A'
			and pp.cppd_propiedadvalor = 'PROP_242'
	)
);
	