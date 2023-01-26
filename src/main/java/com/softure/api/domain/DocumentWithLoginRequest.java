package com.softure.api.domain;

import lombok.Data;

@Data
public class DocumentWithLoginRequest {

	private DocumentRequest document;
	private LoginRequest login;
}
