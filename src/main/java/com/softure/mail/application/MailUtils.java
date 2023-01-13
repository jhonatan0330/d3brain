package com.softure.mail.application;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.softure.logisticpymes.domain.ServidorDTO;

public class MailUtils {
	
	public static final String SEPARADOR = ";;";
	public static final int LONGITUD_MAXIMA_DESCRIPCION = 200;

	public static JavaMailSenderImpl getMailSender(ServidorDTO servidor) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(servidor.getUrl());
		mailSender.setPort((servidor.getPuerto()==null)?587:Integer.parseInt(servidor.getPuerto()));
		mailSender.setUsername(servidor.getUsuario());
		mailSender.setPassword(servidor.getClave());
		Properties prop = mailSender.getJavaMailProperties();
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.debug", "false");
		prop.put("mail.smtp.ssl.trust", servidor.getUrl());
		return mailSender;
	}
	
	public static  String replaceParameterInBodyMessage(String template , String parameters){
		if(parameters==null) return "";
		String[] params  = parameters.split(MailUtils.SEPARADOR);
		int posIgual = -1;
		String codigo = null;
		String textoReemplazar = null;
		for (String iParameter : params) {
			posIgual = iParameter.indexOf("=");
			codigo = iParameter.substring(0,posIgual);
			textoReemplazar = iParameter.substring(posIgual+1, iParameter.length());
			template = template.replace("{"+codigo +"}", textoReemplazar);
		}
		template = template.replaceAll("\\{[A-Za-z0-9_]*\\}", "");
		return template;
	}
}
