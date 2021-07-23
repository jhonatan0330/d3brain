COMMENT ON TABLE usuario_usrp IS '2021-07-22';

CREATE TABLE cargaarchivo_carp (
	ccar_llave character varying(32) NOT NULL,
	ccar_servidor character varying(32),
	ncar_size integer DEFAULT 0 NOT NULL,
	ccar_url character varying(4000),
	dcar_fechainicio timestamp with time zone NOT NULL,
	dcar_fechafin timestamp with time zone NOT NULL,
	ccar_error character varying(4000),
	ccar_usuario character varying(32),
	ccar_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE cargaarchivo_carp
	ADD CONSTRAINT pk_cargaarchivo_carp PRIMARY KEY (ccar_llave);

ALTER TABLE cargaarchivo_carp
	ADD CONSTRAINT fk_cargaarchivoservidor FOREIGN KEY (ccar_servidor) REFERENCES servidor_serp(cser_llave);

ALTER TABLE cargaarchivo_carp
	ADD CONSTRAINT fk_cargaarchivousuario FOREIGN KEY (ccar_usuario) REFERENCES usuario_usrp(cusr_llave);