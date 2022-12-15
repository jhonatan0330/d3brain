COMMENT ON TABLE usuario_usrp IS '2022-12-15';

CREATE INDEX IF NOT EXISTS ix_procesotransicionautomatica_ejecucion
	ON procesotransicionautomatica_ptap
  USING btree
  (dpta_ejecucion);