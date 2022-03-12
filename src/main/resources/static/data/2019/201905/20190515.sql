COMMENT ON TABLE usuario_usrp IS '2019-05-15';

COMMENT ON TABLE usuariosesion_ussp IS '2019.05.15.00';

ALTER TABLE actividad_actp
	DROP COLUMN cact_responsablesiguiente;

ALTER TABLE documentorelaciongestor_drgp
	DROP COLUMN bdrg_anulable;

ALTER TABLE proceso_prcp
	ADD COLUMN bprc_manejasaldos boolean DEFAULT false NOT NULL;

update proceso_prcp  set  bprc_manejasaldos = true where cprc_llave in (select cptr_proceso from procesotransicion_ptrp where cptr_afectasaldo is not null);
