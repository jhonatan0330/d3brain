package com.accounting.plan.domain;


import org.apache.ibatis.type.Alias;

@Alias("ValueDimensionFilterDTO")
public class ValueDimensionFilterDTO {
	private String key;
	private String state;
	private String filter;
	private Integer indexStart;
	private Integer indexEnd;
	private String dimension;
	private String value;
	private String code;
	private String template;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public Integer getIndexStart() {
		return indexStart;
	}

	public void setIndexStart(Integer indexStart) {
		this.indexStart = indexStart;
	}

	public Integer getIndexEnd() {
		return indexEnd;
	}

	public void setIndexEnd(Integer indexEnd) {
		this.indexEnd = indexEnd;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}