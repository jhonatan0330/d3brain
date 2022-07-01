COMMENT ON TABLE usuario_usrp IS '2019-12-27';
COMMENT ON TABLE usuariosesion_ussp IS '2019.12.27.00';

ALTER TABLE cuenta_cuep
	ADD COLUMN ccue_documento character varying(32);


ALTER TABLE cuenta_cuep
	ADD CONSTRAINT fk_cuentadocumento FOREIGN KEY (ccue_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);

UPDATE cuenta_cuep m
SET    ccue_codigo = 'CTA-' || sub.rn
FROM  (SELECT ccue_llave, row_number() OVER (ORDER BY ccue_nombre) AS rn FROM cuenta_cuep) sub
WHERE  m.ccue_llave = sub.ccue_llave;


INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)VALUES('SC_20191227',  'SC_20191227',  'Ingresar las cuentas',  now());

INSERT INTO consecutivo_conp(ccon_llave,  ccon_nombre,  ccon_prefijo, mcon_numeroinicial, mcon_numeroactual)VALUES('CON_CUENTA','CUENTAS','CTA-',100.00,   1000.00);

INSERT INTO documentoplantilla_dplp(cdpl_llave,  cdpl_nombre,  cdpl_consecutivo, cdpl_imagen,  cdpl_codigo,  cdpl_tipo,  cdpl_proceso,  cdpl_objetivo)VALUES('DPL_CUENTA',  'CUENTAS',  'CON_CUENTA', 'http://golyat.cloud/imagenes/modulo.png', 'CTA',  'C',  'RRHH',  'Registrar las Cuentas de control de dinero');

INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave,  cdpc_plantilla,  bdpc_obligatorio, ndpc_orden,  bdpc_editable, bdpc_modificable,  cdpc_nombre,  cdpc_codigo,  cdpc_formato,  cdpc_objetivo)VALUES('DPL_CP_1',  'DPL_CUENTA',  TRUE, 1, TRUE, TRUE,  'NOMBRE',  'NOMBRE',  'T',  'Almacenar el nombre');
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave,  cdpc_plantilla,  bdpc_obligatorio,  ndpc_orden,  bdpc_editable,  bdpc_modificable,  cdpc_nombre,  cdpc_codigo,  cdpc_formato,  cdpc_objetivo)VALUES('DPL_CP_2',  'DPL_CUENTA',  FALSE,  2,  TRUE,  TRUE,  'SOBREGIRO',  'SOBREGIRO',  'N',  'Registrar el valor de sobregiro de la cuenta permitido');

INSERT INTO documentoplantillarol_dprp(cdpr_llave,  cdpr_plantilla,  cdpr_rol, bdpr_crear,  bdpr_vertodos, bdpr_listable, ndpr_orden, bdpr_modificar) 
select substring('CTA_' || crac_llave, 0, 32 ),'DPL_CUENTA', crac_llave, TRUE, true, true, 200, true from rolacceso_racp where crac_estado = 'A' and brac_permisoscompletos = true;

INSERT INTO propiedadvalordefinido_pvdp(cpvd_llave,  cpvd_origen,  cpvd_codigo,  cpvd_nombre,  cpvd_ayuda,  cpvd_grupo, cpvd_motivo)
	VALUES('PROP_99',  'L',  'CUENTA_SOBREGIRO',  'CUENTA SOBREGIRO',  'www.softwareparati.com',  'REQUISITO', 'Este campo almacena el limite de credito de la cuenta');
INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_propiedadvalor,  dppd_fechadefinicion,  dppd_fechaimplementacion,  cppd_motivo,  cppd_cambiocreacion, cppd_tipo,  cppd_codigo)VALUES('54af2822df7f4a4fa3733f9aec39ebc6',  'DPL_CP_1',  '1',  'PROP_94',  now(),  NULL,  'Permite filtrar los documentos por este campo',  'SC_20191227',  'C',  '455');
INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto, cppd_propiedadvalor,  dppd_fechadefinicion,  dppd_fechaimplementacion,  cppd_motivo,  cppd_cambiocreacion, cppd_tipo,  cppd_codigo)VALUES('38d63d5a9f8145cc8d388b2f11dece3c',  'DPL_CUENTA',  'N',  'POR NOMBRE',   'PROP_51',  now(),  NULL,  'El documento se ordenara por el nombre',  'SC_20191227',  'L',  '456');
INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto, cppd_propiedadvalor,  dppd_fechadefinicion,  dppd_fechaimplementacion,  cppd_motivo,  cppd_cambiocreacion, cppd_tipo,  cppd_codigo)VALUES('b530c2dd92a04fd0b171f2006a4b3263',  'DPL_CUENTA',  'DPL_CP_1',  'NOMBRE', 'PROP_44',  now(),  NULL,  'Este campo es la descripcion principal del documento',  'SC_20191227',  'L',  '456');
INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_propiedadvalor,  dppd_fechadefinicion,  dppd_fechaimplementacion,  cppd_motivo,  cppd_cambiocreacion, cppd_tipo,  cppd_codigo)VALUES('15f2a36e6a7647e8940169610be627b1',  'DPL_CP_2',  '1',  'PROP_27',  now(),  NULL,  'Permite visualizar el campo con el formato de moneda configurado en el sistema',  'SC_20191227',  'C',  '460');
INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto, cppd_propiedadvalor,  dppd_fechadefinicion,  dppd_fechaimplementacion,  cppd_motivo,  cppd_cambiocreacion, cppd_tipo,  cppd_codigo)VALUES('38173da7c3cf400b82abf63ca50cf567',  'DPL_CUENTA',  'DPL_CP_2', 'SOBREGIRO',  'PROP_99',  now(),  NULL,  'Este campo almacena el limite de credito de la cuenta',  'SC_20191227', 'L',  '461');

