COMMENT ON TABLE usuario_usrp IS '2018-10-05';
COMMENT ON TABLE usuariosesion_ussp IS '2018.10.05.00';

ALTER TABLE procesoestado_pesp DROP CONSTRAINT fk_procesoestadorolresponsable;

CREATE TABLE procesoestadoactividad_peap (
	cpea_llave character varying(32) NOT NULL,
	cpea_estadoproceso character varying(32) NOT NULL,
	cpea_rol character varying(32) NOT NULL,
	cpea_lider character varying(32),
	npea_duracion integer DEFAULT 0 NOT NULL,
	cpea_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE procesoestado_pesp DROP COLUMN cpes_rolresponsable;

ALTER TABLE rolacceso_racp DROP COLUMN crac_lider;

ALTER TABLE procesoestadoactividad_peap
	ADD CONSTRAINT pk_procesoestadoactividad_peap PRIMARY KEY (cpea_llave);

ALTER TABLE procesoestadoactividad_peap
	ADD CONSTRAINT fk_procesoestadoactividadestado FOREIGN KEY (cpea_estado) REFERENCES procesoestado_pesp(cpes_llave);

ALTER TABLE procesoestadoactividad_peap
	ADD CONSTRAINT fk_procesoestadoactividadlider FOREIGN KEY (cpea_lider) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE procesoestadoactividad_peap
	ADD CONSTRAINT fk_procesoestadoactividadrol FOREIGN KEY (cpea_rol) REFERENCES rolacceso_racp(crac_llave);

ALTER TABLE modeladonegocio_mngp
	DROP COLUMN nmng_nombre,
	ADD COLUMN cmng_nombre character varying(100) NOT NULL;

ALTER TABLE propiedadsistema_psip
	ALTER COLUMN cpsi_texto DROP NOT NULL;

ALTER TABLE rolacceso_racp
	ALTER COLUMN crac_codigo TYPE character varying(20) /* TYPE change - table: rolacceso_racp original: character varying(3) new: character varying(20) */;