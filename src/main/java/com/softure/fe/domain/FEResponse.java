package com.softure.fe.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FEResponse {
	
	private String result;
	private String error;
	private String xml;
	private String cufe;
	private String zipUrl;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCufe() {
		return cufe;
	}
	public void setCufe(String cufe) {
		this.cufe = cufe;
	}
	public String getZipUrl() {
		return zipUrl;
	}
	public void setZipUrl(String zipUrl) {
		this.zipUrl = zipUrl;
	}
	
}
