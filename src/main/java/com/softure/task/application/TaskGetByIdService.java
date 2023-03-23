package com.softure.task.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.dto.exception.ServerException;
import com.softure.task.domain.TaskTaskDTO;
import com.softure.task.domain.TaskTaskResponse;

@Service
public class TaskGetByIdService {

	@Autowired private TaskCRUDTaskService taskService;
	
	public TaskTaskResponse call(String id) throws ServerException{
		TaskTaskDTO dto = taskService.findById(id);
		if(dto!=null) return dto.toResponse();
		return null;
	}
}
