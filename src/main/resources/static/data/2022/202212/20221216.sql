COMMENT ON TABLE usuario_usrp IS '2022-12-16';

CREATE INDEX IF NOT EXISTS ix_procesotransicionautomatica_transicion
	ON procesotransicionautomatica_ptap
  USING btree
  (cpta_transicion);