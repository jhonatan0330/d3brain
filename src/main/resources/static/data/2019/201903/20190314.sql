COMMENT ON TABLE usuario_usrp IS '2019-03-14';
COMMENT ON TABLE usuariosesion_ussp IS '2019.03.14.00';

ALTER TABLE documentoplantilla_dplp
	ADD COLUMN cdpl_ayudas character varying(2000);

ALTER TABLE documentoplantillarol_dprp
	ADD COLUMN cdpr_filtroestados character varying(100);

ALTER TABLE modeladonegocio_mngp
	ADD COLUMN dmng_fechavalidacion timestamp with time zone,
	ADD COLUMN cmng_resultado character varying(2000);
