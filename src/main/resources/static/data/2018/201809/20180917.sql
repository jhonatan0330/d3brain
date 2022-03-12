
COMMENT ON TABLE usuario_usrp IS '2018-09-17';

COMMENT ON TABLE usuariosesion_ussp IS '2018.09.17.00';

DROP TABLE pedidoventaresponsable_pvrp;

CREATE TABLE actividad_actp (
	cact_llave character varying(32) NOT NULL,
	cact_documento character varying(32) NOT NULL,
	cact_responsable character varying(32) NOT NULL,
	dact_fecharegistro timestamp with time zone NOT NULL,
	cact_usuarioregistro character varying(32) NOT NULL,
	dact_fechainactivo timestamp with time zone,
	cact_usuarioinactivo character varying(32),
	nact_tiempo integer DEFAULT 0 NOT NULL,
	dact_fechainicio timestamp with time zone NOT NULL,
	dact_fechafin timestamp with time zone,
	cact_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE modeladonegocio_mngp (
	cmng_llave character varying(32) NOT NULL,
	nmng_nombre integer DEFAULT 0 NOT NULL,
	cmng_archivo character varying(2000) NOT NULL,
	dmng_fechavalidacion timestamp with time zone,
	cmng_logcomparacion character varying(2000),
	dmng_fechaimplementacion timestamp with time zone,
	cmng_logimplementacion character varying(2000),
	cmng_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);



ALTER TABLE actividad_actp
	ADD CONSTRAINT pk_actividad_actp PRIMARY KEY (cact_llave);

ALTER TABLE modeladonegocio_mngp
	ADD CONSTRAINT pk_modeladonegocio_mngp PRIMARY KEY (cmng_llave);

ALTER TABLE actividad_actp
	ADD CONSTRAINT fk_actividaddocumento FOREIGN KEY (cact_documento) REFERENCES public.pedidoventa_pdvp(cpdv_llave);

ALTER TABLE actividad_actp
	ADD CONSTRAINT fk_actividadresponsable FOREIGN KEY (cact_responsable) REFERENCES public.usuario_usrp(cusr_llave);

ALTER TABLE actividad_actp
	ADD CONSTRAINT fk_actividadusuarioinactivo FOREIGN KEY (cact_usuarioinactivo) REFERENCES public.usuario_usrp(cusr_llave);

ALTER TABLE actividad_actp
	ADD CONSTRAINT fk_actividadusuarioregistro FOREIGN KEY (cact_usuarioregistro) REFERENCES public.usuario_usrp(cusr_llave);
