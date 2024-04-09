COMMENT ON TABLE usuario_usrp IS '2024-03-26';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto)
	SELECT 'PROP_257' , 'C', 'TEXTO_LONGITUD_MINIMA', 'TEXTO_LONGITUD_MINIMA', 'REQUISITO', 'T', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_257');
	