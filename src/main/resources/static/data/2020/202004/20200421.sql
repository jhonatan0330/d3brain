COMMENT ON TABLE usuario_usrp IS '2020-04-21';


CREATE TABLE postcalificacion_pclp (
	cpcl_llave character varying(32) NOT NULL,
	cpcl_usuario character varying(32) NOT NULL,
	dpcl_fecha timestamp with time zone NOT NULL,
	cpcl_respuesta character varying(32) NOT NULL,
	bpcl_positiva boolean DEFAULT false NOT NULL,
	cpcl_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE postpregunta_pprp (
	cppr_llave character varying(32) NOT NULL,
	dppr_fecha timestamp with time zone NOT NULL,
	cppr_autor character varying(32) NOT NULL,
	cppr_pregunta character varying(4000) NOT NULL,
	cppr_keywords character varying(4000),
	cppr_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE postrespuesta_prsp (
	cprs_llave character varying(32) NOT NULL,
	dprs_fecha timestamp with time zone NOT NULL,
	cprs_autor character varying(32) NOT NULL,
	cprs_pregunta character varying(32) NOT NULL,
	cprs_respuesta character varying(4000) NOT NULL,
	cprs_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);


ALTER TABLE postcalificacion_pclp
	ADD CONSTRAINT pk_postcalificacion_pclp PRIMARY KEY (cpcl_llave);

ALTER TABLE postpregunta_pprp
	ADD CONSTRAINT pk_postpregunta_pprp PRIMARY KEY (cppr_llave);

ALTER TABLE postrespuesta_prsp
	ADD CONSTRAINT pk_postrespuesta_prsp PRIMARY KEY (cprs_llave);

ALTER TABLE postcalificacion_pclp
	ADD CONSTRAINT fk_postcalificacionrespuesta FOREIGN KEY (cpcl_respuesta) REFERENCES public.postrespuesta_prsp(cprs_llave);

ALTER TABLE postcalificacion_pclp
	ADD CONSTRAINT fk_postcalificacionusuario FOREIGN KEY (cpcl_usuario) REFERENCES public.usuario_usrp(cusr_llave);

ALTER TABLE postpregunta_pprp
	ADD CONSTRAINT fk_postpreguntaautor FOREIGN KEY (cppr_autor) REFERENCES public.usuario_usrp(cusr_llave);

ALTER TABLE postrespuesta_prsp
	ADD CONSTRAINT fk_postrespuestapregunta FOREIGN KEY (cprs_pregunta) REFERENCES postpregunta_pprp(cppr_llave);

ALTER TABLE postrespuesta_prsp
	ADD CONSTRAINT fk_postrespuestaautor FOREIGN KEY (cprs_autor) REFERENCES public.usuario_usrp(cusr_llave);
