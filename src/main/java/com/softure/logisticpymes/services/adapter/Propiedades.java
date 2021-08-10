package com.softure.logisticpymes.services.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.softure.logisticpymes.dto.BasicParamDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;

public class Propiedades {
	//GENERALES
	public static final String FUNCION_SQL_VALIDAR = "FUNCION_SQL_VALIDAR";
	public static final String MENSAJE = "MENSAJE";
	public static final String MENSAJE_REPORTE = "MENSAJE_REPORTE";
	public static final String MENSAJE_DESTINATARIOS_SQL = "MENSAJE_DESTINATARIOS_SQL";
	public static final String MENSAJE_DESTINATARIO = "MENSAJE_DESTINATARIO";
	public static final String API = "API";
	public static final String API_TRANSACCION = "API_TRANSACCION";
	public static final String API_HEADER = "API_HEADER";
	public static final String API_NEW_DOCUMENT = "API_NEW_DOCUMENT";
	public static final String API_SECONDARY_DOCUMENT = "API_SECONDARY_DOCUMENT";
	public static final String API_CODE_DIRECT = "API_CODE_DIRECT";
	public static final String API_CODE_REFERENCE = "API_CODE_REFERENCE";
	public static final String API_CODE_ESPECIAL = "API_CODE_ESPECIAL";
	public static final String API_CODE_MODIFICADOR = "API_CODE_MODIFICADOR";
	
	//CAMPOS
	public static final String FILTRO = "FILTRO";
	public static final String UNIQUE = "UNIQUE";
	public static final String DEFAULT = "DEFAULT";
	public static final String DEPENDE = "DEPENDE";
	public static final String MODIFICAR_CAMPO = "MODIFICAR_CAMPO";
		
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

	public static final String NUMERO_REDONDEO = "NUMERO_REDONDEO";
	public static final String NUMERO_MONEDA = "NUMERO_MONEDA";
	public static final String NUMERO_FORMULA = "NUMERO_FORMULA";
	public static final String NUMERO_FUNCION_SQL = "NUMERO_FUNCION_SQL";
	public static final String NUMERO_STEP = "NUMERO_STEP";
	public static final String TOTAL_FUNCION = "TOTAL_FUNCION";

	//public static final String CUENTA_CATALOGO_FILTRO = "CUENTA_CATALOGO_FILTRO";
	public static final String CUENTA_MOVIMIENTO = "CUENTA_MOVIMIENTO";
	public static final String CUENTA_ABRIR_CAJA = "CUENTA_ABRIR_CAJA";
	public static final String CUENTA_CERRAR_CAJA = "CUENTA_CERRAR_CAJA";

	public static final String CONFIGURACION_ENTIDAD = "CONFIGURACION_ENTIDAD";
	public static final String CONFIGURACION_PLANTILLA_TIPO = "CONFIGURACION_PLANTILLA_TIPO";
	
	public static final String OPCIONES = "OPCIONES";

	public static final String BINARIO_VERDADERO = "BINARIO_VERDADERO";
	public static final String BINARIO_FALSO = "BINARIO_FALSO";

	public static final String DISPONIBILIDAD_CROQUIS = "DISPONIBILIDAD_CROQUIS";
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
	public static final String DETALLE_TARIFA_PRODUCTO = "DETALLE_TARIFA_PRODUCTO";
	public static final String DETALLE_TARIFARIO_SQL = "DETALLE_TARIFARIO_SQL";
	public static final String PRODUCTOS_FUNCION_SQL = "PRODUCTOS_FUNCION_SQL";
	public static final String PRODUCTOS_FUNCION_CAMPO = "PRODUCTOS_FUNCION_CAMPO";
	public static final String PRODUCTOS_TERCERO = "PRODUCTOS_TERCERO";

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

	//////////////////PLANTILLA////////////////////////////
	//Si se coloca una nueva propiedad se coloca en el campo instrucciones
	public static final String TERCERO = "TERCERO";
	public static final String ENCABEZADO = "ENCABEZADO";
	public static final String DESCRIPCION = "DESCRIPCION";
	public static final String CAMPO_EVIDENCIA = "CAMPO_EVIDENCIA";
	public static final String DESCRIPCION_NIVEL2 = "DESCRIPCION_NIVEL2";
	public static final String TOTAL = "TOTAL";
	public static final String CONSECUTIVO = "CONSECUTIVO";
	public static final String FECHA = "FECHA";
	public static final String RESPONSABLE = "RESPONSABLE";
	public static final String ORDEN = "ORDEN";
	public static final String ORDEN_DESCENDENTE = "ORDEN_DESCENDENTE";
	public static final String SOLICITAR_FECHAS = "SOLICITAR_FECHAS";
	public static final String COPY_TEXT = "COPY_TEXT";
	public static final String AYUDA = "AYUDA";
	public static final String PLANTILLA_ANULAR = "PLANTILLA_ANULAR";
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
	public static final String PERMISO_PLANTILLA_CAMBIAR_ESTADO = "PERMISO_PLANTILLA_CAMBIAR_ESTADO";
	public static final String PERMISO_PLANTILLA_VER = "PERMISO_PLANTILLA_VER";
	public static final String PERMISO_PLANTILLA_VER_TODOS = "PERMISO_PLANTILLA_VER_TODOS";
	public static final String PERMISO_PLANTILLA_LISTAR_MENU = "PERMISO_PLANTILLA_LISTAR_MENU";
	public static final String PERMISO_PLANTILLA_LISTAR_MENU_PROCESO = "PERMISO_PLANTILLA_LISTAR_MENU_PROCESO";
	
