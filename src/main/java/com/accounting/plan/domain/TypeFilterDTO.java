package com.accounting.plan.domain;


import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("TypeFilterDTO")
public class TypeFilterDTO extends SharedDataObjectFilter {

	private String catalog;
	private String name;
	private String code;

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

}