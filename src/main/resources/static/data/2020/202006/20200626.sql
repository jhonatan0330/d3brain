
COMMENT ON TABLE usuario_usrp IS '2020-06-26';
COMMENT ON TABLE usuariosesion_ussp IS '2020.06.26.00';

CREATE TABLE gpsdispositivo_gpsp (
	cgps_llave character varying(32) NOT NULL,
	cgps_usuario character varying(32) NOT NULL,
	cgps_nombre character varying(100),
	cgps_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE gpslocalizacion_gplp (
	cgpl_llave character varying(32) NOT NULL,
	cgpl_dispositivo character varying(32) NOT NULL,
	dgpl_fecha timestamp with time zone NOT NULL,
	mgpl_longitud numeric(18,6) DEFAULT 0 NOT NULL,
	mgpl_latitud numeric(18,6) DEFAULT 0 NOT NULL,
	cgpl_direccion character varying(100),
	cgpl_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE gpsdispositivo_gpsp
	ADD CONSTRAINT pk_gpsdispositivo_gpsp PRIMARY KEY (cgps_llave);

ALTER TABLE gpslocalizacion_gplp
	ADD CONSTRAINT pk_gpslocalizacion_gplp PRIMARY KEY (cgpl_llave);

ALTER TABLE gpsdispositivo_gpsp
	ADD CONSTRAINT fk_gpsdispositivousuario FOREIGN KEY (cgps_usuario) REFERENCES public.usuario_usrp(cusr_llave);

ALTER TABLE gpslocalizacion_gplp
	ADD CONSTRAINT fk_gpslocalizaciondispositivo FOREIGN KEY (cgpl_dispositivo) REFERENCES public.gpsdispositivo_gpsp(cgps_llave);
