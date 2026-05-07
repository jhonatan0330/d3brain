COMMENT ON TABLE usuario_usrp IS '2026-05-06';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto)
    SELECT 'PROP_307' , 'C', 'VALIDAR SI SE GENERA EL VINCULO SEGUN CONDICIONES', 'VINCULO_VALIDATE_PREVIOUS_SQL', 'REQUISITO', 'C', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_307');

	
	