 
CREATE TABLE cargamasiva_cmvp(
        ccmv_llave character varying(32) NOT NULL,
        dcmv_fecha timestamp with time zone NOT NULL,
        ccmv_usuario character varying(32) NOT NULL,
        ccmv_archivo character varying(2000) NOT NULL,
        ccmv_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_cargamasiva_cmvp PRIMARY KEY (ccmv_llave)
    );
 
CREATE TABLE cargamasivaerror_cmep(
        ccme_llave character varying(32) NOT NULL,
        ccme_carga character varying(32) NOT NULL,
        dcme_fecha timestamp with time zone NOT NULL,
        ccme_error character varying(4000) NOT NULL,
        ccme_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_cargamasivaerror_cmep PRIMARY KEY (ccme_llave)
    );
