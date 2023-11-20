package com.softure.report.domain;

public class ReportDTO {

	private byte[] content;
	private ReporteEjecucionDTO data;
	private String name;
	
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public ReporteEjecucionDTO getData() {
		return data;
	}
	public void setData(ReporteEjecucionDTO data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
