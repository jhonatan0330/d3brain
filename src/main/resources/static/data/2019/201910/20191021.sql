COMMENT ON TABLE usuario_usrp IS '2019-10-21';
COMMENT ON TABLE usuariosesion_ussp IS '2019.10.21.00';

ALTER TABLE rolacceso_racp
	DROP COLUMN crac_nombre,
	DROP COLUMN crac_imagen,
	DROP COLUMN crac_codigo;

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext
	, '(select crac_nombre from rolacceso_racp where crac_llave = cdpr_rol)'
	, '(select cdpl_nombre from rolacceso_racp, documentoplantilla_dplp where cdpl_llave = crac_plantilla and crac_llave = cdpr_rol)');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cdrd_documento', 'cdpv_documento');

update usuario_usrp set cusr_estado = 'I' where cusr_llave not in (select cerl_usuario from usuariorol_erlp where cerl_estado = 'A');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'language="groovy"', 'language="java"');