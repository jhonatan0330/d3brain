package d3.accounting.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObjectFilter;

@Alias("CatalogFilterDTO")
public class CatalogFilterDTO extends SharedDataObjectFilter {

	private String name;
	private String code;
	private Date initialDateMin;
	private Date initialDateMax;
	private Date finalDateMin;
	private Date finalDateMax;
	private String document;
	private String template;

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

	public Date getInitialDateMin() {
		return initialDateMin;
	}

	public void setInitialDateMin(Date initialDateMin) {
		this.initialDateMin = initialDateMin;
	}

	public Date getInitialDateMax() {
		return initialDateMax;
	}

	public void setInitialDateMax(Date initialDateMax) {
		this.initialDateMax = initialDateMax;
	}

	public Date getFinalDateMin() {
		return finalDateMin;
	}

	public void setFinalDateMin(Date finalDateMin) {
		this.finalDateMin = finalDateMin;
	}

	public Date getFinalDateMax() {
		return finalDateMax;
	}

	public void setFinalDateMax(Date finalDateMax) {
		this.finalDateMax = finalDateMax;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}