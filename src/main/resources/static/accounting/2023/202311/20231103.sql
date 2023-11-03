COMMENT ON TABLE catalogo_ctg IS '2023-11-03';

ALTER TABLE cuenta_cue ADD ccue_situacion varchar(10);

update cuenta_cue set ccue_situacion= 'PLANNING';

ALTER TABLE cuenta_cue ALTER COLUMN ccue_situacion SET NOT NULL;