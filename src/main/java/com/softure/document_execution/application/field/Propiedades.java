package com.softure.document_execution.application.field;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.domain.BasicParamDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadDTO;

public class Propiedades {
	// GENERALES
	public static final String FUNCION_SQL_VALIDAR = "FUNCION_SQL_VALIDAR";
	public static final String MENSAJE = "MENSAJE";
	public static final String MENSAJE_REPORTE = "MENSAJE_REPORTE";
	public static final String MENSAJE_ADJUNTO_URL = "MENSAJE_ADJUNTO_URL";
	public static final String MENSAJE_DESTINATARIOS_SQL = "MENSAJE_DESTINATARIOS_SQL";
	public static final String MENSAJE_DESTINATARIO = "MENSAJE_DESTINATARIO";
	public static final String API = "API";	
	public static final String API_TRANSACCION = "API_TRANSACCION";
	public static final String API_HEADER = "API_HEADER";
	public static final String API_NEW_DOCUMENT = "API_NEW_DOCUMENT";
	public static final String API_SECONDARY_DOCUMENT = "API_SECONDARY_DOCUMENT";
	public static final String API_CODE_DIRECT = "API_CODE_DIRECT";
	public static final String API_CODE_REFERENCE = "API_CODE_REFERENCE";
	public static final String API_CODE_REFERENCE_LIST = "API_CODE_REFERENCE_LIST";
	public static final String API_CODE_ESPECIAL = "API_CODE_ESPECIAL";
	public static final String API_CODE_REPLACE = "API_CODE_REPLACE";
	public static final String API_CODE_MODIFICADOR = "API_CODE_MODIFICADOR";
	public static final String API_VALIDATION = "API_VALIDATION";
	public static final String API_MAX_TRY = "API_MAX_TRY";
	public static final String API_ASYNCHRONOUS = "API_ASYNCHRONOUS";
	public static final String API_AUTHENTICATION = "API_AUTHENTICATION";
	public static final String API_MAIL_NOTIFICATION = "API_MAIL_NOTIFICATION";
	public static final String API_BASE = "API_BASE";

	public static final String API_EXTRACTION = "API_EXTRACTION";
	public static final String API_EXTRACTION_NO_ERROR = "API_EXTRACTION_NO_ERROR";
	public static final String API_EXTRACTION_TO_BASE_64 = "API_EXTRACTION_TO_BASE_64";
	public static final String API_ITERATION_ONE_EXECUTION = "API_ITERATION_ONE_EXECUTION";
	public static final String API_READ_TIMEOUT = "API_READ_TIMEOUT";
	public static final String API_CONNECT_TIMEOUT = "API_CONNECT_TIMEOUT";
	public static final String API_ENCODE_STANDAR = "API_ENCODE_STANDAR";
	public static final String API_PARAMETER = "API_PARAMETER";
	public static final String HTTP_METHOD = "HTTP_METHOD";

	public static final String API_SCHEDULE_TIME_BLOCK = "API_SCHEDULE_TIME_BLOCK";
	public static final String FUNCION_SQL_PREVALIDATE_API = "FUNCION_SQL_PREVALIDATE_API";

	// CAMPOS
	public static final String FILTRO = "FILTRO";
	public static final String UNIQUE = "UNIQUE";
	public static final String DEFAULT = "DEFAULT";
	public static final String DEPENDE = "DEPENDE";
	public static final String INVISIBLE = "INVISIBLE";
	public static final String VISIBLE_VALOR_DEPENDIENTE = "VISIBLE_VALOR_DEPENDIENTE";
	public static final String MODIFICAR_CAMPO = "MODIFICAR_CAMPO";
	
	public static final String INFORMATIVE_DATA = "INFORMATIVE_DATA";
	public static final String UPDATE_INFORMATIVE_FIELD = "UPDATE_INFORMATIVE_FIELD";

	public static final String VINCULO_DATA = "VINCULO_DATA";
	public static final String VINCULO_DELETE = "VINCULO_DELETE";
	public static final String VINCULO_FIELD_SQL = "VINCULO_FIELD_SQL";
	
