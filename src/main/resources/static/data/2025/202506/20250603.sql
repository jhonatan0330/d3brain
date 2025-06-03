COMMENT ON TABLE usuario_usrp IS '2025-06-03';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple)
    SELECT 'PROP_286' , 'L', 'NOTIFICACIONES DE UN DOCUMENTO', 'TEMPLATE_MESSAGE_SQL', 'REQUISITO', true, true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_286');
