COMMENT ON TABLE usuario_usrp IS '2019-05-06';


ALTER TABLE rodamientoasignacion_rasp
	DROP CONSTRAINT fk_rodamientoasignacionviajefinal;

ALTER TABLE rodamientoasignacion_rasp
	DROP CONSTRAINT fk_rodamientoasignacionviajeinicial;

ALTER TABLE viajeescala_esvp
	DROP CONSTRAINT fk_viajeescalaviaje;

CREATE TABLE viaje_viap (
	cvia_llave character varying(32) NOT NULL,
	cvia_ruta character varying(32) NOT NULL,
	dvia_salidaestimada timestamp with time zone NOT NULL,
	dvia_llegadaestimada timestamp with time zone NOT NULL,
	cvia_vehiculo character varying(32),
	cvia_conductor character varying(32),
	cvia_conductorauxiliar character varying(32),
	cvia_observaciones character varying(4000),
	cvia_rodamientoasignacion character varying(32),
	cvia_rodamientogeneracion character varying(32),
	cvia_viajesiguienterodamiento character varying(32),
	cvia_cliente character varying(32),
	cvia_contacto character varying(32),
	cvia_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);


ALTER TABLE viaje_viap
	ADD CONSTRAINT pk_viaje_viap PRIMARY KEY (cvia_llave);
