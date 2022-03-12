
COMMENT ON TABLE usuario_usrp IS '2020-02-04';

COMMENT ON TABLE usuariosesion_ussp IS '2020.02.04.00';

DROP TABLE cuentapermisousuario_cpup;

DROP TABLE catalogo_catp;

ALTER TABLE mensaje_msjp
	ADD COLUMN cmsj_correo character varying(200),
	ALTER COLUMN cmsj_usuario DROP NOT NULL;

delete from permiso_perp where cper_modulo in (select cmdc_llave from modulocontratado_mdcp where cmdc_modulo in ('UITarifario', 'Ventas'));
delete from modulocontratado_mdcp where cmdc_modulo in ('UITarifario', 'Ventas');
delete from modulo_modp  where cmod_llave in ('UITarifario', 'Ventas');


