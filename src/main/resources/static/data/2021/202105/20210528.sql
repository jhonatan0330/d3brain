COMMENT ON TABLE usuario_usrp IS '2021-05-28';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_172' , 'W', 'API_SECONDARY_DOCUMENT', 'API_SECONDARY_DOCUMENT', 'REQUISITO', true);
	
ALTER TABLE relacioninterna_ritp
	ADD COLUMN crit_auxiliar character varying(4000);
