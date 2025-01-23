COMMENT ON TABLE usuario_usrp IS '2025-01-23';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_piderol, bpvd_pideusuario)
    SELECT 'PROP_278' , 'L', 'TEMPLATE COMPROBANTE CONTABLE', 'TEMPLATE_VOUCHER', 'PERMISOS', true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_278');
    