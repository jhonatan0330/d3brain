COMMENT ON TABLE usuario_usrp IS '2019-11-27';
COMMENT ON TABLE usuariosesion_ussp IS '2019.11.27.00';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_ayuda,  cpvd_grupo,  cpvd_motivo) 
	VALUES('PROP_89', 'A', 'ROL', 'ROL', 'www.softwareparati.com', 'REQUISITO', 'Permite asignar el XXX a un YYYY');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_ayuda,  cpvd_grupo,  cpvd_motivo) 
	VALUES('PROP_90', 'A', 'FUNCION_VALIDAR', 'FUNCION ASIGNACION', 'www.softwareparati.com', 'REQUISITO', 'ASigna el XX alk usuario YY cuando se pasa al estado ZZZ');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_ayuda,  cpvd_grupo,  cpvd_motivo) 
	VALUES('PROP_91', 'A', 'MODIFICABLE', 'MODIFICABLE', 'www.softwareparati.com', 'REQUISITO', 'Permite modificar el documento XX cuando se encuentra en el estado YYY');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_ayuda,  cpvd_grupo,  cpvd_motivo) 
	VALUES('PROP_92', 'A', 'COLOR', 'COLOR', 'www.softwareparati.com', 'REQUISITO', 'Cuando el documento tenga el estado XX se va a mostrar de color YYY');

ALTER TABLE procesoestado_pesp
	DROP CONSTRAINT fk_procesoestadorol;

INSERT INTO cambio_cmbp (ccmb_llave, ccmb_nombre, ccmb_motivo, dcmb_fecha, dcmb_fechaaplicacion) VALUES('SC-20191127', 'SC-SIS-20191127', 'Cambiar los campos a propiedades', now(), now());

INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion) 
	select substring('COLOR' || cpes_llave, 0, 32), 'PROP_92', cpes_llave, cpes_color, cpes_color, 'Ver el estado XX con el color YY', now(), now(), 'SC-20191127' from procesoestado_pesp where cpes_color is not null and cpes_estado = 'A';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion) 
	select substring('MOD' || cpes_llave, 0, 32), 'PROP_91', cpes_llave, bpes_modificable, bpes_modificable, 'Permite que se modifique el documento en estado XX', now(), now(), 'SC-20191127' from procesoestado_pesp where bpes_modificable and cpes_estado = 'A';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_texto, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion) 
	select substring('ROL' || cpes_llave, 0, 32), 'PROP_89', cpes_llave, cpes_rol, (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = (select crac_plantilla from rolacceso_racp where crac_llave = cpes_rol)), 'Permite asignar el documetno a cualquier ROL XX cuando se encuentre en estado YY', now(), now(), 'SC-20191127' from procesoestado_pesp where cpes_rol is not null and cpes_estado = 'A';

--update pg_proc set proname = replace(proname, 'state_', 'propiedad_') WHERE proname like 'state_%' and pronargs = 1;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion) 
	select cpes_llave, 'PROP_90', cpes_llave, cpes_funcionasignacion, 'PENDIENTE', now(), now(), 'SC-20191127' from procesoestado_pesp where cpes_funcionasignacion is not null and cpes_estado = 'A';

ALTER TABLE procesoestado_pesp
	DROP COLUMN cpes_color,
	DROP COLUMN bpes_modificable,
	DROP COLUMN cpes_rol,
	DROP COLUMN cpes_funcionasignacion;

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo) 
	VALUES('PROP_93', 'P', 'MANEJA DINERO', 'MANEJA_DINERO', 'www.softwareparati.com', 'REQUISITO', 'Este proceso maneja entrada y salidas de dinero');
	
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion) 
	select substring('MONEY' || cprc_llave, 0 , 32), 'PROP_93', cprc_llave, 'T', 'Este proceso maneja entrada y salidas de dinero', now(), now(), 'SC-20191127' from proceso_prcp where bprc_manejasaldos and cprc_estado = 'A';

ALTER TABLE proceso_prcp
	DROP COLUMN bprc_manejasaldos;
