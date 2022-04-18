COMMENT ON TABLE usuario_usrp IS '2022-04-17';



CREATE TABLE transaccionerror_terp (
	cter_llave character varying(32) NOT NULL,
	dter_fechainicio timestamp with time zone NOT NULL,
	dter_fechafin timestamp with time zone NOT NULL,
	cter_error character varying(4000),
	cter_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE transaccionlog_tlgp (
	ctlg_llave character varying(32) NOT NULL,
	dtlg_fechainicio timestamp with time zone NOT NULL,
	dtlg_fechafin timestamp with time zone NOT NULL,
	ctlg_transaccion character varying(32),
	ctlg_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE documentotransaccion_trap
	DROP COLUMN dtra_fechafin,
	DROP COLUMN ctra_error,
	DROP COLUMN ctra_sincronize;
	

ALTER TABLE transaccionerror_terp
	ADD CONSTRAINT pk_transaccionerror_terp PRIMARY KEY (cter_llave);

ALTER TABLE transaccionlog_tlgp
	ADD CONSTRAINT pk_transaccionlog_tlgp PRIMARY KEY (ctlg_llave);
