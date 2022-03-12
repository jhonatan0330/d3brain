COMMENT ON TABLE usuario_usrp IS '2019-02-04';

ALTER TABLE catalogo_catp
	ADD COLUMN ccat_plantilla character varying(32);

ALTER TABLE catalogo_catp
	ADD CONSTRAINT fk_catalogoplantilla FOREIGN KEY (ccat_plantilla) REFERENCES documentoplantilla_dplp(cdpl_llave);
