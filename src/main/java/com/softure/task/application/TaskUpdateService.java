package com.softure.task.application;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.shared.domain.SharedIdResponse;
import com.softure.task.domain.TaskTaskDTO;
import com.softure.task.domain.TaskTaskRequest;

@Service
public class TaskUpdateService {

	@Autowired private TaskCRUDTaskService taskService;
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public SharedIdResponse call(TaskTaskRequest task, String user) throws ServerException {
		if (task==null) throw new ServerException("Es importante enviar los datos de la tarea");
		if (task.getId()==null || task.getId().isEmpty()) throw new ServerException("Falta la llave de la tarea");
		TaskTaskDTO bd = taskService.findById(task.getId());
		bd.setTitle(task.getTitle());
		bd.setNotes(task.getNotes());
		bd.setPriority(task.getPriority());
		bd.setOrder(task.getOrder());
		if(task.getCompleted()!=null)bd.setCompleted(new Date());
		taskService.update(bd, user);
		return new SharedIdResponse( task.getId() );
	}

}
