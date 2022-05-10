package com.softure.java.services;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;

public class SoftureUtil {

	public static boolean validarFechaInicioFin(Date fechaInicio, Date fechaFin) throws ServerException{
		return validarFechaInicioFin(fechaInicio, fechaFin, null, null);
	}
	
	public static boolean validarFechaInicioFin(Date fechaInicio, Date fechaFin, Date fechaMinima) throws ServerException{
		return validarFechaInicioFin(fechaInicio, fechaFin, fechaMinima, null);
	}
	
	public static boolean validarFechaInicioFin(Date fechaInicio, Date fechaFin,Date fechaMinima,Date fechaMaxima) throws ServerException{
		if(fechaInicio.compareTo(fechaFin)>0)
			throw new ServerException("La fecha de inicio no puede ser menor a la actual");
		if(fechaMinima!=null){
			if(fechaInicio.compareTo(fechaMinima)<0)
				throw new ServerException("La fecha de inicio no puede ser menor a:" +fechaMinima.toString());
		}
		if(fechaMaxima!=null){
			if(fechaFin.compareTo(fechaMaxima)>0)
				throw new ServerException("La fecha de fin no puede ser mayor a:" +fechaMaxima.toString());
		}
		return true;
	}
	
	public static String formatMoney(BigDecimal money){
		NumberFormat format = NumberFormat.getCurrencyInstance();
		if(money ==null)money = BigDecimal.ZERO;
		return format.format(money); 
	}
	
	public static String formatDate(Date fecha){
		DateFormat format = new SimpleDateFormat( "yyyy/MM/dd");
		if(fecha ==null)return "";
		return format.format(fecha);
	}
	
	public static String formatDatePattern(Date fecha, String patternFormat){
		DateFormat format = new SimpleDateFormat(patternFormat);
		if(fecha ==null)return "";
		return format.format(fecha);
	}
	
	public static String formatDateMonth(Date fecha){
		DateFormat format = new SimpleDateFormat( "MMMM yyyy");
		if(fecha ==null)return "";
		return format.format(fecha);
	}

	public static String formatDateYear(Date fecha){
		DateFormat format = new SimpleDateFormat( "yyyy");
		if(fecha ==null)return "";
		return format.format(fecha);
	}
	
	public static String formatDateTime(Date fecha){
		DateFormat format = new SimpleDateFormat( "yyyy/MM/dd hh:mm:ss aa");
		if(fecha ==null)return "";
		return format.format(fecha);
	}
	
	public static String formatWithParameter(Date fecha, String formatString){
		DateFormat format = new SimpleDateFormat(formatString);
		if(fecha == null)return "";
		return format.format(fecha);
	}
	
	// Debo hacer que las cargas masivas soporten varios formatos de fecha
	public static String formatDateMassiveFile(Date fecha){
		DateFormat format = new SimpleDateFormat( "dd/MM/yyyy HH:mm");
		if(fecha ==null)return "";
		return format.format(fecha);
	}
	
	public static String formatNumber(BigDecimal money){
		NumberFormat format = NumberFormat.getNumberInstance();
		if(money ==null)money = BigDecimal.ZERO;
		return format.format(money); 
	}
	
	public static String formatNumberPattern(BigDecimal value, String formatPattern) {
		NumberFormat format = new DecimalFormat(formatPattern);
		if(value ==null)value = BigDecimal.ZERO;
		return format.format(value); 
	}
	
	
	public static Date toDate(String text) throws ServerException{
		Date date1;
		try {
			date1 = new SimpleDateFormat("yyyy-MM-dd").parse(text);
		} catch (ParseException e) {
			throw new ServerException(e.getMessage());
		}
		return date1;
	}
	
	public static String getURL(String url, String serverPath) throws IOException {
		if(!url.contains("http")) {
			//String serverPath =  "D:\\Temp\\LOGISTICPYMES_WEB";
			//serverPath = serverPath + req.getContextPath();
			if(Character.isDigit(url.charAt(0))){
				int posSeparator = -1;
				int lastSeparator = 0;
				for(int i = 0;i <3 ;i++){
					posSeparator =url.indexOf("_" ,posSeparator+1);
					if(posSeparator!=-1){
						serverPath = serverPath + File.separator + url.substring(lastSeparator, posSeparator);
						lastSeparator = posSeparator +1;
					}else{
						break;
					}
				}
			}
			url = serverPath+ File.separator + url;
		}
		return url;
	}
	
	public static String recortar(String texto, int cantidad) {
		if(texto == null || cantidad < 3) return texto;
		if(texto.length()>cantidad)texto = texto.substring(0,cantidad - 3) + "...";
		texto = texto.replaceAll("\\r|\\n", " ");
		texto = texto.replaceAll(";;", "(doble punto y coma)");
		return texto;
	}
	
	public static String formatFunction(String currentFunction) throws ServerException {
		if(currentFunction==null) throw new ServerException("La funcion no puede ser nula");
		currentFunction = currentFunction.replace(" ", "_");
		currentFunction = currentFunction.replace("-", "_");
		currentFunction = currentFunction.replaceAll("\\r|\\n", "__");
		currentFunction = currentFunction.toLowerCase();
		
		currentFunction = Normalizer.normalize(currentFunction, Normalizer.Form.NFD);
		currentFunction = currentFunction.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return currentFunction;
	}
	
	public static String formatSimpleFunction(String currentFunction) throws ServerException {
		if(currentFunction==null) throw new ServerException("La funcion no puede ser nula");
		
		currentFunction = Normalizer.normalize(currentFunction, Normalizer.Form.NFD);
		currentFunction = currentFunction.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return currentFunction;
	}
	
	public static String cleanStartEndSpaces(String str) throws ServerException {
		if(str==null) return null;
		return str.replaceAll("^\\s*","").replaceAll("\\s*$","");
	}
	
	
	/**
	 * De una cadena de texto con la siguiente estructura (codigo = valor;;codigo=valor)crea un map
	 * Tener en cuenta que el separador es @see {@link ConstantesGenerales#PUNTO_COMA_DOBLE}
	 * @param str
	 * @return regreso el mapa vacio si no hay resultados que concuerden
	 * @throws ServerException
	 */
	public static Map<String, String> createMaptoString(String str) {
		Map<String, String> result = new HashMap<String, String>();
		if (str ==null || str.isEmpty()) return result;
		String[] params = str.split(ConstantesGenerales.PUNTO_COMA_DOBLE);
		int posIgual = -1;
		String codigo = null;
		String textoReemplazar = null;
		for (String iParametro : params) {
			posIgual = iParametro.indexOf("=");
			if (posIgual > 0) {
				codigo = iParametro.substring(0, posIgual);
				textoReemplazar = iParametro.substring(posIgual + 1, iParametro.length());
				result.put(codigo, textoReemplazar);
			}
		}
		return result;
	}
	
}
