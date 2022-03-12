COMMENT ON TABLE usuario_usrp IS '2020-03-03';

COMMENT ON TABLE usuariosesion_ussp IS '2020.03.03.00';

ALTER TABLE documentorelacionexpediente_dexp
	ADD COLUMN mdex_valor numeric(18,6) DEFAULT 0 NOT NULL;

ALTER TABLE propiedad_ppdp
	ADD COLUMN dppd_fechainicial timestamp with time zone,
	ADD COLUMN dppd_fechafinal timestamp with time zone,
	ADD COLUMN cppd_usuario character varying(32);

ALTER TABLE propiedadvalordefinido_pvdp
	ADD COLUMN bpvd_piderol boolean DEFAULT false NOT NULL,
	ADD COLUMN bpvd_pideusuario boolean DEFAULT false NOT NULL,
	ADD COLUMN bpvd_pidefechas boolean DEFAULT false NOT NULL;

