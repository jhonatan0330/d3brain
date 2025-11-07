package com.softure.java.services;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.shared.domain.SharedConstants;

import jakarta.servlet.http.HttpServletRequest;

import com.shared.domain.ServerException;

public class SoftureUtil {

	public static boolean validarFechaInicioFin(Date fechaInicio, Date fechaFin) throws ServerException {
		return validarFechaInicioFin(fechaInicio, fechaFin, null, null);
	}

	public static boolean validarFechaInicioFin(Date fechaInicio, Date fechaFin, Date fechaMinima)
			throws ServerException {
		return validarFechaInicioFin(fechaInicio, fechaFin, fechaMinima, null);
	}

	public static boolean validarFechaInicioFin(Date fechaInicio, Date fechaFin, Date fechaMinima, Date fechaMaxima)
			throws ServerException {
		if (fechaInicio.compareTo(fechaFin) > 0)
			throw new ServerException("La fecha de inicio no puede ser menor a la actual");
		if (fechaMinima != null) {
			if (fechaInicio.compareTo(fechaMinima) < 0)
				throw new ServerException("La fecha de inicio no puede ser menor a:" + fechaMinima.toString());
		}
		if (fechaMaxima != null) {
			if (fechaFin.compareTo(fechaMaxima) > 0)
				throw new ServerException("La fecha de fin no puede ser mayor a:" + fechaMaxima.toString());
		}
		return true;
	}

