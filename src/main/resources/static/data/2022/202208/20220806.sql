COMMENT ON TABLE usuario_usrp IS '2022-08-06';
--COMMENT ON TABLE usuariosesion_ussp IS '2022.08.06.00';

ALTER TABLE usuarioautenticacion_uaup
	DROP CONSTRAINT if exists usuarioautenticacion_uaup_cuau_sesion_key;

CREATE TABLE usuariosesionerror_usep (
	cuse_llave character varying(32) NOT NULL,
	cuse_sesion character varying(100),
	cuse_clave character varying(100),
	cuse_ip character varying(100),
	duse_fecha timestamp with time zone NOT NULL,
	cuse_error character varying(4000) NOT NULL,
	cuse_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE usuariosesion_ussp
	ADD COLUMN cuss_ip character varying(100);

ALTER TABLE usuariosesionerror_usep
	ADD CONSTRAINT pk_usuariosesionerror_usep PRIMARY KEY (cuse_llave);
