COMMENT ON TABLE catalogo_ctg IS '2023-12-01';

ALTER TABLE valor_vlr ADD dvlr_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE valor_vlr ADD cvlr_creacionusuario varchar(32);
ALTER TABLE valor_vlr ADD cvlr_creacionusuarionombre varchar(200);
ALTER TABLE valor_vlr ADD dvlr_modificacionfecha timestamptz;

ALTER TABLE formato_frm ADD dfrm_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE formato_frm ADD cfrm_creacionusuario varchar(32);
ALTER TABLE formato_frm ADD cfrm_creacionusuarionombre varchar(200);
ALTER TABLE formato_frm ADD dfrm_modificacionfecha timestamptz;

ALTER TABLE linea_lin ADD dlin_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE linea_lin ADD clin_creacionusuario varchar(32);
ALTER TABLE linea_lin ADD clin_creacionusuarionombre varchar(200);
ALTER TABLE linea_lin ADD dlin_modificacionfecha timestamptz;

ALTER TABLE hecho_hch ADD dhch_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE hecho_hch ADD chch_creacionusuario varchar(32);
ALTER TABLE hecho_hch ADD chch_creacionusuarionombre varchar(200);
ALTER TABLE hecho_hch ADD dhch_modificacionfecha timestamptz;

ALTER TABLE dimension_dim ADD ddim_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE dimension_dim ADD cdim_creacionusuario varchar(32);
ALTER TABLE dimension_dim ADD cdim_creacionusuarionombre varchar(200);
ALTER TABLE dimension_dim ADD ddim_modificacionfecha timestamptz;

ALTER TABLE catalogo_ctg ADD dctg_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE catalogo_ctg ADD cctg_creacionusuario varchar(32);
ALTER TABLE catalogo_ctg ADD cctg_creacionusuarionombre varchar(200);
ALTER TABLE catalogo_ctg ADD dctg_modificacionfecha timestamptz;

ALTER TABLE cuenta_cue ADD dcue_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE cuenta_cue ADD ccue_creacionusuario varchar(32);
ALTER TABLE cuenta_cue ADD ccue_creacionusuarionombre varchar(200);
ALTER TABLE cuenta_cue ADD dcue_modificacionfecha timestamptz;

