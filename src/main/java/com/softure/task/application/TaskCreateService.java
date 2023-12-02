package com.softure.task.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.task.domain.TaskTaskDTO;
import com.softure.task.domain.TaskTaskRequest;

@Service
public class TaskCreateService {

	@Autowired private TaskCRUDTaskService taskService;
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse call(TaskTaskRequest task, String user) throws ServerException {
		TaskTaskDTO dto = task.toModel();
		dto.setUser(user);
		dto.setCreatedUser(user);
		return new SharedIdResponse(taskService.save(dto));
	}

}
