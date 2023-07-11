package com.softure.api.domain;

public class DocumentWithLoginRequest {

	private DocumentRequest document;
	private LoginRequest login;
	
	public DocumentRequest getDocument() {
		return document;
	}
	public void setDocument(DocumentRequest document) {
		this.document = document;
	}
	public LoginRequest getLogin() {
		return login;
	}
	public void setLogin(LoginRequest login) {
		this.login = login;
	}
	
}
