COMMENT ON TABLE usuario_usrp IS '2023-10-18';

ALTER TABLE documentoplantilla_dplp ALTER COLUMN cdpl_codigo type VARCHAR(32);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_245' , 'P', 'MENSAJE ADJUNTO URL', 'MENSAJE_ADJUNTO_URL', 'REQUISITO', true);