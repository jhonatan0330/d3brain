package com.softure.api.domain;

public class DocumentFilterWithLoginRequest {
	
	private DocumentFilterRequest document;
	private LoginRequest login;
	
	public DocumentFilterRequest getDocument() {
		return document;
	}
	public void setDocument(DocumentFilterRequest document) {
		this.document = document;
	}
	public LoginRequest getLogin() {
		return login;
	}
	public void setLogin(LoginRequest login) {
		this.login = login;
	}
	
}
