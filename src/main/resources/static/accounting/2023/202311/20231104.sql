COMMENT ON TABLE catalogo_ctg IS '2023-11-04';

ALTER TABLE cuenta_cue ADD ccue_wbs varchar(50);

update cuenta_cue set ccue_wbs = '1.';

ALTER TABLE cuenta_cue ALTER COLUMN ccue_wbs SET NOT NULL;