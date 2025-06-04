
CREATE TABLE account.registroauxiliar_rax (
	crax_llave varchar(32) NOT NULL,
	crax_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_registroauxiliar_rax PRIMARY KEY (crax_llave)
);

ALTER TABLE account.registroauxiliar_rax ADD crax_comprobante varchar(32) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_registro varchar(32) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_cuenta varchar(32) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_auxiliartipo varchar(32) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_auxiliardocumento varchar(32);
