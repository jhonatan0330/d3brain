package com.softure.task.infrastructure;

import java.util.List;

import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper("TaskSoftureMapper")
public interface TaskMapper {

	TaskDTO insert(TaskDTO dto);

	TaskDTO update(TaskDTO dto);

	int count(TaskFilterDTO filter);
	
	TaskDTO getOne(TaskFilterDTO filter);

	List<TaskDTO> getMany(TaskFilterDTO filter);

}