COMMENT ON TABLE usuario_usrp IS '2023-07-20';

ALTER TABLE transaccionlog_tlgp ADD ctlg_sesion varchar(100) NULL;

update propiedadvalordefinido_pvdp set bpvd_multiple = true
where cpvd_llave = 'PROP_80'