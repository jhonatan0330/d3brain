COMMENT ON TABLE usuario_usrp IS '2018-09-25';
COMMENT ON TABLE usuariosesion_ussp IS '2018.09.25.00';

delete from actividad_actp;

CREATE TABLE catalogocontable_ctgp (
	cctg_llave character varying(32) NOT NULL,
	cctg_nombre character varying(100) NOT NULL,
	dctg_fechainicial timestamp with time zone NOT NULL,
	dctg_fechafinal timestamp with time zone NOT NULL,
	cctg_codigo character varying(20) NOT NULL,
	cctg_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE comprobanteconfiguracion_cnfp (
	ccnf_llave character varying(32) NOT NULL,
	ccnf_plantilla character varying(32) NOT NULL,
	ccnf_catalogo character varying(32) NOT NULL,
	ccnf_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE comprobanteconfiguraciondetalle_ccdp (
	cccd_llave character varying(32) NOT NULL,
	cccd_configuracion character varying(32) NOT NULL,
	cccd_cuenta character varying(32) NOT NULL,
	cccd_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE comprobantecontable_comp (
	ccom_llave character varying(32) NOT NULL,
	dcom_fecha timestamp with time zone NOT NULL,
	ccom_codigo character varying(20) NOT NULL,
	ccom_documento character varying(32) NOT NULL,
	ccom_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE comprobantecuentadetalle_ccdp (
	cccd_llave character varying(32) NOT NULL,
	cccd_cuenta character varying(32) NOT NULL,
	mccd_debe numeric(18,6) DEFAULT 0 NOT NULL,
	mccd_haber numeric(18,6) DEFAULT 0 NOT NULL,
	cccd_comprobante character varying(32) NOT NULL,
	cccd_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE cuentacontable_ctap (
	ccta_llave character varying(32) NOT NULL,
	ccta_codigo character varying(20) NOT NULL,
	ccta_nombre character varying(50) NOT NULL,
	mcta_sobregiro numeric(18,6) DEFAULT 0 NOT NULL,
	ccta_catalogo character varying(32) NOT NULL,
	ccta_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE cuentacontablemovimiento_ccmp (
	cccm_llave character varying(32) NOT NULL,
	mccm_montoaplicado numeric(18,6) DEFAULT 0 NOT NULL,
	mccm_saldoinicial numeric(18,6) DEFAULT 0 NOT NULL,
	mccm_saldofinal numeric(18,6) DEFAULT 0 NOT NULL,
	cccm_anterior character varying(32),
	cccm_siguiente character varying(32),
	cccm_comprobante character varying(32) NOT NULL,
	cccm_cuenta character varying(32) NOT NULL,
	dccm_fechaevento timestamp with time zone NOT NULL,
	dccm_fecharegistro timestamp with time zone NOT NULL,
	cccm_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE actividad_actp
	ADD COLUMN cact_rol character varying(32) NOT NULL,
	ALTER COLUMN dact_fechainicio SET NOT NULL,
	ALTER COLUMN dact_fechafin SET NOT NULL;

ALTER TABLE catalogocontable_ctgp
	ADD CONSTRAINT pk_catalogocontable_ctgp PRIMARY KEY (cctg_llave);

ALTER TABLE comprobanteconfiguracion_cnfp
	ADD CONSTRAINT pk_comprobanteconfiguracion_cnfp PRIMARY KEY (ccnf_llave);

ALTER TABLE comprobanteconfiguraciondetalle_ccdp
	ADD CONSTRAINT pk_comprobanteconfiguraciondetalle_ccdp PRIMARY KEY (cccd_llave);

ALTER TABLE comprobantecontable_comp
	ADD CONSTRAINT pk_comprobantecontable_comp PRIMARY KEY (ccom_llave);

ALTER TABLE comprobantecuentadetalle_ccdp
	ADD CONSTRAINT pk_comprobantecuentadetalle_ccdp PRIMARY KEY (cccd_llave);

ALTER TABLE cuentacontable_ctap
	ADD CONSTRAINT pk_cuentacontable_ctap PRIMARY KEY (ccta_llave);

ALTER TABLE cuentacontablemovimiento_ccmp
	ADD CONSTRAINT pk_cuentacontablemovimiento_ccmp PRIMARY KEY (cccm_llave);

ALTER TABLE comprobanteconfiguracion_cnfp
	ADD CONSTRAINT fk_comprobanteconfiguracioncatalogo FOREIGN KEY (ccnf_catalogo) REFERENCES public.catalogocontable_ctgp(cctg_llave);

ALTER TABLE comprobanteconfiguracion_cnfp
	ADD CONSTRAINT fk_comprobanteconfiguracionplantilla FOREIGN KEY (ccnf_plantilla) REFERENCES public.documentoplantilla_dplp(cdpl_llave);

ALTER TABLE comprobanteconfiguraciondetalle_ccdp
	ADD CONSTRAINT fk_comprobanteconfiguraciondetalleconfiguracion FOREIGN KEY (cccd_configuracion) REFERENCES public.comprobanteconfiguracion_cnfp(ccnf_llave);

ALTER TABLE comprobanteconfiguraciondetalle_ccdp
	ADD CONSTRAINT fk_comprobanteconfiguraciondetallecuenta FOREIGN KEY (cccd_cuenta) REFERENCES public.cuentacontable_ctap(ccta_llave);

ALTER TABLE comprobantecontable_comp
	ADD CONSTRAINT fk_comprobantecontabledocumento FOREIGN KEY (ccom_documento) REFERENCES public.pedidoventa_pdvp(cpdv_llave);

ALTER TABLE comprobantecuentadetalle_ccdp
	ADD CONSTRAINT fk_comprobantecuentadetallecomprobante FOREIGN KEY (cccd_comprobante) REFERENCES public.comprobantecontable_comp(ccom_llave);

ALTER TABLE comprobantecuentadetalle_ccdp
	ADD CONSTRAINT fk_comprobantecuentadetallecuenta FOREIGN KEY (cccd_cuenta) REFERENCES public.cuentacontable_ctap(ccta_llave);

ALTER TABLE cuentacontable_ctap
	ADD CONSTRAINT fk_cuentacontablecatalogo FOREIGN KEY (ccta_catalogo) REFERENCES public.catalogocontable_ctgp(cctg_llave);

ALTER TABLE cuentacontablemovimiento_ccmp
	ADD CONSTRAINT fk_cuentacontablemovimientocomprobante FOREIGN KEY (cccm_comprobante) REFERENCES public.comprobantecuentadetalle_ccdp(cccd_llave);

ALTER TABLE cuentacontablemovimiento_ccmp
	ADD CONSTRAINT fk_cuentacontablemovimientocuenta FOREIGN KEY (cccm_cuenta) REFERENCES public.cuentacontable_ctap(ccta_llave);

ALTER TABLE actividad_actp
	DROP COLUMN cact_rol;

ALTER TABLE actividad_actp
	DROP CONSTRAINT fk_actividadresponsable;

ALTER TABLE actividad_actp
	ADD CONSTRAINT fk_actividadresponsable FOREIGN KEY (cact_responsable) REFERENCES public.usuariorol_erlp(cerl_llave);

