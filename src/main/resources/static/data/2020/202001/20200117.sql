COMMENT ON TABLE usuario_usrp IS '2020-01-17';

update propiedadvalordefinido_pvdp set bpvd_propiedadboolean = true where cpvd_llave in ('PROP_77', 'PROP_78', 'PROP_79', 'PROP_81', 'PROP_85');

update propiedadvalordefinido_pvdp set cpvd_codigo = 'FUNCION_SQL_VALIDAR' where cpvd_codigo = 'FUNCION_VALIDAR';

update propiedadvalordefinido_pvdp set cpvd_codigo = 'PROCESO_FUNCION_SQL' where cpvd_codigo = 'PROCESO_FUNCION';

ALTER TABLE documentorelaciongestor_drgp
	ADD COLUMN cdrg_ubicacion character varying(32);

update documentoplantilla_dplp set cdpl_tipo = 'F' where cdpl_tipo = 'O';
