package com.task.task.infrastructure;

import java.util.List;

import com.task.task.domain.TaskDTO;
import com.task.task.domain.TaskFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "TaskTaskMapper")
public interface TaskMapper {

	TaskDTO insert(TaskDTO dto);

	TaskDTO update(TaskDTO dto);

	int count(TaskFilterDTO filter);
	
	TaskDTO getOne(TaskFilterDTO filter);

	List<TaskDTO> getMany(TaskFilterDTO filter);

}