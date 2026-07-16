package com.task.task.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.task.task.application.base.TaskService;
import com.task.task.domain.TaskDTO;
import com.task.task.domain.TaskFilterDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class TaskGetByUserService {

	private final TaskService taskService;

	public TaskGetByUserService(@Lazy TaskService taskService) {
		this.taskService = taskService;
	}

	public List<TaskDTO> call(String user) throws ServerException {
		TaskFilterDTO filter = new TaskFilterDTO();
		filter.setUser(user);
		filter.setState(SharedConstants.STATE_ACTIVE);
		return taskService.getMany(filter);
	}
}
