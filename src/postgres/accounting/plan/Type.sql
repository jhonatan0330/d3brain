
CREATE TABLE account.tipocomprobante_tcm (
	ctcm_llave varchar(32) NOT NULL,
	ctcm_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_tipocomprobante_tcm PRIMARY KEY (ctcm_llave)
);

ALTER TABLE account.tipocomprobante_tcm ADD ctcm_catalogo varchar(32) NOT NULL ;
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_nombre varchar(100) NOT NULL ;
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_codigo varchar(100) NOT NULL ;
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_servicio varchar(32);
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_patron varchar(1) NOT NULL ;
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_consecutivo varchar(32) NOT NULL ;
