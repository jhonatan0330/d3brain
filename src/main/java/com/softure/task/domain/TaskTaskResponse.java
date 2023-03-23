package com.softure.task.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class TaskTaskResponse
{
	private String id;
	private String user;
	private String title;
	private String notes;
	private Date completed;
	private Date dueDate;
	private Integer priority;
	private Integer order;
}