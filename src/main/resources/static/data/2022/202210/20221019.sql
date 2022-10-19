COMMENT ON TABLE usuario_usrp IS '2022-10-19';

 
CREATE TABLE cargamasiva_cmvp(
        ccmv_llave character varying(32) NOT NULL,
        dcmv_fecha timestamp with time zone NOT NULL,
        ccmv_usuario character varying(32) NOT NULL,
        ccmv_archivo character varying(2000) NOT NULL,
        ccmv_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_cargamasiva_cmvp PRIMARY KEY (ccmv_llave)
    );
 
 
CREATE TABLE cargamasivaitem_cmip(
        ccmi_llave character varying(32) NOT NULL,
        ccmi_carga character varying(32) NOT NULL,
        ccmi_modelo character varying(4000) NOT NULL,
        ccmi_progreso character varying(1) NOT NULL,
        ccmi_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_cargamasivaitem_cmip PRIMARY KEY (ccmi_llave)
    );
