package com.softure.api.domain;

import lombok.Data;

@Data
public class DocumentFilterWithLoginRequest {
	
	private DocumentFilterRequest document;
	private LoginRequest login;
	
}
