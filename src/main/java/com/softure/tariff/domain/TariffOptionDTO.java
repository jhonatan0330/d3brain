package com.softure.tariff.domain;

import org.apache.ibatis.type.Alias;

@Alias("TariffOptionDTO")
public class TariffOptionDTO  {

	private String key;
	private String code;
	private String name;
	private String productId;
	private String template;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}

}