	public static String formatMoney(BigDecimal money) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		if (money == null)
			money = BigDecimal.ZERO;
		return format.format(money);
	}

	public static String formatDate(Date fecha) {
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		if (fecha == null)
			return "";
		return format.format(fecha);
	}

	public static String formatDatePattern(Date fecha, String patternFormat) {
		if(patternFormat.compareTo("LOCAL_API")==0) {
			return formatDatePattern(fecha, "yyyy-MM-dd") + "T" + formatDatePattern(fecha, "HH:mm:ss") +  ".000-0500";
		}
		DateFormat format = new SimpleDateFormat(patternFormat);
		if (fecha == null) return "";
		return format.format(fecha);
	}

	public static String formatDateMonth(Date fecha) {
		DateFormat format = new SimpleDateFormat("MMMM yyyy");
		if (fecha == null)
			return "";
		return format.format(fecha);
	}

	public static String formatDateYear(Date fecha) {
		DateFormat format = new SimpleDateFormat("yyyy");
		if (fecha == null)
			return "";
		return format.format(fecha);
	}

	public static String formatDateTime(Date fecha) {
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
		if (fecha == null)
			return "";
		return format.format(fecha);
	}

	public static String formatWithParameter(Date fecha, String formatString) {
		DateFormat format = new SimpleDateFormat(formatString);
		if (fecha == null)
			return "";
		return format.format(fecha);
	}

	// Debo hacer que las cargas masivas soporten varios formatos de fecha
	public static String formatDateMassiveFile(Date fecha) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		if (fecha == null)
			return "";
		return format.format(fecha);
	}

	public static String formatNumber(BigDecimal money) {
		NumberFormat format = NumberFormat.getNumberInstance();
		if (money == null)
			money = BigDecimal.ZERO;
		return format.format(money);
	}

	public static String formatNumberPattern(BigDecimal value, String formatPattern) {
		NumberFormat format = new DecimalFormat(formatPattern);
		if (value == null)
			value = BigDecimal.ZERO;
		return format.format(value);
	}

	public static final String[] FORMATOS_FECHA;
	static {
		FORMATOS_FECHA = new String[] { "dd/MM/yyyy hh:mm a", "dd/MM/yyyy hh:mm:ss a", "dd/MM/yyyy", "yyyy-MM-dd",
				"dd-MMM-yyyy", "dd.MM.yy", "yyyy.MM.dd G 'at' hh:mm:ss z", "EEE, MMM d, ''yy", "h:mm a", "H:mm",
				"H:mm:ss:SSS", "K:mm a,z", "yyyy.MMMMM.dd GGG hh:mm aaa", "EEE MMM d hh:mm:ss zZ yyyy" };
	}

	public static Timestamp verificarFechaHora(String texto) {
		Date dateParse = toDate(texto);
		if(dateParse ==null) return null;
		return new Timestamp(dateParse.getTime());
	}

	public static Date toDate(String text) {
		for (String formatString : FORMATOS_FECHA) {
			try {
				return new SimpleDateFormat(formatString).parse(text);
			} catch (ParseException e) {
				continue;
			}
		}
		return null;
		
	}

	public static String getURL(String url, String serverPath) throws IOException {
		if (!url.contains("http")) {
			// String serverPath = "D:\\Temp\\LOGISTICPYMES_WEB";
			// serverPath = serverPath + req.getContextPath();
			if (Character.isDigit(url.charAt(0))) {
				int posSeparator = -1;
				int lastSeparator = 0;
				for (int i = 0; i < 3; i++) {
					posSeparator = url.indexOf("_", posSeparator + 1);
					if (posSeparator != -1) {
						serverPath = serverPath + File.separator + url.substring(lastSeparator, posSeparator);
						lastSeparator = posSeparator + 1;
					} else {
						break;
					}
				}
			}
			url = serverPath + File.separator + url;
		}
		return url;
	}

	public static String recortar(String texto, int cantidad) {
		if (texto == null || cantidad < 3)
			return texto;
		if (texto.length() > cantidad)
			texto = texto.substring(0, cantidad - 3) + "...";
		texto = texto.replaceAll("\\r|\\n", " ");
		texto = texto.replaceAll(";;", "(doble punto y coma)");
		return texto;
	}

	public static String formatFunction(String currentFunction) throws ServerException {
		if (currentFunction == null)
			throw new ServerException("La funcion no puede ser nula");
		currentFunction = cleanStartEndSpaces(currentFunction);
		currentFunction = currentFunction.replaceAll("\\s+", "_");
		currentFunction = currentFunction.replaceAll("\\-", "_");
		currentFunction = currentFunction.replaceAll("\\r|\\n", "_");
		currentFunction = currentFunction.toLowerCase();

		currentFunction = Normalizer.normalize(currentFunction, Normalizer.Form.NFD);
		currentFunction = currentFunction.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		
	    currentFunction = currentFunction.replaceAll("[^a-z0-9\\_]", "_");
	    currentFunction = currentFunction.replaceAll("_+", "_");
	    
	    currentFunction = currentFunction.replaceAll("^_+|_+$", "");
	    
		return currentFunction;
	}

	public static String formatSimpleFunction(String currentFunction) throws ServerException {
		if (currentFunction == null)
			throw new ServerException("La funcion no puede ser nula");

		currentFunction = Normalizer.normalize(currentFunction, Normalizer.Form.NFD);
		currentFunction = currentFunction.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		// No se si estas las necesite
//		currentFunction = currentFunction.replaceAll("\\r|\\n", "_");
//		currentFunction = currentFunction.replaceAll("\\-", "_");
		currentFunction = currentFunction.replaceAll("\\s+", "_");
		return currentFunction.toUpperCase();
	}

	public static String cleanStartEndSpaces(String str){
		if (str == null)
			return null;
		return str.replaceAll("^\\s*", "").replaceAll("\\s*$", "");
	}

	/**
	 * De una cadena de texto con la siguiente estructura (codigo =
	 * valor;;codigo=valor)crea un map Tener en cuenta que el separador es @see
	 * {@link SharedConstants#PUNTO_COMA_DOBLE}
	 * 
	 * @param str
	 * @return regreso el mapa vacio si no hay resultados que concuerden
	 * @throws ServerException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> createMaptoString(String str) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (str == null || str.isEmpty())
			return result;
		String[] params = str.split(SharedConstants.PUNTO_COMA_DOBLE);
		int posIgual = -1;
		String codigo = null;
		String textoReemplazar = null;
		for (String iParametro : params) {
			posIgual = iParametro.indexOf("=");
			if (posIgual > 0) {
				codigo = iParametro.substring(0, posIgual);
				textoReemplazar = iParametro.substring(posIgual + 1, iParametro.length());
				if (codigo.contains("[")) {
					String newCode = codigo.substring(0, codigo.indexOf("["));
					if(textoReemplazar.contains(SharedConstants.LINEA_MEDIA_DOBLE)) {
						result.put(newCode, generateItemsFromParameters((ArrayList<Object>) result.get(newCode), textoReemplazar));
					}else {
						ArrayList<String> arrayObjectsString = (ArrayList<String>) result.get(newCode);
						if (arrayObjectsString == null)	arrayObjectsString = new ArrayList<>();
						arrayObjectsString.add(textoReemplazar);
						result.put(newCode, arrayObjectsString);	
					}
				} else {
					result.put(codigo, textoReemplazar);
				}
			}
		}
		return result;
	}
	
	private static ArrayList<Object> generateItemsFromParameters(ArrayList<Object> arrayObjects , String str){
		String[] params = str.split(SharedConstants.LINEA_MEDIA_DOBLE);
		if (arrayObjects == null) arrayObjects = new ArrayList<>();
		Map<String, String> parametersItem =  new HashMap<String, String>();
		int posIgual = -1;
		String codigo = null;
		String textoReemplazar = null;
		for (String iParametro : params) {
			posIgual = iParametro.indexOf(SharedConstants.COMA_DOBLE);
			if (posIgual > 0) {
				codigo = iParametro.substring(0, posIgual);
				textoReemplazar = iParametro.substring(posIgual + 2, iParametro.length());
				parametersItem.put(codigo, textoReemplazar);
				//Esto casi lo copie de process template
				if(codigo.contains("(")) {
		        	String newKey = codigo;
		        	while (newKey.contains("(")) {
						newKey = codigo.replace("(", "_").replace(")", "").replace(":", "_").replace("/", "_").replace("-", "_");
					}
		        	parametersItem.put(newKey, textoReemplazar);
		        	//mapParams.remove(entry.getKey());
		        	//Por el momento no borro las entradas para una proxima
		        }
			}
		}
		if(!parametersItem.isEmpty()) arrayObjects.add(parametersItem);
		return arrayObjects;
	}
	
    public static String encrypt(String data, String secretKey) throws Exception {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
	public static String encryptSHA384(String input) throws ServerException {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-384");

			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);

			String hashtext = no.toString(16);

			while (hashtext.length() < 96) {
				hashtext = "0" + hashtext;
			}

			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public static String generarLlave() {
		UUID uuid = UUID.randomUUID();
		String gen = uuid.toString();
		gen = gen.replaceAll("-", "");
		return gen;
	}
	
	public static boolean isUUID(String value) {
		if (value == null)
			return false;
		if (value.length() != 32)
			return false;
		if (value.contains(" "))
			return false;
		if (value.contains("-"))
			return false;
		return true;
	}

	public static String getRequestUrl(HttpServletRequest request) {
		String baseUrl = request.getScheme() + "://" + request.getServerName();
		if (!(request.getScheme().equals("http") && request.getServerPort() == 80) &&
		    !(request.getScheme().equals("https") && request.getServerPort() == 443)) {
		    baseUrl += ":" + request.getServerPort();
		}
		return baseUrl;
	}
	
	public static Date agregarMinutos(Date pFecha, int pMinutos) {
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(pFecha);
        cal.add(Calendar.MINUTE, pMinutos); 
        return cal.getTime();

    }
	
	public static String maskError(String pMessage) {
		if (pMessage.indexOf("Where:")!=-1) {
			return pMessage.substring( ((pMessage.indexOf("ERROR:")!=-1)?pMessage.indexOf("ERROR"):0 ), pMessage.indexOf("Where:"));
		}
		return pMessage;
	}
}
