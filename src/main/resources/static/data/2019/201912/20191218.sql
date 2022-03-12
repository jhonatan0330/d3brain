COMMENT ON TABLE usuario_usrp IS '2019-12-18';

update propiedadvalordefinido_pvdp set cpvd_nombre = 'SOLICITAR FECHAS EN CONSULTA', bpvd_propiedadboolean = true, cpvd_motivo = 'Es necesario al momento de consultar colocar las fechas' where cpvd_llave = 'PROP_40';
update propiedad_ppdp set cppd_motivo = 'Es necesario al momento de consultar colocar las fechas' where cppd_propiedadvalor = 'PROP_40';

update propiedadvalordefinido_pvdp set cpvd_nombre = 'SOLICITAR FECHAS EN CONSULTA', bpvd_propiedadboolean = true, cpvd_motivo = 'Es necesario al momento de consultar colocar las fechas' where cpvd_llave = 'PROP_55';
update propiedad_ppdp set cppd_motivo = 'Es necesario al momento de consultar colocar las fechas' where cppd_propiedadvalor = 'PROP_55';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'El documento asignara este usuario como responsable de gestionarlo' where cpvd_llave = 'PROP_50';
update propiedad_ppdp set cppd_motivo = 'El documento asignara este usuario como responsable de gestionarlo' where cppd_propiedadvalor = 'PROP_50';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'El documento se ordenara por el nombre' where cpvd_llave = 'PROP_51';
update propiedad_ppdp set cppd_motivo = 'El documento se ordenara por el nombre' where cppd_propiedadvalor = 'PROP_51';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'El documento se ordenara por el nombre de la Z a la A' where cpvd_llave = 'PROP_52';
update propiedad_ppdp set cppd_motivo = 'El documento se ordenara por el nombre de la Z a la A' where cppd_propiedadvalor = 'PROP_52';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'Este campo es la descripcion principal del documento' where cpvd_llave = 'PROP_44';
update propiedad_ppdp set cppd_motivo = 'Este campo es la descripcion principal del documento' where cppd_propiedadvalor = 'PROP_44';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'El documento modificara los estados de este proceso' where cpvd_llave = 'PROP_37';
update propiedad_ppdp set cppd_motivo = 'El documento modificara los estados de este proceso' where cppd_propiedadvalor = 'PROP_37';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'El reporte tendra el encabezado general del sistema' where cpvd_llave = 'PROP_70';
update propiedad_ppdp set cppd_motivo = 'El reporte tendra el encabezado general del sistema' where cppd_propiedadvalor = 'PROP_70';