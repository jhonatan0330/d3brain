package com.softure.task.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.softure.shared.domain.SharedDataObject;



@Alias("TaskTaskDTO")
public class TaskTaskDTO extends SharedDataObject
{
	private String user;
	private String title;
	private String notes;
	private Date completed;
	private Date dueDate;
	private Integer priority;
	private Integer order;

    public TaskTaskResponse toResponse() {
    	TaskTaskResponse result = new TaskTaskResponse();
    	result.setId(getId());
    	result.setUser(user);
    	result.setTitle(title);
    	result.setNotes(notes);
    	result.setCompleted(completed);
    	result.setDueDate(dueDate);
    	result.setPriority(priority);
    	result.setOrder(order);
    	return result;
    }

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

	public Date getCompleted() {
		return completed;
	}

	public void setCompleted(Date completed) {
		this.completed = completed;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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