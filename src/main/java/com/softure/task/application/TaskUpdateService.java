package com.softure.task.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskRequest;

@Service
public class TaskUpdateService {

	@Autowired private TaskService taskService;
	
	public IdResponse call(TaskRequest task, String token) throws ServerException {
		if (task==null) throw new ServerException("Es importante enviar los datos de la tarea");
		if (task.getLlaveTabla()==null || task.getLlaveTabla().isEmpty()) throw new ServerException("Falta la llave de la tarea");
		TaskDTO bd = taskService.consultaXId(task.getLlaveTabla());
		bd.setTitle(task.getTitle());
		bd.setNotes(task.getNotes());
		bd.setPriority(task.getPriority());
		bd.setOrder(task.getOrder());
		taskService.actualizar(bd, token);
		return new IdResponse( task.getLlaveTabla() );
	}

}
