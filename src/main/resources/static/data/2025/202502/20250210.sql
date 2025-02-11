COMMENT ON TABLE usuario_usrp IS '2025-02-10';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_piderol, bpvd_pideusuario, bpvd_propiedadboolean)
    SELECT 'PROP_279' , 'L', 'MOSTRAR EN EL CHAT', 'CONTACT_CHAT', 'PERMISOS', true, true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_279');
    