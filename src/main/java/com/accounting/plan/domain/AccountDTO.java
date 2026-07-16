package com.accounting.plan.domain;

import com.shared.domain.SharedDataObject;
import org.apache.ibatis.type.Alias;

@Alias("AccountDTO")
public class AccountDTO extends SharedDataObject {

	private String catalog;
	private String catalogDocument;
	private String wbs;
	private String name;
	private String code;
	private String parent;
	private String parentDocument;
	private Integer level;
	private String type;
	private String operation;
	private String template;
	private String field;
	private String document;

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getCatalogDocument() {
		return catalogDocument;
	}

	public void setCatalogDocument(String catalogDocument) {
		this.catalogDocument = catalogDocument;
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

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParentDocument() {
		return parentDocument;
	}

	public void setParentDocument(String parentDocument) {
		this.parentDocument = parentDocument;
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

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

}