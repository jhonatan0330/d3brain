package com.softure.task.domain;

import java.util.Date;

import com.softure.shared.domain.SharedDataObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO extends SharedDataObject
{
	private String user;
	private String title;
	private String notes;
	private Date completed;
	private Date dueDate;
	private Integer priority;
	private Integer order;


    public TaskResponse toResponse() {
    	TaskResponse result = new TaskResponse();
    	result.setLlaveTabla(getId());
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