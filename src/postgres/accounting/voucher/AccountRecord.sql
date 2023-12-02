
CREATE TABLE x_${catalogCode}_registro_reg (
	creg_llave varchar(32) NOT NULL,
	creg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_x_${catalogCode}_registro_reg PRIMARY KEY (creg_llave)
);

ALTER TABLE x_${catalogCode}_registro_reg ADD dreg_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE x_${catalogCode}_registro_reg ADD creg_creacionusuario varchar(32);
ALTER TABLE x_${catalogCode}_registro_reg ADD creg_creacionusuarionombre varchar(200);
ALTER TABLE x_${catalogCode}_registro_reg ADD dreg_modificacionfecha timestamptz;


ALTER TABLE x_${catalogCode}_registro_reg ADD creg_comprobante varchar(32) NOT NULL ;
ALTER TABLE x_${catalogCode}_registro_reg ADD creg_cuenta varchar(32) NOT NULL ;
ALTER TABLE x_${catalogCode}_registro_reg ADD creg_codigo varchar(100) NOT NULL ;
ALTER TABLE x_${catalogCode}_registro_reg ADD creg_descripcion varchar(200);
ALTER TABLE x_${catalogCode}_registro_reg ADD dreg_fecha timestamp with time zone NOT NULL ;
ALTER TABLE x_${catalogCode}_registro_reg ADD mreg_positivo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE x_${catalogCode}_registro_reg ADD mreg_negativo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE x_${catalogCode}_registro_reg ADD mreg_valor NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE x_${catalogCode}_registro_reg ADD creg_tercero varchar(32);
ALTER TABLE x_${catalogCode}_registro_reg ADD creg_terceroid varchar(200);
ALTER TABLE x_${catalogCode}_registro_reg ADD creg_terceronombre varchar(200);
