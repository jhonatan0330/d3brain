package com.softure.api.domain;

import java.util.List;

import lombok.Data;

@Data
public class FieldRequest {
	
	private String field;
	private String value;
	private List<ProductRequest> products;
}
