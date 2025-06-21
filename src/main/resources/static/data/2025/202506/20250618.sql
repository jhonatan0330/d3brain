COMMENT ON TABLE usuario_usrp IS '2025-06-18';

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20250618',  'SC_20250618',  'Retirara propiedades de ELIMINAR PLANTILLA',  now());
	
update propiedadvalordefinido_pvdp pp 
set cpvd_estado = 'I'
where pp.cpvd_llave in ('PROP_79', 'PROP_261');

update propiedad_ppdp pp 
set cppd_estado = 'I', cppd_cambioeliminacion = 'SC_20250618'
where pp.cppd_propiedadvalor in ('PROP_79', 'PROP_261');


update propiedadvalordefinido_pvdp pp 
set bpvd_multiple = true
where pp.cpvd_llave = 'PROP_196';


update propiedad_ppdp
set cppd_estado = 'I'
where cppd_llave in (
	select pp.cppd_llave from usuario_usrp uu 
		inner join propiedad_ppdp pp on (pp.cppd_usuario = uu.cusr_llave and pp.cppd_estado = 'A')
	where uu.cusr_estado  = 'I'
);

update propiedad_ppdp
set cppd_estado = 'I'
where cppd_llave in (
	select pp.cppd_llave from usuario_usrp uu 
		inner join propiedad_ppdp pp on (pp.cppd_usuarioexcluyente  = uu.cusr_llave and pp.cppd_estado = 'A')
	where uu.cusr_estado  = 'I'
);

update propiedad_ppdp
set cppd_estado = 'I'
where cppd_llave in (
	select pp.cppd_llave from rolacceso_racp rr  
		inner join propiedad_ppdp pp on (pp.cppd_rol = rr.crac_llave and pp.cppd_estado = 'A')
	where rr.crac_estado  = 'I'
);


update propiedad_ppdp
set cppd_estado = 'I'
where cppd_llave in (
	select pp.cppd_llave from rolacceso_racp rr  
		inner join propiedad_ppdp pp on (pp.cppd_rolexcluyente  = rr.crac_llave and pp.cppd_estado = 'A')
	where rr.crac_estado  = 'I'
);
