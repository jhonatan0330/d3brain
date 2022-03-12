COMMENT ON TABLE usuario_usrp IS '2019-11-04';
COMMENT ON TABLE usuariosesion_ussp IS '2019.11.04.00';
/*
 * 1. Colocar el objetivo a proceso, plantilla y campos, ademas de la tabla de realcion interna
 * 2. Colocar los parametros como una tabla para permitir mejora administracion
 * 3. retirar columnas que no se usan
 * 4. Mejorar la tabla de requerimientos
 * 5. Crear el proceso de recursos humanos para los roles
 */

--1.

ALTER TABLE proceso_prcp ADD COLUMN cprc_objetivo character varying(4000);
update proceso_prcp set cprc_objetivo = 'PENDIENTE';
ALTER TABLE proceso_prcp ALTER COLUMN cprc_objetivo set not null;

ALTER TABLE documentoplantillacaracteristica_dpcp ADD COLUMN cdpc_objetivo character varying(4000);
update documentoplantillacaracteristica_dpcp set cdpc_objetivo = 'PENDIENTE';
ALTER TABLE documentoplantillacaracteristica_dpcp ALTER COLUMN cdpc_objetivo set not null;

ALTER TABLE documentoplantilla_dplp ADD COLUMN cdpl_objetivo character varying(4000);
update documentoplantilla_dplp set cdpl_objetivo = 'PENDIENTE';
ALTER TABLE documentoplantilla_dplp ALTER COLUMN cdpl_objetivo set not null;

