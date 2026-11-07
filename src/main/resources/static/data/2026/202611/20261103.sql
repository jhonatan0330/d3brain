COMMENT ON TABLE usuario_usrp IS '2026-11-03';

update propiedadvalordefinido_pvdp
set bpvd_propiedadboolean = false,bpvd_textoculto = true, cpvd_uso_motivo = null
where cpvd_llave in ('PROP_273');

