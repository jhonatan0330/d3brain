package d3.java.dto.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FlexException extends Exception {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger.getLogger(FlexException.class.getName());

	private String command;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public FlexException(String message, String command, Throwable cause, Logger logger) {
		super(message, cause);
		logger.log(Level.SEVERE, message, this);
		this.command = command;
	}

	public FlexException(String message, String command, Logger logger) {
		super(message);
		logger.log(Level.WARNING, message);
		this.command = command;
	}

	public FlexException(String message, Logger logger) {
		super(message);
		logger.log(Level.SEVERE, message, this);
	}

	public FlexException(String message) {
		super((message.indexOf("Where:") != -1)
				? message.substring(((message.indexOf("ERROR:") != -1) ? message.indexOf("ERROR") : 0),
						message.indexOf("Where:"))
				: message);
		logger.log(Level.SEVERE, message, this);
	}

	public FlexException(String message, String origen) {
		super((message.indexOf("Where:") != -1)
				? (origen + "\n" + message.substring(((message.indexOf("ERROR:") != -1) ? message.indexOf("ERROR") : 0),
						message.indexOf("Where:")))
				: (origen + "\n" + message));
		logger.log(Level.SEVERE, origen + "\n" + message, this);
	}
}