CREATE TABLE relacioninterna_ritp (
	crit_llave character varying(32) NOT NULL,
	crit_plantillaorigen character varying(32) NOT NULL,
	crit_campoorigen character varying(32) NOT NULL,
	crit_plantilladestino character varying(32),
	crit_campodestino character varying(32),
	crit_requerimiento character varying(32),
	crit_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

--3.
ALTER TABLE rolacceso_racp
	DROP COLUMN brac_credenciales;

--4. 

delete from requerimiento_reqp;

ALTER TABLE requerimiento_reqp
	DROP COLUMN creq_texto,
	ADD COLUMN creq_clasificacion character varying(1) NOT NULL,
	ADD COLUMN creq_nombre character varying(100) NOT NULL,
	ADD COLUMN creq_detalle character varying(4000) NOT NULL,
	ADD COLUMN creq_avance character varying(1) NOT NULL;


--2.
CREATE TABLE propiedadvalordefinido_pvdp (
	cpvd_llave character varying(32) NOT NULL,
	cpvd_origen character varying(1) NOT NULL,
	cpvd_codigo character varying(100) NOT NULL,
	cpvd_nombre character varying(100) NOT NULL,
	bpvd_lazy boolean DEFAULT false NOT NULL,
	cpvd_formato character varying(1) NOT NULL,
	cpvd_ayuda character varying(100) NOT NULL,
	cpvd_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_01' , 'C', 'BASICA', 'BASICA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_02' , 'C', 'ARCHIVO_TAMANO_MAXIMO', 'ARCHIVO_TAMANO_MAXIMO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_03' , 'C', 'BINARIO_VERDADERO', 'BINARIO_VERDADERO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_04' , 'C', 'BINARIO_FALSO', 'BINARIO_FALSO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_05' , 'C', 'BODEGA_FIJA', 'BODEGA_FIJA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_06' , 'C', 'BODEGA_MOVIMIENTO', 'BODEGA_MOVIMIENTO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_07' , 'C', 'CONFIGURACION_PLANTILLA_TIPO', 'CONFIGURACION_PLANTILLA_TIPO', 'T', 'www.softwareparati.com');	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_08' , 'C', 'CONFIGURACION_ENTIDAD', 'CONFIGURACION_ENTIDAD', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_09' , 'C', 'CUENTA_CATALOGO_MOVIMIENTO', 'CUENTA_CATALOGO_MOVIMIENTO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_10' , 'C', 'CUENTA_CATALOGO_FILTRO', 'CUENTA_CATALOGO_FILTRO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_11' , 'C', 'CUENTA_ABRIR_CAJA', 'CUENTA_ABRIR_CAJA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_12' , 'C', 'CUENTA_CERRAR_CAJA', 'CUENTA_CERRAR_CAJA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_13' , 'C', 'DETALLE_NUMERO_COLUMNAS', 'DETALLE_NUMERO_COLUMNAS', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_14' , 'C', 'DETALLE_TECLADO', 'DETALLE_TECLADO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_15' , 'C', 'DETALLE_TARIFARIO', 'DETALLE_TARIFARIO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_16' , 'C', 'DETALLE_OCULTAR_IMAGENES', 'DETALLE_OCULTAR_IMAGENES', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_17' , 'C', 'UNICO_PRODUCTO', 'UNICO_PRODUCTO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_18' , 'C', 'DETALLE_FORMULA', 'DETALLE_FORMULA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_19' , 'C', 'PLANTILLA_AUXILIAR', 'PLANTILLA_AUXILIAR', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_20' , 'C', 'AUTOLOAD', 'AUTOLOAD', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_21' , 'C', 'FECHA_SIN_HORA', 'FECHA_SIN_HORA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_22' , 'C', 'FECHA_SIN_CALENDAR', 'FECHA_SIN_CALENDAR', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_23' , 'C', 'FECHA_MAXIMA', 'FECHA_MAXIMA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_24' , 'C', 'FECHA_MINIMA', 'FECHA_MINIMA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_25' , 'C', 'FECHA_RANGO', 'FECHA_RANGO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_26' , 'C', 'FECHA_RANGO_MAXIMO', 'FECHA_RANGO_MAXIMO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_27' , 'C', 'NUMERO_MONEDA', 'NUMERO_MONEDA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_28' , 'C', 'NUMERO_FORMULA', 'NUMERO_FORMULA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_29' , 'C', 'NUMERO_FUNCION', 'NUMERO_FUNCION', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_30' , 'C', 'NUMERO_STEP', 'NUMERO_STEP', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_31' , 'C', 'NUMERO_REDONDEO', 'NUMERO_REDONDEO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_32' , 'C', 'MULTIPLE', 'MULTIPLE', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_33' , 'C', 'CAMPO_HEREDADO', 'CAMPO_HEREDADO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_34' , 'C', 'FORMATO', 'FORMATO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_35' , 'C', 'PROCESO_POP', 'PROCESO_POP', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_36' , 'C', 'PROCESO_ACCIONES', 'PROCESO_ACCIONES', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_37' , 'C', 'PROCESO_GESTIONAR_ESTADOS', 'PROCESO_GESTIONAR_ESTADOS', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_38' , 'C', 'PROCESO_DIVISION', 'PROCESO_DIVISION', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_39' , 'C', 'PROCESO_VALOR', 'PROCESO_VALOR', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_40' , 'C', 'SOLICITAR_FECHAS', 'SOLICITAR_FECHAS', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_41' , 'C', 'PROCESO_FUNCION', 'PROCESO_FUNCION', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_42' , 'L', 'TERCERO', 'TERCERO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_43' , 'L', 'ENCABEZADO', 'ENCABEZADO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_44' , 'L', 'DESCRIPCION', 'DESCRIPCION', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_45' , 'L', 'DESCRIPCION_NIVEL2', 'DESCRIPCION_NIVEL2', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_46' , 'L', 'SUBTOTAL', 'SUBTOTAL', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_47' , 'L', 'TOTAL', 'TOTAL', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_48' , 'L', 'CONSECUTIVO', 'CONSECUTIVO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_49' , 'L', 'FECHA', 'FECHA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_50' , 'L', 'RESPONSABLE', 'RESPONSABLE', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_51' , 'L', 'ORDEN', 'ORDEN', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_52' , 'L', 'ORDEN_DESCENDENTE', 'ORDEN_DESCENDENTE', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_53' , 'L', 'AYUDA', 'AYUDA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_54' , 'L', 'FUNCION_VALIDAR', 'FUNCION_VALIDAR', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_55' , 'L', 'SOLICITAR_FECHAS', 'SOLICITAR_FECHAS', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_56' , 'L', 'COPY_TEXT', 'COPY_TEXT', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_57' , 'L', 'MENSAJE', 'MENSAJE', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_58' , 'L', 'MENSAJE_DESTINATARIOS', 'MENSAJE_DESTINATARIOS', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_59' , 'T', 'FUNCION_VALIDAR', 'FUNCION_VALIDAR', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_60' , 'T', 'MENSAJE', 'MENSAJE', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_61' , 'S', 'FILE_SERVER', 'FILE_SERVER', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_62' , 'S', 'FILE_SERVER_FOLDER_BASE', 'FILE_SERVER_FOLDER_BASE', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_63' , 'S', 'TEMP', 'TEMP', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_64' , 'S', 'FTP_SERVER', 'FTP_SERVER', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_65' , 'S', 'FTP_PORT', 'FTP_PORT', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_66' , 'S', 'FTP_USER', 'FTP_USER', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_67' , 'S', 'FTP_PASS', 'FTP_PASS', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_68' , 'S', 'FTP_URL_BASE', 'FTP_URL_BASE', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_69' , 'T', 'MENSAJE_DESTINATARIOS', 'MENSAJE_DESTINATARIOS', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_70' , 'E', 'REPORTE_ENCABEZADO', 'REPORTE_ENCABEZADO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_71' , 'E', 'REPORTE_EXCEL', 'REPORTE_EXCEL', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_72' , 'E', 'P_SUBREPORT_', 'P_SUBREPORT_', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_73' , 'P', 'MENSAJE', 'MENSAJE', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_74' , 'P', 'MENSAJE_DESTINATARIOS', 'MENSAJE_DESTINATARIOS', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_75' , 'C', 'FORMATO', 'FORMATO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_76' , 'C', 'TEXTO_FORMULA', 'TEXTO_FORMULA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_77' , 'M', 'PERMISO_PLANTILLA_CREAR', 'PERMISO_PLANTILLA_CREAR', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_78' , 'M', 'PERMISO_PLANTILLA_MODIFICAR', 'PERMISO_PLANTILLA_MODIFICAR', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_79' , 'M', 'PERMISO_PLANTILLA_ELIMINAR', 'PERMISO_PLANTILLA_ELIMINAR', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_80' , 'M', 'PERMISO_PLANTILLA_INICIO_RAPIDO', 'PERMISO_PLANTILLA_INICIO_RAPIDO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_81' , 'M', 'PERMISO_PLANTILLA_OCULTAR_TOTAL', 'PERMISO_PLANTILLA_OCULTAR_TOTAL', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_82' , 'M', 'PERMISO_PLANTILLA_CAMPO_FILTRO', 'PERMISO_PLANTILLA_CAMPO_FILTRO', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_83' , 'M', 'PERMISO_PLANTILLA_FILTROS_BASE', 'PERMISO_PLANTILLA_FILTROS_BASE', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_84' , 'M', 'PERMISO_PLANTILLA_CARGA_MASIVA', 'PERMISO_PLANTILLA_CARGA_MASIVA', 'T', 'www.softwareparati.com');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_85' , 'M', 'PERMISO_PLANTILLA_CAMBIAR_ESTADO', 'PERMISO_PLANTILLA_CAMBIAR_ESTADO', 'T', 'www.softwareparati.com');

ALTER TABLE propiedad_ppdp
	ADD COLUMN cppd_propiedadvalor character varying(32);

update propiedad_ppdp set cppd_propiedadvalor = (select cpvd_llave from propiedadvalordefinido_pvdp where cpvd_codigo = cppd_key and cppd_tipo = cpvd_origen);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda)
select substring(cppd_key||'_' ||cppd_tipo,0,32), cppd_tipo, cppd_key, cppd_key,'T', 'www.softwareparati.com'
from propiedad_ppdp where cppd_propiedadvalor is null
group by cppd_key, cppd_tipo;

