
CREATE TABLE cuenta_cue (
	ccue_llave varchar(32) NOT NULL,
	ccue_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_cuenta_cue PRIMARY KEY (ccue_llave)
);

ALTER TABLE cuenta_cue ADD dcue_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE cuenta_cue ADD ccue_creacionusuario varchar(32);
ALTER TABLE cuenta_cue ADD ccue_creacionusuarionombre varchar(200);
ALTER TABLE cuenta_cue ADD dcue_modificacionfecha timestamptz;


ALTER TABLE cuenta_cue ADD ccue_catalogo varchar(32) NOT NULL ;
ALTER TABLE cuenta_cue ADD ccue_wbs varchar(50) NOT NULL ;
ALTER TABLE cuenta_cue ADD ccue_nombre varchar(100) NOT NULL ;
ALTER TABLE cuenta_cue ADD ccue_codigo varchar(100);
ALTER TABLE cuenta_cue ADD ccue_situacion varchar(10) NOT NULL ;
ALTER TABLE cuenta_cue ADD ccue_padre varchar(32);
ALTER TABLE cuenta_cue ADD ncue_nivel int NOT NULL DEFAULT 0;
ALTER TABLE cuenta_cue ADD ccue_tipo varchar(1) NOT NULL ;
ALTER TABLE cuenta_cue ADD ccue_naturaleza varchar(1) NOT NULL ;
ALTER TABLE cuenta_cue ADD ccue_plantilla varchar(32);
ALTER TABLE cuenta_cue ADD ccue_campo varchar(32);
