COMMENT ON TABLE usuario_usrp IS '2023-10-16';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	select
	'PROP_242' , 'L', 'PLANTILLA REGISTRA DIFERENCIAS', 'PLANTILLA_DIFERENCIAS', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_242');
	