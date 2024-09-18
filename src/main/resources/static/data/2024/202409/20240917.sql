COMMENT ON TABLE usuario_usrp IS '2024-09-17';

update  propiedad_ppdp pp
set cppd_motivo = null
where cppd_estado = 'A' and cppd_motivo = '';