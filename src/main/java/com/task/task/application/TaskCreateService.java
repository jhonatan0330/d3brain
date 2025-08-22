package com.task.task.application;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.task.task.application.base.TaskService;
import com.task.task.domain.TaskDTO;
import com.task.task.domain.TaskRequest;

@Service
public class TaskCreateService {

	@Autowired @Lazy  private TaskService taskService;
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse call(TaskRequest task, String user) throws ServerException {
		TaskDTO dto = task.toModel();
		dto.setUser(user);
		dto.setCreatedAt(new Date());
		taskService.save(dto);
		return new SharedIdResponse(dto.getKey());
	}

}
