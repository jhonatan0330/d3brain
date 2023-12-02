package com.accounting.plan.domain;


import com.shared.domain.SharedDataObject;
import org.apache.ibatis.type.Alias;

@Alias("DimensionDTO")
public class DimensionDTO extends SharedDataObject{

	private String account;
	private String name;
	private String code;
	private String field;
	private String type;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}