COMMENT ON TABLE usuario_usrp IS '2023-06-13';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_230' , 'E', 'OMITIR LA IMPRESION DEL REPORTE', 'REP_EXCLUDE_STORAGE_FILE', 'REQUISITO', true);
	
ALTER TABLE reporteejecucion_rejp ADD crej_url varchar(4000) NULL;