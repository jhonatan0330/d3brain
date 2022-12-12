package com.softure.api.domain;

import java.util.List;

import lombok.Data;

@Data
public class ApiVO {

	private String template;
	private List<FieldVO> fields;
}
