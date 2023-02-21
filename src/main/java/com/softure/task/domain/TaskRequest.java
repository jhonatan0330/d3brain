package com.softure.task.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TaskRequest
{
	private String llaveTabla;
	private String user;
	private String title;
	private String notes;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private boolean completed;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date dueDate;
	private Integer priority;
	private Integer order;
    
	public TaskDTO toModel() {
		TaskDTO result = new TaskDTO();
		result.setUser(this.user);
		result.setTitle(this.title);
		result.setNotes(this.notes);
		result.setDueDate(this.dueDate);
		result.setPriority(this.priority);
		result.setOrder(this.order);
		return result;
	}
}