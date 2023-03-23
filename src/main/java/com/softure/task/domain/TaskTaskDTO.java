package com.softure.task.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.softure.shared.domain.SharedDataObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
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
    
}