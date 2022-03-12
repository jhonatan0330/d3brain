COMMENT ON TABLE usuario_usrp IS '2018-09-11';
COMMENT ON TABLE usuariosesion_ussp IS '2018.09.11.00';


CREATE TABLE propiedadsistema_psip (
	cpsi_llave character varying(32) NOT NULL,
	cpsi_key character varying(100) NOT NULL,
	cpsi_valor character varying(100) NOT NULL,
	cpsi_texto character varying(100) NOT NULL,
	cpsi_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);


ALTER TABLE procesoestado_pesp
	ADD COLUMN cpes_rolresponsable character varying(32);

ALTER TABLE procesotransicion_ptrp
	DROP COLUMN bptr_responsableobligatorio;

ALTER TABLE rolacceso_racp
	ADD COLUMN crac_lider character varying(32);


ALTER TABLE propiedadsistema_psip
	ADD CONSTRAINT pk_propiedadsistema_psip PRIMARY KEY (cpsi_llave);

ALTER TABLE procesoestado_pesp
	ADD CONSTRAINT fk_procesoestadorolresponsable FOREIGN KEY (cpes_rolresponsable) REFERENCES rolacceso_racp(crac_llave);
