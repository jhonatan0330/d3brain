package com.accounting.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class VoucherRequest {
	
	private String catalog;
	private String concept;
	private Date factDate;
	private BigDecimal value;
	private String document;
	private List<VoucherLineRequest> lines;
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public Date getFactDate() {
		return factDate;
	}
	public void setFactDate(Date factDate) {
		this.factDate = factDate;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public List<VoucherLineRequest> getLines() {
		return lines;
	}
	public void setLines(List<VoucherLineRequest> lines) {
		this.lines = lines;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	
	
}
