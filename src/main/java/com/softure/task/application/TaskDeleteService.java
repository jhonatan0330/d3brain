package com.softure.task.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;

@Service
public class TaskDeleteService {

	@Autowired private TaskCRUDTaskService taskService;

	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse call(String id, String user) throws ServerException {
		taskService.delete(id, user);
		return new SharedIdResponse(id);
	}

}
