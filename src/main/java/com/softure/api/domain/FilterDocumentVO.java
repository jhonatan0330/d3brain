package com.softure.api.domain;

import java.util.List;

import lombok.Data;

@Data
public class FilterDocumentVO {

	private String template;
	private String code;
	private String filterText;
	private List<String> states;
	private String active;
	private int page;
	private int size;
}
