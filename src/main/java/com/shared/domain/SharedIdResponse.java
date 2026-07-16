package com.shared.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.softure.document_execution.domain.DocumentMessage;

@JsonInclude(Include.NON_NULL)
public class SharedIdResponse {

	private String id;
	private String code;
	private String state;
	private String comment;
	private List<DocumentMessage> messages;

	public SharedIdResponse(String id) {
		this.id = id;
	}

	public SharedIdResponse(String llaveTabla, String nombre) {
		this.id = llaveTabla;
		this.code = nombre;
	}

	public SharedIdResponse(String llaveTabla, String nombre, String state, String comment) {
		this.id = llaveTabla;
		this.code = nombre;
		this.setState(state);
		this.comment = comment;
	}

	public SharedIdResponse(String llaveTabla, String nombre, String state, List<DocumentMessage> messages) {
		this.id = llaveTabla;
		this.code = nombre;
		this.setState(state);
		this.messages = messages;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<DocumentMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<DocumentMessage> messages) {
		this.messages = messages;
	}

}
