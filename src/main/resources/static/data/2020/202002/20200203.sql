
COMMENT ON TABLE usuariosesion_ussp IS '2020.02.03.00';

ALTER TABLE documentorelaciongestor_drgp
	ADD COLUMN cdrg_valores character varying(32);

ALTER TABLE mensaje_msjp
	ADD COLUMN cmsj_reporte character varying(32);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo) 
	VALUES('PROP_111' , 'T', 'MENSAJE REPORTE', 'MENSAJE_REPORTE', 'www.softwareparati.com', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo) 
	VALUES('PROP_112' , 'P', 'MENSAJE REPORTE', 'MENSAJE_REPORTE', 'www.softwareparati.com', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo) 
	VALUES('PROP_113' , 'L', 'MENSAJE REPORTE', 'MENSAJE_REPORTE', 'www.softwareparati.com', 'REQUISITO');