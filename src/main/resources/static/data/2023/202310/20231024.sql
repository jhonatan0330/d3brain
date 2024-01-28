COMMENT ON TABLE usuario_usrp IS '2023-10-24';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo)
	select
	'PROP_244' , 'T', 'GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION', 'CAMPO PARA GENERAR DOCUMENTO DEL RESULTADO DE LA ITERACION', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_244');