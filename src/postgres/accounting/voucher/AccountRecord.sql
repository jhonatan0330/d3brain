
CREATE TABLE account.registro_reg (
	creg_llave varchar(32) NOT NULL,
	creg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_registro_reg PRIMARY KEY (creg_llave)
);

ALTER TABLE account.registro_reg ADD creg_comprobante varchar(32) NOT NULL ;
ALTER TABLE account.registro_reg ADD creg_cuenta varchar(32) NOT NULL ;
ALTER TABLE account.registro_reg ADD creg_descripcion varchar(200);
ALTER TABLE account.registro_reg ADD dreg_fecha timestamp with time zone NOT NULL ;
ALTER TABLE account.registro_reg ADD mreg_positivo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.registro_reg ADD mreg_negativo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.registro_reg ADD mreg_valor NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.registro_reg ADD creg_tipo varchar(1);
