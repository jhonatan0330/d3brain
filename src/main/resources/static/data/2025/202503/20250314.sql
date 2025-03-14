COMMENT ON TABLE usuario_usrp IS '2025-03-14';

ALTER TABLE account.tipocomprobante_tcm drop btcm_automatico;

ALTER TABLE account.tipocomprobante_tcm ADD ctcm_servicio varchar(32);