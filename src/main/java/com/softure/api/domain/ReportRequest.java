package com.softure.api.domain;

import java.util.List;

public class ReportRequest {

	private String template;
	private String code;
	private String documentId;
	private List<ReportParameterRequest> parameters;

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public List<ReportParameterRequest> getParameters() {
		return parameters;
	}

	public void setParameters(List<ReportParameterRequest> parameters) {
		this.parameters = parameters;
	}

}