INSERT INTO relacioninterna_ritp(crit_llave, crit_propiedad,  crit_plantilla,  crit_campo)VALUES('f3feb78989bf43069934b706c73e5eea', 'b530c2dd92a04fd0b171f2006a4b3263',  'DPL_CUENTA',  'DPL_CP_1');
INSERT INTO relacioninterna_ritp(crit_llave, crit_propiedad,  crit_plantilla,  crit_campo)VALUES('e870d5995df04d2dbbf4de5058991fa7', '38173da7c3cf400b82abf63ca50cf567',  'DPL_CUENTA',  'DPL_CP_2');

INSERT INTO usuario_usrp(cusr_llave, cusr_identificacion, cusr_nombre)
SELECT 'SYSTEM', 'SYSTEM', 'SISTEMA' WHERE NOT EXISTS (SELECT cusr_llave FROM usuario_usrp  WHERE cusr_llave = 'SYSTEM');

update cuenta_cuep set dcue_fechaconciliacion = now() where dcue_fechaconciliacion is null;

INSERT INTO pedidoventa_pdvp (cpdv_llave, cpdv_funcionario, dpdv_fecha, dpdv_fecharegistro, cpdv_plantilla, cpdv_nombre, mpdv_consecutivo, cpdv_textofiltro, cpdv_estado)
	select substring(ccue_codigo || ccue_llave, 0 , 32), 'SYSTEM', dcue_fechaconciliacion, dcue_fechaconciliacion, 'DPL_CUENTA', ccue_codigo, 0, ccue_nombre, ccue_estado from cuenta_cuep;

INSERT INTO documentotransaccion_trap (ctra_llave, dtra_fecha, ctra_usuario, ctra_documento) 
	select substring(ccue_codigo || ccue_llave, 0 , 32), dcue_fechaconciliacion, 'SYSTEM', substring(ccue_codigo || ccue_llave, 0 , 32) from cuenta_cuep;

INSERT INTO pedidoventacaracteristica_pvcp (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, cpvc_transaccionregistro) 
	select substring('1' || ccue_codigo || ccue_llave, 0 , 32), substring(ccue_codigo || ccue_llave, 0 , 32), 'DPL_CP_1', ccue_nombre, substring(ccue_codigo || ccue_llave, 0 , 32) from cuenta_cuep;

INSERT INTO pedidoventacaracteristica_pvcp (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, mpvc_valornumero, cpvc_transaccionregistro) 
	select substring('2' || ccue_codigo || ccue_llave, 0 , 32), substring(ccue_codigo || ccue_llave, 0 , 32), 'DPL_CP_2', mcue_sobregiro, mcue_sobregiro, substring(ccue_codigo || ccue_llave, 0 , 32) from cuenta_cuep where mcue_sobregiro !=0;

update cuenta_cuep set ccue_documento = substring(ccue_codigo || ccue_llave, 0 , 32);

ALTER TABLE cuenta_cuep	ALTER COLUMN ccue_documento SET NOT NULL;

delete from permiso_perp where cper_modulo in (select cmdc_llave from modulocontratado_mdcp where cmdc_modulo = 'Cuentas');
delete from modulocontratado_mdcp where cmdc_modulo = 'Cuentas';
delete from modulo_modp where cmod_llave = 'Cuentas';

INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_propiedadvalor,  dppd_fechadefinicion,   
	cppd_motivo,  cppd_cambiocreacion, cppd_tipo,  cppd_codigo)
select substring('PA' || cdpc_llave, 0 , 32) ,  cdpc_llave,  'DPL_CUENTA',  'PROP_19',  now(),
	'Obtiene los datos de la base de datos de cuentas',  'SC_20191227',  'C',  '465' 
	from documentoplantillacaracteristica_dpcp where cdpc_formato = 'C' and cdpc_estado = 'A';