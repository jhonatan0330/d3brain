package com.softure.task.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskFilterDTO;
import com.softure.task.domain.TaskResponse;

@Service
public class TaskGetByUserService {

	@Autowired private TaskService taskService;
	
	public List<TaskResponse> call(String token) throws ServerException{
		TaskFilterDTO filter = new TaskFilterDTO();
		filter.setUser(taskService.getUserFlex(token));
		filter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<TaskDTO> tasks = taskService.listarConsulta(filter);
		return tasks.stream().map(TaskDTO::toResponse).collect(Collectors.toList());
	}
}
