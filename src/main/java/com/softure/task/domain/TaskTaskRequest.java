package com.softure.task.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class TaskTaskRequest
{
	private String id;
	
	private String user;
	
	private String title;
	
	private String notes;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date completed;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date dueDate;
	
	private Integer priority;
	
	private Integer order;

    public TaskTaskDTO toModel() {
    	TaskTaskDTO result = new TaskTaskDTO();
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
    
}