	public static final String AUTOLOAD = "AUTOLOAD";
	public static final String AUTOLOAD_SAVE = "AUTOLOAD_SAVE";
	public static final String MULTIPLE = "MULTIPLE";
	public static final String CAMPO_HEREDADO_1 = "CAMPO_HEREDADO";
	public static final String FORMATO = "FORMATO";
	public static final String PLANTILLA_AUXILIAR = "PLANTILLA_AUXILIAR";
	public static final String TEXTO_LARGO = "BASICA";
	public static final String OK = "TRUE";
	public static final String TRUE = "1";
	public static final String READ_QR = "READ_QR";
	public static final String SAVE_TO_SELECT = "SAVE_TO_SELECT";

	public static final String ARCHIVO_TAMANO_MAXIMO = "ARCHIVO_TAMANO_MAXIMO";
	public static final String ARCHIVO_TIPO = "BASICA";
	public static final String MULTIPLE_FILE = "MULTIPLE_FILE";
	public static final String VALIDATE_ORIENTATION = "VALIDATE_ORIENTATION";
	public static final String PORCENTAJE_CALIDAD = "PORCENTAJE_CALIDAD";

	public static final String FECHA_CON_HORA = "FECHA_CON_HORA";
	public static final String FECHA_SIN_CALENDAR = "FECHA_SIN_CALENDAR";
	public static final String FECHA_MAXIMA = "FECHA_MAXIMA";
	public static final String FECHA_MINIMA = "FECHA_MINIMA";
	public static final String FECHA_MAXIMA_CAMPO = "FECHA_MAXIMA_CAMPO";
	public static final String FECHA_MINIMA_CAMPO = "FECHA_MINIMA_CAMPO";
	public static final String FECHA_RANGO = "FECHA_RANGO";
	public static final String FECHA_RANGO_MAXIMO = "FECHA_RANGO_MAXIMO";
	public static final String FECHA_TIMER_BACK = "FECHA_TIMER_BACK";
	public static final String FECHA_FUNCION_SQL = "FECHA_FUNCION_SQL";

	public static final String TEXTO_FORMULA = "TEXTO_FORMULA";
	public static final String TEXTO_LONGITUD = "TEXTO_LONGITUD";
	public static final String TEXTO_LONGITUD_MINIMA = "TEXTO_LONGITUD_MINIMA";

	public static final String NUMERO_REDONDEO = "NUMERO_REDONDEO";
	public static final String NUMERO_MONEDA = "NUMERO_MONEDA";
	public static final String NUMERO_FORMULA = "NUMERO_FORMULA";
	public static final String NUMERO_MAXIMO = "NUMERO_MAXIMO";
	public static final String NUMERO_MINIMO = "NUMERO_MINIMO";
	public static final String NUMERO_FUNCION_SQL = "NUMERO_FUNCION_SQL";
	public static final String NUMERO_STEP = "NUMERO_STEP";
	public static final String TOTAL_FUNCION = "TOTAL_FUNCION";
	public static final String FUNCION_NUMBER_ALL_CALCULATE_SAVE = "FUNCION_NUMBER_ALL_CALCULATE_SAVE";

	// public static final String CUENTA_CATALOGO_FILTRO = "CUENTA_CATALOGO_FILTRO";
	public static final String CUENTA_MOVIMIENTO = "CUENTA_MOVIMIENTO";
	public static final String CUENTA_ABRIR_CAJA = "CUENTA_ABRIR_CAJA";
	public static final String CUENTA_CERRAR_CAJA = "CUENTA_CERRAR_CAJA";
	public static final String CUENTA_ANULAR_MOVIMIENTO = "CUENTA_ANULAR_MOVIMIENTO";

	public static final String CONFIGURACION_ENTIDAD = "CONFIGURACION_ENTIDAD";
	public static final String CONFIGURACION_PLANTILLA_TIPO = "CONFIGURACION_PLANTILLA_TIPO";

	public static final String OPCIONES = "OPCIONES";

	public static final String BINARIO_VERDADERO = "BINARIO_VERDADERO";
	public static final String BINARIO_FALSO = "BINARIO_FALSO";

	public static final String DISPONIBILIDAD_CROQUIS = "DISPONIBILIDAD_CROQUIS"; // CROQUIS FUENTE
	public static final String DISPONIBILIDAD_FUNCION_SQL = "DISPONIBILIDAD_FUNCION_SQL";
	
