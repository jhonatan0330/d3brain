
CREATE TABLE config.configtemplaterelation_ctr (
	cctr_llave varchar(32) NOT NULL,
	cctr_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_configtemplaterelation_ctr PRIMARY KEY (cctr_llave)
);

ALTER TABLE config.configtemplaterelation_ctr ADD cctr_entity varchar(32) NOT NULL ;
ALTER TABLE config.configtemplaterelation_ctr ADD cctr_entityfield varchar(100) NOT NULL ;
ALTER TABLE config.configtemplaterelation_ctr ADD cctr_template varchar(32) NOT NULL ;
ALTER TABLE config.configtemplaterelation_ctr ADD cctr_templatefield varchar(32) NOT NULL ;
