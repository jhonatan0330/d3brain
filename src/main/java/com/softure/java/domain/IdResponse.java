package com.softure.java.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class IdResponse {
	private String id;
	private String code;
	
	public IdResponse(String id) {
		super();
		this.id = id;
	}
	
	
}
