package com.softure.api.domain;

import java.util.List;

public class FieldRequest {
	
	private String field;
	private String value;
	private List<ProductRequest> products;
	private DocumentRequest parentDocument;
	
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
	public List<ProductRequest> getProducts() {
		return products;
	}
	public void setProducts(List<ProductRequest> products) {
		this.products = products;
	}
	public DocumentRequest getParentDocument() {
		return parentDocument;
	}
	public void setParentDocument(DocumentRequest parentDocument) {
		this.parentDocument = parentDocument;
	}
	
	
}
