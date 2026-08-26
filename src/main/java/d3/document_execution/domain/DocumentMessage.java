package d3.document_execution.domain;

import java.util.Date;

public class DocumentMessage {

	private String message;
	private String type;
	private Date date;
	private String documentCode;
	private String documentId;
	private String documenttemplate;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public String getDocumenttemplate() {
		return documenttemplate;
	}

	public void setDocumenttemplate(String documenttemplate) {
		this.documenttemplate = documenttemplate;
	}

}
