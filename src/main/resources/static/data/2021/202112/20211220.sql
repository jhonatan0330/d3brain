COMMENT ON TABLE usuario_usrp IS '2021-12-20';

ALTER TABLE propiedad_ppdp
	ADD COLUMN cppd_rolexcluyente character varying(32),
	ADD COLUMN cppd_usuarioexcluyente character varying(32),
	ALTER COLUMN cppd_valor TYPE character varying(180000);