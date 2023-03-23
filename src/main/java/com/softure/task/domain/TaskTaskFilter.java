package com.softure.task.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.softure.shared.domain.SharedDataObjectFilter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("TaskTaskFilter")
public class TaskTaskFilter extends SharedDataObjectFilter
{

	private String user;
	private String title;
	private String notes;
	private Date completed;
	private Date dueDate;
	private Integer priority;
	private Integer order;

}