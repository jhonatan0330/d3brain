COMMENT ON TABLE usuario_usrp IS '2020-10-12';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_textoculto, cpvd_origencategoria) 
	VALUES('PROP_156' , 'A', 'ITERACION_SQL', 'ITERACION_SQL', 'REQUISITO', true, 'R');
	

update propiedadvalordefinido_pvdp set cpvd_origencategoria = 'E' where cpvd_llave in ('PROP_92', 'PROP_91', 'PROP_90', 'PROP_89', 'PROP_136'); 

update propiedadvalordefinido_pvdp set cpvd_origencategoria = 'D' where cpvd_llave in ('PROP_146'); 

update propiedadvalordefinido_pvdp set bpvd_solicitamotivo = true where cpvd_llave in ('PROP_156', 'PROP_146'); 
