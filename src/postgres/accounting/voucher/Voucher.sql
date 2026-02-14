
CREATE TABLE account.comprobante_cmp (
	ccmp_llave varchar(32) NOT NULL,
	ccmp_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_comprobante_cmp PRIMARY KEY (ccmp_llave)
);

ALTER TABLE account.comprobante_cmp ADD ccmp_catalogo varchar(32) NOT NULL ;
ALTER TABLE account.comprobante_cmp ADD ccmp_codigo varchar(100) NOT NULL ;
ALTER TABLE account.comprobante_cmp ADD ccmp_tipo varchar(32) NOT NULL ;
ALTER TABLE account.comprobante_cmp ADD ccmp_concepto varchar(200);
ALTER TABLE account.comprobante_cmp ADD dcmp_fechacomprobante timestamp with time zone NOT NULL ;
ALTER TABLE account.comprobante_cmp ADD mcmp_valor NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.comprobante_cmp ADD ccmp_documento varchar(32);
ALTER TABLE account.comprobante_cmp ADD ccmp_expediente varchar(32);
ALTER TABLE account.comprobante_cmp ADD dcmp_fechaanulacion timestamp with time zone;
ALTER TABLE account.comprobante_cmp ADD dcmp_creacionfecha timestamp with time zone NOT NULL ;
