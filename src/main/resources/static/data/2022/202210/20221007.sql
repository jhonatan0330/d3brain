COMMENT ON TABLE usuario_usrp IS '2022-10-07';

ALTER TABLE tarifa_tarp
	ADD COLUMN ctar_dimension2 character varying(32),
	ADD COLUMN ctar_dimension3 character varying(32),
	ADD COLUMN ctar_dimension4 character varying(32);
	
ALTER TABLE tarifario_trfp
	ADD COLUMN ctrf_tipodimension2 character varying(32),
	ADD COLUMN ctrf_tipodimension3 character varying(32),
	ADD COLUMN ctrf_tipodimension4 character varying(32);
	