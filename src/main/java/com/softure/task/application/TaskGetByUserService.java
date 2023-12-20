package com.softure.task.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.task.application.base.TaskService;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskFilterDTO;

@Service
public class TaskGetByUserService {

	@Autowired private TaskService taskService;
	
	public List<TaskDTO> call(String user) throws ServerException{
		TaskFilterDTO filter = new TaskFilterDTO();
		filter.setUser(user);
		filter.setState(SharedConstants.STATE_ACTIVE);
		return taskService.getMany(filter);
	}
}
