package com.task.task.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.task.task.application.base.TaskService;
import com.task.task.domain.TaskDTO;
import com.task.task.domain.TaskFilterDTO;

@Service
public class TaskGetByUserService {

	@Autowired @Lazy  private TaskService taskService;
	
	public List<TaskDTO> call(String user) throws ServerException{
		TaskFilterDTO filter = new TaskFilterDTO();
		filter.setUser(user);
		filter.setState(SharedConstants.STATE_ACTIVE);
		return taskService.getMany(filter);
	}
}
