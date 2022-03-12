
COMMENT ON TABLE usuario_usrp IS '2019-01-02';
COMMENT ON TABLE usuariosesion_ussp IS '2019.01.02.00';

INSERT INTO reportebase_rpbp (crpb_llave, crpb_nombre, crpb_jaspertext, crpb_plantilla, crpb_codigo, brpb_soloexistente)
SELECT 'PER001-' ||substring(cdpl_llave, 0,25), 'PERMISOS DE USUARIO', '', cdpl_llave, 'PER001', true FROM documentoplantilla_dplp where cdpl_llave in (select crac_plantilla from rolacceso_racp);