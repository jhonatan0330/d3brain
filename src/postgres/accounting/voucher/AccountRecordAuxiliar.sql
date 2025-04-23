
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
ALTER TABLE account.registroauxiliar_rax ADD crax_auxiliarcode varchar(200) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_auxiliarnombre varchar(200) NOT NULL ;

ALTER TABLE account.registroauxiliar_rax ADD drax_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.registroauxiliar_rax ADD crax_creacionusuario varchar(32);
ALTER TABLE account.registroauxiliar_rax ADD crax_creacionusuarionombre varchar(200);
ALTER TABLE account.registroauxiliar_rax ADD drax_modificacionfecha timestamptz;