update propiedad_ppdp set cppd_propiedadvalor = (select cpvd_llave from propiedadvalordefinido_pvdp where cpvd_codigo = cppd_key and cppd_tipo = cpvd_origen) where cppd_propiedadvalor is null;

ALTER TABLE propiedad_ppdp
	DROP COLUMN cppd_tipo,
	DROP COLUMN cppd_key,
	Alter COLUMN cppd_propiedadvalor set NOT NULL;

ALTER TABLE propiedadvalordefinido_pvdp
	ADD CONSTRAINT pk_propiedadvalordefinido_pvdp PRIMARY KEY (cpvd_llave);

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT pk_relacioninterna_ritp PRIMARY KEY (crit_llave);

ALTER TABLE propiedad_ppdp
	ADD CONSTRAINT fk_propiedadpropiedadvalor FOREIGN KEY (cppd_propiedadvalor) REFERENCES propiedadvalordefinido_pvdp(cpvd_llave);

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternacampodestino FOREIGN KEY (crit_campodestino) REFERENCES documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternacampoorigen FOREIGN KEY (crit_campoorigen) REFERENCES documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternaplantilladestino FOREIGN KEY (crit_plantilladestino) REFERENCES documentoplantilla_dplp(cdpl_llave);

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternaplantillaorigen FOREIGN KEY (crit_plantillaorigen) REFERENCES documentoplantilla_dplp(cdpl_llave);

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternarequerimiento FOREIGN KEY (crit_requerimiento) REFERENCES requerimiento_reqp(creq_llave);

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_propiedadvalor)
select 
substring('E_' ||cdpr_llave,0,32), cdpr_llave, 'TRUE', 'TRUE', 'PROP_79'
from documentoplantillarol_dprp where bdpr_eliminar = true;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_propiedadvalor)
select 
substring('J_' ||cdpr_llave,0,32), cdpr_llave, 'TRUE', 'TRUE', 'PROP_85'
from documentoplantillarol_dprp where bdpr_cambioestado = true;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_propiedadvalor)
select 
substring('M_' ||cdpr_llave,0,32), cdpr_llave, 'TRUE', 'TRUE', 'PROP_84'
from documentoplantillarol_dprp where bdpr_cargamasiva = true;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_propiedadvalor)
select 
substring('F_' ||cdpr_llave,0,32), cdpr_llave, cdpr_filtroestados, cdpr_filtroestados, 'PROP_83'
from documentoplantillarol_dprp where cdpr_filtroestados is not null;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_propiedadvalor)
select 
substring('C_' ||cdpr_llave,0,32), cdpr_llave, cdpr_campofiltro, cdpr_campofiltro, 'PROP_82'
from documentoplantillarol_dprp where cdpr_campofiltro is not null;

