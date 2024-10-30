package com.accounting.plan.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("AccountFilterDTO")
public class AccountFilterDTO extends SharedDataObjectFilter {

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
	private Date initialDateMin;
	private Date initialDateMax;
	private Date finalDateMin;
	private Date finalDateMax;
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

}