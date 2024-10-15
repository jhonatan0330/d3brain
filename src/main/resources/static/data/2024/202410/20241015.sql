COMMENT ON TABLE usuario_usrp IS '2024-10-15';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
	SELECT 'PROP_269' , 'A', 'HTTP METODO PETICION', 'HTTP_METHOD', 'PERMISOS', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_269');
