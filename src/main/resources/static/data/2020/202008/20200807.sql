COMMENT ON TABLE usuario_usrp IS '2020-08-07';

update propiedadvalordefinido_pvdp set bpvd_propiedadboolean = true
	where cpvd_llave = 'PROP_22';

update propiedadvalordefinido_pvdp set bpvd_pideusuario = true
	where cpvd_llave = 'PROP_140';

ALTER TABLE propiedadvalordefinido_pvdp
	DROP COLUMN cpvd_ayuda;
