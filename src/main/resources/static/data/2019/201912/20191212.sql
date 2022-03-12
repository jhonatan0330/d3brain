COMMENT ON TABLE usuario_usrp IS '2019-12-12';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'El documento queda registrado con el valor registrado en este campo' where cpvd_llave = 'PROP_47';
update propiedad_ppdp set cppd_motivo = 'El documento queda registrado con el valor registrado en este campo' where cppd_propiedadvalor = 'PROP_47';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'El documento queda registrado con la fecha colocada en este campo' where cpvd_llave = 'PROP_49';
update propiedad_ppdp set cppd_motivo = 'El documento queda registrado con la fecha colocada en este campo' where cppd_propiedadvalor = 'PROP_49';

update propiedadvalordefinido_pvdp set cpvd_nombre = 'FORMATO MONEDA', bpvd_propiedadboolean = true, cpvd_motivo = 'Permite visualizar el campo con el formato de moneda configurado en el sistema' where cpvd_llave = 'PROP_27';
update propiedad_ppdp set cppd_motivo = 'El documento queda registrado con la fecha colocada en este campo' where cppd_propiedadvalor = 'PROP_27';

update propiedadvalordefinido_pvdp set cpvd_nombre = 'TEXTO LARGO', bpvd_propiedadboolean = true, cpvd_motivo = 'Permite colocar una descripcion larga con mayusculas y minusculas' where cpvd_llave = 'PROP_01';
update propiedad_ppdp set cppd_motivo = 'Permite colocar una descripcion larga con mayusculas y minusculas' where cppd_propiedadvalor = 'PROP_01';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_origencategoria, cpvd_motivo, bpvd_textoculto) 
	VALUES('PROP_98' , 'C', 'TIPOS SOPORTADOS', 'ARCHIVO_TIPO', 'www.softwareparati.com', 'REQUISITO', 'A', 'Permite subir archivos de los siguientes tipos', true);
	
update propiedadvalordefinido_pvdp set cpvd_nombre = 'MOSTRAR EN POP UP', bpvd_propiedadboolean = true, cpvd_motivo = 'Muestra el listado en pantalla completa por encima del formulario y deja  escoger la deseada' where cpvd_llave = 'PROP_35';
update propiedad_ppdp set cppd_motivo = 'Muestra el listado en pantalla completa por encima del formulario y deja  escoger la deseada' where cppd_propiedadvalor = 'PROP_35';

update propiedadvalordefinido_pvdp set bpvd_propiedadboolean = true, cpvd_motivo = 'Permite que se carguen automaticamente las opciones que se pueden escoger al cargar el formulario' where cpvd_llave = 'PROP_20';
update propiedad_ppdp set cppd_motivo = 'Permite que se carguen automaticamente las opciones que se pueden escoger al cargar el formulario' where cppd_propiedadvalor = 'PROP_20';

update propiedadvalordefinido_pvdp set cpvd_nombre = 'ACCIONES CRUD', bpvd_propiedadboolean = true, cpvd_motivo = 'Permite realizar acciones de crear o modificar desde este campo. Teniendo en cuenta los permisos que asigne el administrador' where cpvd_llave = 'PROP_36';
update propiedad_ppdp set cppd_motivo = 'Permite realizar acciones de crear o modificar desde este campo. Teniendo en cuenta los permisos que asigne el administrador' where cppd_propiedadvalor = 'PROP_36';

update propiedadvalordefinido_pvdp set cpvd_nombre = 'FECHA CON HORA' where cpvd_llave = 'PROP_97';
