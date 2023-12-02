package com.shared.domain;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(ServerException.class.getName());

	private static final String OPEN = "[[";
	private static final String CLOSE = "]]";
	
	public ServerException(String message) {
		super((message.indexOf("Where:")!=-1)?message.substring( ((message.indexOf("ERROR:")!=-1)?message.indexOf("ERROR"):0 ), message.indexOf("Where:")):message);
		logger.log(Level.SEVERE, message,this);
	}

	public ServerException(String message, String origen) {
		super(prepareMessage(message, origen));
		logger.log(Level.SEVERE, message,this);
	}
	
	public String getOrigen() {
		if(this.getMessage().startsWith(OPEN)) {
			return this.getMessage().substring(2, this.getMessage().lastIndexOf(CLOSE));
		}else {
			return null;	
		}
	}
	
	public String getTextMessage() {
		if(this.getMessage().startsWith(OPEN)) {
			return this.getMessage().substring(this.getMessage().lastIndexOf(CLOSE) + 2, this.getMessage().length());
		}else {
			return this.getMessage();	
		}
	}
	
	private static String prepareMessage(String message, String origen) {
		String result = (message.indexOf("Where:")!=-1)?message.substring( ((message.indexOf("ERROR:")!=-1)?message.indexOf("ERROR"):0 ), message.indexOf("Where:")):message;
		if(origen!=null) result = OPEN + origen + CLOSE + result;
		return result;
	}
}
