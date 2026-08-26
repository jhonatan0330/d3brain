COMMENT ON TABLE usuario_usrp IS '2026-08-24';

CREATE TABLE IF NOT EXISTS cargamasiva_cmvp (
	ccmv_llave character varying(32) NOT NULL,
	dcmv_fecha timestamp with time zone,
	ccmv_usuario character varying(32),
	ccmv_archivo character varying(255),
	ccmv_estado character varying(1) DEFAULT 'A'::character varying NOT NULL,
	ccmv_mensaje text,
	ccmv_plantilla character varying(32),
	ccmv_progreso character varying(255),
	CONSTRAINT cargamasiva_cmvp_pkey PRIMARY KEY (ccmv_llave)
);

CREATE TABLE IF NOT EXISTS cargamasivaitem_cmip (
	ccmi_llave character varying(32) NOT NULL,
	ccmi_carga character varying(32) NOT NULL,
	ccmi_modelo text,
	ccmi_progreso character varying(255),
	ccmi_estado character varying(1) DEFAULT 'A'::character varying NOT NULL,
	ccmi_documento character varying(32),
	ccmi_nombre character varying(255),
	ccmi_fechaserializacion timestamp with time zone,
	ccmi_fechasincronizacion timestamp with time zone,
	CONSTRAINT cargamasivaitem_cmip_pkey PRIMARY KEY (ccmi_llave)
);

ALTER TABLE cargamasivaitem_cmip
	ADD CONSTRAINT cargamasivaitem_cmip_fk_carga FOREIGN KEY (ccmi_carga)
	REFERENCES cargamasiva_cmvp (ccmv_llave);