	public static final String SECCION_FUNCION_SQL = "SECCION_FUNCION_SQL";

	public static final String DETALLE_NUMERO_COLUMNAS = "DETALLE_NUMERO_COLUMNAS";
	public static final String DETALLE_TECLADO = "DETALLE_TECLADO";
	public static final String DETALLE_TARIFARIO = "DETALLE_TARIFARIO";
	public static final String DETALLE_OCULTAR_IMAGENES = "DETALLE_OCULTAR_IMAGENES";
	public static final String DETALLE_FORMULA = "DETALLE_FORMULA";
	public static final String DETALLE_CATEGORIA = "DETALLE_CATEGORIA";
	public static final String PRODUCTO_CAMPO_VALOR_UNITARIO = "PRODUCTO_CAMPO_VALOR_UNITARIO";
	public static final String PRODUCTO_CAMPO_VALOR_MINIMO = "PRODUCTO_CAMPO_VALOR_MINIMO";
	public static final String PRODUCTO_CAMPO_CANTIDAD = "PRODUCTO_CAMPO_CANTIDAD";
	public static final String PRODUCTO_CAMPO_TOTAL = "PRODUCTO_CAMPO_TOTAL";
	public static final String UNICO_PRODUCTO = "UNICO_PRODUCTO";
	public static final String ITEM_DETAIL_FORM_VISIBLE = "ITEM_DETAIL_FORM_VISIBLE";
	
	public static final String DETALLE_TARIFA_PRODUCTO = "DETALLE_TARIFA_PRODUCTO";
	public static final String DETALLE_TARIFARIO_SQL = "DETALLE_TARIFARIO_SQL";
	public static final String DETALLE_OCULTAR_UNIDADES_NOMBRE_CANTIDAD = "DETALLE_OCULTAR_UNIDADES_NOMBRE_CANTIDAD";
	public static final String PRODUCTOS_FUNCION_SQL = "PRODUCTOS_FUNCION_SQL";
	public static final String PRODUCTOS_FUNCION_CAMPO = "PRODUCTOS_FUNCION_CAMPO";
	public static final String PRODUCTOS_TERCERO = "PRODUCTOS_TERCERO";
	public static final String PRODUCTO_PUESTO = "PRODUCTO_PUESTO";

	public static final String PROCESO_POP = "PROCESO_OCULTAR_POP";
	public static final String PROCESO_ACCIONES = "PROCESO_ACCIONES";
	public static final String PROCESO_GESTIONAR_ESTADOS = "PROCESO_GESTIONAR_ESTADOS";
	public static final String PROCESO_DIVISION = "PROCESO_DIVISION";
	public static final String PROCESO_VALOR = "PROCESO_VALOR";
	public static final String PROCESO_FUNCION_SQL = "PROCESO_FUNCION_SQL";
	public static final String PROCESO_INCLUIR_TRAZA_PRINCIPAL = "INCLUIR_TRAZA_PRINCIPAL";
	public static final String ALERTAR_CAMPO_PROCESO = "ALERTAR_CAMPO_PROCESO";

	public static final String BODEGA_MOVIMIENTO = "BODEGA_MOVIMIENTO";
	public static final String VINCULO_MAKE_IN_OTHER_FORM = "VINCULO_MAKE_IN_OTHER_FORM";
	public static final String VINCULO_GET_PREVIOUS_SQL = "VINCULO_GET_PREVIOUS_SQL";
	

	////////////////// PLANTILLA////////////////////////////
	// Si se coloca una nueva propiedad se coloca en el campo instrucciones
	public static final String TERCERO = "TERCERO";
	public static final String ENCABEZADO = "ENCABEZADO";
	public static final String DESCRIPCION = "DESCRIPCION";
	public static final String CAMPO_EVIDENCIA = "CAMPO_EVIDENCIA";
	public static final String DESCRIPCION_NIVEL2 = "DESCRIPCION_NIVEL2";
	public static final String TOTAL = "TOTAL";
	public static final String CONSECUTIVO = "CONSECUTIVO";

	public static final String CORREO_ROL = "CORREO_ROL";
	public static final String CELULAR_ROL = "CELULAR_ROL";

