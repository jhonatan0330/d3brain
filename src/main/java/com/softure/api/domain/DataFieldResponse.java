package com.softure.api.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DataFieldResponse {

	private String field;
	private String value;
	private String internalId;
	private List<DocumentResponse> documents;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public List<DocumentResponse> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentResponse> documents) {
		this.documents = documents;
	}

}
