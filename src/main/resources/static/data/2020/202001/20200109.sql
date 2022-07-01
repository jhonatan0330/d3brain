COMMENT ON TABLE usuario_usrp IS '2020-01-09';

update usuario_usrp set cusr_imagen = 'http://golyat.cloud/imagenes/avatar.png';

ALTER TABLE productocaracteristica_pcrp
	ADD COLUMN bpcr_visiblerender boolean DEFAULT false NOT NULL;
ALTER TABLE proceso_prcp
	ADD COLUMN cprc_macroproceso character varying(32);