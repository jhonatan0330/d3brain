COMMENT ON TABLE usuario_usrp IS '2025-01-28';

CREATE TABLE account.pila_stk (
	cstk_llave varchar(32) NOT NULL,
	cstk_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_pila_stk PRIMARY KEY (cstk_llave)
);

ALTER TABLE account.pila_stk ADD cstk_comprobante varchar(32) NOT NULL ;

ALTER TABLE account.pila_stk ADD dstk_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.pila_stk ADD cstk_creacionusuario varchar(32);
ALTER TABLE account.pila_stk ADD cstk_creacionusuarionombre varchar(200);
ALTER TABLE account.pila_stk ADD dstk_modificacionfecha timestamptz;


ALTER TABLE account.comprobante_cmp DROP COLUMN mcmp_negativo;
ALTER TABLE account.comprobante_cmp DROP COLUMN mcmp_positivo;
ALTER TABLE account.comprobante_cmp DROP COLUMN ccmp_tiponombre;
ALTER TABLE account.tipocomprobante_tcm ADD btcm_automatico bool NOT NULL DEFAULT false;

ALTER TABLE account.catalogo_ctg DROP COLUMN cctg_consecutivo;

ALTER TABLE account.tipocomprobante_tcm ADD ctcm_consecutivo varchar(32) NOT NULL ;
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_patron varchar(1) NOT NULL ;