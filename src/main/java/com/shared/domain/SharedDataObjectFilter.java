package com.shared.domain;

import java.util.Date;

public abstract class SharedDataObjectFilter {

	private String key;
	private Date createdAtStart;
    private Date createdAtEnd;
    private String createdUser;
    private Date updatedAtStart;
    private Date updatedAtEnd;
    private Integer startRow;
	private Integer endRow;
	private String filter;
    private String state;
    
    
	public String getKey() {
		return key;
	}
	public void setKey(String id) {
		this.key = id;
	}
	public Date getCreatedAtStart() {
		return createdAtStart;
	}
	public void setCreatedAtStart(Date createdAtStart) {
		this.createdAtStart = createdAtStart;
	}
	public Date getCreatedAtEnd() {
		return createdAtEnd;
	}
	public void setCreatedAtEnd(Date createdAtEnd) {
		this.createdAtEnd = createdAtEnd;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public Date getUpdatedAtStart() {
		return updatedAtStart;
	}
	public void setUpdatedAtStart(Date updatedAtStart) {
		this.updatedAtStart = updatedAtStart;
	}
	public Date getUpdatedAtEnd() {
		return updatedAtEnd;
	}
	public void setUpdatedAtEnd(Date updatedAtEnd) {
		this.updatedAtEnd = updatedAtEnd;
	}
	public Integer getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public Integer getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filterText) {
		this.filter = filterText;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
    
}
