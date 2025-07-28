COMMENT ON TABLE usuario_usrp IS '2025-07-23';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
    SELECT 'PROP_289' , 'C', 'HTML DEL CAMPO CONSULTADO EN BD', 'HTML_DOCUMENT_SQL', 'REQUISITO', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_289');
