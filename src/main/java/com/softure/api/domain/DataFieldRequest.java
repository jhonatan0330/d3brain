package com.softure.api.domain;

import java.util.List;

import lombok.Data;

@Data
public class DataFieldRequest {
	private String template;
	private String code;
	private List<FieldRequest> preconditions;
}
