package com.accounting.plan.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.shared.domain.SharedParamObject;
import org.apache.ibatis.type.Alias;

@Alias("CatalogDTO")
public class CatalogDTO extends SharedParamObject{

	private String name;
	private String code;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date initialDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date finalDate;
	private String consecutive;
	private String document;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	public String getConsecutive() {
		return consecutive;
	}

	public void setConsecutive(String consecutive) {
		this.consecutive = consecutive;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

}