	public static final String FECHA = "FECHA";
	public static final String PLANTILLA_IMAGEN= "PLANTILLA_IMAGEN";
	public static final String PLANTILLA_FECHA_INICIO = "PLANTILLA_FECHA_INICIO";
	public static final String PLANTILLA_FECHA_FINAL = "PLANTILLA_FECHA_FINAL";
	public static final String RESPONSABLE = "RESPONSABLE";
	public static final String ORDEN = "ORDEN";
	public static final String ORDEN_DESCENDENTE = "ORDEN_DESCENDENTE";
	public static final String SOLICITAR_FECHAS = "SOLICITAR_FECHAS";
	public static final String COPY_TEXT = "COPY_TEXT";
	public static final String AYUDA = "AYUDA";
	public static final String UBICACION = "UBICACION";
	public static final String FUNCION_SQL_VALIDAR_ANTES = "FUNCION_SQL_VALIDAR_ANTES";
	public static final String FUNCION_SQL_NEW_ANTES = "FUNCION_SQL_NEW_ANTES";

	public static final String CUENTA_SOBREGIRO = "CUENTA_SOBREGIRO";

	public static final String PERMISO_PLANTILLA_CREAR = "PERMISO_PLANTILLA_CREAR";
	public static final String PERMISO_PLANTILLA_MODIFICAR = "PERMISO_PLANTILLA_MODIFICAR";
	public static final String PERMISO_PLANTILLA_INICIO_RAPIDO = "PERMISO_PLANTILLA_INICIO_RAPIDO";
	public static final String PERMISO_PLANTILLA_OCULTAR_TOTAL = "PERMISO_PLANTILLA_OCULTAR_TOTAL";
	public static final String PERMISO_PLANTILLA_CAMPO_FILTRO = "PERMISO_PLANTILLA_CAMPO_FILTRO";
	public static final String PERMISO_PLANTILLA_CARGA_MASIVA = "PERMISO_PLANTILLA_CARGA_MASIVA";
	public static final String PLANTILLA_CARGA_MASIVA_MULTIPLE = "PLANTILLA_CARGA_MASIVA_MULTIPLE";
	public static final String PERMISO_PLANTILLA_CAMBIAR_ESTADO = "PERMISO_PLANTILLA_CAMBIAR_ESTADO";
	public static final String PERMISO_PLANTILLA_VER = "PERMISO_PLANTILLA_VER";
	public static final String PERMISO_PLANTILLA_VER_TODOS = "PERMISO_PLANTILLA_VER_TODOS";
	public static final String PERMISO_PLANTILLA_LISTAR_MENU = "PERMISO_PLANTILLA_LISTAR_MENU";
	public static final String PERMISO_PLANTILLA_LISTAR_MENU_PROCESO = "PERMISO_PLANTILLA_LISTAR_MENU_PROCESO";
	

	public static final String PERMISO_CAMPO_BLOQUEAR = "PERMISO_CAMPO_BLOQUEAR";
	public static final String PERMISO_CAMPO_MODIFICABLE = "PERMISO_CAMPO_MODIFICABLE";
	public static final String PERMISO_CAMPO_RENDER = "PERMISO_CAMPO_RENDER";
	public static final String PERMISO_CAMPO_OPCIONAL = "PERMISO_CAMPO_OPCIONAL";

	public static final String PLANTILLA_ANULAR = "PLANTILLA_ANULAR";
	public static final String PLANTILLA_ACTIVAR = "PLANTILLA_ACTIVAR";
	public static final String PLANTILLA_DIFERENCIAS = "PLANTILLA_DIFERENCIAS";
	public static final String CAMPO_DIFERENCIAS = "CAMPO_DIFERENCIAS";
	public static final String PLANTILLA_TIPO_ROL = "PLANTILLA_TIPO_ROL";
	public static final String PLANTILLA_TIPO_REPORTE = "PLANTILLA_TIPO_REPORTE";
	public static final String PLANTILLA_TIPO_CUENTA = "PLANTILLA_TIPO_CUENTA";
	public static final String PLANTILLA_TIPO_PRODUCTO = "PLANTILLA_TIPO_PRODUCTO";
	public static final String TIPO_PRODUCTO_FORMULARIO_DETALLADO = "TIPO_PRODUCTO_FORMULARIO_DETALLADO";

