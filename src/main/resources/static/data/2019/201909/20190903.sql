COMMENT ON TABLE usuario_usrp IS '2019-09-04';


ALTER TABLE procesotransicion_ptrp
	ALTER COLUMN cptr_plantilla DROP NOT NULL;

ALTER TABLE propiedad_ppdp
	ALTER COLUMN cppd_valor TYPE character varying(4000) /* TYPE change - table: propiedad_ppdp original: character varying(100) new: character varying(4000) */;