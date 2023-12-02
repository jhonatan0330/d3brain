
CREATE TABLE x_${catalogCode}_comprobante_cmp (
	ccmp_llave varchar(32) NOT NULL,
	ccmp_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_x_${catalogCode}_comprobante_cmp PRIMARY KEY (ccmp_llave)
);

ALTER TABLE x_${catalogCode}_comprobante_cmp ADD ccmp_catalogo varchar(32) NOT NULL ;
ALTER TABLE x_${catalogCode}_comprobante_cmp ADD ccmp_codigo varchar(100) NOT NULL ;
ALTER TABLE x_${catalogCode}_comprobante_cmp ADD ccmp_concepto varchar(200);
ALTER TABLE x_${catalogCode}_comprobante_cmp ADD dcmp_fechacomprobante timestamp with time zone NOT NULL ;
ALTER TABLE x_${catalogCode}_comprobante_cmp ADD dcmp_fechacreacion timestamp with time zone NOT NULL ;
ALTER TABLE x_${catalogCode}_comprobante_cmp ADD ccmp_usuariocreador varchar(32) NOT NULL ;
ALTER TABLE x_${catalogCode}_comprobante_cmp ADD mcmp_positivo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE x_${catalogCode}_comprobante_cmp ADD mcmp_negativo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE x_${catalogCode}_comprobante_cmp ADD mcmp_valor NUMERIC(18,6) NOT NULL DEFAULT 0;