ALTER TABLE documentoplantillarol_dprp
	DROP COLUMN bdpr_totalvisiblerender,
	DROP COLUMN cdpr_campofiltro,
	DROP COLUMN bdpr_eliminar,
	DROP COLUMN cdpr_filtroestados,
	DROP COLUMN bdpr_cargamasiva,
	DROP COLUMN bdpr_cambioestado;

INSERT INTO proceso_prcp ( cprc_llave, cprc_nombre, cprc_codigo, cprc_objetivo, nprc_prioridad) VALUES ('RRHH', 'PROCESO DE GESTION DE RECURSOS HUMANOS', 'RRHH', 'Gestionar el recurso humano de la organizacion',100);
INSERT INTO procesoestado_pesp ( cpes_llave, cpes_nombre, cpes_codigo, npes_avance, cpes_proceso, cpes_estadodocumento,  cpes_color, bpes_modificable) 
	VALUES ('RRHH_ACTIVO', 'RRHH ACTIVO', 'ACTIVO', 10, 'RRHH', 'A', NULL, TRUE);
INSERT INTO procesoestado_pesp ( cpes_llave, cpes_nombre, cpes_codigo, npes_avance, cpes_proceso, cpes_estadodocumento, cpes_color, bpes_modificable)
	VALUES ('RRHH_INACTIVO', 'RRHH INACTIVO', 'INACTIVO', 1, 'RRHH', 'I', NULL, TRUE);
INSERT INTO consecutivo_conp(ccon_llave, ccon_nombre) VALUES ('ANULAR_RRHH', 'ANULAR_RRHH');
INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_tipo, cdpl_codigo, cdpl_nombre, cdpl_consecutivo, cdpl_proceso, cdpl_objetivo)
    VALUES ('RRHH_ANULAR', 'F', 'ANULAR_RRHH', 'ANULAR RRHH', 'ANULAR_RRHH', 'RRHH', 'Inactivar a los roles creados por el proceso');
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, cdpc_formato, cdpc_nombre, cdpc_codigo, ndpc_orden, bdpc_obligatorio, cdpc_objetivo)
    VALUES ('RRHH_ANULAR_1', 'RRHH_ANULAR', 'Z', 'DOCUMENTO', 'DOCUMENTO', 1, TRUE, 'Rol que se desea inactivar');
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, cdpc_formato, cdpc_nombre, cdpc_codigo, ndpc_orden, cdpc_objetivo)
    VALUES ('RRHH_ANULAR_2', 'RRHH_ANULAR', 'T', 'MOTIVO', 'MOTIVO', 2, 'Justificar el motivo de la inactivacion');