	public static final String PLANTILLA_TIPO_CONFIGURATION = "PLANTILLA_TIPO_CONFIGURATION";
	public static final String PLANTILLA_RENDER_ESPECIAL_SQL = "PLANTILLA_RENDER_ESPECIAL_SQL";
	public static final String PLANTILLA_HISTORIAL_ACTIVO = "PLANTILLA_HISTORIAL_ACTIVO";
	public static final String TEMPLATE_VOUCHER = "TEMPLATE_VOUCHER";
	public static final String TEMPLATE_MESSAGE_SQL = "TEMPLATE_MESSAGE_SQL";
	public static final String HTML_DOCUMENT_SQL = "HTML_DOCUMENT_SQL";
	
	public static final String PLANTILLA_MONITOR = "PLANTILLA_MONITOR";
	public static final String API_ACCOUNT_CATALOG = "API_ACCOUNT_CATALOG";
	public static final String REPORT_MODULE_REFERENCE = "REPORT_MODULE_REFERENCE";

	public static final String GPS = "GPS";
	public static final String RELACIONAR_DOCUMENTOS = "RELACIONAR_DOCUMENTOS";
	public static final String RETIRAR_DOCUMENTOS = "RETIRAR_DOCUMENTOS";
	public static final String PLANTILLA_OCULTAR_GUARDAR = "PLANTILLA_OCULTAR_GUARDAR";
	public static final String PERIODO_LIMPIEZA_HISTORICO = "PERIODO_LIMPIEZA_HISTORICO";

	public static final String PLANTILLA_INICIA_PROCESO = "PLANTILLA_INICIA_PROCESO";

	public static final String REPORTE_ENCABEZADO = "REPORTE_ENCABEZADO";
	public static final String REPORTE_ENCABEZADO_EXCEL = "REPORTE_ENCABEZADO_EXCEL";
	public static final String REPORTE_PIE_PAGINA = "REPORTE_PIE_PAGINA";
	public static final String REPORTE_EXCEL = "REPORTE_EXCEL";
	public static final String REPORTE_JRXML = "REPORTE_JRXML";
	public static final String P_SUBREPORT_ = "P_SUBREPORT_";
	public static final String REP_VISIBLE_STATE = "REP_VISIBLE_STATE";
	public static final String REP_PRINT_ONE = "REP_PRINT_ONE";
	public static final String REPORTE_IMAGEN = "REPORTE_IMAGEN";
	public static final String REP_EXCLUDE_STORAGE_FILE = "REP_EXCLUDE_STORAGE_FILE";
	public static final String OCULTAR_REPORTE = "OCULTAR_REPORTE";
	public static final String REP_AUTOPRINT = "REP_AUTOPRINT";
	public static final String REP_TYPE_EXPORT = "REP_TYPE_EXPORT";
	public static final String REPORT_QUERY = "REPORT_QUERY";
	public static final String CONNECTION_STRING_DB = "CONNECTION_STRING_DB";

	public static final String ROL = "ROL";
	public static final String FUNCION_SQL_ESTADO_ASIGNAR = "FUNCION_SQL_ESTADO_ASIGNAR";
	public static final String ESTADO_ASIGNAR = "ESTADO_ASIGNAR";
	public static final String MODIFICABLE = "MODIFICABLE";
	public static final String COLOR = "COLOR";

	public static final String GENERA_DOCUMENTO_CAMPO = "GENERA_DOCUMENTO_CAMPO";
	public static final String GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR = "GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR";
	public static final String GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE = "GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE";
	public static final String GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION = "GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION";
	public static final String GENERA_DOCUMENTO_FUNCION_SQL = "GENERA_DOCUMENTO_FUNCION_SQL";
	public static final String GENERA_DOCUMENTO_TEXTO = "GENERA_DOCUMENTO_TEXTO";
	public static final String DECISION_SQL = "DECISION_SQL";
	public static final String API_SQL = "API_SQL";
	public static final String ITERACION_SQL = "ITERACION_SQL";
	public static final String ADD_ITERATION_DOCUMENT = "ADD_ITERATION_DOCUMENT";

