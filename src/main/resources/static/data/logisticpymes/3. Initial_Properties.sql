INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean)
	VALUES('PROP_01' , 'C', 'TEXTO LARGO', 'BASICA', 'FORMATO', 'T', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_02' , 'C', 'ARCHIVO_TAMANO_MAXIMO', 'ARCHIVO_TAMANO_MAXIMO', 'REQUISITO', 'A');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_03' , 'C', 'BINARIO_VERDADERO', 'BINARIO_VERDADERO', 'FORMATO', 'I');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_04' , 'C', 'BINARIO_FALSO', 'BINARIO_FALSO', 'FORMATO', 'I');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_05' , 'C', 'BODEGA_FIJA', 'BODEGA_FIJA', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_06' , 'C', 'BODEGA_MOVIMIENTO', 'BODEGA_MOVIMIENTO', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_07' , 'C', 'CONFIGURACION_PLANTILLA_TIPO', 'CONFIGURACION_PLANTILLA_TIPO', 'REQUISITO', 'G');	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_08' , 'C', 'CONFIGURACION_ENTIDAD', 'CONFIGURACION_ENTIDAD', 'REQUISITO' ,'G', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_09' , 'C', 'TIPO DE MOVIMIENTO', 'CUENTA_MOVIMIENTO', 'REQUISITO', 'Z', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_11' , 'C', 'CUENTA_ABRIR_CAJA', 'CUENTA_ABRIR_CAJA', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_12' , 'C', 'CUENTA_CERRAR_CAJA', 'CUENTA_CERRAR_CAJA', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_13' , 'C', 'DETALLE_NUMERO_COLUMNAS', 'DETALLE_NUMERO_COLUMNAS', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_14' , 'C', 'DETALLE_TECLADO', 'DETALLE_TECLADO', 'REQUISITO', 'J', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_15' , 'C', 'DETALLE_TARIFARIO', 'DETALLE_TARIFARIO', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_16' , 'C', 'DETALLE_OCULTAR_IMAGENES', 'DETALLE_OCULTAR_IMAGENES', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_17' , 'C', 'UNICO_PRODUCTO', 'UNICO_PRODUCTO', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_18' , 'C', 'DETALLE_FORMULA', 'DETALLE_FORMULA', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_19' , 'C', 'FUENTE DE DATOS', 'PLANTILLA_AUXILIAR', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_20' , 'C', 'AUTOLOAD', 'AUTOLOAD', 'REQUISITO', 'Z', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_22' , 'C', 'FECHA_SIN_CALENDAR', 'FECHA_SIN_CALENDAR', 'REQUISITO', 'F', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_23' , 'C', 'FECHA_MAXIMA', 'FECHA_MAXIMA', 'REQUISITO', 'F');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_24' , 'C', 'FECHA_MINIMA', 'FECHA_MINIMA', 'REQUISITO', 'F');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_25' , 'C', 'FECHA_RANGO', 'FECHA_RANGO', 'REQUISITO', 'F');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_26' , 'C', 'FECHA_RANGO_MAXIMO', 'FECHA_RANGO_MAXIMO', 'REQUISITO', 'F');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_27' , 'C', 'FORMATO MONEDA', 'NUMERO_MONEDA', 'REQUISITO', 'N', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_28' , 'C', 'NUMERO_FORMULA', 'NUMERO_FORMULA', 'REQUISITO', 'N', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_29' , 'C', 'NUMERO_FUNCION', 'NUMERO_FUNCION_SQL', 'REQUISITO', 'N', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_30' , 'C', 'NUMERO_STEP', 'NUMERO_STEP', 'REQUISITO', 'N');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_31' , 'C', 'NUMERO_REDONDEO', 'NUMERO_REDONDEO', 'REQUISITO', 'N');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_32' , 'C', 'MULTIPLE', 'MULTIPLE', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_33' , 'C', 'CAMPO_HEREDADO', 'CAMPO_HEREDADO', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_34' , 'C', 'FORMATO', 'FORMATO', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_35' , 'C', 'MOSTRAR EN POP UP', 'PROCESO_POP', 'REQUISITO', 'Z', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple)
	VALUES('PROP_36' , 'C', 'ACCIONES CRUD', 'PROCESO_ACCIONES', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_37' , 'C', 'RUTA BPM GESTION', 'PROCESO_GESTIONAR_ESTADOS', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_38' , 'C', 'PROCESO_DIVISION', 'PROCESO_DIVISION', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_39' , 'C', 'PROCESO_VALOR', 'PROCESO_VALOR', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_40' , 'C', 'SOLICITAR FECHAS EN CONSULTA', 'SOLICITAR_FECHAS', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_41' , 'C', 'FUNCION CONSULTA DATOS', 'PROCESO_FUNCION_SQL', 'REQUISITO', 'Z', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_42' , 'L', 'TERCERO', 'TERCERO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_43' , 'L', 'ENCABEZADO', 'ENCABEZADO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_44' , 'L', 'DESCRIPCION', 'DESCRIPCION', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_45' , 'L', 'DESCRIPCION_NIVEL2', 'DESCRIPCION_NIVEL2', 'REQUISITO');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_47' , 'L', 'TOTAL', 'TOTAL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_48' , 'L', 'CONSECUTIVO', 'CONSECUTIVO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_49' , 'L', 'FECHA', 'FECHA', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_50' , 'L', 'RESPONSABLE', 'RESPONSABLE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_51' , 'L', 'ORDEN', 'ORDEN', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_52' , 'L', 'ORDEN_DESCENDENTE', 'ORDEN_DESCENDENTE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_53' , 'L', 'AYUDA', 'AYUDA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple) 
	VALUES('PROP_54' , 'L', 'FUNCION_SQL_VALIDAR', 'FUNCION_SQL_VALIDAR', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_55' , 'L', 'SOLICITAR FECHAS EN CONSULTA', 'SOLICITAR_FECHAS', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_56' , 'L', 'COPY_TEXT', 'COPY_TEXT', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_57' , 'L', 'MENSAJE', 'MENSAJE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_58' , 'L', 'FUNCION DESTINATARIOS DEL MENSAJE', 'MENSAJE_DESTINATARIOS_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple) 
	VALUES('PROP_59' , 'T', 'FUNCION_SQL_VALIDAR', 'FUNCION_SQL_VALIDAR', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_60' , 'T', 'MENSAJE', 'MENSAJE', 'REQUISITO');
--Borradas las propiedades de sistema
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_69' , 'T', 'FUNCION DESTINATARIOS DEL MENSAJE', 'MENSAJE_DESTINATARIOS_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_70' , 'E', 'ENCABEZADO DOCUMENTO', 'REPORTE_ENCABEZADO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_71' , 'E', 'OPCION EN EXCEL', 'REPORTE_EXCEL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_72' , 'E', 'P_SUBREPORT_', 'P_SUBREPORT_', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_73' , 'P', 'MENSAJE', 'MENSAJE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_74' , 'P', 'FUNCION DESTINATARIOS DEL MENSAJE', 'MENSAJE_DESTINATARIOS_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_75' , 'C', 'FORMATO', 'FORMATO', 'REQUISITO', 'T');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_76' , 'C', 'TEXTO_FORMULA', 'TEXTO_FORMULA', 'REQUISITO', 'T', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_77' , 'L', 'PERMISO CREAR', 'PERMISO_PLANTILLA_CREAR', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_78' , 'L', 'PERMISO MODIFICAR', 'PERMISO_PLANTILLA_MODIFICAR', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_79' , 'L', 'PERMISO ELIMINAR', 'PERMISO_PLANTILLA_ELIMINAR', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_piderol, bpvd_multiple) 
	VALUES('PROP_80' , 'L', 'INICIO_RAPIDO', 'PERMISO_PLANTILLA_INICIO_RAPIDO', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_81' , 'L', 'OCULTAR_TOTAL', 'PERMISO_PLANTILLA_OCULTAR_TOTAL', 'PERMISOS', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_82' , 'L', 'FILTRO POR CAMPO', 'PERMISO_PLANTILLA_CAMPO_FILTRO', 'PERMISOS');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_83' , 'L', 'ESTADOS POR DEFECTO CONSULTA', 'PERMISO_PLANTILLA_FILTROS_BASE', 'PERMISOS');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_84' , 'L', 'CARGAS MASIVAS', 'PERMISO_PLANTILLA_CARGA_MASIVA', 'PERMISOS', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_85' , 'L', 'CAMBIAR ESTADOS', 'PERMISO_PLANTILLA_CAMBIAR_ESTADO', 'PERMISOS', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_87' , 'L', 'BENEFICIO', 'BENEFICIO', 'BENEFICIO');	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo, bpvd_multiple) 
	VALUES('PROP_88' , 'E', 'COLUMNA', 'COLUMNA', 'COLUMNAS DEL REPORTE', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_89', 'A', 'ROL', 'ROL', 'REQUISITO', 'E');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, cpvd_origencategoria) 
	VALUES('PROP_90', 'A', 'FUNCION_SQL_ESTADO_ASIGNAR', 'FUNCION ASIGNACION', 'REQUISITO', true, true, 'E');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean, cpvd_origencategoria) 
	VALUES('PROP_91', 'A', 'MODIFICABLE', 'MODIFICABLE', 'REQUISITO', true,'E');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_92', 'A', 'COLOR', 'COLOR', 'REQUISITO', 'E');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_94' , 'C', 'FILTRO', 'FILTRO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_95' , 'C', 'VALOR POR DEFECTO', 'DEFAULT', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_96' , 'C', 'CAMPOS DEPENDENCIA', 'DEPENDE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_97' , 'C', 'FECHA CON HORA', 'FECHA_CON_HORA', 'REQUISITO', 'F', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_98' , 'C', 'TIPOS SOPORTADOS', 'ARCHIVO_TIPO', 'REQUISITO', 'A', true);
