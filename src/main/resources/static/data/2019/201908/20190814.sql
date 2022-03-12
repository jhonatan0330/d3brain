COMMENT ON TABLE usuario_usrp IS '2019-08-14';

COMMENT ON TABLE usuariosesion_ussp IS '2019.08.14.00';

CREATE TABLE pedidoventaajuste_pvap (
	cpva_llave character varying(32) NOT NULL,
	cpva_documento character varying(32) NOT NULL,
	dpva_fecha timestamp with time zone NOT NULL,
	cpva_estadoinicial character varying(32) NOT NULL,
	cpva_estadofinal character varying(32) NOT NULL,
	cpva_responsable character varying(32) NOT NULL,
	cpva_motivo character varying(4000) NOT NULL,
	cpva_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE documentoplantillarol_dprp
	ADD COLUMN bdpr_cargamasiva boolean DEFAULT false NOT NULL,
	ADD COLUMN bdpr_cambioestado boolean DEFAULT false NOT NULL;


ALTER TABLE pedidoventaajuste_pvap
	ADD CONSTRAINT pk_pedidoventaajuste_pvap PRIMARY KEY (cpva_llave);

ALTER TABLE pedidoventaajuste_pvap
	ADD CONSTRAINT fk_pedidoventaajustedocumento FOREIGN KEY (cpva_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE pedidoventaajuste_pvap
	ADD CONSTRAINT fk_pedidoventaajusteestadofinal FOREIGN KEY (cpva_estadofinal) REFERENCES procesoestado_pesp(cpes_llave);

ALTER TABLE pedidoventaajuste_pvap
	ADD CONSTRAINT fk_pedidoventaajusteestadoinicial FOREIGN KEY (cpva_estadoinicial) REFERENCES procesoestado_pesp(cpes_llave);


ALTER TABLE pedidoventaajuste_pvap
	ADD CONSTRAINT fk_pedidoventaajusteresponsable FOREIGN KEY (cpva_responsable) REFERENCES public.usuario_usrp(cusr_llave);
