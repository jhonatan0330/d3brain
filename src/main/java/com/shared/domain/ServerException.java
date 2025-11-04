package com.shared.domain;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(ServerException.class.getName());

	private static final String OPEN = "[[";
	private static final String CLOSE = "]]";

	public ServerException(String message) {
		super(prepareMessage(message, null));
		logger.log(Level.SEVERE, message, this);
	}


	public ServerException(String message, String origen) {
		super(prepareMessage(message, origen));
		logger.log(Level.SEVERE, message, this);
	}

	public ServerException(String message, boolean _print) {
		super(prepareMessage(message, null));
		if (_print)
			logger.log(Level.SEVERE, message, this);
	}
	
	public ServerException(String message, Throwable cause) {
		super(prepareMessage(message, null), cause);
		logger.log(Level.SEVERE, message, cause);
	}

	public String getOrigen() {
		String msg = getMessage();
        if (msg != null && msg.startsWith(OPEN)) {
            return msg.substring(2, msg.lastIndexOf(CLOSE));
        }
        return null;
	}

	public String getTextMessage() {
		String msg = getMessage();
        if (msg != null && msg.startsWith(OPEN)) {
            return msg.substring(msg.lastIndexOf(CLOSE) + 2);
        }
        return msg;
	}

	private static String prepareMessage(String message, String origen) {
		if (message == null)
			return null;
		String result = message;
		if (message.contains("Where:")) {
			int start = message.indexOf("ERROR:") != -1 ? message.indexOf("ERROR:") : 0;
			result = message.substring(start, message.indexOf("Where:"));
		}
		if (origen != null)
			result = OPEN + origen + CLOSE + result;
		return result;
	}
}
