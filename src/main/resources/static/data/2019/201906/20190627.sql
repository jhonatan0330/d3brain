COMMENT ON TABLE usuario_usrp IS '2019-06-27';

delete from permiso_perp where cper_modulo  in (select cmdc_llave from modulocontratado_mdcp  where cmdc_modulo = 'Ventas');