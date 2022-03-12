COMMENT ON TABLE usuario_usrp IS '2019-11-26';
COMMENT ON TABLE usuariosesion_ussp IS '2019.11.25.00';

CREATE TABLE cambio_cmbp (
	ccmb_llave character varying(32) NOT NULL,
	ccmb_nombre character varying(100) NOT NULL,
	ccmb_motivo character varying(4000) NOT NULL,
	dcmb_fecha timestamp with time zone NOT NULL,
	dcmb_fechaaplicacion timestamp with time zone,
	bcmb_grabando boolean DEFAULT false NOT NULL,
	ccmb_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

INSERT INTO cambio_cmbp (ccmb_llave, ccmb_nombre, ccmb_motivo, dcmb_fecha, dcmb_fechaaplicacion)
VALUES('SC-1', 'SC-1', 'CONFIGURACION INICIAL DEL SISTEMA', now(), now());

ALTER TABLE propiedad_ppdp
	ADD COLUMN cppd_cambiocreacion character varying(32),
	ADD COLUMN cppd_cambioeliminacion character varying(32);

update propiedad_ppdp set cppd_cambiocreacion = 'SC-1';

ALTER TABLE propiedad_ppdp
	ALTER COLUMN cppd_cambiocreacion set not null;
	
ALTER TABLE propiedadvalordefinido_pvdp
	DROP COLUMN cpvd_formato,
	ADD COLUMN cpvd_origencategoria character varying(1);

ALTER TABLE cambio_cmbp
	ADD CONSTRAINT pk_cambio_cmbp PRIMARY KEY (ccmb_llave);

ALTER TABLE propiedad_ppdp
	ADD CONSTRAINT fk_propiedadcambiocreacion FOREIGN KEY (cppd_cambiocreacion) REFERENCES cambio_cmbp(ccmb_llave);

ALTER TABLE propiedad_ppdp
	ADD CONSTRAINT fk_propiedadcambioeliminacion FOREIGN KEY (cppd_cambioeliminacion) REFERENCES cambio_cmbp(ccmb_llave);
