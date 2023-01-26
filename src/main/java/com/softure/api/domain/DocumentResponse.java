package com.softure.api.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class DocumentResponse {

	private String template;
	private String id;
	private String code;
	private String active;
	private String stateId;
	private String stateName;
	private List<FieldResponse> fields;
}
