COMMENT ON TABLE usuario_usrp IS '2024-08-06';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
	SELECT 'PROP_264' , 'O', 'LANDING PAGE', 'LANDING_PAGE', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_264');
	