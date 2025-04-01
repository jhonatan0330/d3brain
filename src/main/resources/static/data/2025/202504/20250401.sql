COMMENT ON TABLE usuario_usrp IS '2025-04-01';

update propiedad_ppdp pd
set cppd_propiedadvalor = 'PROP_244'
where pd.cppd_llave in (
	select pp3.cppd_llave from procesoestado_pesp pp
		inner join procesotransicion_ptrp pp2 on (pp2.cptr_estadopartida = pp.cpes_llave and pp2.cptr_estado = 'A') 
		inner join propiedad_ppdp pp3 on (pp3.cppd_campo = pp2.cptr_llave and pp3.cppd_estado = 'A' and pp3.cppd_propiedadvalor = 'PROP_213')
	where cpes_tipo = 'R'
);