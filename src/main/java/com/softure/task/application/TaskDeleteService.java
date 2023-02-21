package com.softure.task.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.task.domain.TaskDTO;

@Service
public class TaskDeleteService {

	@Autowired private TaskService taskService;

	public IdResponse call(String id, String token) throws ServerException {
		TaskDTO dto = taskService.consultaXId(id);
		dto.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		taskService.update(dto);
		return new IdResponse(id);
	}

}
