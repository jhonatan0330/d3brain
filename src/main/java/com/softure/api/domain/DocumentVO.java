package com.softure.api.domain;

import java.util.List;

import lombok.Data;

@Data
public class DocumentVO {

	private String template;
	private String id;
	private String code;
	private String active;
	private String stateId;
	private String stateName;
	private List<FieldVO> fields;
}
