package com.task.task.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.task.task.application.base.TaskService;
import org.springframework.context.annotation.Lazy;

@Service
public class TaskDeleteService {

	private final TaskService taskService;

	public TaskDeleteService(@Lazy TaskService taskService) {
		this.taskService = taskService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(String id, String user) throws ServerException {
		taskService.delete(id);
		return new SharedIdResponse(id);
	}

}
