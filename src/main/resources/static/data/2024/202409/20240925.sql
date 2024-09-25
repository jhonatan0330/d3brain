COMMENT ON TABLE usuario_usrp IS '2024-09-25';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean)
	SELECT 'PROP_268' , 'L', 'DUPLICAR', 'PERMISO_PLANTILLA_DUPLICAR', 'PERMISOS', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_268');
	