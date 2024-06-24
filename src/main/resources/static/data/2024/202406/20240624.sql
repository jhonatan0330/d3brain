COMMENT ON TABLE usuario_usrp IS '2024-06-24';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
	SELECT 'PROP_260' , 'O', 'LAYOUT APPLICATION', 'LAYOUT_APP', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_260');