	// TRANSICION
	public static final String TEMPORIZADOR = "TEMPORIZADOR";

	public static final String OCULTAR_MENSAJE_LICENCIA = "OCULTAR_MENSAJE_LICENCIA";
	public static final String FORCE_NOTIFICATION = "FORCE_NOTIFICACTION";
	public static final String TABLERO_CONTROL_SQL = "TABLERO_CONTROL_SQL";

	// Organizacion
	public static final String API_KEY = "API_KEY";
	public static final String COVERAGE_IMAGE = "COVERAGE_IMAGE";
	public static final String COVERAGE_TEMPLATE = "COVERAGE_TEMPLATE";
	public static final String LAYOUT_APP = "LAYOUT_APP";
	public static final String LANDING_PAGE = "LANDING_PAGE";
	public static final String HEADER_PAGE = "HEADER_PAGE";
	public static final String PUBLIC_USER = "PUBLIC_USER";
	public static final String PLANTILLA_NUEVO_USUARIO = "PLANTILLA_NUEVO_USUARIO";
	public static final String PLANTILLA_PERMISO_PUBLICO = "PLANTILLA_PERMISO_PUBLICO";
	public static final String APP_ADMIN = "APP_ADMIN";
	public static final String APP_SESSION_TIME = "APP_SESSION_TIME";
	
	
	//ol
	public static final String TIEMPO_NUEVA_CLAVE = "TIEMPO_NUEVA_CLAVE";	
	
	
	public static final String[] DEPENDENT_PROPS = { Propiedades.DEPENDE, Propiedades.MODIFICAR_CAMPO, Propiedades.INFORMATIVE_DATA,
			Propiedades.RELACIONAR_DOCUMENTOS, Propiedades.RETIRAR_DOCUMENTOS,
			Propiedades.UPDATE_INFORMATIVE_FIELD, Propiedades.FECHA_MAXIMA_CAMPO, Propiedades.FECHA_MINIMA_CAMPO };

	public static PropiedadDTO crearParametro(String tipo, String campo, String key, String valor, String token) {
		PropiedadDTO parametroTipo = new PropiedadDTO();
		parametroTipo.setTipo(tipo);
		parametroTipo.setCampo(campo);
		parametroTipo.setKey(key);
		parametroTipo.setValor(valor);
		return parametroTipo;
	}

	public static boolean validarBloqueo(PropiedadDTO property) {
		if (property == null)
			return false;
		return validateScheduleTime(property.getBloqueo(), new Date());
	}
	
	public static Date getNextDateTimeSchedule(String blockText, Date dateToValidate) {
		if(validateScheduleTime(blockText, dateToValidate))return dateToValidate;
		String[] grupos = transformBlockTextToArray(blockText);
		if (grupos.length == 0)
			return dateToValidate;
		// Mas o menos copiado de validar
		Calendar timeToValidate = Calendar.getInstance();
		timeToValidate.setTime(dateToValidate);
		for (String iBloqueo : grupos) {
			iBloqueo = iBloqueo.replace("]", "");
			if (!iBloqueo.isEmpty()) {
				try {
					if (isDayBloqueo(iBloqueo)) {
						int horaInicial = Integer.parseInt(iBloqueo.substring(1, 3));
						if (timeToValidate.get(Calendar.HOUR_OF_DAY) >= horaInicial) {
							int minutoInicial = Integer.parseInt(iBloqueo.substring(4, 6));
							if (timeToValidate.get(Calendar.HOUR_OF_DAY) != horaInicial
									|| (timeToValidate.get(Calendar.HOUR_OF_DAY) == horaInicial
											&& timeToValidate.get(Calendar.MINUTE) >= minutoInicial)) {
								int horaFinal = Integer.parseInt(iBloqueo.substring(7, 9));
								int minutoFinal = Integer.parseInt(iBloqueo.substring(10, 12));
								if (timeToValidate.get(Calendar.HOUR_OF_DAY) < horaFinal) {
									timeToValidate.set(Calendar.HOUR_OF_DAY, horaFinal);
									timeToValidate.set(Calendar.MINUTE, minutoFinal);
									timeToValidate.add(Calendar.MINUTE, 1);
									// return timeToValidate.getTime(); sigue para validar ams grupo
								} else {
									if (timeToValidate.get(Calendar.HOUR_OF_DAY) == horaFinal
											&& timeToValidate.get(Calendar.MINUTE) < minutoFinal) {
										timeToValidate.set(Calendar.HOUR_OF_DAY, horaFinal);
										timeToValidate.set(Calendar.MINUTE, minutoFinal);
										timeToValidate.add(Calendar.MINUTE, 1);
										// return timeToValidate.getTime();
									}
								}
							}
						}
					}

				} catch (NumberFormatException e) {
					return dateToValidate;
				}
			}
		}
		return timeToValidate.getTime();
	}

