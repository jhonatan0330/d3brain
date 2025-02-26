package com.shared.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class SharedIdResponse {
	
	private String id;
	private String code;
	private String comment;
	
	public SharedIdResponse(String id) {
		this.id = id;
	}

	public SharedIdResponse(String llaveTabla, String nombre) {
		this.id = llaveTabla;
		this.code = nombre;
	}
	
	public SharedIdResponse(String llaveTabla, String nombre, String comment) {
		this.id = llaveTabla;
		this.code = nombre;
		this.comment = comment;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
