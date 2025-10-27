COMMENT ON TABLE usuario_usrp IS '2025-10-24';

update propiedadvalordefinido_pvdp
	set cpvd_estado = 'I'
where cpvd_llave = 'PROP_27';

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20251024',  'SC_20251024',  'Retirar las propiedades tipo moneda',  now());
	
update propiedad_ppdp pp 
	set cppd_estado = 'I', cppd_cambioeliminacion = 'SC_20251024'
where pp.cppd_propiedadvalor = 'PROP_27'
		and pp.cppd_estado = 'A';