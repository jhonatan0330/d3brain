package com.softure.task.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskRequest;

@Service
public class TaskCreateService {

	@Autowired private TaskService taskService;
	
	public IdResponse call(TaskRequest task, String token) throws ServerException {
		TaskDTO dto = task.toModel();
		dto = taskService.guardar(dto, token);
		return new IdResponse(dto.getLlaveTabla());
	}

}
