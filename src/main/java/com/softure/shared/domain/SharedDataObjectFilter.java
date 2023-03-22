package com.softure.shared.domain;

import java.util.Date;

public abstract class SharedDataObjectFilter {

	private String id;
	private Date createdAtStart;
    private Date createdAtEnd;
    private String createdUser;
    private Date updatedAtStart;
    private Date updatedAtEnd;
    private String updatedUser;
    private Integer startRow;
	private Integer endRow;
	private String filterText;
    private String state;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
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
	public String getFilterText() {
		return filterText;
	}
	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
    
}
