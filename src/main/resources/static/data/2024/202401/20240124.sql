COMMENT ON TABLE usuario_usrp IS '2024-01-24';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
	select
	'PROP_251' , 'W', 'API CORREO NOTIFICACION ERRORES', 'API_MAIL_NOTIFICATION', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_251');