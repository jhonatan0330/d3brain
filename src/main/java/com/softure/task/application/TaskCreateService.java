package com.softure.task.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.task.application.base.TaskService;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskRequest;

@Service
public class TaskCreateService {

	@Autowired private TaskService taskService;
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse call(TaskRequest task, String user) throws ServerException {
		TaskDTO dto = task.toModel();
		dto.setUser(user);
		dto.setCreatedUser(user);
		taskService.save(dto);
		return new SharedIdResponse(dto.getKey());
	}

}
