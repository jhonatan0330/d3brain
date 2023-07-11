package com.softure.api.domain;


public class DataFieldWithLoginRequest {
	
	private LoginRequest login;
	private DataFieldRequest field;
	public LoginRequest getLogin() {
		return login;
	}
	public void setLogin(LoginRequest login) {
		this.login = login;
	}
	public DataFieldRequest getField() {
		return field;
	}
	public void setField(DataFieldRequest field) {
		this.field = field;
	}
	
}
