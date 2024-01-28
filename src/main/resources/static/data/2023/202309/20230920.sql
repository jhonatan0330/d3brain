COMMENT ON TABLE usuario_usrp IS '2023-09-20';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_multiple) 
	select
	'PROP_238' , 'W', 'REEMPLAZAR CODIGO REFERENCIADO TIPO LISTA', 'API_CODE_REFERENCE_LIST', 'REQUISITO', true, true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_238');