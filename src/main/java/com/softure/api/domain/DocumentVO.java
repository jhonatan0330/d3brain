package com.softure.api.domain;

import java.util.List;

import lombok.Data;

@Data
public class DocumentVO {

	private String template;
	private List<FieldVO> fields;
}
