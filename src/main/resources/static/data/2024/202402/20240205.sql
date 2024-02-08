COMMENT ON TABLE usuario_usrp IS '2024-02-05';

CREATE SCHEMA config AUTHORIZATION postgres;

CREATE TABLE config.configtemplaterelation_ctr (
	cctr_llave varchar(32) NOT NULL,
	cctr_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_configtemplaterelation_ctr PRIMARY KEY (cctr_llave)
);

ALTER TABLE config.configtemplaterelation_ctr ADD dctr_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE config.configtemplaterelation_ctr ADD cctr_creacionusuario varchar(32);
ALTER TABLE config.configtemplaterelation_ctr ADD cctr_creacionusuarionombre varchar(200);
ALTER TABLE config.configtemplaterelation_ctr ADD dctr_modificacionfecha timestamptz;


ALTER TABLE config.configtemplaterelation_ctr ADD cctr_entity varchar(32) NOT NULL ;
ALTER TABLE config.configtemplaterelation_ctr ADD cctr_ varchar(100) NOT NULL ;
ALTER TABLE config.configtemplaterelation_ctr ADD cctr_template varchar(32) NOT NULL ;
ALTER TABLE config.configtemplaterelation_ctr ADD cctr_templatefield varchar(32) NOT NULL ;

update propiedadvalordefinido_pvdp set cpvd_estado = 'I' where cpvd_llave= 'PROP_101';
update propiedadvalordefinido_pvdp set cpvd_estado = 'I' where cpvd_llave= 'PROP_102';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
	select
	'PROP_255' , 'L', 'TIPO CONFIGURACION HOMOLOGADA', 'PLANTILLA_TIPO_CONFIGURATION', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_255');

ALTER TABLE documentoplantillacaracteristica_dpcp ALTER COLUMN cdpc_objetivo DROP NOT NULL;

ALTER TABLE tarifario_trfp ADD ctrf_documento varchar(32);

