COMMENT ON TABLE usuario_usrp IS '2023-10-11';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	select
	'PROP_240' , 'T', 'MENSAJE ADJUNTO URL', 'MENSAJE_ADJUNTO_URL', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_240');
	
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean)
	select
	'PROP_241' , 'L', 'MENSAJE ADJUNTO URL', 'MENSAJE_ADJUNTO_URL', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_241'); 

ALTER TABLE mensaje_msjp ADD cmsj_adjuntourl varchar(2000) NULL;