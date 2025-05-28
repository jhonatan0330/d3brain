
CREATE TABLE account.pila_stk (
	cstk_llave varchar(32) NOT NULL,
	cstk_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_pila_stk PRIMARY KEY (cstk_llave)
);

ALTER TABLE account.pila_stk ADD cstk_comprobante varchar(32) NOT NULL ;
ALTER TABLE account.pila_stk ADD cstk_accion varchar(1);

ALTER TABLE account.pila_stk ADD dstk_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.pila_stk ADD cstk_creacionusuario varchar(32);
ALTER TABLE account.pila_stk ADD cstk_creacionusuarionombre varchar(200);
ALTER TABLE account.pila_stk ADD dstk_modificacionfecha timestamptz;
