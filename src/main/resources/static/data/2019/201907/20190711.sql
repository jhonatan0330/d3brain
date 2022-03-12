COMMENT ON TABLE usuario_usrp IS '2019-07-11';

update actividad_actp set cact_comentario = 'Asignacion automatica' where cact_comentario is null;

ALTER TABLE actividad_actp
	ALTER COLUMN cact_comentario SET NOT NULL;

delete from permiso_perp where cper_modulo  in (select cmdc_llave from modulocontratado_mdcp where cmdc_modulo in ('UIRoles', 'UIOperacion', 'UIPasajes', 'UIControlRodamiento', 'UIDespachos', 'Inventarios'));
delete from modulocontratado_mdcp where cmdc_modulo in ('UIRoles', 'UIOperacion', 'UIPasajes', 'UIControlRodamiento', 'UIDespachos', 'Inventarios');
delete from modulo_modp where cmod_llave in ('UIRoles', 'UIOperacion', 'UIPasajes', 'UIControlRodamiento', 'UIDespachos', 'Inventarios');


