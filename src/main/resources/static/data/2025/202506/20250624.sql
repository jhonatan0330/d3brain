COMMENT ON TABLE usuario_usrp IS '2025-06-24';

ALTER TABLE account.comprobante_cmp ADD dcmp_creacionfecha timestamp with time zone  ;

update account.comprobante_cmp set dcmp_creacionfecha = now() where dcmp_creacionfecha is null;

ALTER TABLE account.comprobante_cmp ALTER COLUMN dcmp_creacionfecha SET NOT NULL;

ALTER TABLE usuarioautenticacion_uaup ADD duau_fechacreacion timestamp with time zone  ;

update usuarioautenticacion_uaup set duau_fechacreacion = now() where duau_fechacreacion is null;

ALTER TABLE usuarioautenticacion_uaup ALTER COLUMN duau_fechacreacion SET NOT NULL;