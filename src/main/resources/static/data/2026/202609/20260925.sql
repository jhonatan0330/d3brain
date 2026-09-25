COMMENT ON TABLE usuario_usrp IS '2026-09-25';

ALTER TABLE tenant_ten ADD COLUMN IF NOT EXISTS cten_codigo varchar(32);


CREATE UNIQUE INDEX IF NOT EXISTS idx_tenant_ten_codigo ON tenant_ten(cten_codigo);
