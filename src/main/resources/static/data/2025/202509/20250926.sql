COMMENT ON TABLE usuario_usrp IS '2025-09-26';

ALTER TABLE public.usuariorol_erlp DROP CONSTRAINT If exists fk_usuarioroldocumento;

ALTER TABLE account.catalogo_ctg ALTER COLUMN dctg_fechainicial DROP NOT NULL;

ALTER TABLE account.catalogo_ctg ALTER COLUMN cctg_documento DROP NOT NULL;

ALTER TABLE account.catalogo_ctg ALTER COLUMN cctg_plantila DROP NOT NULL;

ALTER TABLE account.cuenta_cue ALTER COLUMN ccue_documento DROP NOT NULL;