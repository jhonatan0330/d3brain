package com.accounting.plan.domain;


import com.shared.domain.SharedDataObject;
import org.apache.ibatis.type.Alias;

@Alias("TypeDTO")
public class TypeDTO extends SharedDataObject{

	private String catalog;
	private String name;
	private String code;
	private boolean automatic;
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

	public boolean getAutomatic() {
		return automatic;
	}

	public void setAutomatic(boolean automatic) {
		this.automatic = automatic;
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