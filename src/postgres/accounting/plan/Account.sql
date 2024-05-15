
CREATE TABLE account.cuenta_cue (
	ccue_llave varchar(32) NOT NULL,
	ccue_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_cuenta_cue PRIMARY KEY (ccue_llave)
);

ALTER TABLE account.cuenta_cue ADD dcue_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.cuenta_cue ADD ccue_creacionusuario varchar(32);
ALTER TABLE account.cuenta_cue ADD ccue_creacionusuarionombre varchar(200);
ALTER TABLE account.cuenta_cue ADD dcue_modificacionfecha timestamptz;


ALTER TABLE account.cuenta_cue ADD ccue_catalogo varchar(32) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_wbs varchar(50) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_nombre varchar(100) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_codigo varchar(100);
ALTER TABLE account.cuenta_cue ADD ccue_padre varchar(32);
ALTER TABLE account.cuenta_cue ADD ncue_nivel int NOT NULL DEFAULT 0;
ALTER TABLE account.cuenta_cue ADD ccue_tipo varchar(1) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_naturaleza varchar(1) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_plantilla varchar(32);
ALTER TABLE account.cuenta_cue ADD ccue_campo varchar(32);
ALTER TABLE account.cuenta_cue ADD dcue_fechainicial timestamp with time zone NOT NULL ;
ALTER TABLE account.cuenta_cue ADD dcue_fechafinal timestamp with time zone NOT NULL ;
