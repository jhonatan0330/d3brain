package com.softure.task.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.dto.exception.ServerException;
import com.softure.shared.domain.SharedConstants;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskFilter;
import com.softure.task.domain.TaskResponse;

@Service
public class TaskGetByUserService {

	@Autowired private TaskCRUDTaskService taskService;
	
	public List<TaskResponse> call(String user) throws ServerException{
		TaskFilter filter = new TaskFilter();
		filter.setUser(user);
		filter.setState(SharedConstants.STATE_ACTIVE);
		List<TaskDTO> tasks = taskService.findMany(filter);
		return tasks.stream().map(TaskDTO::toResponse).collect(Collectors.toList());
	}
}
