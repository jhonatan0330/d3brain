package com.accounting.plan.domain;


import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("FormatVoucherFilterDTO")
public class FormatVoucherFilterDTO extends SharedDataObjectFilter {

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