	public static boolean validateScheduleTime(String blockText, Date dateToValidate) {
		if (blockText == null || blockText.isEmpty())
			return true;
		String[] grupos = transformBlockTextToArray(blockText);
		if (grupos.length == 0)
			return false;
		Calendar timeToValidate = Calendar.getInstance();
		timeToValidate.setTime(dateToValidate);
		for (String iBloqueo : grupos) {
			iBloqueo = iBloqueo.replace("]", "");
			if (!iBloqueo.isEmpty()) {
				try {
					if (isDayBloqueo(iBloqueo)) {
						int horaInicial = Integer.parseInt(iBloqueo.substring(1, 3));
						if (timeToValidate.get(Calendar.HOUR_OF_DAY) >= horaInicial) {
							int minutoInicial = Integer.parseInt(iBloqueo.substring(4, 6));
							if (timeToValidate.get(Calendar.HOUR_OF_DAY) != horaInicial
									|| (timeToValidate.get(Calendar.HOUR_OF_DAY) == horaInicial
											&& timeToValidate.get(Calendar.MINUTE) >= minutoInicial)) {
								int horaFinal = Integer.parseInt(iBloqueo.substring(7, 9));
								if (timeToValidate.get(Calendar.HOUR_OF_DAY) < horaFinal) {
									return false;
								} else {
									int minutoFinal = Integer.parseInt(iBloqueo.substring(10, 12));
									if (timeToValidate.get(Calendar.HOUR_OF_DAY) == horaFinal
											&& timeToValidate.get(Calendar.MINUTE) < minutoFinal) {
										return false;
									}
								}
							}
						}
					}

				} catch (NumberFormatException e) {
					return false;
				}
			}
		}
		return true;
	}

	private static String[] transformBlockTextToArray(String blockText) {
		String[] grupos = null;
		// [(HH:MM-HH:MM)(L,M,X,J,V,S,D)(1,2,3,4,5,6,7,)][(HH:MM-HH:MM)(L,M,X,J,V,S,D)(1,2,3,4,5,6,7,)][(HH:MM-HH:MM)(L,M,X,J,V,S,D)(1,2,3,4,5,6,7,)]
		if (blockText.startsWith("[")) {
			grupos = blockText.split("\\[");
		} else {
			grupos = new String[1];
			grupos[0] = blockText;
		}
		return grupos;
	}

	private static boolean isDayBloqueo(String iBloqueo) {
		if (iBloqueo.length() > 13) {
			Calendar ahora = new GregorianCalendar();
			// valido que no sea el numero del día
			String bloqNDays = iBloqueo.substring(14, iBloqueo.length() - 1);
			String[] gNDays = bloqNDays.split(",");
			for (String iNDay : gNDays) {
				if (!iNDay.isEmpty()) {
					try {
						int cNDay = Integer.parseInt(iNDay);
						if (ahora.get(Calendar.DAY_OF_MONTH) == cNDay) {
							return true;
						}
					} catch (NumberFormatException e) {
						int cDay = -1;
						switch (iNDay) {
						case "D":
							cDay = 1;
							break;
						case "L":
							cDay = 2;
							break;
						case "M":
							cDay = 3;
							break;
						case "X":
							cDay = 4;
							break;
						case "J":
							cDay = 5;
							break;
						case "V":
							cDay = 6;
							break;
						case "S":
							cDay = 7;
							break;
						default:
							break;
						}
						if (ahora.get(Calendar.DAY_OF_WEEK) == cDay) {
							return true;
						}
					}

				}
			}
		} else {
			return true;
		}
		return false;
	}

