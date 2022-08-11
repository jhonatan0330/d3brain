COMMENT ON TABLE usuario_usrp IS '2022-08-08';

CREATE TABLE usuarioautenticacionautorizacion_uaap (
	cuaa_llave character varying(32) NOT NULL,
	cuaa_usuario character varying(32) NOT NULL,
	duaa_fechamaxima timestamp with time zone NOT NULL,
	duaa_fechasolicitud timestamp with time zone NOT NULL,
	cuaa_correo character varying(100),
	cuaa_ipsolicitud character varying(100) NOT NULL,
	cuaa_codigo character varying(100),
	duaa_fecharedencion timestamp with time zone,
	cuaa_key character varying(100),
	cuaa_ipredencion character varying(100),
	cuaa_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE usuarioautenticacion_uaup
	DROP COLUMN duau_fechacreacion,
	ADD COLUMN duau_fechamaxima timestamp with time zone,
	ADD COLUMN cuau_autorizacioncrea character varying(32),
	ADD COLUMN cuau_autorizacionelimina character varying(32);


ALTER TABLE usuarioautenticacionautorizacion_uaap
	ADD CONSTRAINT pk_usuarioautenticacionautorizacion_uaap PRIMARY KEY (cuaa_llave);

ALTER TABLE usuarioautenticacion_uaup
	ADD CONSTRAINT fk_usuarioautenticacionautorizacioncrea FOREIGN KEY (cuau_autorizacioncrea) REFERENCES usuarioautenticacionautorizacion_uaap(cuaa_llave);

ALTER TABLE usuarioautenticacionautorizacion_uaap
	ADD CONSTRAINT fk_usuarioautenticacionautorizacionusuario FOREIGN KEY (cuaa_usuario) REFERENCES usuario_usrp(cusr_llave);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_207' , 'L', 'CORREO ROL', 'CORREO_ROL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_208' , 'L', 'CELULAR ROL', 'CELULAR_ROL', 'REQUISITO');
	
ALTER TABLE usuario_usrp ALTER COLUMN cusr_telefono type character varying(50);
	