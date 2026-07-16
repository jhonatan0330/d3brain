package com.task.task.application;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.task.task.application.base.TaskService;
import com.task.task.domain.TaskDTO;
import com.task.task.domain.TaskRequest;
import org.springframework.context.annotation.Lazy;

@Service
public class TaskCreateService {

	private final TaskService taskService;

	public TaskCreateService(@Lazy TaskService taskService) {
		this.taskService = taskService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(TaskRequest task, String user) throws ServerException {
		TaskDTO dto = task.toModel();
		dto.setUser(user);
		dto.setCreatedAt(new Date());
		taskService.save(dto);
		return new SharedIdResponse(dto.getKey());
	}

}
