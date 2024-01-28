COMMENT ON TABLE usuario_usrp IS '2023-10-19';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	select
	'PROP_246' , 'E', 'TIPO REPORTE', 'REP_TYPE_EXPORT', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_246');