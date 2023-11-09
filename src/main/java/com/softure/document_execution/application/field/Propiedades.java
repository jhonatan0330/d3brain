package com.softure.document_execution.application.field;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

	public static final String API_EXTRACTION = "API_EXTRACTION";
	public static final String API_EXTRACTION_NO_ERROR = "API_EXTRACTION_NO_ERROR";
	public static final String API_EXTRACTION_TO_BASE_64 = "API_EXTRACTION_TO_BASE_64";
	public static final String API_ITERATION_ONE_EXECUTION = "API_ITERATION_ONE_EXECUTION";
	public static final String API_READ_TIMEOUT = "API_READ_TIMEOUT";
	public static final String API_CONNECT_TIMEOUT = "API_CONNECT_TIMEOUT";

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

	public static final String FECHA_CON_HORA = "FECHA_CON_HORA";
	public static final String FECHA_SIN_CALENDAR = "FECHA_SIN_CALENDAR";
	public static final String FECHA_MAXIMA = "FECHA_MAXIMA";
	public static final String FECHA_MINIMA = "FECHA_MINIMA";
	public static final String FECHA_RANGO = "FECHA_RANGO";
	public static final String FECHA_RANGO_MAXIMO = "FECHA_RANGO_MAXIMO";
	public static final String FECHA_TIMER_BACK = "FECHA_TIMER_BACK";

	public static final String TEXTO_FORMULA = "TEXTO_FORMULA";
	public static final String TEXTO_LONGITUD = "TEXTO_LONGITUD";

	public static final String NUMERO_REDONDEO = "NUMERO_REDONDEO";
	public static final String NUMERO_MONEDA = "NUMERO_MONEDA";
	public static final String NUMERO_FORMULA = "NUMERO_FORMULA";
	public static final String NUMERO_MAXIMO = "NUMERO_MAXIMO";
	public static final String NUMERO_MINIMO = "NUMERO_MINIMO";
	public static final String NUMERO_FUNCION_SQL = "NUMERO_FUNCION_SQL";
	public static final String NUMERO_STEP = "NUMERO_STEP";
	public static final String TOTAL_FUNCION = "TOTAL_FUNCION";

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
	//public static final String INVENTARIO_OBLIGATORIO = "INVENTARIO_OBLIGATORIO";
	public static final String INVENTARIO_OPCIONAL = "INVENTARIO_OPCIONAL";
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

	public static final String BODEGA_FIJA = "BODEGA_FIJA";
	public static final String BODEGA_MOVIMIENTO = "BODEGA_MOVIMIENTO";

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
	public static final String RESPONSABLE = "RESPONSABLE";
	public static final String ORDEN = "ORDEN";
	public static final String ORDEN_DESCENDENTE = "ORDEN_DESCENDENTE";
	public static final String SOLICITAR_FECHAS = "SOLICITAR_FECHAS";
	public static final String COPY_TEXT = "COPY_TEXT";
	public static final String AYUDA = "AYUDA";
	public static final String UBICACION = "UBICACION";
	public static final String FUNCION_SQL_VALIDAR_ANTES = "FUNCION_SQL_VALIDAR_ANTES";

	public static final String CUENTA_SOBREGIRO = "CUENTA_SOBREGIRO";

	public static final String PERMISO_PLANTILLA_CREAR = "PERMISO_PLANTILLA_CREAR";
	public static final String PERMISO_PLANTILLA_MODIFICAR = "PERMISO_PLANTILLA_MODIFICAR";
	public static final String PERMISO_PLANTILLA_ELIMINAR = "PERMISO_PLANTILLA_ELIMINAR";
	public static final String PERMISO_PLANTILLA_INICIO_RAPIDO = "PERMISO_PLANTILLA_INICIO_RAPIDO";
	public static final String PERMISO_PLANTILLA_OCULTAR_TOTAL = "PERMISO_PLANTILLA_OCULTAR_TOTAL";
	public static final String PERMISO_PLANTILLA_CAMPO_FILTRO = "PERMISO_PLANTILLA_CAMPO_FILTRO";
	public static final String PERMISO_PLANTILLA_FILTROS_BASE = "PERMISO_PLANTILLA_FILTROS_BASE";
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
	public static final String PLANTILLA_DIFERENCIAS = "PLANTILLA_DIFERENCIAS";
	public static final String CAMPO_DIFERENCIAS = "CAMPO_DIFERENCIAS";
	public static final String PLANTILLA_TIPO_ROL = "PLANTILLA_TIPO_ROL";
	public static final String PLANTILLA_TIPO_REPORTE = "PLANTILLA_TIPO_REPORTE";
	public static final String PLANTILLA_TIPO_CUENTA = "PLANTILLA_TIPO_CUENTA";
	public static final String PLANTILLA_TIPO_PRODUCTO = "PLANTILLA_TIPO_PRODUCTO";
	public static final String PLANTILLA_TIPO_BODEGA = "PLANTILLA_TIPO_BODEGA";
	public static final String PLANTILLA_RENDER_ESPECIAL_SQL = "PLANTILLA_RENDER_ESPECIAL_SQL";
	public static final String PLANTILLA_HISTORIAL_ACTIVO = "PLANTILLA_HISTORIAL_ACTIVO";
	public static final String PLANTILLA_MONITOR = "PLANTILLA_MONITOR";

	public static final String GPS = "GPS";
	public static final String RELACIONAR_DOCUMENTOS = "RELACIONAR_DOCUMENTOS";
	public static final String RETIRAR_DOCUMENTOS = "RETIRAR_DOCUMENTOS";
	public static final String PLANTILLA_OCULTAR_GUARDAR = "PLANTILLA_OCULTAR_GUARDAR";
	public static final String PERIODO_LIMPIEZA_HISTORICO = "PERIODO_LIMPIEZA_HISTORICO";

	public static final String PLANTILLA_INICIA_PROCESO = "PLANTILLA_INICIA_PROCESO";

	// REPORTE
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

	// ESTADO PROCESO
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
	public static final String ITERACION_SQL = "ITERACION_SQL";

	// TRANSICION
	public static final String TEMPORIZADOR = "TEMPORIZADOR";

	public static final String OCULTAR_MENSAJE_LICENCIA = "OCULTAR_MENSAJE_LICENCIA";
	public static final String FORCE_NOTIFICATION = "FORCE_NOTIFICACTION";
	public static final String TABLERO_CONTROL_SQL = "TABLERO_CONTROL_SQL";

	// Organizacion
	public static final String API_KEY = "API_KEY";
	public static final String COVERAGE_IMAGE = "COVERAGE_IMAGE";

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

	public static String instrucciones(String formato) {
		if (formato == null)
			return "Sin instrucciones por formato no enviado";
		String ruleProperty = null;
		switch (formato) {
		case ALERTAR_CAMPO_PROCESO: {
			ruleProperty = "Muestra un mensaje en la creacion del documento en el momento que se seleccion un objeto de un campo proceso.\n"
					+ "En la propiedad se debe relacionar el campo que se va a mostrar del objeto seleccionado"
					+ "Si el documento no viene con ese campo no se muestra ningun mensaje";
			break;
		}
		case API_TRANSACCION:
		case API: {
			ruleProperty = "Identifica el API que se va a ejecutar al guardar el documento o realizar la transicion (en el caso de la transicion simpre va el documento gque genero la accion ).\n";
			break;
		}
		case API_KEY: {
			ruleProperty = "Coloca un texto que funcionara como token de validacion para los sistemas que se conecten por medio del API(x-api-key).\n";
			break;
		}
		case API_ASYNCHRONOUS: {
			ruleProperty = "Esta propiedad hace que el API se ejecute al terminar todo el proceso de la transaccion de forma asincrona, ejemplo los mensajes de texto que se vana  enviar cuando todo termine ok";
			break;
		}
		case API_HEADER: {
			ruleProperty = "Variables del Header de la peticion del API Valor contiene el KEY y Motivo contiene el texto.\n";
			break;
		}
		case API_CODE_DIRECT: {
			ruleProperty = "Se encarga de reemplazar un valor en el template.\n\nEn el template debes tener la estructura => {{D_XXXXXXX}} , donde XXXXXX es el codigo del campo";
			break;
		}
		case API_CODE_REFERENCE: {
			ruleProperty = "Se encarga de reemplazar un valor en el template, buscando en OTRA PLANTILLA.\n\nEn el template debes tener la estructura => {{R_XXXXXXX}} , donde XXXXXX es el codigo del campo.\n\nLo mas importante en los links relacionar la cadena de pasos en los campos que se debe seguir hasta llegar al campo deseado.\n EJ el nombre de un vendedor en una guia, entregada => (Propiedad se coloca el campo Guia), en los links se coloca el campoo guia vendedor y se agrega vendedor nombre\n\nPAra los campos fechas ademas de incluir el formato con el campo auxiliar tambien puedes icluir en parentesis la suma o resta de milisegundos";
			break;
		}
		case API_CODE_REFERENCE_LIST: {
			ruleProperty = "Cuando en un campo referenciado colcoas un campo PROCESO y MULTIPLE, el busca dentro de los documentos que tiene ese multiple los campos para colocarlos en los parametros quedan dentro de un array de la siguiente forma R_ITEMS=0;;I_ITEMS[1]=--L_NUM,,1--L_VAL,,0--L_VALOR,,24000--L_GUIA,,CR9007113;;I_ITEMS[2]=--L_NUM,,2--L_VAL,,0--L_VALOR,,43600 y en el tempalte usa lo siguiente <#if I_ITEMS??> <#list I_ITEMS as REMESA>  <cbc:ID>${REMESA.L_NUM}</cbc:ID> ";
			break;
		}
		case API_CODE_ESPECIAL: {
			ruleProperty = "Se encarga de reemplazar un valor en el template.\n\nColoca en el campo TEXTO de la propiedad el codigo que va a reeemplazar y en el VALOR coloca el texto que quieres que se modifique.\n\nSE crea un TRUCO para la fecha actual: el codigo debe empezar por E_FECHA_XXXXXX y en el VALOR de la propiedad colocas el formato tipo fecha\nAhora si quieres sumar valores debes seguir estas instrucciones: al final coloca el tiempo a sumar en parentesis dentro un signo el numero y una letra del tiempo, por el momento solo suma Dias ejemplo: E_FECHA_XXXX(+15*24*60*60*1000) OJO tienes que hacer la multiplicacion en la calculadora sino el sistema te caclula 10 años mas\n\nE_ID = llave id del documento\nE_CODE = codigo del documento\nE_CODE_MODIFICATOR = codigo del documento modificador";
			break;
		}
		case API_CODE_MODIFICADOR: {
			ruleProperty = "Se encarga de reemplazar un valor en el template, tomando como base el documento que genero la accion (Solo en transiciones).\n\nEn el template debes tener la estructura => {{M_XXXXXXX}} , donde XXXXXX es el codigo del campo, el valor auxiliar corresponde a formatos de fecha";
			break;
		}
		case API_ITERATION_ONE_EXECUTION: {
			ruleProperty = "En una transicion que va despues de un iterador, se va a ejecutar un api por cada documento SI Seleccionas esta propiedad SOLO SE VA A EJECUTAR UN API";
			break;
		}
		case API_EXTRACTION: {
			ruleProperty = "Extrae un valor con expresion regular, si tienes dudas busca por internet Java String match, esa extracción se debe colocar en un campo\n\nEn las relaciones se coloca el campo que deseamos que actualice con el valor a extraer.\n\n";
			break;
		}
		case API_EXTRACTION_NO_ERROR: {
			ruleProperty = "Extrae un valor con expresion regular, si tienes dudas busca por internet Java String match, esa extracción se debe colocar en un campo\n\nEn las relaciones se coloca el campo que deseamos que actualice con el valor a extraer.\n\n";
			break;
		}
		case API_EXTRACTION_TO_BASE_64: {
			ruleProperty = "Extrae un valor con expresion regular, si tienes dudas busca por internet Java String match, esa extracción se debe colocar en un campo\n\nEn las relaciones se coloca el campo que deseamos que actualice con el valor a extraer.\n\n";
			break;
		}
		case API_MAX_TRY: {
			ruleProperty = "Coloca un numero entre 2 y 3 para que se repita el llamado al WS";
			break;
		}
		case API_NEW_DOCUMENT: {
			ruleProperty = "Al recibir la respuesta del api puedes crear documento(s), tienes que seleccionar el nombre de la plantilla.\n\nEn el motivo es MUY IMPORTANTE que crees una expresion regular que extraiga la informacion que quieres para crear tu documento.\nCada match de la expresion regular sera un documento";
			break;
		}
		case API_READ_TIMEOUT: {
			ruleProperty = "Cuando se ejecuta un API se tiene un tiempo en el cual puede MANTENERSE CONETADO el sistema sin generar este error, tienes que colocar el numero de milisegundos que soportara el api sin generar el error read timeout";
			break;
		}
		case API_CONNECT_TIMEOUT: {
			ruleProperty = "Cuando se ejecuta un API se tiene un tiempo PARA CONECTARNOS sin generar este error, tienes que colocar el numero de milisegundos que soportara el api sin generar el error connect timeout";
			break;
		}
		case API_VALIDATION: {
			ruleProperty = "Coloca una expresion regular para que se valide que esa expresion regular haga match, si tienes dudas busca por internet Java String match y busca simuladores online de la expresion regular.\n\nTen cuidado de no dejar enter al final";
			break;
		}
		case API_SCHEDULE_TIME_BLOCK: {
			ruleProperty = "(Solo API asincronos)En caso que tu API tenga algunas restricciones de tiempo en esta propiedad tienes que colocar el horario disponible\n\nEl horario se coloca siguiendo la forma en que colocas los bloqueos de las propiedades:\n\n[(HH:MM-HH:MM)] =>Todos los dias en esas horas\n[(00:00-23:59)(L,M,X,J,V,S,D,1,2,3,4,5,6,7,....)]=>Los dias del segundo parentesis y en el horario del primer grupo\nEscoge la combinacion que necesites\nPueden existir varios bloqueos cada uno dentro de []";
			break;
		}
		case AUTOLOAD: {
			ruleProperty = "Define si carga la información desde el ingreso al modulo o por peticion del usuario.\n";
			break;
		}
		case AUTOLOAD_SAVE: {
			ruleProperty = "El campo si al guardar esta vacio va a consultar la funcion de BD o la fuente de datos y va a tomar la primera respuesta colocandola en este campo .\n";
			break;
		}
		case ARCHIVO_TIPO: {
			ruleProperty = "Filtra el tipo de archivo, para usar varias extensiones separalas por coma(,).\n";
			break;
		}
		case ARCHIVO_TAMANO_MAXIMO: {
			ruleProperty = " : Restringue el tamaño maximo de los archivos a subir (en MB).\n";
			break;
		}
		case AYUDA: {
			ruleProperty = " Url que lleva a la ayuda del formulario.\n";
			break;
		}
		case BINARIO_VERDADERO: {
			ruleProperty = " Texto a colocar en la opcion verdadero.\n";
			break;
		}
		case BINARIO_FALSO: {
			ruleProperty = " Texto a colocar en la opción falso.\n";
			break;
		}
		case BODEGA_FIJA: {
			ruleProperty = " Coloca el nombre o codigo de la bodega fija de ese inventario, No se guarda en BD ese registro.\n";
			break;
		}
		case BODEGA_MOVIMIENTO: {
			ruleProperty = " (E,S,T,ED,SD,EC,SC).\nEntrada(E) Salida(S) Transformacion(T) D=Directo(solo toma el producto), C=Composicion(Solo toma la composicion del producto)"
					+ "En caso de ser varios dependientes se crean varios parametros\n\n"
					+ "Tener en cuenta que este campo debe ir primero que los dependientes de los productos"
					+ "Debes colocar en las relaciones el camino para profundizar los documentos que van a mover inventario\n\n";
			break;
		}
		case CAMPO_HEREDADO_1: {
			ruleProperty = "Esta propiedad indica que los(el) documentos(s) que se visualizan o crean tienen un campo que referencian el documento actual.\n\n"
					+ "En los links de la propiedad se debe referenciar el campo de la plantilla que contiene la referencia al documento actual\n\n"
					+ "Ejemplo un documento CLIENTE con un campo CONTACTOS, este campo se le coloca la propiedad HEREDADO, para que puedas crear los contactos de ese cliente. El contacto debe tener un campo proceso que va a guardar el dato del cliente";
			break;
		}
		case CELULAR_ROL: {
			ruleProperty = " Codigo del campo que va a colocar almacenar el telefono del usuario.\n";
			break;
		}
		case CONFIGURACION_PLANTILLA_TIPO: {
			ruleProperty = " (Comunicale al desarrollador que esta función se necesita) Si es tipo plantilla sirve para filtrar los tipo.\n";
			break;
		}
		case CONFIGURACION_ENTIDAD: {
			ruleProperty = " Carga diferentes parametros de configuracion.\n" + "CATEGORIA_PRODUCTOS\n" + "PROCESO\n"
					+ "PRODUCTOS\n" + "PLANTILLAS\n" + "ROLES\n" + "ENCUESTAS\n" + "TARIFARIO\n" + "BODEGAS\n"
					+ "FORMATO_EXPORTAR\n";
			break;
		}
		case CONSECUTIVO: {
			ruleProperty = " Codigo del campo que va a colocar consecutivo del documento, se usa con un consecutivo manual.\n";
			break;
		}
		case COPY_TEXT: {
			ruleProperty = " Al momento de guardar el documento se va a copiar este texto en el portapapeles, [CODE]=(codigo del documento).\n";
			break;
		}
		case COVERAGE_IMAGE: {
			ruleProperty = " Coloca toda la url de laimagen de fondo que deseas ver en el perfil principal.\n";
			break;
		}
		case CORREO_ROL: {
			ruleProperty = " Codigo del campo que va a colocar almacenar el correo del usuario.\n";
			break;
		}
		case CUENTA_MOVIMIENTO: {
			ruleProperty = " Si desea generar un movimiento de caja coloca I (Ingresos) G (Gastos).\n";
			break;
		}
		case CUENTA_ANULAR_MOVIMIENTO: {
			ruleProperty = " Este campo anula un movimiento que este vinculado con el proceso seleccionado, lo elimina del movimiento de cuentas\n";
			break;
		}
		case CUENTA_ABRIR_CAJA: {
			ruleProperty = " Si desea iniciar turn en una caja.\n";
			break;
		}
		case CUENTA_CERRAR_CAJA: {
			ruleProperty = " Si desea cerrar el turno en una caja.\n";
			break;
		}
		case DECISION_SQL: {
			ruleProperty = "Genera una funcion que devuelve una cadena de texto, para que las transacciones siguientes lo tomen por el nombre.\n"
					+ "CREATE OR REPLACE FUNCTION decision_${llaveTabla}(documento character varying, modificador character varying) RETURNS character varying AS";
			break;
		}
		case DEFAULT: {
			ruleProperty = "Coloca un valor en el campo (Tipo numero y texto), para los tipo proceso solo funciona para autoload y busca el valor con el id seleccionado.\n";
			break;
		}
		case DESCRIPCION: {
			ruleProperty = " Coloca una descripcion al documento segun los campos del mismo.\n";
			break;
		}
		case CAMPO_EVIDENCIA: {
			ruleProperty = "Selecciona un campo de la plantilla para mostrarlo en la trazabilidad como link para ver las evidencias del documento.\n";
			break;
		}
		case DESCRIPCION_NIVEL2: {
			ruleProperty = " Toma la descripcion de un campo proceso para el.\n";
			break;
		}
		case DETALLE_NUMERO_COLUMNAS: {
			ruleProperty = " EN la interfaz web coloca el numero de columnas deseado, por defecto 1.\n";
			break;
		}
		case DETALLE_TECLADO: {
			ruleProperty = " Carga un teclado debajo del espacio del campo texto que filtra.\n";
			break;
		}
		case DETALLE_TARIFARIO: {
			ruleProperty = " Escoge el tarifario que va a colocar los valores a los productos.\n";
			break;
		}
		case DETALLE_OCULTAR_IMAGENES: {
			ruleProperty = " Oculta las imagenes de los productos.\n";
			break;
		}
		case DETALLE_TARIFARIO_SQL: {
			ruleProperty = "Se crea una funcion para traer las tarifas, los campos depende se envian como parametros. Tener cuidado en el orden de los parametros es en orden ALFABETICO del codigo.\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(producto character varying, producto_base character varying, parametros character varying[])  RETURNS SETOF tarifa_tarp AS";
			break;
		}
		case DETALLE_FORMULA: {
			ruleProperty = " Este parametro indica el valor total de la suma de productos escogidos si se requiere que sea diferente a la sumatoria de los productos. Se utiliza una formula para sumar o restar.\n";
			break;
		}
		case DETALLE_OCULTAR_UNIDADES_NOMBRE_CANTIDAD: {
			ruleProperty = " Ocultas la columna Valor Unidad.\n Coloca en valor el nombre que quieres en la columna unidades";
			break;
		}
		case DISPONIBILIDAD_CROQUIS: {
			ruleProperty = "Relaciona el campo del formulario que tiene la estructura o del que inicia a buscar la estructura, con las relaciones traza un camino para llegar al campo que tiene el croquis, no lo olvides";
			break;
		}
		case ENCABEZADO: {
			ruleProperty = " Colocar los parametros de encabezado (SEDE) de reportes dinamicos.\n";
			break;
		}
		case FECHA: {
			ruleProperty = "  Codigo del campo que va a colocar la fecha del documento.\n";
			break;
		}
		case FECHA_CON_HORA: {
			ruleProperty = " La fecha muestra horas.\n";
			break;
		}
		case FECHA_SIN_CALENDAR: {
			ruleProperty = " La fecha no muestra las fechas para escoger dia.\n";
			break;
		}
		case FECHA_MAXIMA: {
			ruleProperty = " Coloca un numero del tiempo maximo (actual + num), el tiempo es en milisegundos.\n";
			break;
		}
		case FECHA_MINIMA: {
			ruleProperty = " Coloca un numero del tiempo minimo (actual - num), el tiempo es en milisegundos.\n";
			break;
		}
		case FECHA_RANGO: {
			ruleProperty = " La fecha tiene un rango, especialmente para reportes.  (*) =  Todos los rangos. D = Dia, M = Meses, R = Rango. Puedes combinar separado por (;break;}).\n";
			break;
		}
		case FECHA_RANGO_MAXIMO: {
			ruleProperty = " Cuando es rango, este es un limite de tiempo entre la fecha de incio y la fecha de fin el tiempo es en milisegundos.\n";
			break;
		}
		case FECHA_TIMER_BACK: {
			ruleProperty = "Activando esta propiedad se va a mostrar un reloj en cuenta regresiva segun al fecha seleccionada.\n";
			break;
		}
		case FORMATO: {
			ruleProperty = " Para campos texto N(Solo numero), E(Correo electronico), T(Telefono).\n\n Para campos numero se utiliza un DecimalFormat\n\nS(Simple) = El texto no se coloca en mayusculas";
			break;
		}
		case FUNCION_SQL_VALIDAR_ANTES: {
			ruleProperty = " Antes de iniciar a ejecutar las validaciones y los almacenamientos se va a ejecutar esta funcion de BD con resultados S y N.\n\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}( documento character varying, token character varying, parametros character varying[]) RETURNS void AS";
			break;
		}
		case FUNCION_SQL_VALIDAR: {
			ruleProperty = " Al momento de ejecutar la transicion se va a ejecutar esta funcion de BD con resultados S y N.\n\n"
					+ "Cuando solo es un formulario al guardar y desea validar el campo documento tiene la llave del documento y modificador es null\n\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, modificador character varying, token character varying) RETURNS character varying AS";
			break;
		}
		case FUNCION_SQL_PREVALIDATE_API: {
			ruleProperty = "En algunos casos es necesario realizar unas validaciones previas al API que eviten que enviemos errores al endpoint, lo hacemos con una funcion de base de datos "
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, modificador character varying, parametros character varying[]) RETURNS void AS\n\n"
					+ "Esta funcion sirve para obtener un parametro en el texto de parametros que extrae el api SELECT SUBSTRING (parametros,'CODE=([\\d\\w\\s,]*)') as texto_extrae_parametros"
					+ "\n\n si necesita que salga algun error utiliza raise exception"
					+ "\n\nEste es un ejemplo de validar un telefono"
					+ "\ndeclare\n"
					+ "  numero_telefono character varying;\n"
					+ "begin\r\n"
					+ "  SELECT SUBSTRING (parametros,'TELEFONO=([\\d]*)') into numero_telefono;\r\n"
					+ "  if ( SELECT REGEXP_MATCHES(numero_telefono,'^\\d{10}$') is not null ) then\r\n"
					+ "  	-- todo OK\r\n"
					+ "  else\r\n"
					+ "    raise exception 'El telefono recibido debe contener 10 numeros. Enviaste el numero %', numero_telefono;    \r\n"
					+ "  end if;\r\n"
					+ "end";
			break;
		}
		case GENERA_DOCUMENTO_CAMPO: {
			ruleProperty = "La transicion debe tener plantilla.\n\nDe esta plantilla referenciamos el campo a llenar y en los links colocamos el campo del documento maestro que va a copiar el campo";
			break;
		}
		case GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR: {
			ruleProperty = "La transicion debe tener plantilla.\nDe esta plantilla referenciamos el campo a llenar con el id del documento que GENERA LA ACCION\n";
			break;
		}
		case GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE: {
			ruleProperty = "La transicion debe tener plantilla.\nDe esta plantilla referenciamos el campo a llenar con el id del documento del proceso que estamos trabajando\n";
			break;
		}
		case GENERA_DOCUMENTO_FUNCION_SQL: {
			ruleProperty = "Crea un campo para agregar a un documento.\nDebes crear una funcion para obtener los datos del campo.\nEn las relaciones debes colocar una relacion al campo que deseas llenar\n"
					+ "\n\nAyuda para generar el campo\n"
					+ "\nbegin return next(SELECT ROW(null, null, null, null, null, null, null, null, null, null, null)::pedidoventacaracteristica_pvcp); end\n"
					+ "\n\nEstructura de la funcion\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, modificador character varying) RETURNS SETOF pedidoventacaracteristica_pvcp AS";
			break;
		}
		case INVENTARIO_OPCIONAL: {
			ruleProperty = "En los productos, dependiendo la categoria pueden tener inventario, por defecto vamos a obligar a tener inventario, con esta propiedad no obligamos el producto y omite esa configuracion";
			break;
		}
		case INFORMATIVE_DATA: {
			ruleProperty = "Seleccionar el campo que tiene el documento del cual vamos a obtener la informacion y en las refencias colocas el campo del cual vamos a traer la informacion del documento";
			break;
		}
		case FORCE_NOTIFICATION: {
			ruleProperty = "Indica que al abrir el sistema en caso que tenga notificaciones sin leer se van a mostrar al usuario inmediatamente";
			break;
		}
		case FUNCION_SQL_ESTADO_ASIGNAR: {
			ruleProperty = "Cuando un documento llegue a este estado sera asignado al usuario que devuelva la funcion.\nEl documento que envia es el id del expediente, para el modificador necesitamos unsc para mejorar la funcion\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, modificador character varying) RETURNS character varying AS";
			break;
		}
		case INVISIBLE: {
			ruleProperty = "Oculta el campo a la vista, aunque solamente esta oculto.\n";
			break;
		}
		case ITERACION_SQL: {
			ruleProperty = "Genera una funcion que devuelve varios documentos, para que se ejecute una transaccion sobre ellos.\n"
					+ "CREATE OR REPLACE FUNCTION iteracion_${llaveTabla}(documento character varying, modificador character varying) RETURNS SETOF pedidoventa_pdvp AS";
			break;
		}
		case MENSAJE: {
			ruleProperty = "Coloca el nombre de un template de mensaje de correo, este template va a ser enviado por correo.\n";
			break;
		}
		case MENSAJE_ADJUNTO_URL: {
			ruleProperty = "Referencias un campo para obtener la url del documento que se va a adjuntar en el correo.\n";
			break;
		}
		case MENSAJE_DESTINATARIO: {
			ruleProperty = "Coloca el usuario de la plataforma que va a recibir una copia del reporte.\n\n Adicionalmente en las relaciones de este campo puedes colocar una cadena de relaciones para identificar el campo de text que quieres que reciba el correo ej: Entrega de Guia (Tiene un campo guia -Relacion 1-(Dentro de la guia hay un campo cliente -Relacion 2-(Dentro del cliente hay un campo correo -Relacion 3-)))";
			break;
		}
		case MENSAJE_DESTINATARIOS_SQL: {
			ruleProperty = "Funcion de BD que trae usuarios o solo correos a los que se les debe enviar el mensaje"
					+ "\n(documento): la funcion tiene una variable que el es id del documento."
					+ "\n para la funcion puedes usar : return query (select null::character varying, 'correo@cambiame.com') "
					+ "\n\nEstructura de la funcion\n"
					+ " CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying) RETURNS TABLE (usuario character varying, correo character varying)";
			break;
		}
		case MODIFICAR_CAMPO: {
			ruleProperty = " Sirve para colocar el valor de un campo de la plantilla actual en otra plantilla (la que se referecie como propiedad), primero selecciona el nombre del campo que tiene el valor (origen) y despues en las relaciones escoge el campo que va a recibir el nuevo valor (destino)\n";
			break;
		}
		case MULTIPLE: {
			ruleProperty = " En cada tipo de campo es diferente pero su objetivo es permitir escoger varios items o agregar varios items\n";
			break;
		}
		case MULTIPLE_FILE: {
			ruleProperty = "Permite que el campo cargue varias imagenes o archivos de adjuntos\n";
			break;
		}
		case NUMERO_MONEDA: {
			ruleProperty = " Identifica el campo como tipo moneda\n";
			break;
		}
		case NUMERO_FORMULA: {
			ruleProperty = " Formula para calcular el valor del campo.\n";
			break;
		}
		case NUMERO_MINIMO: {
			ruleProperty = " Formula para calcular el valor MINimo de un campo.\nEn caso que coloque un valor en el motivo de la propiedad este mensaje se va a mostrar";
			break;
		}
		case NUMERO_MAXIMO: {
			ruleProperty = " Formula para calcular el valor MAXimo de un campo.\nEn caso que coloque un valor en el motivo de la propiedad este mensaje se va a mostrar";
			break;
		}
		case NUMERO_FUNCION_SQL: {
			ruleProperty = " Funcion que calcula un numero \n Se envia el id del documento actual y los depende el valoropcion\nCuando el campo es numero, bloqueado y tiene esta propiedad se calcula al guardar, pero debe estar de ultima en el orden del formulario.\n\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, parametros character varying[])  RETURNS SETOF numeric AS";
			break;
		}
		case NUMERO_STEP: {
			ruleProperty = " El numero avanza segun el valor de este parametro con las flechas.\n";
			break;
		}
		case NUMERO_REDONDEO: {
			ruleProperty = " Numero de digitos decimales que tendra el campo(Por defect 0 numero, 2 si es moneda).Maximo 6\n";
			break;
		}
		case OCULTAR_MENSAJE_LICENCIA: {
			ruleProperty = "El rol que tenga esta propieadd no vera nunca los mensajes de vencimiento de sistema\n";
			break;
		}
		case OCULTAR_REPORTE: {
			ruleProperty = "Evita que el reporte sea mostrado al usuario\n";
			break;
		}
		case OPCIONES: {
			ruleProperty = "Esta opcion se mostrara como una opción, si llenas la propiedad texto ese valor se colocara como el identificador y en la propiedad el campo valor sera el nombre.\nNo es obligatorio llenar el campo texto ";
			break;
		}
		case ORDEN: {
			ruleProperty = " Determina si se ordena por nombre(N), descripcion(D) o por fecha(F), Default F.\n";
			break;
		}
		case ORDEN_DESCENDENTE: {
			ruleProperty = " Determina si se ordena por nombre(N) o por fecha(F), Default F.\n";
			break;
		}
		case PERMISO_PLANTILLA_INICIO_RAPIDO: {
			ruleProperty = "Colcoa el campo que deseas copiar en un formulario nuevo que se abrira al momento de guardar un registro";
			break;
		}
		case PERMISO_PLANTILLA_LISTAR_MENU:
		case PERMISO_PLANTILLA_LISTAR_MENU_PROCESO: {
			ruleProperty = "Muestra en el menu principal este proceso o plantilla para el usuario y que se pase al listado de esos documentos.\n";
			break;
		}
		case PLANTILLA_AUXILIAR: {
			ruleProperty = " Tiene el id de la plantilla que se usa en este proceso. (Puede colocar nombre o codigo, el lo convierte)\n";
			break;
		}
		case PLANTILLA_OCULTAR_GUARDAR: {
			ruleProperty = "En la pantalla el formulario no muestra el boton guardar, asi los usuarios se obligan a ejecutar las siguientes transiciones\n";
			break;
		}
		case PERIODO_LIMPIEZA_HISTORICO: {
			ruleProperty = "Cantidad de dias que van a tenerse en cuenta para pasar los registros a las tablas de historicos (los documentos que pasen la fecha de creacion mayor se van a migrar)"
					+ "\n\n Si la plantilla hace parte del inicio de un proceso solo se va a migrar a la tabla historico los documentos inactivos o finalizados"
					+ "\n\n Si la plantilla NO hace parte del inicio de un proceso se van a pasar todos los documentos esto es muy util para los reportes"
					+ "\n\n VALOR: Coloca el numero de dias que se van mantener en la tabla principal el registro de ese tipo de plantilla"
					+ "TEXTO: Coloca la frecuencia con la que se va a repetir esta transicion (YY:MM:DD:HH:MM).\n Ej 1 Cada 3 dias = 00:00:03:00:00\n Ej 2 Cada 1 hora = 00:00:00:01:00\n Ej 2 Cada mes y medio = 00:01:15:00:00\n\nColoca la fecha inicial para que ese sea el punto de partida del temporizador\n\ncrae una relacion con la MISMA plantilla de la transicion y el campo para saber en que campo coloca los documentos";
			break;
		}
		case PLANTILLA_CARGA_MASIVA_MULTIPLE: {
			ruleProperty = "Relaciona un campo para que se realice una carga masiva multiple, busca el codigo del campo que va a subir los mnultiples\n";
			break;
		}
		case PLANTILLA_MONITOR: {
			ruleProperty = "Nos permite monitorear la plantilla en el motor de indicadores, se debe escoger un catalogo para incluir la plantilla y despues en el catalogo se puede administrar";
			break;
		}
		case PLANTILLA_HISTORIAL_ACTIVO: {
			ruleProperty = " Al momento de consultar el historial por defecto el muestra solo la opcion de documentos, con esta porpiedad se pueden activar las otras opciones de forma inicial, para lograrlo debes usar numeros de la siguiente forma:\n\n1-Documentos\n2-Asignaciones\n3-Mensajes\n4-Inventario\n5-Automaticas\n6-Reportes\n7-API";
			break;
		}
		case PLANTILLA_TIPO_ROL: {
			ruleProperty = " Asocia esta plantilla con un rol del sistema, creandolo\n";
			break;
		}
		case PLANTILLA_TIPO_REPORTE: {
			ruleProperty = " Crear la configuracion de un reporte\n";
			break;
		}
		case PLANTILLA_TIPO_CUENTA: {
			ruleProperty = " Define que esta plantilla relaciona los documentos creados con una cuenta\n";
			break;
		}
		case PLANTILLA_TIPO_PRODUCTO: {
			ruleProperty = " Define que esta plantilla va a crear un producto\nSe debe llenar con una categoria de producto";
			break;
		}
		case PLANTILLA_TIPO_BODEGA: {
			ruleProperty = "Los documentos de esta plantilla van a crear bodegas\n";
			break;
		}
		case PLANTILLA_RENDER_ESPECIAL_SQL: {
			ruleProperty = "Cuando una plantilla debe tener un campo que no pertenece a ella se utiliza una funcion para traer los campos deseados\n"
					+ "\n\nSe recibe el id del documento y a partir de el se debe generar la consulta"
					+ "\n\nCREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying) RETURNS SETOF pedidoventacaracteristica_pvcp AS";
			break;
		}
		case PROCESO_POP: {
			ruleProperty = " Para las listas autoload muestra el pop up y deja para escoger en el formulario.\n";
			break;
		}
		case PROCESO_ACCIONES: {
			ruleProperty = " Permite que se puedan realizar acciones de crear y modificar.\n Se debe definir la plantilla que tiene permisos para crear";
			break;
		}
		case PROCESO_GESTIONAR_ESTADOS: {
			ruleProperty = " Es el camino que debi seguir la transaccion para modificar estados de documentos. * -> se usa para indicar todos los documentos. Se coloca los codigos de las planatillas separads por punto y coma(;break;}). se pueden colocar varios caminos\n";
			break;
		}
		case PROCESO_DIVISION: {
			ruleProperty = " Propiedad ESPECIAL que divide el documento.\n";
			break;
		}
		case PROCESO_VALOR: {
			ruleProperty = " Toma el valor del campo (0 - Valor de la cuenta, 1 - total , 2 - saldo) de los documentos.\n";
			break;
		}
		case PROCESO_FUNCION_SQL: {
			ruleProperty = "Coloca el SQL de los datos que quieres obtener.\n\nSi tienes un dependiente ese va ubicado en el campo documento\n\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[])\r\n"
					+ "RETURNS SETOF pedidoventa_pdvp AS\n"
					+ "select * from pedidoventa_pdvp where cpdv_plantilla = '' and (codigo_exacto is null or cpdv_nombre = codigo_exacto) and (fechaminima is null or dpdv_fecha >=fechaminima) and (fechamaxima is null or dpdv_fecha < fechamaxima) and (filtro is null or (cpdv_nombre like upper('%' ||filtro|| '%') or upper(cpdv_textofiltro) like upper('%' || filtro || '%'))) order by cpdv_nombre desc limit cant offset pagina";
			break;
		}
		case PRODUCTO_CAMPO_VALOR_MINIMO: {
			ruleProperty = "referencia el campo del producto que va a mostrar el valor minimo.\n";
			break;
		}
		case PRODUCTO_CAMPO_VALOR_UNITARIO: {
			ruleProperty = "Referencia el campo que va a mostrar el valor unitario.\n";
			break;
		}
		case PRODUCTO_CAMPO_CANTIDAD: {
			ruleProperty = "EL softwate toma este campo como la cantidad a registrar del item de venta.\n";
			break;
		}
		case PRODUCTO_CAMPO_TOTAL: {
			ruleProperty = "Este campo sera el valor total del producto.\n";
			break;
		}
		case PRODUCTO_PUESTO: {
			ruleProperty = "Este campo consultara un producto y este producto sera aplicado al puesto con una sola unidad, se usa principalmente para obtener el detall del producto.\n";
			break;
		}
		case PROCESO_INCLUIR_TRAZA_PRINCIPAL: {
			ruleProperty = "Cuando colocas esta propiedad en el proceso que se selecciona en el campo aparecera este documento como parte de la trazabilidad.\n No es necesario usar BPM.\n";
			break;
		}
		case P_SUBREPORT_: {
			ruleProperty = " Coloque los diferentes subreportes que se necesitan.\n";
			break;
		}
		case READ_QR: {
			ruleProperty = "En los campos proceso muestra el boton para activar la camara de lectura de codigos QR, selecciona el formato del codigo de barras, entre menos mejor\n\nFormatos = 'AZTEC','CODABAR','CODE_39','CODE_93','CODE_128','DATA_MATRIX','EAN_8','EAN_13','ITF','MAXICODE','PDF_417','QR_CODE','RSS_14','RSS_EXPANDED','UPC_A','UPC_E','UPC_EAN_EXTENSION'";
			break;
		}
		case RELACIONAR_DOCUMENTOS: {
			ruleProperty = "Permite AGREGAR documentos a un campo proceso multiple de otro documento.\n\nRelaciona el campo actual de la plantilla y en los links relaciona el campo de la plantilla destino";
			break;
		}
		case RETIRAR_DOCUMENTOS: {
			ruleProperty = "Permite QUITAR documentos a un campo proceso multiple de otro documento.\n\nRelaciona el campo actual de la plantilla y en los links relaciona el campo de la plantilla destino";
			break;
		}
		case RESPONSABLE: {
			ruleProperty = " Codigo del campo que relaciona el responsable de la actividad.\n";
			break;
		}
		case REPORTE_ENCABEZADO: {
			ruleProperty = " Toma como base este reporte para dibujar el encabezado de cada pagina.\nEl reporte debe tener la linea <parameter name=\\\"P_KEY\\\" class=\\\"java.lang.String\\\"/> para reemplazar y va a agregar otro parametro NOMBRE para que no se duplique";
			break;
		}
		case REPORTE_EXCEL: {
			ruleProperty = "Nombre del reporte que se va a ejecutar cuando sea en excel, puede estar inactivo.\n";
			break;
		}
		case REP_AUTOPRINT: {
			ruleProperty = "El reporte se imprime despues de creado o modificado el documento.\n";
			break;
		}
		case REP_TYPE_EXPORT: {
			ruleProperty = "Esta propiedad permite definr el tipo de reporte a exportar las siguientes son las opciones:.\n XLS :  Descarga un reporte en excel \n HTML :  Muestra el reporte en una pagina web \n PDF : descarga un formato en PDF";
			break;
		}
		case REP_VISIBLE_STATE: {
			ruleProperty = "El reporte solo se visualiza si el documento se encuentra en un estado especifico.\n";
			break;
		}
		case REP_EXCLUDE_STORAGE_FILE: {
			ruleProperty = "El reporte no se almacena en el servidor.\n";
			break;
		}
		case REP_PRINT_ONE: {
			ruleProperty = "El reporte solo se puede imprimir una sola vez.\n";
			break;
		}
		case REPORTE_IMAGEN: {
			ruleProperty = "Carga la imagen en base 64 de un reporte, esta imagen se pasara como un parametro al reporte, se guarda con el formato para que sea visible en un explorador (ej: data:image/png;base64,iVBORw0KGg...)\n\n<import value=\"org.apache.commons.codec.binary.Base64\"/>\n\n new ByteArrayInputStream(Base64.decodeBase64($P{P_IMAGEN_LOGO}.getBytes()))\n\n Usa esta url para modificar la imagen https://www.base64-image.de/";
			break;
		}
		case REPORTE_JRXML: {
			ruleProperty = "Se coloca el texto del jrxml.\n\nLas imagenes deben estar como parametros o como campos de la consulta el valida el texto del source de la imagen sea un parametro para base64 o un campo($)\nimageExpression><![CDATA[$ \nimageExpression><![CDATA[new ByteArrayInputStream";
			break;
		}
		case SAVE_TO_SELECT: {
			ruleProperty = "En algunos formularios es necesario avanzar rapido al seleccionar un dato, con esta propiedad s envia a guardar el formulario, muy util para cargues y descargues con pistolas de QR, se usa mucho con la propiedad INICIO RAPIDO";
			break;
		}
		case SOLICITAR_FECHAS: {
			ruleProperty = " Obliga al usuario colocar fechas al momento de realizar la consulta.\n";
			break;
		}
		case TABLERO_CONTROL_SQL: {
			ruleProperty = "Muestra un item en el menu que traera una serie de objetos que se definen en un query.\n"
					+ "En el campo TEXTO va el nombre del tablero\n"
					+ "En el campo MOTIVO va la url de la imagen del tablero\n"
					+ "\nEsta consulta no tiene dependientes ya que viene solo del menu, el unico dato considerable es el TOKEN y los filtros de fecha y cantidad\n\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[])\r\n"
					+ "RETURNS SETOF pedidoventa_pdvp AS\n\n"
					+ "select * from pedidoventa_pdvp where cpdv_plantilla = '' and (codigo_exacto is null or cpdv_nombre = codigo_exacto) and (fechaminima is null or dpdv_fecha >=fechaminima) and (fechamaxima is null or dpdv_fecha < fechamaxima) and (filtro is null or (cpdv_nombre like upper('%' ||filtro|| '%') or upper(cpdv_textofiltro) like upper('%' || filtro || '%'))) order by cpdv_nombre desc limit cant offset pagina";
			break;
		}
		case TEMPORIZADOR: {
			ruleProperty = " VALOR: funcion sql que consulta datos\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying)\n"
					+ "RETURNS SETOF pedidoventa_pdvp AS"
					+ "TEXTO: Coloca la frecuencia con la que se va a repetir esta transicion (YY:MM:DD:HH:MM).\n Ej 1 Cada 3 dias = 00:00:03:00:00\n Ej 2 Cada 1 hora = 00:00:00:01:00\n Ej 2 Cada mes y medio = 00:01:15:00:00\n\nColoca la fecha inicial para que ese sea el punto de partida del temporizador\n\ncrae una relacion con la MISMA plantilla de la transicion y el campo para saber en que campo coloca los documentos";
			break;
		}
		case TEXTO_FORMULA: {
			ruleProperty = " calcula un valor texto segun los dependientes.";
			break;
		}
		case TEXTO_LONGITUD: {
			ruleProperty = "Coloca un numero que va a limitar la cantidad de caracteres que puedes escribir.";
			break;
		}
		case TOTAL: {
			ruleProperty = " Codigo del campo que va a colocar el valor TOTAL del documento.\n";
			break;
		}
		case TOTAL_FUNCION: {
			ruleProperty = " Se usa en los campos de un producto para calcular el total segun una funcion.\nHoy no se exactamente como se usa o donde se usa";
			break;
		}
		case UBICACION: {
			ruleProperty = "Esta propiedad referencia el campo del documento que al que se la a asignar que tiene ese documento, Ej una VENTA en una trnsicion de enviar con un formulario con campo VEHICULO en esta propiedad, despues de esta transición en la trazabilidad va a decir que quedo en el vehiculo seleccionado"
					+ "\n\n No se puede asignar a una transicion que termine en una decision";
			break;
		}		
		case UPDATE_INFORMATIVE_FIELD: {
			ruleProperty = "Esta propiedad referencia los campos del documento que tienen campos informativos que tienen que ser actualizados"
					+ "\n\nEn las relaciones debes referenciar el campo informativo de la plantilla destino"
					+ "\n\nTen en cuenta que solo se actualizaran documentos en estado activo, los inactivos y cerrados no se actualizan";
			break;
		}
		case UNICO_PRODUCTO: {
			ruleProperty = " Oculta la lista para escoger un solo producto.\n";
			break;
		}
		case VALIDATE_ORIENTATION: {
			ruleProperty = "Valida que sea una imagen y su orientacion\n 1 = Valida ancho sea mayor que alto (Horizontal)\n 2 = Valida alto sea mayor que ancho (Vertical)";
			break;
		}
		case VISIBLE_VALOR_DEPENDIENTE: {
			ruleProperty = "Muestra un campo si el dependiente cumple con un valor(texto), prima la propiedad invisible.\n";
			break;
		}
		default: {
			ruleProperty = "Por favor solicita un cambio para agregar informacion de este key";
			break;
		}
		}
		return ruleProperty;
	}

}
