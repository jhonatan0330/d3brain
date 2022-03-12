COMMENT ON TABLE usuario_usrp IS '2020-04-02';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo) 
	VALUES('PROP_132' , 'L', 'PLANTILLA ANULAR', 'PLANTILLA_ANULAR', 'www.softwareparati.com', 'REQUISITO');
	
ALTER TABLE documentorelaciongestor_drgp
	ALTER COLUMN cdrg_estadofinal DROP NOT NULL;

ALTER TABLE documentorelaciongestor_drgp
	ALTER COLUMN cdrg_estadoinicial DROP NOT NULL;
	
update documentorelaciongestor_drgp set cdrg_valores = null where cdrg_valores is not null;