package com.softure.api.domain;

import lombok.Data;

@Data
public class FilterWithLoginVO {

	private LoginVO login;
	private FilterDocumentVO document;
	
}
