COMMENT ON TABLE usuario_usrp IS '2020-07-25';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_pidefechas ) 
	VALUES('PROP_140' , 'T', 'TEMPORIZADOR', 'TEMPORIZADOR', 'www.softwareparati.com', 'REQUISITO', true);

CREATE TABLE procesotransicionautomatica_ptap (
	cpta_llave character varying(32) NOT NULL,
	cpta_transicion character varying(32) NOT NULL,
	cpta_propiedad character varying(32) NOT NULL,
	dpta_fecha timestamp with time zone NOT NULL,
	dpta_ejecucion timestamp with time zone,
	cpta_mensaje character varying(4000) NOT NULL,
	cpta_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE procesodecisionrespuesta_pdrp
	ADD COLUMN cpdr_afectasaldo character varying(1);

ALTER TABLE propiedad_ppdp
	ALTER COLUMN cppd_motivo DROP NOT NULL;

ALTER TABLE propiedadvalordefinido_pvdp
	ADD COLUMN bpvd_solicitamotivo boolean DEFAULT false NOT NULL;

ALTER TABLE procesotransicionautomatica_ptap
	ADD CONSTRAINT pk_procesotransicionautomatica_ptap PRIMARY KEY (cpta_llave);

ALTER TABLE procesotransicionautomatica_ptap
	ADD CONSTRAINT fk_procesotransicionautomaticapropiedad FOREIGN KEY (cpta_propiedad) REFERENCES propiedad_ppdp(cppd_llave);

ALTER TABLE procesotransicionautomatica_ptap
	ADD CONSTRAINT fk_procesotransicionautomaticatransicion FOREIGN KEY (cpta_transicion) REFERENCES procesotransicion_ptrp(cptr_llave);

update propiedad_ppdp set cppd_motivo = null  where cppd_motivo = 'PENDIENTE';
update propiedad_ppdp set cppd_motivo = null  where cppd_motivo = (select cpvd_motivo from propiedadvalordefinido_pvdp where cpvd_llave = cppd_propiedadvalor);

ALTER TABLE propiedadvalordefinido_pvdp
	DROP COLUMN cpvd_motivo;
	
update propiedad_ppdp set cppd_motivo = null  where cppd_motivo is not null and length( cppd_motivo  ) < 10;