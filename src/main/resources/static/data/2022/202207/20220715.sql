
COMMENT ON TABLE usuario_usrp IS '2022-07-15';

ALTER TABLE usuarioautenticacion_uaup
	ADD COLUMN duau_fechacreacion timestamp with time zone;

ALTER TABLE usuario_usrp
	ADD COLUMN cusr_telefono character varying(1);