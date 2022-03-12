COMMENT ON TABLE usuario_usrp IS '2020-08-15';
COMMENT ON TABLE usuariosesion_ussp IS '2020.08.15.00';

ALTER TABLE propiedad_ppdp
	ADD COLUMN cppd_bloqueo character varying(200);

ALTER TABLE propiedadvalordefinido_pvdp
	ADD COLUMN bpvd_pidetiempobloqueo boolean DEFAULT false NOT NULL;

