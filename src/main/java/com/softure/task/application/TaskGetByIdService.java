package com.softure.task.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.dto.exception.ServerException;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskResponse;

@Service
public class TaskGetByIdService {

	@Autowired private TaskCRUDTaskService taskService;
	
	public TaskResponse call(String id) throws ServerException{
		TaskDTO dto = taskService.findById(id);
		if(dto!=null) return dto.toResponse();
		return null;
	}
}
