COMMENT ON TABLE usuario_usrp IS '2024-05-14';

ALTER TABLE account.cuenta_cue ADD dcue_fechainicial timestamp with time zone;
ALTER TABLE account.cuenta_cue ADD dcue_fechafinal timestamp with time zone;

ALTER TABLE account.cuenta_cue DROP COLUMN ccue_situacion;

ALTER TABLE account.cuenta_cue ADD CONSTRAINT fk_cuentacatalogo FOREIGN KEY (ccue_catalogo) REFERENCES account.catalogo_ctg(cctg_llave);

delete from account.mapa_resultados_rmp ;
