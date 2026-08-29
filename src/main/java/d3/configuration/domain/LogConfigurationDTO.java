package d3.configuration.domain;

import java.time.LocalDateTime;

import d3.shared.domain.SharedConstants;

public class LogConfigurationDTO {

	private String logs = "";
	private String root = "";
	private String logsError = "";

	public void setRoot(String _root) {
		this.root = _root;
	}

	public String getRoot() {
		return this.root;
	}

	public void info(String log) {
		this.logs = this.logs + "INFO::::" + "[" + LocalDateTime.now().withNano(0).toString() + " - " + this.root
				+ " ] " + log + SharedConstants.NEW_LINE;
		System.out.println("[" + this.root + " ]" + log);
	}

	public void error(String log) {
		this.logs = this.logs + "ERROR:::" + "[" + LocalDateTime.now().withNano(0).toString() + " - " + this.root
				+ " ] " + log + SharedConstants.NEW_LINE;
		this.logsError = this.logsError + "ERROR:::" + "[" + LocalDateTime.now().withNano(0).toString() + " - "
				+ this.root + " ] " + log + SharedConstants.NEW_LINE;
		System.out.println("[" + this.root + " ]" + log);
	}

	public void warn(String log) {
		this.logs = this.logs + "WARN:::" + "[" + LocalDateTime.now().withNano(0).toString() + " - " + this.root + " ] "
				+ log + SharedConstants.NEW_LINE;
		System.out.println("[" + this.root + " ]" + log);
	}

	public String getLogs() {
		return "-----------------------------------------------" + SharedConstants.NEW_LINE
				+ "------------------ERROR------------------------" + SharedConstants.NEW_LINE
				+ "-----------------------------------------------" + SharedConstants.NEW_LINE + this.logsError
				+ SharedConstants.NEW_LINE + "-----------------------------------------------"
				+ SharedConstants.NEW_LINE + "------------------INFO-------------------------"
				+ SharedConstants.NEW_LINE + "-----------------------------------------------"
				+ SharedConstants.NEW_LINE + this.logs;
	}

}
