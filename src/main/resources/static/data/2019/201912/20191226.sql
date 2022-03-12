COMMENT ON TABLE usuario_usrp IS '2019-12-26';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'Este campo es la identificacion del documento' where cpvd_llave = 'PROP_48';
update propiedad_ppdp set cppd_motivo = 'Este campo es la identificacion del documento' where cppd_propiedadvalor = 'PROP_48';

