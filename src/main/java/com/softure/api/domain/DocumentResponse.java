package com.softure.api.domain;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DocumentResponse {

	private String template;
	private String id;
	private String code;
	private String active;
	private String stateId;
	private String stateName;
	private List<FieldResponse> fields;
	private BigDecimal fullValue;
	private BigDecimal pendingValue;
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
	public List<FieldResponse> getFields() {
		return fields;
	}
	public void setFields(List<FieldResponse> fields) {
		this.fields = fields;
	}
	public BigDecimal getFullValue() {
		return fullValue;
	}
	public void setFullValue(BigDecimal fullValue) {
		this.fullValue = fullValue;
	}
	public BigDecimal getPendingValue() {
		return pendingValue;
	}
	public void setPendingValue(BigDecimal pendingValue) {
		this.pendingValue = pendingValue;
	}
	
}
