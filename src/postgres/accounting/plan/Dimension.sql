
CREATE TABLE dimension_dim (
	cdim_llave varchar(32) NOT NULL,
	cdim_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_dimension_dim PRIMARY KEY (cdim_llave)
);

ALTER TABLE dimension_dim ADD ddim_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE dimension_dim ADD cdim_creacionusuario varchar(32);
ALTER TABLE dimension_dim ADD cdim_creacionusuarionombre varchar(200);
ALTER TABLE dimension_dim ADD ddim_modificacionfecha timestamptz;


ALTER TABLE dimension_dim ADD cdim_cuenta varchar(32) NOT NULL ;
ALTER TABLE dimension_dim ADD cdim_nombre varchar(100) NOT NULL ;
ALTER TABLE dimension_dim ADD cdim_codigo varchar(100) NOT NULL ;
ALTER TABLE dimension_dim ADD cdim_campo varchar(32) NOT NULL ;
ALTER TABLE dimension_dim ADD cdim_tipo varchar(1) NOT NULL ;