INSERT INTO procesotransicion_ptrp ( cptr_llave, cptr_nombre, cptr_proceso, cptr_estadopartida, cptr_estadollegada, cptr_plantilla, bptr_documentador)
	VALUES('RRHH_ANULAR', 'ANULAR','RRHH', 'RRHH_ACTIVO', 'RRHH_INACTIVO', 'RRHH_ANULAR', TRUE);

update pedidoventa_pdvp set cpdv_estadoexpediente = 'RRHH_ACTIVO' where cpdv_estado = 'A'
and cpdv_plantilla in (select cdpl_llave
	from rolacceso_racp, documentoplantilla_dplp where crac_plantilla = cdpl_llave
and cdpl_llave not in (select cptr_plantilla from procesotransicion_ptrp where cptr_estadopartida is null));

update pedidoventa_pdvp set cpdv_estadoexpediente = 'RRHH_INACTIVO' where cpdv_estado = 'I'
and cpdv_plantilla in (select cdpl_llave
	from rolacceso_racp, documentoplantilla_dplp where crac_plantilla = cdpl_llave
and cdpl_llave not in (select cptr_plantilla from procesotransicion_ptrp where cptr_estadopartida is null));

INSERT INTO procesotransicion_ptrp ( cptr_llave, cptr_nombre, cptr_proceso, cptr_estadollegada, cptr_plantilla, cptr_estado)
select substring('RH_'||crac_llave,0,32), cdpl_nombre, 'RRHH', 'RRHH_ACTIVO', cdpl_llave, crac_estado
	from rolacceso_racp, documentoplantilla_dplp where crac_plantilla = cdpl_llave
and cdpl_llave not in (select cptr_plantilla from procesotransicion_ptrp where cptr_estadopartida is null);

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'bdpr_eliminar', 'false');
update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'bdpr_totalvisiblerender', 'true');

CREATE OR REPLACE FUNCTION movimiento_descripcion(id_documento character varying)
  RETURNS character varying LANGUAGE plpgsql AS '
DECLARE plantilla character varying; 
DECLARE plantilla_campo_descripcion character varying;
DECLARE plantilla_campo_descripcion_nivel2 character varying;
DECLARE id_documento_principal character varying;
DECLARE descripcion_anidada character varying;
BEGIN 
    IF id_documento IS NULL THEN 
        RETURN NULL;
    END IF;
    SELECT cpdv_plantilla INTO plantilla FROM pedidoventa_pdvp where cpdv_llave = id_documento;
    SELECT cppd_valor INTO plantilla_campo_descripcion FROM propiedad_ppdp 
    	where cppd_campo = plantilla and cppd_estado = ''A'' and cppd_propiedadvalor = (select cpvd_llave from propiedadvalordefinido_pvdp where cpvd_codigo = ''DESCRIPCION'' and cpvd_origen = ''L'');
    IF plantilla_campo_descripcion IS NOT NULL THEN 
	RETURN (select cpvc_valortext from campo_documento where cdrc_documento = id_documento and cpvc_campo = plantilla_campo_descripcion);
    ELSE
	SELECT cppd_valor INTO plantilla_campo_descripcion_nivel2 FROM propiedad_ppdp 
		where cppd_campo = plantilla and cppd_estado = ''A'' and cppd_propiedadvalor = (select cpvd_llave from propiedadvalordefinido_pvdp where cpvd_codigo = ''DESCRIPCION_NIVEL2'' and cpvd_origen = ''L'');
	IF plantilla_campo_descripcion_nivel2 IS NOT NULL THEN
		SELECT cpvc_valoropcion INTO id_documento_principal FROM campo_documento WHERE cdrc_documento = id_documento and cpvc_campo = plantilla_campo_descripcion_nivel2;
		CASE WHEN id_documento_principal IS  NULL THEN 
		    RETURN NULL;
		ELSE
		    SELECT movimiento_descripcion(id_documento_principal) INTO descripcion_anidada;
		    IF descripcion_anidada IS NULL THEN
			RETURN (select cpdv_nombre from pedidoventa_pdvp pcd where cpdv_llave = id_documento_principal);
		    ELSE
			RETURN ''('' || (select cpdv_nombre from pedidoventa_pdvp pcd where cpdv_llave = id_documento_principal) ||'') ''|| descripcion_anidada;
		    END IF;
		END CASE;
	ELSE
		RETURN NULL;
	END IF;
    END IF;
END; 
';

DROP TRIGGER clave ON usuariorol_erlp;

DROP FUNCTION log_clave();