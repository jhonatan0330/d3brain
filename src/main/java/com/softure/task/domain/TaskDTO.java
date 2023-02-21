package com.softure.task.domain;

import java.util.Date;

import com.softure.java.domain.BasicDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO extends BasicDTO
{
	private String user;
	private String title;
	private String notes;
	private Date completed;
	private Date dueDate;
	private Integer priority;
	private Integer order;
    private Date createdAt;
    private Date updatedAt;
    
    public TaskResponse toResponse() {
    	TaskResponse result = new TaskResponse();
    	result.setCompleted(completed!=null);
    	result.setDueDate(dueDate);
    	result.setLlaveTabla(getLlaveTabla());
    	result.setNotes(notes);
    	result.setOrder(order);
    	result.setPriority(priority);
    	result.setTitle(title);
    	return result;
    }
    
}