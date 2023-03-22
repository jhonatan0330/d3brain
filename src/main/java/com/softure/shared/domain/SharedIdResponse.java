package com.softure.shared.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SharedIdResponse {
	private String id;
	private String code;
	
	public SharedIdResponse(String id) {
		this.id = id;
	}
	
	
}