	public static final String PERMISO_CAMPO_BLOQUEAR = "PERMISO_CAMPO_BLOQUEAR";
	public static final String PERMISO_CAMPO_MODIFICABLE = "PERMISO_CAMPO_MODIFICABLE";
	public static final String PERMISO_CAMPO_RENDER = "PERMISO_CAMPO_RENDER";
	public static final String PERMISO_CAMPO_OPCIONAL = "PERMISO_CAMPO_OPCIONAL";
	
	public static final String PLANTILLA_TIPO_ROL = "PLANTILLA_TIPO_ROL";
	public static final String PLANTILLA_TIPO_REPORTE = "PLANTILLA_TIPO_REPORTE";
	public static final String PLANTILLA_TIPO_CUENTA = "PLANTILLA_TIPO_CUENTA";
	public static final String PLANTILLA_TIPO_PRODUCTO = "PLANTILLA_TIPO_PRODUCTO";
	public static final String PLANTILLA_TIPO_BODEGA = "PLANTILLA_TIPO_BODEGA";
	public static final String PLANTILLA_RENDER_ESPECIAL_SQL = "PLANTILLA_RENDER_ESPECIAL_SQL";
	
	public static final String GPS = "GPS";
	public static final String RELACIONAR_DOCUMENTOS = "RELACIONAR_DOCUMENTOS";
	public static final String RETIRAR_DOCUMENTOS = "RETIRAR_DOCUMENTOS";
	public static final String PLANTILLA_OCULTAR_GUARDAR = "PLANTILLA_OCULTAR_GUARDAR";
	public static final String PERIODO_LIMPIEZA_HISTORICO = "PERIODO_LIMPIEZA_HISTORICO";
	
	
	//REPORTE
	public static final String REPORTE_ENCABEZADO = "REPORTE_ENCABEZADO";
	public static final String REPORTE_ENCABEZADO_EXCEL = "REPORTE_ENCABEZADO_EXCEL";
	public static final String REPORTE_PIE_PAGINA = "REPORTE_PIE_PAGINA";
	public static final String REPORTE_EXCEL = "REPORTE_EXCEL";
	public static final String REPORTE_JRXML = "REPORTE_JRXML";
	public static final String P_SUBREPORT_ = "P_SUBREPORT_";
	
	//ESTADO PROCESO
	public static final String ROL = "ROL";
	public static final String FUNCION_SQL_ESTADO_ASIGNAR = "FUNCION_SQL_ESTADO_ASIGNAR";
	public static final String ESTADO_ASIGNAR = "ESTADO_ASIGNAR";
	public static final String MODIFICABLE = "MODIFICABLE";
	public static final String COLOR = "COLOR";
	
	public static final String GENERA_DOCUMENTO_CAMPO = "GENERA_DOCUMENTO_CAMPO";
	public static final String GENERA_DOCUMENTO_FUNCION_SQL = "GENERA_DOCUMENTO_FUNCION_SQL";
	public static final String DECISION_SQL = "DECISION_SQL";
	public static final String ITERACION_SQL = "ITERACION_SQL";
	
	
	//TRANSICION
	public static final String TEMPORIZADOR = "TEMPORIZADOR";
	
	public static final String OCULTAR_MENSAJE_LICENCIA = "OCULTAR_MENSAJE_LICENCIA";
	public static final String FORCE_NOTIFICATION = "FORCE_NOTIFICACTION";
	public static final String TABLERO_CONTROL_SQL = "TABLERO_CONTROL_SQL";
	

	public static PropiedadDTO crearParametro(String tipo, String campo, String key, String valor, String token) {
		PropiedadDTO parametroTipo =  new PropiedadDTO();
		parametroTipo.setTipo(tipo);
		parametroTipo.setCampo(campo);
		parametroTipo.setKey(key);
		parametroTipo.setValor(valor);
		return parametroTipo;
	}
	