INSERT INTO propiedadvalordefinido_pvdp(cpvd_llave,  cpvd_origen,  cpvd_codigo,  cpvd_nombre,  cpvd_grupo)
	VALUES('PROP_99',  'L',  'CUENTA_SOBREGIRO',  'CUENTA SOBREGIRO',  'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_100' , 'E', 'PIE DE PAGINA', 'REPORTE_PIE_PAGINA', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_multiple) 
	VALUES('PROP_101' , 'L', 'IMAGEN DOCUMENTACION', 'IMAGEN', 'IMAGEN', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_102' , 'E', 'REQUERIMIENTO', 'REQUERIMIENTO', 'REQUISITO', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_104' , 'C', 'VISIBLE EN EL RENDER', 'PERMISO_CAMPO_RENDER', 'PERMISOS', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_105' , 'C', 'MODIFICABLE', 'PERMISO_CAMPO_MODIFICABLE', 'PERMISOS', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_107' , 'L', 'LISTABLE EN MENU', 'PERMISO_PLANTILLA_LISTAR_MENU', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_108' , 'L', 'VER TODOS', 'PERMISO_PLANTILLA_VER_TODOS', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_109' , 'L', 'FUNCION CALCULA TOTAL', 'TOTAL_FUNCION', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_110' , 'C', 'DETALLE_TARIFARIO_PRODUCTO', 'DETALLE_TARIFARIO_PRODUCTO', 'REQUISITO', 'N');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_111' , 'T', 'MENSAJE REPORTE', 'MENSAJE_REPORTE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_112' , 'P', 'MENSAJE REPORTE', 'MENSAJE_REPORTE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_113' , 'L', 'MENSAJE REPORTE', 'MENSAJE_REPORTE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_114' , 'L', 'PERMISO VER FORMULARIOS', 'PERMISO_PLANTILLA_VER', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_115' , 'T', 'MENSAJE DESTINATARIO', 'MENSAJE_DESTINATARIO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_116' , 'P', 'MENSAJE DESTINATARIO', 'MENSAJE_DESTINATARIO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_117' , 'L', 'MENSAJE DESTINATARIO', 'MENSAJE_DESTINATARIO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_118' , 'C', 'OPCIONES', 'OPCIONES', 'REQUISITO', 'G', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_120' , 'L', 'FUNCION CONSULTA DATOS', 'PROCESO_FUNCION_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_121' , 'C', 'MODIFICAR CAMPO PRINCIPAL', 'MODIFICAR_CAMPO', 'REQUISITO', 'Z', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_122' , 'C', 'CONSULTA PRODUCTOS FUNCION ', 'PRODUCTOS_FUNCION_SQL', 'REQUISITO', 'J', TRUE, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_123' , 'C', 'CONSULTA PRODUCTOS CAMPO', 'PRODUCTOS_FUNCION_CAMPO', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_124' , 'C', 'CONSULTA PRODUCTOS TERCERO', 'PRODUCTOS_TERCERO', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_125' , 'L', 'GENERAR DOCUMENTOS', 'GENERAR_DOCUMENTOS_SQL', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_126' , 'T', 'REQUERIMIENTO_TRANSICION', 'REQUERIMIENTO_TRANSICION', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_127' , 'C', 'CATEGORIA PRODUCTO', 'DETALLE_CATEGORIA', 'REQUISITO', 'J');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_128' , 'C', 'REQUERIMIENTO_CAMPO', 'REQUERIMIENTO_CAMPO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_129' , 'C', 'INCLUIR EN TRAZABILIDAD', 'INCLUIR_TRAZA_PRINCIPAL', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_130' , 'P', 'REQUERIMIENTO_PROCESO', 'REQUERIMIENTO PROCESO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_necesitadesarrollo) 
	VALUES('PROP_131' , 'L', 'REQUERIMIENTO_PLANTILLA', 'REQUERIMIENTO PLANTILLA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_132' , 'L', 'PLANTILLA ANULAR', 'PLANTILLA_ANULAR', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_133' , 'T', 'UBICACION', 'UBICACION', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_134' , 'C', 'OPCIONAL', 'PERMISO_CAMPO_OPCIONAL', 'PERMISOS', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_135' , 'C', 'BLOQUEAR', 'PERMISO_CAMPO_BLOQUEAR', 'PERMISOS', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_136' , 'A', 'ESTADO_ASIGNAR', 'ASIGNACION DE USUARIO', 'REQUISITO', 'E');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_137' , 'E', 'ENCABEZADO EN EXCEL', 'REPORTE_ENCABEZADO_EXCEL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo,  bpvd_textoculto) 
	VALUES('PROP_138' , 'E', 'JRXML', 'REPORTE_JRXML', 'REQUISITO',  true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple) 
	VALUES('PROP_139' , 'L', 'VALIDACION ANTERIOR A GENERAR EL DOCUMENTO', 'FUNCION_SQL_VALIDAR_ANTES', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_pidefechas, bpvd_solicitamotivo, bpvd_pideusuario, bpvd_pidetiempobloqueo) 
	VALUES('PROP_140' , 'T', 'TEMPORIZADOR', 'TEMPORIZADOR', 'REQUISITO', true, true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_141' , 'L', 'TIPO ROL', 'PLANTILLA_TIPO_ROL', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_142' , 'L', 'TIPO REPORTE', 'PLANTILLA_TIPO_REPORTE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_143' , 'L', 'TIPO CUENTA', 'PLANTILLA_TIPO_CUENTA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_144' , 'L', 'TIPO PRODUCTO', 'PLANTILLA_TIPO_PRODUCTO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_145' , 'L', 'TIPO BODEGA', 'PLANTILLA_TIPO_BODEGA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_textoculto, cpvd_origencategoria, bpvd_solicitamotivo) 
	VALUES('PROP_146' , 'A', 'DECISION_SQL', 'DECISION_SQL', 'REQUISITO', true, 'D', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_147' , 'C', 'FUNCION TARIFAS', 'DETALLE_TARIFARIO_SQL', 'REQUISITO', 'J', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo) 
	VALUES('PROP_148' , 'L', 'PRODUCTO_CAMPO_VALOR_UNITARIO', 'PRODUCTO CAMPO VALOR UNITARIO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo) 
	VALUES('PROP_149' , 'L', 'PRODUCTO_CAMPO_VALOR_MINIMO', 'PRODUCTO CAMPO VALOR MINIMO', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo) 
	VALUES('PROP_150' , 'L', 'PRODUCTO_CAMPO_CANTIDAD', 'PRODUCTO CAMPO CANTIDAD', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_151' , 'T', 'GENERA_DOCUMENTO_CAMPO', 'CAMPO PARA GENERAR DOCUMENTO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple, cpvd_origencategoria) 
	VALUES('PROP_152' , 'C', 'RELACIONAR_DOCUMENTOS', 'RELACIONAR DOCUMENTOS', 'REQUISITO', true, 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple, cpvd_origencategoria) 
	VALUES('PROP_153' , 'C', 'RETIRAR_DOCUMENTOS', 'RETIRAR DOCUMENTOS', 'REQUISITO', true, 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_154' , 'C', 'LLENAR AL GUARDAR', 'AUTOLOAD_SAVE', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_155' , 'L', 'OCULTAR GUARDAR', 'PLANTILLA_OCULTAR_GUARDAR', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_textoculto, cpvd_origencategoria, bpvd_solicitamotivo) 
	VALUES('PROP_156' , 'A', 'ITERACION_SQL', 'ITERACION_SQL', 'REQUISITO', true, 'R', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_157' , 'C', 'MULTIPLES ADJUNTOS', 'MULTIPLE_FILE', 'REQUISITO', 'A');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_158' , 'C', 'CAMPO UNICO', 'UNIQUE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple, bpvd_solicitamotivo) 
	VALUES('PROP_159' , 'T', 'GENERA_DOCUMENTO_FUNCION_SQL', 'FUNCION PARA GENERAR UN CAMPO EN DOCUMENTO', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_160' , 'L', 'CAMPO RENDER ESPECIAL', 'PLANTILLA_RENDER_ESPECIAL_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_161' , 'L', 'TRANSFERIR', 'PERMISO_PLANTILLA_TRANSFERIR', 'REQUISITO',true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_162' , 'C', 'CROQUIS FUENTE', 'DISPONIBILIDAD_CROQUIS', 'REQUISITO', 'U');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_propiedadboolean) 
	VALUES('PROP_163' , 'C', 'FIRMA', 'ARCHIVO_FIRMA', 'REQUISITO', 'A', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto, bpvd_solicitamotivo) 
	VALUES('PROP_164' , 'C', 'CROQUIS DISPONIBILIDAD', 'DISPONIBILIDAD_FUNCION_SQL', 'REQUISITO', 'U',true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_165' , 'C', 'AUTOLOAD', 'AUTOLOAD', 'REQUISITO', 'J', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_166' , 'C', 'CUENTA REGRESICA', 'FECHA_TIMER_BACK', 'REQUISITO', 'F', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_167' , 'C', 'ESCOGER VARIOS', 'MULTIPLE_SELECCION', 'REQUISITO', 'U', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_168' , 'C', 'ORIENTACION', 'VALIDATE_ORIENTATION', 'REQUISITO', 'U');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_169' , 'L', 'API', 'API', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, bpvd_solicitamotivo, bpvd_textoculto) 
	VALUES('PROP_170' , 'W', 'API_HEADER', 'API_HEADER', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, bpvd_solicitamotivo, bpvd_textoculto) 
	VALUES('PROP_171' , 'W', 'API_NEW_DOCUMENT', 'API_NEW_DOCUMENT', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean ) 
	VALUES('PROP_173' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO FORMULARIO', 'API_CODE_DIRECT', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_multiple) 
	VALUES('PROP_174' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO REFERENCIADO', 'API_CODE_REFERENCE', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_175' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO ESPECIAL', 'API_CODE_REFERENCE', 'REQUISITO', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_177' , 'W', 'REEMPLAZAR DEL TEMPLATE CODIGO GENERA ACCION', 'API_CODE_MODIFICADOR', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_178' , 'L', 'CAMPO EVIDENCIA', 'CAMPO_EVIDENCIA', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_179' , 'R', 'OCULTAR MENSAJE LICENCIA', 'OCULTAR_MENSAJE_LICENCIA', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_180' , 'O', 'REITAR LA LECTURA DE NOTIFICACIONES AL ABRIR EL SISTEMA', 'FORCE_NOTIFICATION', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_181' , 'C', 'LEER CODIGO QR', 'READ_QR', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_solicitamotivo, bpvd_piderol) 
	VALUES('PROP_182' , 'O', 'TABLERO DE CONTROL', 'TABLERO_CONTROL_SQL', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_183' , 'P', 'LISTABLE EN MENU', 'PERMISO_PLANTILLA_LISTAR_MENU_PROCESO', 'PERMISOS', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo) 
	VALUES('PROP_184' , 'L', 'PRODUCTO_CAMPO_TOTAL', 'PRODUCTO CAMPO TOTAL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_pidefechas, bpvd_solicitamotivo, bpvd_pideusuario)
	VALUES('PROP_185' , 'L', 'PERIODO LIMPIEZA A HISTORICO', 'PERIODO_LIMPIEZA_HISTORICO', 'REQUISITO', true, true, true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_186' , 'C', 'ALERTAR AL SELECCIONAR', 'ALERTAR_CAMPO_PROCESO', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_solicitamotivo, bpvd_multiple) 
	VALUES('PROP_187' , 'E', 'FUNCION_SQL_VALIDAR', 'FUNCION_SQL_VALIDAR', 'REQUISITO', true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_piderol) 
	VALUES('PROP_188' , 'C', 'CAMPO INVISIBLE U OCULTO', 'INVISIBLE', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_pidefechas, bpvd_solicitamotivo, bpvd_pideusuario, bpvd_pidetiempobloqueo) 
	VALUES('PROP_189' , 'L', 'TEMPORIZADOR', 'TEMPORIZADOR', 'REQUISITO', true, true, true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_190' , 'L', 'PLANTILLA_INSTRUCCION_CREAR', 'PLANTILLA_INSTRUCCION_CREAR', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple, bpvd_textoculto) 
	VALUES('PROP_191' , 'W', 'API_VALIDATION', 'API_VALIDATION', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_192' , 'W', 'API_EXTRACTION', 'API_EXTRACTION', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_193' , 'A', 'API', 'API', 'REQUISITO', 'P');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo) 
	VALUES('PROP_194' , 'W', 'API_MAX_TRY', 'API MAXIMO NUMERO DE INTENTOS', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo) 
	VALUES('PROP_195' , 'W', 'API_AUTHENTICATION', 'API EJECUTAR PARA AUTENTICAR', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_196' , 'C', 'VISIBLE CAMPO SEGUN VALOR DEPENDIENTE', 'VISIBLE_VALOR_DEPENDIENTE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_197' , 'L', 'PLANTILLA CARGA MASIVA MULTIPLE', 'PLANTILLA_CARGA_MASIVA_MULTIPLE', 'PERMISOS');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, bpvd_textoculto) 
	VALUES('PROP_198' , 'W', 'API ASYNCRONO AL FINALIZAR', 'API_ASYNCHRONOUS', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_199' , 'C', 'ANULAR MOVIMIENTO', 'CUENTA_ANULAR_MOVIMIENTO', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_200' , 'C', 'DETALLE OCULTAR UNIDADES NOMBRE CANTIDAD', 'DETALLE_OCULTAR_UNIDADES_NOMBRE_CANTIDAD', 'REQUISITO', 'J', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_201' , 'C', 'NUMERO MAXIMO', 'NUMERO_MAXIMO', 'REQUISITO', 'N', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple, bpvd_textoculto) 
	VALUES('PROP_202' , 'T', 'GENERA_DOCUMENTO_TEXTO', 'GENERAR UN CAMPO CON UN TEXTO', 'REQUISITO', true, true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_203' , 'C', 'NUMERO MINIMO', 'NUMERO_MINIMO', 'REQUISITO', 'N', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_204' , 'W', 'API_EXTRACTION_TO_BASE_64', 'API_EXTRACTION_TO_BASE_64', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_205' , 'E', 'REPORTE VISIBLE EN EL ESTADO', 'REP_VISIBLE_STATE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_206' , 'E', 'IMPRESION UNICA DEL REPORTE', 'REP_PRINT_ONE', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_207' , 'L', 'CORREO ROL', 'CORREO_ROL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_208' , 'L', 'CELULAR ROL', 'CELULAR_ROL', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_209' , 'C', 'PERMITIR LINKS DIRECTAMENTE', 'ARCHIVO_URL_USUARIO', 'REQUISITO', 'A', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_210' , 'C', 'LINKS EXTERNO', 'LINK_EXTERNO', 'REQUISITO', 'Z');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean, cpvd_origencategoria) 
	VALUES('PROP_211' , 'A', 'API_ITERATION_ONE_EXECUTION', 'API_ITERATION_ONE_EXECUTION', 'REQUISITO', true, 'P');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_212' , 'E', 'IMAGEN EN REPORTE', 'REPORTE_IMAGEN', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo)
	VALUES('PROP_213' , 'T', 'GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR', 'CAMPO PARA GENERAR DOCUMENTO DEL FORM GENERADOR', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo)
	VALUES('PROP_214' , 'T', 'GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE', 'CAMPO PARA GENERAR DOCUMENTO DEL FORM EXPEDIENTE', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_215' , 'C', 'TEXTO_LONGITUD', 'TEXTO_LONGITUD', 'REQUISITO', 'T', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_216' , 'L', 'PLANTILLA_HISTORIAL_ACTIVO', 'PLANTILLA_HISTORIAL_ACTIVO', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_217' , 'W', 'CONNECT TIMEOUT', 'API_CONNECT_TIMEOUT', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo) 
	VALUES('PROP_218' , 'W', 'READ TIMEOUT', 'API_READ_TIMEOUT', 'REQUISITO');
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
	VALUES('PROP_219' , 'O', 'API-KEY', 'API_KEY', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_multiple)
	VALUES('PROP_220' , 'O', 'COVERAGE_IMAGE', 'COVERAGE_IMAGE', 'REQUISITO', true, true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_221' , 'C', 'PRODUCTO APLICADO UBICACION CROQUIS', 'PRODUCTO_PUESTO', 'REQUISITO', 'U');

-- La borro y al cambio por la propiedad
--INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean) 
--	VALUES('PROP_222' , 'L', 'INVENTARIO_OBLIGATORIO', 'CREAR INVENTARIO EN BODEGA DEL PRODUCTO', 'REQUISITO', true);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_223' , 'W', 'API - HORAS EN QUE EL API APLAZA', 'API_SCHEDULE_TIME_BLOCK', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto, bpvd_multiple) 
	VALUES('PROP_224' , 'W', 'API - VALIDAR ANTES DE EJECUTAR', 'FUNCION_SQL_PREVALIDATE_API', 'REQUISITO', true,  true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_225' , 'C', 'RELACIONAR DOCUMENTO Y CAMPO', 'INFORMATIVE_DATA', 'REQUISITO', 'V');
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_226' , 'L', 'INVENTARIO_OPCIONAL', 'OMITIR CREAR INVENTARIO EN BODEGA DEL PRODUCTO', 'REQUISITO', true);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria) 
	VALUES('PROP_227' , 'C', 'FORMATO', 'FORMATO', 'REQUISITO', 'N');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_228' , 'C', 'LEER CODIGO QR', 'READ_QR', 'REQUISITO', 'T', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_textoculto) 
	VALUES('PROP_229' , 'C', 'BINARIO_PREGUNTA', 'BINARIO_PREGUNTA', 'REQUISITO', 'I', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_230' , 'E', 'OMITIR LA IMPRESION DEL REPORTE', 'REP_EXCLUDE_STORAGE_FILE', 'REQUISITO', true);
	INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_multiple) 
	VALUES('PROP_231' , 'C', 'ACTUALIZAR CAMPO INFORMATIVO', 'UPDATE_INFORMATIVE_FIELD', 'REQUISITO', 'Z', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_232' , 'C', 'GUARDAR AL SELECCIONAR', 'SAVE_TO_SELECT', 'REQUISITO', 'Z', true);
