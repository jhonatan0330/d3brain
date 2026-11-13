COMMENT ON TABLE usuario_usrp IS '2026-11-11';

CREATE TABLE account.indicadormeta_imt (
	cimt_llave varchar(32) NOT NULL,
	cimt_indicador varchar(32) NOT NULL,
	cimt_nombre varchar(100) NOT NULL,
	nimt_valorsuperior numeric(18,2) NULL,
	nimt_valorinferior numeric(18,2) NULL,
	cimt_color varchar(20) NULL,
	cimt_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	CONSTRAINT pk_indicadormeta_int PRIMARY KEY (cimt_llave),
	CONSTRAINT fk_indicadormeta_indicador FOREIGN KEY (cimt_indicador) REFERENCES account.indicador_ind(cind_llave)
);
