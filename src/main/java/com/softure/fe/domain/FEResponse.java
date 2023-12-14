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
	private String zipBase64;
	private String digestValue;
	private String signatureValue;
	private String x509Certificate;
	
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
	public String getZipBase64() {
		return zipBase64;
	}
	public void setZipBase64(String zipBase64) {
		this.zipBase64 = zipBase64;
	}
	public String getDigestValue() {
		return digestValue;
	}
	public void setDigestValue(String digestValue) {
		this.digestValue = digestValue;
	}
	public String getSignatureValue() {
		return signatureValue;
	}
	public void setSignatureValue(String signatureValue) {
		this.signatureValue = signatureValue;
	}
	public String getX509Certificate() {
		return x509Certificate;
	}
	public void setX509Certificate(String x509Certificate) {
		this.x509Certificate = x509Certificate;
	}
}
