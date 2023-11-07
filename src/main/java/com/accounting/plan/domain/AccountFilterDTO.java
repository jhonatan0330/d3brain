package com.accounting.plan.domain;


import org.apache.ibatis.type.Alias;

@Alias("AccountFilterDTO")
public class AccountFilterDTO {
	private String key;
	private String state;
	private String filter;
	private Integer indexStart;
	private Integer indexEnd;
	private String catalog;
	private String wbs;
	private String name;
	private String code;
	private String status;
	private String parent;
	private Integer level;
	private String type;
	private String operation;
	private String template;
	private String field;

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

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getWbs() {
		return wbs;
	}

	public void setWbs(String wbs) {
		this.wbs = wbs;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}