
COMMENT ON TABLE usuario_usrp IS '2020-01-15';
COMMENT ON TABLE usuariosesion_ussp IS '2020.01.15.00';

ALTER TABLE propiedad_ppdp
	ADD COLUMN cppd_rol character varying(32);

ALTER TABLE propiedad_ppdp
	ADD CONSTRAINT fk_propiedadrol FOREIGN KEY (cppd_rol) REFERENCES rolacceso_racp(crac_llave);

INSERT INTO cambio_cmbp (ccmb_llave, ccmb_nombre, ccmb_motivo, dcmb_fecha, dcmb_fechaaplicacion)
	VALUES('SC-20200115', 'SC-SIS-20200115', 'Integrar los permisos de roles en propiedades', now(), now());

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_103' , 'C', 'OBLIGATORIO', 'PERMISO_CAMPO_OBLIGATORIO', 'www.softwareparati.com', 'PERMISOS', 'Es obligatorio registrar la informacion del campo', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_104' , 'C', 'VISIBLE EN EL RENDER', 'PERMISO_CAMPO_RENDER', 'www.softwareparati.com', 'PERMISOS', 'Este campo se visualiza cuando se listen los datos', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_105' , 'C', 'MODIFICABLE', 'PERMISO_CAMPO_MODIFICABLE', 'www.softwareparati.com', 'PERMISOS', 'Se tiene permisos para modificar el campo', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_106' , 'C', 'EDITABLE', 'PERMISO_CAMPO_EDITABLE', 'www.softwareparati.com', 'PERMISOS', 'Se tiene permisos para editar el campo', true);

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo)
	select 'OBL' || substring( cdpc_codigo, 4, 8)|| substring( cdpc_llave, 4, 21), 'C', 'PROP_103', cdpc_llave, '1', 'Es obligatorio registrar la informacion del campo', now(), now(), 'SC-20200115' , '20200115'
		from documentoplantillacaracteristica_dpcp where bdpc_obligatorio and cdpc_estado = 'A';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo)
	select 'VIS' || substring( cdpc_codigo, 4, 8)|| substring(cdpc_llave, 4, 21), 'C', 'PROP_104', cdpc_llave, '1', 'Este campo se visualiza cuando se listen los datos', now(), now(), 'SC-20200115' , '20200115'
		from documentoplantillacaracteristica_dpcp where bdpc_visiblerender and cdpc_estado = 'A';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo)
	select 'MOD' || substring( cdpc_codigo, 4, 8)|| substring(cdpc_llave, 4, 21), 'C', 'PROP_105', cdpc_llave, '1', 'Se tiene permisos para modificar el campo', now(), now(), 'SC-20200115', '20200115' 
		from documentoplantillacaracteristica_dpcp where bdpc_modificable and cdpc_estado = 'A';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo)
	select 'EDI' || substring( cdpc_codigo, 4, 8)|| substring(cdpc_llave, 4, 21), 'C', 'PROP_106', cdpc_llave, '1', 'Se tiene permisos para editar el campo', now(), now(), 'SC-20200115' , '20200115'
		from documentoplantillacaracteristica_dpcp where bdpc_editable and cdpc_estado = 'A';

update propiedadvalordefinido_pvdp set cpvd_origen = 'L', cpvd_grupo = 'PERMISOS' where cpvd_origen = 'M';
update propiedad_ppdp set cppd_tipo = 'L', cppd_campo = coalesce((select cdpr_plantilla from documentoplantillarol_dprp where cdpr_llave = cppd_campo), 'ERROR') where cppd_tipo = 'M';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo, cppd_rol)
	select 'IRA' ||  substring(cdpr_llave|| cdpr_plantilla, 4, 29), 'L', 'PROP_80', cdpr_plantilla, '1', 'Permite que al ingresar al modulo se cree de inmediato un registro', now(), now(), 'SC-20200115' , '20200115', cdpr_rol
		from documentoplantillarol_dprp where bdpr_iniciorapido and cdpr_estado = 'A';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'Permite que al ingresar al modulo se cree de inmediato un registro', cpvd_grupo = 'PERMISOS' where cpvd_llave = 'PROP_80';	

update documentoplantillarol_dprp set ndpr_orden = 60 where  cdpr_llave = 'CAJCST003';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo, cppd_rol)
	select 'PCR' ||  substring(cdpr_llave , 4, 20) || substring(ndpr_orden || cdpr_plantilla, 1, 9), 'L', 'PROP_77', cdpr_plantilla, '1', 'Permite crear registros', now(), now(), 'SC-20200115' , '20200115', cdpr_rol
		from documentoplantillarol_dprp where bdpr_crear and cdpr_estado = 'A';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'Permite crear registros' where cpvd_llave = 'PROP_77';	

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo, cppd_rol)
	select 'PMD' || substring(cdpr_llave|| cdpr_plantilla, 4, 29), 'L', 'PROP_78', cdpr_plantilla, '1', 'Permite modificar registros', now(), now(), 'SC-20200115' , '20200115', cdpr_rol
		from documentoplantillarol_dprp where bdpr_modificar and cdpr_estado = 'A';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'Permite modificar registros', cpvd_grupo = 'PERMISOS' where cpvd_llave = 'PROP_78';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_107' , 'L', 'LISTABLE EN MENU', 'PERMISO_PLANTILLA_LISTAR_MENU', 'www.softwareparati.com', 'PERMISOS', 'Este documento se visualizara como un modulo en el sistema', true);

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo, cppd_rol)
	select 'PLS' || substring(cdpr_llave|| cdpr_plantilla, 4, 29), 'L', 'PROP_107', cdpr_plantilla, '1', 'Este documento se visualizara como un modulo en el sistema', now(), now(), 'SC-20200115' , '20200115', cdpr_rol
		from documentoplantillarol_dprp where bdpr_listable and cdpr_estado = 'A';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_108' , 'L', 'VER TODOS', 'PERMISO_PLANTILLA_VER_TODOS', 'www.softwareparati.com', 'PERMISOS', 'Se tiene permiso de visualizar todos los registros', true);

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo, cppd_rol)
	select  'PVT' ||  substring(cdpr_llave , 4, 20) || substring(ndpr_orden || cdpr_plantilla, 1, 9), 'L', 'PROP_108', cdpr_plantilla, '1', 'Se tiene permiso de visualizar todos los registros', now(), now(), 'SC-20200115' , '20200115', cdpr_rol
		from documentoplantillarol_dprp where bdpr_vertodos and cdpr_estado = 'A';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo, cppd_rol)
	select 'PEDI' || substring(cdpp_llave, 1, 27), 'C', 'PROP_106', cdpp_caracteristica, '1', 'Se tiene permisos para modificar el campo por el rol', now(), now(), 'SC-20200115', '20200115', cdpp_rol 
		from documentoplantillapermiso_dppp where bdpp_editable and cdpp_estado = 'A';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_codigo, cppd_rol)
	select 'PMOD' || substring(cdpp_llave, 1, 27), 'C', 'PROP_105', cdpp_caracteristica, '1', 'Se tiene permisos para editar el campo por el rol', now(), now(), 'SC-20200115' , '20200115', cdpp_rol
		from documentoplantillapermiso_dppp where bdpp_modificable and cdpp_estado = 'A';


DROP TABLE documentoplantillapermiso_dppp;

DROP TABLE documentoplantillarol_dprp;

ALTER TABLE documentoplantillacaracteristica_dpcp
	DROP COLUMN bdpc_obligatorio,
	DROP COLUMN bdpc_visiblerender,
	DROP COLUMN bdpc_editable,
	DROP COLUMN bdpc_modificable;
