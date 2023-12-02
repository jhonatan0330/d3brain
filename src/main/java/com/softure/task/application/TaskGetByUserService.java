package com.softure.task.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.SharedConstants;
import com.softure.java.dto.exception.ServerException;
import com.softure.task.domain.TaskTaskDTO;
import com.softure.task.domain.TaskTaskFilter;
import com.softure.task.domain.TaskTaskResponse;

@Service
public class TaskGetByUserService {

	@Autowired private TaskCRUDTaskService taskService;
	
	public List<TaskTaskResponse> call(String user) throws ServerException{
		TaskTaskFilter filter = new TaskTaskFilter();
		filter.setUser(user);
		filter.setState(SharedConstants.STATE_ACTIVE);
		List<TaskTaskDTO> tasks = taskService.findMany(filter);
		return tasks.stream().map(TaskTaskDTO::toResponse).collect(Collectors.toList());
	}
}
