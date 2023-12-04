package com.accounting.plan.domain;


import com.shared.domain.SharedDataObject;
import org.apache.ibatis.type.Alias;

@Alias("TypeDTO")
public class TypeDTO extends SharedDataObject{

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