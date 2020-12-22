
CREATE TABLE IF NOT EXISTS auditoria_audp (
	caud_llave character varying(32) NOT NULL,
	caud_usuario character varying(32) NOT NULL,
	caud_clase character varying(100) NOT NULL,
	caud_llaveclase character varying(32) NOT NULL,
	caud_operacion character varying(4000) NOT NULL,
	daud_fecha timestamp with time zone NOT NULL,
	caud_estado character varying(1) NOT NULL
);

ALTER TABLE auditoria_audp
	ADD CONSTRAINT pk_auditoria_audp PRIMARY KEY (caud_llave);

ALTER TABLE auditoria_audp
	ADD CONSTRAINT fk_auditoriausuario FOREIGN KEY (caud_usuario) REFERENCES usuario_usrp(cusr_llave);

ALTER TABLE auditoria_audp
	ALTER COLUMN caud_estado SET DEFAULT 'A'::character varying;
