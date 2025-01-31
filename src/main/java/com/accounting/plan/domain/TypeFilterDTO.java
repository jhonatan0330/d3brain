package com.accounting.plan.domain;


import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("TypeFilterDTO")
public class TypeFilterDTO extends SharedDataObjectFilter {

	private String catalog;
	private String name;
	private String code;
	private Boolean automaticFilter;
	private String pattern;
	private String consecutive;

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

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

	public Boolean getAutomaticFilter() {
		return automaticFilter;
	}

	public void setAutomaticFilter(Boolean automaticFilter) {
		this.automaticFilter = automaticFilter;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getConsecutive() {
		return consecutive;
	}

	public void setConsecutive(String consecutive) {
		this.consecutive = consecutive;
	}

}