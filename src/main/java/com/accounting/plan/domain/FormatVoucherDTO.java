package com.accounting.plan.domain;


import com.shared.domain.SharedDataObject;
import org.apache.ibatis.type.Alias;

@Alias("FormatVoucherDTO")
public class FormatVoucherDTO extends SharedDataObject{

	private String catalog;
	private String template;

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