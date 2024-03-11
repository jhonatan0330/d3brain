package com.learning.helpcenter.domain;


import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("ArticleFilterDTO")
public class ArticleFilterDTO extends SharedDataObjectFilter {

	private String title;
	private String automatic;
	private String entityType;
	private String entity;
	private String document;
	private String image;
	private String introduction;
	private String help;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAutomatic() {
		return automatic;
	}

	public void setAutomatic(String automatic) {
		this.automatic = automatic;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

}