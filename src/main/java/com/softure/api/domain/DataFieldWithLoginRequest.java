package com.softure.api.domain;

import lombok.Data;

@Data
public class DataFieldWithLoginRequest {
	private LoginRequest login;
	private DataFieldRequest field;
}
