COMMENT ON TABLE usuario_usrp IS '2018-05-09';

COMMENT ON TABLE usuariosesion_ussp IS '2018.05.09.00';

CREATE TABLE plantillapropiedad_pprp (
	cppr_llave character varying(32) NOT NULL,
	cppr_plantilla character varying(32) NOT NULL,
	cppr_key character varying(100) NOT NULL,
	cppr_valor character varying(100) NOT NULL,
	cppr_texto character varying(100),
	cppr_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE plantillapropiedad_pprp
	ADD CONSTRAINT pk_plantillapropiedad_pprp PRIMARY KEY (cppr_llave);

ALTER TABLE plantillapropiedad_pprp
	ADD CONSTRAINT fk_plantillapropiedadplantilla FOREIGN KEY (cppr_plantilla) REFERENCES public.documentoplantilla_dplp(cdpl_llave);

update reportebase_rpbp set brpb_soloexistente = true where crpb_plantilla = 'CAJA';