package com.softure.api.domain;

import lombok.Data;

@Data
public class LoginRequest {
	private String user;
	private String password;
}
