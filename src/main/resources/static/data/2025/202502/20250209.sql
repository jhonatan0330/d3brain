COMMENT ON TABLE usuario_usrp IS '2025-02-09';

CREATE INDEX ix_webserviceejecucion_fecha
	ON webserviceejecucion_wsep
  USING btree
  (dwse_fecha);

 ALTER TABLE account.tipocomprobante_tcm ALTER COLUMN ctcm_consecutivo DROP NOT NULL;