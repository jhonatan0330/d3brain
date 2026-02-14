COMMENT ON TABLE usuario_usrp IS '2026-02-13';

ALTER TABLE account.comprobante_cmp ADD ccmp_expediente varchar(32);
ALTER TABLE account.registro_reg ADD creg_expediente varchar(32);
ALTER TABLE account.registro_reg ADD creg_vinculocuenta varchar(32);