	public static boolean validarBloqueo(PropiedadDTO propiedad) {
		if(propiedad==null) return false;
		if(propiedad.getBloqueo()==null) return true;
		String[] grupos = null;
		//[(HH:MM-HH:MM)(L,M,X,J,V,S,D)(1,2,3,4,5,6,7,)][(HH:MM-HH:MM)(L,M,X,J,V,S,D)(1,2,3,4,5,6,7,)][(HH:MM-HH:MM)(L,M,X,J,V,S,D)(1,2,3,4,5,6,7,)]
		if(propiedad.getBloqueo().startsWith("[")) {
			grupos = propiedad.getBloqueo().split("\\[");
		}else {
			grupos = new String[1];
			grupos[0] = propiedad.getBloqueo();
		}
		if(grupos.length==0) return false;
		Calendar ahora = new GregorianCalendar();
		for (String iBloqueo : grupos) {
			iBloqueo = iBloqueo.replace("]", "");
			if(!iBloqueo.isEmpty()) {
				try {
					if( isDayBloqueo(iBloqueo) ) {
						int horaInicial = Integer.parseInt(iBloqueo.substring(1, 3));
						if(ahora.get(Calendar.HOUR_OF_DAY)>=horaInicial) {
							int minutoInicial = Integer.parseInt(iBloqueo.substring(4, 6));
							if(ahora.get(Calendar.MINUTE)>=minutoInicial) {
								int horaFinal = Integer.parseInt(iBloqueo.substring(7, 9));
								if(ahora.get(Calendar.HOUR_OF_DAY)<=horaFinal) {
									int minutoFinal = Integer.parseInt(iBloqueo.substring(10, 12));
									if(ahora.get(Calendar.MINUTE)<minutoFinal) {
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
	
	private static boolean isDayBloqueo(String iBloqueo) {
		if(iBloqueo.length()>13) {
			Calendar ahora = new GregorianCalendar();
			//valido que no sea el numero del día
			String bloqNDays = iBloqueo.substring(14, iBloqueo.length()-1);
			String[] gNDays = bloqNDays.split(",");
			for (String iNDay : gNDays) {
				if(!iNDay.isEmpty()) {
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
		if(pCampo==null || pCampo.getPropiedades()==null ||  pCampo.getPropiedades().isEmpty()) return "";
		for (PropiedadDTO param : pCampo.getPropiedades()){
			if(param.getKey().compareTo(key)==0 && validarBloqueo(param)) return param.getValor(); 
		}
		return "";
	}

	public static PropiedadDTO obtenerParametro(BasicParamDTO pCampo, String key) {
		if(pCampo==null || pCampo.getPropiedades()==null || pCampo.getPropiedades().isEmpty()) return null;
		for (PropiedadDTO param : pCampo.getPropiedades()){
			if(param.getKey().compareTo(key)==0 && validarBloqueo(param)) return param; 
		}
		return null;
	}
	
	public static PropiedadDTO obtenerParametro(DocumentoPlantillaCaracteristicaDTO pCampo, String key) {
		if(pCampo==null || pCampo.getPropiedades()==null || pCampo.getPropiedades().isEmpty()) return null;
		for (PropiedadDTO param : pCampo.getPropiedades()){
			if(param.getKey().compareTo(key)==0 && validarBloqueo(param)) return param; 
		}
		return null;
	}

	public static List<PropiedadDTO> obtenerVariosParametro(BasicParamDTO pCampo, String key) {
		if(pCampo==null || pCampo.getPropiedades()==null || pCampo.getPropiedades().isEmpty()) return null;
		List<PropiedadDTO> parametros = new ArrayList<PropiedadDTO>();
		for (PropiedadDTO param : pCampo.getPropiedades()){
			if(param.getKey().compareTo(key)==0 && validarBloqueo(param)) parametros.add(param); 
		}
		if(parametros.isEmpty()) return null;
		return parametros;
	}
	
	public static List<PropiedadDTO> retirarPropiedad(BasicParamDTO pCampo, String key) {
		if(pCampo==null || pCampo.getPropiedades()==null ) return new ArrayList<PropiedadDTO>();
		List<PropiedadDTO> retirables =new ArrayList<PropiedadDTO>();
		for (PropiedadDTO param : pCampo.getPropiedades()){
			if(param.getKey().compareTo(key)==0)  retirables.add(param);
		}
		if(!retirables.isEmpty()) {
			for (PropiedadDTO paramR : retirables){
				pCampo.getPropiedades().remove(paramR);		
			}
		}
		return pCampo.getPropiedades();
	}
	
	public static String instrucciones(String formato){
		if(formato == null ) return "Sin instrucciones por formato no enviado";
		String ruleProperty = null;
		switch(formato) {
			case ALERTAR_CAMPO_PROCESO : {ruleProperty = "Muestra un mensaje en la creacion del documento en el momento que se seleccion un objeto de un campo proceso.\n"
					+ "En la propiedad se debe relacionar el campo que se va a mostrar del objeto seleccionado"
					+ "Si el documento no viene con ese campo no se muestra ningun mensaje";break;}
			case API_TRANSACCION: 
			case API : {ruleProperty = "Identifica el APi que se va a ejecutar al guardar el documento o realizar la transicion (en el caso de la transicion simpre va el documento gque genero la accion ).\n";break;}
			case API_HEADER : {ruleProperty = "Variables del Header de la peticion del API VA.\n";break;}
			case API_CODE_DIRECT : {ruleProperty = "Se encarga de reemplazar un valor en el template.\n\nEn el template debes tener la estructura => {{D_XXXXXXX}} , donde XXXXXX es el codigo del campo";break;}
			case API_CODE_REFERENCE : {ruleProperty = "Se encarga de reemplazar un valor en el template, buscando en OTRA PLANTILLA.\n\nEn el template debes tener la estructura => {{R_XXXXXXX}} , donde XXXXXX es el codigo del campo.\n\nLo mas importante en los links relacionar la cadena de pasos en los campos que se debe seguir hasta llegar al campo deseado.\n EJ el nombre de un vendedor en una guia, entregada => (Propiedad se coloca el campo Guia), en los links se coloca el campoo guia vendedor y se agrega vendedor nombre";break;}
			case API_CODE_ESPECIAL : {ruleProperty = "Se encarga de reemplazar un valor en el template.\n\nColoca en el campo TEXTO de la propiedad el codigo que va a reeemplazar y en el VALOR coloca el texto que quieres que se modifique.\n\nSE crea un TRUCO para la fecha actual: el codigo debe empezar por E_FECHA_XXXXXX y en el VALOR de la propiedad colocas el formato tipo fecha";break;}
			case API_CODE_MODIFICADOR : {ruleProperty = "Se encarga de reemplazar un valor en el template, tomando como base el documento que genero la accion (Solo en transiciones).\n\nEn el template debes tener la estructura => {{M_XXXXXXX}} , donde XXXXXX es el codigo del campo";break;}
			case AUTOLOAD : {ruleProperty =  "Define si carga la información desde el ingreso al modulo o por peticion del usuario.\n";break;}
			case AUTOLOAD_SAVE : {ruleProperty =  "El campo si al guardar esta vacio va a consultar la funcion de BD o la fuente de datos y va a tomar la primera respuesta colocandola en este campo .\n";break;}
			case ARCHIVO_TIPO: {ruleProperty =   "Filtra el tipo de archivo, para usar varias extensiones separalas por coma(,).\n";break;}
			case ARCHIVO_TAMANO_MAXIMO: {ruleProperty =  " : Restringue el tamaño maximo de los archivos a subir (en MB).\n";break;}
			case AYUDA : {ruleProperty =  " Url que lleva a la ayuda del formulario.\n";break;}
			case BINARIO_VERDADERO : {ruleProperty =  " Texto a colocar en la opcion verdadero.\n";break;}
			case BINARIO_FALSO : {ruleProperty =  " Texto a colocar en la opción falso.\n";break;}
			case BODEGA_FIJA : {ruleProperty =  " Coloca el nombre o codigo de la bodega fija de ese inventario, No se guarda en BD ese registro.\n" ;break;}
			case BODEGA_MOVIMIENTO : {ruleProperty =  " (E,S,T,ED,SD,EC,SC).\nEntrada(E) Salida(S) Transformacion(T) D=Directo(solo toma el producto), C=Composicion(Solo toma la composicion del producto)"
						+ "En caso de ser varios dependientes se crean varios parametros\n\n"
						+ "Tener en cuenta que este campo debe ir primero que los dependientes de los productos";break;}
			case CAMPO_HEREDADO_1 : {ruleProperty =  "Esta propiedad indica que los(el) documentos(s) que se visualizan o crean tienen un campo que referencian el documento actual.\n\n"
					+ "En los links de la propiedad se debe referenciar el campo de la plantilla que contiene la referencia al documento actual\n\n"
					+ "Ejemplo un documento CLIENTE con un campo CONTACTOS, este campo se le coloca la propiedad HEREDADO, para que puedas crear los contactos de ese cliente. El contacto debe tener un campo proceso que va a guardar el dato del cliente";break;}
			case CONFIGURACION_PLANTILLA_TIPO : {ruleProperty =  " (Comunicale al desarrollador que esta función se necesita) Si es tipo plantilla sirve para filtrar los tipo.\n";break;}
			case CONFIGURACION_ENTIDAD : {ruleProperty =  " Carga diferentes parametros de configuracion.\n" 
						+ "CATEGORIA_PRODUCTOS\n" 
						+ "PROCESO\n"
						+ "PRODUCTOS\n"
						+ "PLANTILLAS\n"
						+ "ROLES\n"
						+ "ENCUESTAS\n"
						+ "TARIFARIO\n"
						+ "BODEGAS\n"
						+ "FORMATO_EXPORTAR\n";break;}
			case CONSECUTIVO : {ruleProperty =  " Codigo del campo que va a colocar consecutivo del documento, se usa con un consecutivo manual.\n";break;}
			case COPY_TEXT : {ruleProperty =  " Al momento de guardar el documento se va a copiar este texto en el portapapeles, [CODE]=(codigo del documento).\n";break;}
			case CUENTA_MOVIMIENTO : {ruleProperty =  " Si desea generar un movimiento de caja coloca I (Ingresos) G (Gastos).\n";break;}
			case CUENTA_ABRIR_CAJA : {ruleProperty =  " Si desea iniciar turn en una caja.\n";break;}
			case CUENTA_CERRAR_CAJA : {ruleProperty =  " Si desea cerrar el turno en una caja.\n";break;}
			case DECISION_SQL : {ruleProperty =  "Genera una funcion que devuelve una cadena de texto, para que las transacciones siguientes lo tomen por el nombre.\n"
					+ "CREATE OR REPLACE FUNCTION decision_${llaveTabla}(documento character varying, modificador character varying) RETURNS character varying AS";break;}
			case DEFAULT : {ruleProperty =  "Coloca un valor en el campo (Tipo numero y texto), para los tipo proceso solo funciona para autoload y busca el valor con el id seleccionado.\n";break;}
			case DESCRIPCION : {ruleProperty =  " Coloca una descripcion al documento segun los campos del mismo.\n";break;}
			case CAMPO_EVIDENCIA : {ruleProperty =  "Selecciona un campo de la plantilla para mostrarlo en la trazabilidad como link para ver las evidencias del documento.\n";break;}
			case DESCRIPCION_NIVEL2 : {ruleProperty =  " Toma la descripcion de un campo proceso para el.\n";break;}			
			case DETALLE_NUMERO_COLUMNAS : {ruleProperty =  " EN la interfaz web coloca el numero de columnas deseado, por defecto 1.\n";break;}
			case DETALLE_TECLADO : {ruleProperty =  " Carga un teclado debajo del espacio del campo texto que filtra.\n";break;}
			case DETALLE_TARIFARIO : {ruleProperty =  " Escoge el tarifario que va a colocar los valores a los productos.\n";break;}
			case DETALLE_OCULTAR_IMAGENES : {ruleProperty =  " Oculta las imagenes de los productos.\n";break;}
			case DETALLE_TARIFARIO_SQL : {ruleProperty =  "Se crea una funcion para traer las tarifas, los campos depende se envian como parametros. Tener cuidado en el orden de los parametros es en orden ALFABETICO del codigo.\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(producto character varying, producto_base character varying, parametros character varying[])  RETURNS SETOF tarifa_tarp AS";break;}
			case DETALLE_FORMULA : {ruleProperty =  " Este parametro indica el valor total de la suma de productos escogidos si se requiere que sea diferente a la sumatoria de los productos. Se utiliza una formula para sumar o restar.\n";break;}
			case DISPONIBILIDAD_CROQUIS : {ruleProperty =  "Relaciona el campo del formulario que tiene la estructura";break;}
			case ENCABEZADO : {ruleProperty =  " Colocar los parametros de encabezado (SEDE) de reportes dinamicos.\n";break;}
			case FECHA : {ruleProperty =  "  Codigo del campo que va a colocar la fecha del documento.\n";break;}
			case FECHA_CON_HORA : {ruleProperty =  " La fecha muestra horas.\n";break;}
			case FECHA_SIN_CALENDAR : {ruleProperty =  " La fecha no muestra las fechas para escoger dia.\n";break;}
			case FECHA_MAXIMA : {ruleProperty =  " Coloca un numero del tiempo maximo (actual + num), el tiempo es en milisegundos.\n";break;}
			case FECHA_MINIMA : {ruleProperty =  " Coloca un numero del tiempo minimo (actual - num), el tiempo es en milisegundos.\n";break;}
			case FECHA_RANGO : {ruleProperty =  " La fecha tiene un rango, especialmente para reportes.  (*) =  Todos los rangos. D = Dia, M = Meses, R = Rango. Puedes combinar separado por (;break;}).\n";break;}
			case FECHA_RANGO_MAXIMO : {ruleProperty =  " Cuando es rango, este es un limite de tiempo entre la fecha de incio y la fecha de fin el tiempo es en milisegundos.\n";break;}
			case FECHA_TIMER_BACK : {ruleProperty =  "Activando esta propiedad se va a mostrar un reloj en cuenta regresiva segun al fecha seleccionada.\n";break;}
			case FORMATO : {ruleProperty =  " Para campos texto N(Solo numero), E(Correo electronico).\n\n Para campos numero se utiliza un DecimalFormat";break;}
			case FUNCION_SQL_VALIDAR : {ruleProperty =  " Al momento de ejecutar la transicion se va a ejecutar esta funcion de BD con resultados S y N.\n\n"
					+ "Cuando solo es un formulario al guardar y desea validar el campo documento tiene la llave del documento y modificador es null\n\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, modificador character varying) RETURNS character varying AS";break;}
			case GENERA_DOCUMENTO_CAMPO : {ruleProperty =  "La transicion debe tener plantilla.\nDe esta plantilla referenciamos el campo a llenar y en los links colocamos el campo del documento maestro que va a copiar el campo\n'nLas propiedades sin link se llena con el documento actual (y si este campo en el documento maestro es multiple se generan muchos documentos de la transicion)\n";break;}
			case GENERA_DOCUMENTO_FUNCION_SQL : {ruleProperty =  "Crea un campo para agregar a un documento.\nDebes crear una funcion para obtener los datos del campo.\nEn las relaciones debes colocar una relacion al campo que deseas llenar\n"
					+ "\n\nAyuda para generar el campo\n"
					+ "\nbegin return next(SELECT ROW(null, null, null, null, null, null, null, null, null, null, null)::pedidoventacaracteristica_pvcp); end\n"
					+ "\n\nEstructura de la funcion\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, modificador character varying) RETURNS SETOF pedidoventacaracteristica_pvcp AS";break;}
			case FORCE_NOTIFICATION : {ruleProperty =  "Indica que al abrir el sistema en caso que tenga notificaciones sin leer se van a mostrar al usuario inmediatamente";break;}
			case FUNCION_SQL_ESTADO_ASIGNAR : {ruleProperty =  "Cuando un documento llegue a este estado sera asignado al usuario que devuelva la funcion.\nEl documento que envia es el id del expediente, para el modificador necesitamos unsc para mejorar la funcion\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, modificador character varying) RETURNS character varying AS";break;}
			case ITERACION_SQL : {ruleProperty =  "Genera una funcion que devuelve varios documentos, para que se ejecute una transaccion sobre ellos.\n"
					+ "CREATE OR REPLACE FUNCTION iteracion_${llaveTabla}(documento character varying, modificador character varying) RETURNS SETOF pedidoventa_pdvp AS";break;}
			case MENSAJE : {ruleProperty =  "Coloca el nombre de un template de mensaje de correo, este template va a ser enviado por correo.\n";break;}
			case MENSAJE_DESTINATARIO : {ruleProperty =  "Coloca el usuario de la plataforma que va a recibir una copia del reporte.\n\n Adicionalmente en las relaciones de este campo puedes colocar una cadena de relaciones para identificar el campo de text que quieres que reciba el correo ej: Entrega de Guia (Tiene un campo guia -Relacion 1-(Dentro de la guia hay un campo cliente -Relacion 2-(Dentro del cliente hay un campo correo -Relacion 3-)))";break;}
			case MENSAJE_DESTINATARIOS_SQL : {ruleProperty =  "Funcion de BD que trae usuarios o solo correos a los que se les debe enviar el mensaje"
					+ "\n(documento): la funcion tiene una variable que el es id del documento."
					+ "\n para la funcion puedes usar : return query (select null::character varying, 'correo@cambiame.com') "
					+ "\n\nEstructura de la funcion\n"
					+ " CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying) RETURNS TABLE (usuario character varying, correo character varying)";break;}
			case MULTIPLE : {ruleProperty =  " En cada tipo de campo es diferente pero su objetivo es permitir escoger varios items o agregar varios items\n";break;}
			case MULTIPLE_FILE : {ruleProperty =  "Permite que el campo cargue varias imagenes o archivos de adjuntos\n";break;}			
			case NUMERO_MONEDA : {ruleProperty =  " Identifica el campo como tipo moneda\n";break;}
			case NUMERO_FORMULA : {ruleProperty =  " Formula para calcular el valor del campo.\n";break;}
			case NUMERO_FUNCION_SQL : {ruleProperty =  " Funcion que calcula un numero \n Se envia el id del documento actual y los depende el valoropcion\nCuando el campo es numero, bloqueado y tiene esta propiedad se calcula al guardar, pero debe estar de ultima en el orden del formulario.\n\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, parametros character varying[])  RETURNS SETOF numeric AS";break;}
			case NUMERO_STEP : {ruleProperty =  " El numero avanza segun el valor de este parametro con las flechas.\n";break;}
			case NUMERO_REDONDEO : {ruleProperty =  " Numero de digitos decimales que tendra el campo(Por defect 0 numero, 2 si es moneda).Maximo 6\n";break;}
			case OCULTAR_MENSAJE_LICENCIA : {ruleProperty =  "El rol que tenga esta propieadd no vera nunca los mensajes de vencimiento de sistema\n";break;}
			case ORDEN : {ruleProperty =  " Determina si se ordena por nombre(N) o por fecha(F), Default F.\n";break;}
			case ORDEN_DESCENDENTE : {ruleProperty =  " Determina si se ordena por nombre(N) o por fecha(F), Default F.\n";break;}
			case PERMISO_PLANTILLA_LISTAR_MENU :
			case PERMISO_PLANTILLA_LISTAR_MENU_PROCESO : {ruleProperty =  "Muestra en el menu principal este proceso o plantilla para el usuario y que se pase al listado de esos documentos.\n";break;}
			case PLANTILLA_AUXILIAR : {ruleProperty =  " Tiene el id de la plantilla que se usa en este proceso. (Puede colocar nombre o codigo, el lo convierte)\n";break;}
			case PLANTILLA_OCULTAR_GUARDAR : {ruleProperty =  "En la pantalla el formulario no muestra el boton guardar, asi los usuarios se obligan a ejecutar las siguientes transiciones\n";break;}
			case PERIODO_LIMPIEZA_HISTORICO : {ruleProperty =  "Cantidad de dias que van a tenerse en cuenta para pasar los registros a las tablas de historicos (los documentos que pasen la fecha de creacion mayor se van a migrar)"
					+ "\n\n Si la plantilla hace parte del inicio de un proceso solo se va a migrar a la tabla historico los documentos inactivos o finalizados"
					+ "\n\n Si la plantilla NO hace parte del inicio de un proceso se van a pasar todos los documentos esto es muy util para los reportes"
					+ "\n\n VALOR: Coloca el numero de dias que se van mantener en la tabla principal el registro de ese tipo de plantilla"
					+ "TEXTO: Coloca la frecuencia con la que se va a repetir esta transicion (YY:MM:DD:HH:MM).\n Ej 1 Cada 3 dias = 00:00:03:00:00\n Ej 2 Cada 1 hora = 00:00:00:01:00\n Ej 2 Cada mes y medio = 00:01:15:00:00\n\nColoca la fecha inicial para que ese sea el punto de partida del temporizador\n\ncrae una relacion con la MISMA plantilla de la transicion y el campo para saber en que campo coloca los documentos";break;}
			case PLANTILLA_TIPO_ROL : {ruleProperty =  " Asocia esta plantilla con un rol del sistema, creandolo\n";break;}
			case PLANTILLA_TIPO_REPORTE : {ruleProperty =  " Crear la configuracion de un reporte\n";break;}
			case PLANTILLA_TIPO_CUENTA : {ruleProperty =  " Define que esta plantilla relaciona los documentos creados con una cuenta\n";break;}
			case PLANTILLA_TIPO_PRODUCTO : {ruleProperty =  " Define que esta plantilla va a crear un producto\nSe debe llenar con una categoria de producto";break;}
			case PLANTILLA_TIPO_BODEGA : {ruleProperty =  "Los documentos de esta plantilla van a crear bodegas\n";break;}
			case PLANTILLA_RENDER_ESPECIAL_SQL : {ruleProperty =  "Cuando una plantilla debe tener un campo que no pertenece a ella se utiliza una funcion para traer los campos deseados\n"
					+ "\n\nSe recibe el id del documento y a partir de el se debe generar la consulta"
					+ "\n\nCREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying) RETURNS SETOF pedidoventacaracteristica_pvcp AS";break;}
			case PROCESO_POP : {ruleProperty =  " Para las listas autoload muestra el pop up y deja para escoger en el formulario.\n";break;}
			case PROCESO_ACCIONES : {ruleProperty =  " Permite que se puedan realizar acciones de crear y modificar.\n Se debe definir la plantilla que tiene permisos para crear";break;}
			case PROCESO_GESTIONAR_ESTADOS : {ruleProperty =  " Es el camino que debi seguir la transaccion para modificar estados de documentos. * -> se usa para indicar todos los documentos. Se coloca los codigos de las planatillas separads por punto y coma(;break;}). se pueden colocar varios caminos\n";break;}
			case PROCESO_DIVISION : {ruleProperty =  " Propiedad ESPECIAL que divide el documento.\n";break;}
			case PROCESO_VALOR : {ruleProperty =  " Toma el valor del campo (0 - Valor de la cuenta, 1 - total , 2 - saldo) de los documentos.\n";break;}
			case PROCESO_FUNCION_SQL : {ruleProperty =  "Coloca el SQL de los datos que quieres obtener.\n\nSi tienes un dependiente ese va ubicado en el campo documento\n\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[])\r\n" 
					+ "RETURNS SETOF pedidoventa_pdvp AS\n"
					+ "select * from pedidoventa_pdvp where cpdv_plantilla = '' and (codigo_exacto is null or cpdv_nombre = codigo_exacto) and (fechaminima is null or dpdv_fecha >=fechaminima) and (fechamaxima is null or dpdv_fecha < fechamaxima) and (filtro is null or (cpdv_nombre like upper('%' ||filtro|| '%') or upper(cpdv_textofiltro) like upper('%' || filtro || '%'))) order by cpdv_nombre desc limit cant offset pagina";break;}
			case PRODUCTO_CAMPO_VALOR_MINIMO : {ruleProperty =  "referencia el campo del producto que va a mostrar el valor minimo.\n";break;}
			case PRODUCTO_CAMPO_VALOR_UNITARIO : {ruleProperty =  "Referencia el campo que va a mostrar el valor unitario.\n";break;}
			case PRODUCTO_CAMPO_CANTIDAD : {ruleProperty =  "EL softwate toma este campo como la cantidad a registrar del item de venta.\n";break;}
			case PRODUCTO_CAMPO_TOTAL : {ruleProperty =  "Este campo sera el valor total del producto.\n";break;}
			case PROCESO_INCLUIR_TRAZA_PRINCIPAL : {ruleProperty =  "Cuando colocas esta propiedad en el proceso que se selecciona en el campo aparecera este documento como parte de la trazabilidad.\n No es necesario usar BPM.\n";break;}
			case P_SUBREPORT_ : {ruleProperty =  " Coloque los diferentes subreportes que se necesitan.\n";break;}
			case READ_QR : {ruleProperty =  "En los campos proceso muestra el boton para activar la camara de lectura de codigos QR";break;}
			case RELACIONAR_DOCUMENTOS : {ruleProperty =  "Permite AGREGAR documentos a un campo proceso multiple de otro documento.\n\nRelaciona el campo actual de la plantilla y en los links relaciona el campo de la plantilla destino";break;}
			case RETIRAR_DOCUMENTOS : {ruleProperty =  "Permite QUITAR documentos a un campo proceso multiple de otro documento.\n\nRelaciona el campo actual de la plantilla y en los links relaciona el campo de la plantilla destino";break;}
			case RESPONSABLE : {ruleProperty =  " Codigo del campo que relaciona el responsable de la actividad.\n";break;}
			case REPORTE_ENCABEZADO : {ruleProperty =  " Toma como base este reporte para dibujar el encabezado de cada pagina.\nEl reporte debe tener la linea <parameter name=\\\"P_KEY\\\" class=\\\"java.lang.String\\\"/> para reemplazar y va a agregar otro parametro NOMBRE para que no se duplique";break;}
			case REPORTE_EXCEL : {ruleProperty =  "Nombre del reporte que se va a ejecutar cuando sea en excel, puede estar inactivo.\n";break;}
			case SOLICITAR_FECHAS : {ruleProperty =  " Obliga al usuario colocar fechas al momento de realizar la consulta.\n";break;}
			case TABLERO_CONTROL_SQL : {ruleProperty =  "Muestra un item en el menu que traera una serie de objetos que se definen en un query.\n"
				+ "En el campo TEXTO va el nombre del tablero\n"
				+ "En el campo MOTIVO va la url de la imagen del tablero\n"
				+ "\nEsta consulta no tiene dependientes ya que viene solo del menu, el unico dato considerable es el TOKEN y los filtros de fecha y cantidad\n\n"
				+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[])\r\n" 
				+ "RETURNS SETOF pedidoventa_pdvp AS\n\n"
				+ "select * from pedidoventa_pdvp where cpdv_plantilla = '' and (codigo_exacto is null or cpdv_nombre = codigo_exacto) and (fechaminima is null or dpdv_fecha >=fechaminima) and (fechamaxima is null or dpdv_fecha < fechamaxima) and (filtro is null or (cpdv_nombre like upper('%' ||filtro|| '%') or upper(cpdv_textofiltro) like upper('%' || filtro || '%'))) order by cpdv_nombre desc limit cant offset pagina";break;}
			case TEMPORIZADOR : {ruleProperty =  " VALOR: funcion sql que consulta datos\n"
					+ "CREATE OR REPLACE FUNCTION propiedad_${llaveTabla}(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying)\n" 
					+ "RETURNS SETOF pedidoventa_pdvp AS"
					+ "TEXTO: Coloca la frecuencia con la que se va a repetir esta transicion (YY:MM:DD:HH:MM).\n Ej 1 Cada 3 dias = 00:00:03:00:00\n Ej 2 Cada 1 hora = 00:00:00:01:00\n Ej 2 Cada mes y medio = 00:01:15:00:00\n\nColoca la fecha inicial para que ese sea el punto de partida del temporizador\n\ncrae una relacion con la MISMA plantilla de la transicion y el campo para saber en que campo coloca los documentos";break;}
			case TEXTO_FORMULA : {ruleProperty =  " calcula un valor texto segun los dependientes.";break;}
			case TOTAL : {ruleProperty =  " Codigo del campo que va a colocar el valor TOTAL del documento.\n";break;}
			case TOTAL_FUNCION : {ruleProperty =  " Se usa en los campos de un producto para calcular el total segun una funcion.\nHoy no se exactamente como se usa o donde se usa";break;}
			case UBICACION : {ruleProperty =  "Esta propiedad referencia el campo del documento que al que se la a asignar que tiene ese documento, Ej una VENTA en una trnsicion de enviar con un formulario con campo VEHICULO en esta propiedad, despues de esta transición en la trazabilidad va a decir que quedo en el vehiculo seleccionado"
					+ "\n\n No se puede asignar a una transicion que termine en una decision";break;}
			case UNICO_PRODUCTO : {ruleProperty =  " Oculta la lista para escoger un solo producto.\n";break;}
			case VALIDATE_ORIENTATION : {ruleProperty =  "Valida que sea una imagen y su orientacion\n 1 = Valida ancho sea mayor que alto (Horizontal)\n 2 = Valida alto sea mayor que ancho (Vertical)";break;}
			default : {ruleProperty =  "Por favor solicita un cambio para agregar informacion de este key";break;}
		}
		return ruleProperty;
	}
	
}
