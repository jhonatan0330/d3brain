COMMENT ON TABLE usuario_usrp IS '2020-06-23';

CREATE TABLE cuentaauxiliardocumento_cadp (
	ccad_llave character varying(32) NOT NULL,
	ccad_documento character varying(32) NOT NULL,
	ccad_cuentaauxiliar character varying(32) NOT NULL,
	ccad_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE cuentaauxiliarplantilla_capp (
	ccap_llave character varying(32) NOT NULL,
	ccap_cuentaprincipal character varying(32) NOT NULL,
	ccap_plantilla character varying(32) NOT NULL,
	ccap_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE cuentacontable_ctap
	ADD COLUMN ccta_cuentapadre character varying(32);

ALTER TABLE cuentaauxiliardocumento_cadp
	ADD CONSTRAINT pk_cuentaauxiliardocumento_cadp PRIMARY KEY (ccad_llave);

ALTER TABLE cuentaauxiliarplantilla_capp
	ADD CONSTRAINT pk_cuentaauxiliarplantilla_capp PRIMARY KEY (ccap_llave);

ALTER TABLE cuentaauxiliardocumento_cadp
	ADD CONSTRAINT fk_cuentaauxiliardocumentocuentaauxiliar FOREIGN KEY (ccad_cuentaauxiliar) REFERENCES public.cuentacontable_ctap(ccta_llave);

ALTER TABLE cuentaauxiliardocumento_cadp
	ADD CONSTRAINT fk_cuentaauxiliardocumentodocumento FOREIGN KEY (ccad_documento) REFERENCES public.pedidoventa_pdvp(cpdv_llave);

ALTER TABLE cuentaauxiliarplantilla_capp
	ADD CONSTRAINT fk_cuentaauxiliarplantillacuentaprincipal FOREIGN KEY (ccap_cuentaprincipal) REFERENCES public.cuentacontable_ctap(ccta_llave);

ALTER TABLE cuentaauxiliarplantilla_capp
	ADD CONSTRAINT fk_cuentaauxiliarplantillaplantilla FOREIGN KEY (ccap_plantilla) REFERENCES public.documentoplantilla_dplp(cdpl_llave);

ALTER TABLE cuentacontable_ctap
	ADD CONSTRAINT fk_cuentacontablecuentapadre FOREIGN KEY (ccta_cuentapadre) REFERENCES public.cuentacontable_ctap(ccta_llave);
