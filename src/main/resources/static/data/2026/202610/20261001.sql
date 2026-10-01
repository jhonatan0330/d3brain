COMMENT ON TABLE usuario_usrp IS '2026-10-01';

ALTER TABLE tenant_ten ADD COLUMN IF NOT EXISTS cten_fechavalidez DATE;

