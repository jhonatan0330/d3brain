package com.accounting.plan.domain;


import org.apache.ibatis.type.Alias;

@Alias("FormatVoucherDTO")
public class FormatVoucherDTO {
	private String key;
	private String state;
	private String catalog;
	private String template;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}