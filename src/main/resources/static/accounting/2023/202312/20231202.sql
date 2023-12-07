COMMENT ON TABLE catalogo_ctg IS '2023-12-01';

CREATE TABLE tipocomprobante_tcm (
	ctcm_llave varchar(32) NOT NULL,
	ctcm_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_tipocomprobante_tcm PRIMARY KEY (ctcm_llave)
);

ALTER TABLE tipocomprobante_tcm ADD dtcm_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE tipocomprobante_tcm ADD ctcm_creacionusuario varchar(32);
ALTER TABLE tipocomprobante_tcm ADD ctcm_creacionusuarionombre varchar(200);
ALTER TABLE tipocomprobante_tcm ADD dtcm_modificacionfecha timestamptz;


ALTER TABLE tipocomprobante_tcm ADD ctcm_catalogo varchar(32) NOT NULL ;
ALTER TABLE tipocomprobante_tcm ADD ctcm_nombre varchar(100) NOT NULL ;
ALTER TABLE tipocomprobante_tcm ADD ctcm_codigo varchar(100) NOT NULL ;
