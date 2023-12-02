package com.shared.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class SharedIdResponse {
	
	private String id;
	private String code;
	
	public SharedIdResponse(String id) {
		this.id = id;
	}

	public SharedIdResponse(String llaveTabla, String nombre) {
		this.id = llaveTabla;
		this.code = nombre;
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
	
	
}
