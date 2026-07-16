package com.softure.api.domain;

import java.util.List;

public class DataFieldRequest {

	private String template;
	private String code;
	private List<FieldRequest> preconditions;

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

	public List<FieldRequest> getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(List<FieldRequest> preconditions) {
		this.preconditions = preconditions;
	}
}
