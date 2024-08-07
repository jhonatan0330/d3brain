COMMENT ON TABLE usuario_usrp IS '2024-08-05';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean)
	SELECT 'PROP_157' , 'C', 'MULTIPLES ADJUNTOS', 'MULTIPLE_FILE', 'REQUISITO', 'A', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_157');

update propiedadvalordefinido_pvdp
set bpvd_propiedadboolean = true
where cpvd_llave = 'PROP_157';