	public static String obtenerValor(BasicParamDTO pCampo, String key) {
		if (pCampo == null || pCampo.getPropiedades() == null || pCampo.getPropiedades().isEmpty())
			return "";
		for (PropiedadDTO param : pCampo.getPropiedades()) {
			if (param.getKey().compareTo(key) == 0 && validarBloqueo(param))
				return param.getValor();
		}
		return "";
	}

	public static PropiedadDTO obtenerParametro(BasicParamDTO pCampo, String key) {
		if (pCampo == null || pCampo.getPropiedades() == null || pCampo.getPropiedades().isEmpty())
			return null;
		for (PropiedadDTO param : pCampo.getPropiedades()) {
			if (param.getKey().compareTo(key) == 0 && validarBloqueo(param))
				return param;
		}
		return null;
	}

	public static PropiedadDTO obtenerParametro(DocumentoPlantillaCaracteristicaDTO pCampo, String key) {
		if (pCampo == null || pCampo.getPropiedades() == null || pCampo.getPropiedades().isEmpty())
			return null;
		for (PropiedadDTO param : pCampo.getPropiedades()) {
			if (param.getKey().compareTo(key) == 0 && validarBloqueo(param))
				return param;
		}
		return null;
	}

	public static List<PropiedadDTO> obtenerVariosParametro(BasicParamDTO pCampo, String key) {
		if (pCampo == null || pCampo.getPropiedades() == null || pCampo.getPropiedades().isEmpty())
			return null;
		List<PropiedadDTO> parametros = new ArrayList<PropiedadDTO>();
		for (PropiedadDTO param : pCampo.getPropiedades()) {
			if (param.getKey().compareTo(key) == 0 && validarBloqueo(param))
				parametros.add(param);
		}
		if (parametros.isEmpty())
			return null;
		return parametros;
	}

	public static List<PropiedadDTO> obtenerVariosParametro(BasicParamDTO pCampo, String[] keys) {
		if (pCampo == null || pCampo.getPropiedades() == null || pCampo.getPropiedades().isEmpty())
			return null;
		List<PropiedadDTO> parametros = new ArrayList<PropiedadDTO>();
		for (PropiedadDTO param : pCampo.getPropiedades()) {
			for (String key : keys) {
				if (param.getKey().compareTo(key) == 0 && validarBloqueo(param)) {
					parametros.add(param);
					break;
				}
			}
		}
		if (parametros.isEmpty())
			return null;
		return parametros;
	}

	public static List<PropiedadDTO> retirarPropiedad(BasicParamDTO pCampo, String key) {
		if (pCampo == null || pCampo.getPropiedades() == null)
			return new ArrayList<PropiedadDTO>();
		List<PropiedadDTO> retirables = new ArrayList<PropiedadDTO>();
		for (PropiedadDTO param : pCampo.getPropiedades()) {
			if (param.getKey().compareTo(key) == 0)
				retirables.add(param);
		}
		if (!retirables.isEmpty()) {
			for (PropiedadDTO paramR : retirables) {
				pCampo.getPropiedades().remove(paramR);
			}
		}
		return pCampo.getPropiedades();
	}
	
	public static boolean isFunctionNotFreeMarker(String value) {
		if (value == null) return true;
		value = SoftureUtil.cleanStartEndSpaces(value);
		return (value.toLowerCase().startsWith("declare") || value.toLowerCase().startsWith("begin"));
	}

	public static void clearPropertiesToOut(List<PropiedadDTO> props) {
		if(props==null) return;
		for(PropiedadDTO iProp : props) {
			iProp.setBloqueo(null);
			iProp.setCambioCreacion(null);
			iProp.setCambioEliminacion(null);
			iProp.setRol(null);
			iProp.setFechaDefinicion(null);
			iProp.setFechaFinal(null);
			iProp.setFechaInicial(null);
			iProp.setRolExcluyente(null);
			iProp.setRolExcluyenteNombre(null);
			iProp.setRolNombre(null);
			iProp.setUsuario(null);
			iProp.setUsuarioExcluyenteNombre(null);
			iProp.setUsuarioNombre(null);
			if(iProp.getKey().contains("SQL"))iProp.setValor("OK");		
		}
	}
	
}
