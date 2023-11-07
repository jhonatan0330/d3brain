COMMENT ON TABLE catalogo_ctg IS '2023-11-04';

ALTER TABLE cuenta_cue ADD ncue_nivel int NOT NULL DEFAULT 0;

ALTER TABLE cuenta_cue ALTER COLUMN ccue_codigo DROP NOT NULL;