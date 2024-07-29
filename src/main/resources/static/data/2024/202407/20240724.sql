COMMENT ON TABLE usuario_usrp IS '2024-07-24';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
	SELECT 'PROP_263' , 'R', 'TIEMPO DE SOLICITAR NUEVA CLAVE', 'TIEMPO_NUEVA_CLAVE', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_263');
