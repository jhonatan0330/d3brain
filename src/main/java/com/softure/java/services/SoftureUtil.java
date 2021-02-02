package com.softure.java.services;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	public static String formatNumber(BigDecimal money){
		NumberFormat format = NumberFormat.getNumberInstance();
		if(money ==null)money = BigDecimal.ZERO;
		return format.format(money); 
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
}
