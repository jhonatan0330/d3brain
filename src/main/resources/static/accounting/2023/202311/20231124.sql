COMMENT ON TABLE catalogo_ctg IS '2023-11-24';

ALTER TABLE catalogo_ctg ADD cctg_consecutivo varchar(32);

ALTER TABLE cuenta_cue ADD CONSTRAINT FK_CuentaCatalogo FOREIGN KEY (ccue_catalogo) REFERENCES catalogo_ctg(cctg_llave);