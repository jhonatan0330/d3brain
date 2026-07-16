package com.softure.api.domain;

import java.util.List;

public class DocumentRequest {

	private String template;
	private String id;
	private String code;
	private String active;
	private String stateId;
	private String stateName;
	private List<FieldRequest> fields;

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<FieldRequest> getFields() {
		return fields;
	}

	public void setFields(List<FieldRequest> fields) {
		this.fields = fields;
	}

}
