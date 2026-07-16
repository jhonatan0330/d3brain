package com.configuration.homologate.domain;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("ConfigTemplateRelationFilterDTO")
public class ConfigTemplateRelationFilterDTO extends SharedDataObjectFilter {

	private String entity;
	private String entityField;
	private String template;
	private String templateField;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getEntityField() {
		return entityField;
	}

	public void setEntityField(String entityField) {
		this.entityField = entityField;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplateField() {
		return templateField;
	}

	public void setTemplateField(String templateField) {
		this.templateField = templateField;
	}

}