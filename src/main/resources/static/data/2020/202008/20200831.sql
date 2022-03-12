COMMENT ON TABLE usuario_usrp IS '2020-08-31';

UPDATE propiedadvalordefinido_pvdp SET bpvd_propiedadboolean = TRUE WHERE cpvd_llave = 'PROP_14';

UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_41';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_54';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_58';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_59';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_69';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_74';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_90';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_109';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_120';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_122';
UPDATE propiedadvalordefinido_pvdp SET bpvd_textoculto = TRUE, bpvd_solicitamotivo = TRUE WHERE cpvd_llave = 'PROP_139';

ALTER TABLE cuenta_cuep
	DROP column if exists mcue_cierremaximo;
