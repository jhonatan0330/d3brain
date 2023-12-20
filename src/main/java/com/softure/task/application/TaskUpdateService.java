package com.softure.task.application;

import java.util.Date;

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
public class TaskUpdateService {

	@Autowired private TaskService taskService;
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse call(TaskRequest task, String user) throws ServerException {
		if (task==null) throw new ServerException("Es importante enviar los datos de la tarea");
		if (task.getKey()==null || task.getKey().isEmpty()) throw new ServerException("Falta la llave de la tarea");
		TaskDTO bd = taskService.getById(task.getKey());
		bd.setTitle(task.getTitle());
		bd.setNotes(task.getNotes());
		bd.setPriority(task.getPriority());
		bd.setOrder(task.getOrder());
		if(task.getCompleted()!=null)bd.setCompleted(new Date());
		taskService.update(bd);
		return new SharedIdResponse( task.getKey() );
	}

}
