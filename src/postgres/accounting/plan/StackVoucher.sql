
CREATE TABLE account.pila_stk (
	cstk_llave varchar(32) NOT NULL,
	cstk_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_pila_stk PRIMARY KEY (cstk_llave)
);

ALTER TABLE account.pila_stk ADD cstk_comprobante varchar(32) NOT NULL ;
ALTER TABLE account.pila_stk ADD dstk_creacionfecha timestamp with time zone NOT NULL ;
ALTER TABLE account.pila_stk ADD cstk_accion varchar(1);
