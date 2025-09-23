COMMENT ON TABLE usuario_usrp IS '2025-09-21';


update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'GENERAR CAMPO VINCULO DEL DOCUMENTO SELECCIONADO'
where cpvd_llave  = 'PROP_294';

	UPDATE propiedad_ppdp pp
	SET cppd_valor = regexp_replace(
	    cppd_valor,
	    'uuid="[^"]+"',
	    'uuid="' || (md5(random()::text || clock_timestamp()::text)::uuid)::text || '"'
	)
	WHERE pp.cppd_propiedadvalor = 'PROP_138'
		and pp.cppd_estado = 'A';