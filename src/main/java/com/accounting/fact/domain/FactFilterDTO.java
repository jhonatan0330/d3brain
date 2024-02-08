package com.accounting.fact.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("FactFilterDTO")
public class FactFilterDTO extends SharedDataObjectFilter {

	private Date registerDateMin;
	private Date registerDateMax;
	private Date factDateMin;
	private Date factDateMax;
	private String dimension;
	private String value;
	private String code;
	private String template;
	private String id;

	public Date getRegisterDateMin() {
		return registerDateMin;
	}

	public void setRegisterDateMin(Date registerDateMin) {
		this.registerDateMin = registerDateMin;
	}

	public Date getRegisterDateMax() {
		return registerDateMax;
	}

	public void setRegisterDateMax(Date registerDateMax) {
		this.registerDateMax = registerDateMax;
	}

	public Date getFactDateMin() {
		return factDateMin;
	}

	public void setFactDateMin(Date factDateMin) {
		this.factDateMin = factDateMin;
	}

	public Date getFactDateMax() {
		return factDateMax;
	}

	public void setFactDateMax(Date factDateMax) {
		this.factDateMax = factDateMax;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}