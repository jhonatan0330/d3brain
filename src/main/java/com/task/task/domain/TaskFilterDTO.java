package com.task.task.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.shared.domain.SharedDataObjectFilter;

@Alias("TaskFilterDTO")
public class TaskFilterDTO extends SharedDataObjectFilter {

	private String user;
	private String title;
	private String notes;
	private Date completedMin;
	private Date completedMax;
	private Date dueDateMin;
	private Date dueDateMax;
	private Integer priority;
	private Integer order;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getCompletedMin() {
		return completedMin;
	}

	public void setCompletedMin(Date completedMin) {
		this.completedMin = completedMin;
	}

	public Date getCompletedMax() {
		return completedMax;
	}

	public void setCompletedMax(Date completedMax) {
		this.completedMax = completedMax;
	}

	public Date getDueDateMin() {
		return dueDateMin;
	}

	public void setDueDateMin(Date dueDateMin) {
		this.dueDateMin = dueDateMin;
	}

	public Date getDueDateMax() {
		return dueDateMax;
	}

	public void setDueDateMax(Date dueDateMax) {
		this.dueDateMax = dueDateMax;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}