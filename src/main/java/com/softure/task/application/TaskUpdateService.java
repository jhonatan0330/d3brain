package com.softure.task.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.task.domain.TaskRequest;

@Service
public class TaskUpdateService {

	@Autowired private TaskService taskService;
	
	public IdResponse call(TaskRequest task, String token) throws ServerException {
		taskService.update(task.toModel());
		return new IdResponse( task.getLlaveTabla() );
	}

}
