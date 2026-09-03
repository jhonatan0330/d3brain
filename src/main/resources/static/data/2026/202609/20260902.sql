COMMENT ON TABLE usuario_usrp IS '2026-09-02';

ALTER TABLE documentoplantilla_dplp
  ADD COLUMN cdpl_tipo VARCHAR(1),
  ADD COLUMN cdpl_padre VARCHAR(32);