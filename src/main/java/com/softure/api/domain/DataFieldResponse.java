package com.softure.api.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class DataFieldResponse {
	
	private String field;
	private String value;
	private String internalId;
	private List<DocumentResponse> documents;
	
}
