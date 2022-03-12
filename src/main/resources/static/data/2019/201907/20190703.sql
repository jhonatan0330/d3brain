COMMENT ON TABLE usuario_usrp IS '2019-07-03';
COMMENT ON TABLE usuariosesion_ussp IS '2019.07.03.00';

CREATE TABLE mensaje_msjp (
	cmsj_llave character varying(32) NOT NULL,
	cmsj_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE requerimiento_reqp (
	creq_llave character varying(32) NOT NULL,
	creq_tipo character varying(1) NOT NULL,
	dreq_fecha timestamp with time zone NOT NULL,
	creq_id character varying(32) NOT NULL,
	creq_texto character varying(4000) NOT NULL,
	creq_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE mensaje_msjp
	ADD CONSTRAINT pk_mensaje_msjp PRIMARY KEY (cmsj_llave);

ALTER TABLE requerimiento_reqp
	ADD CONSTRAINT pk_requerimiento_reqp PRIMARY KEY (creq_llave);
