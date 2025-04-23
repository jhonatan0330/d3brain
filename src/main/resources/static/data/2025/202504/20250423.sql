COMMENT ON TABLE usuario_usrp IS '2025-04-23';

ALTER TABLE account.comprobante_cmp
  ADD CONSTRAINT uk_comprobante_tipodocumento UNIQUE(ccmp_tipo, ccmp_documento);