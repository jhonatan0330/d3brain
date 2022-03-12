
COMMENT ON TABLE usuario_usrp IS '2018-08-11';
COMMENT ON TABLE usuariosesion_ussp IS '2018.08.11.00';

ALTER TABLE procesotransicion_ptrp
	ADD COLUMN bptr_responsableobligatorio boolean DEFAULT false NOT NULL;

ALTER TABLE proceso_prcp
	ADD COLUMN cprc_codigo character varying(50),
	ADD COLUMN cprc_descripcion character varying(4000);

update proceso_prcp set cprc_codigo = cprc_nombre;

ALTER TABLE procesotransicion_ptrp
	ALTER COLUMN cptr_estadollegada DROP NOT NULL;

ALTER TABLE proceso_prcp
	ALTER COLUMN cprc_codigo SET NOT NULL;