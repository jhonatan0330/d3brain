package com.softure.task.application;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.java.dto.exception.ServerException;
import com.softure.shared.application.SharedCRUDService;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskFilter;
import com.softure.task.infrastructure.TaskMapper;

@Service("TaskService")
public class TaskCRUDTaskService extends SharedCRUDService<TaskDTO, TaskFilter> {

	@Autowired
	private TaskMapper taskMapper;

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = taskMapper;
	}

	@Override
	public TaskDTO findById(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Task");
		TaskFilter dto = new TaskFilter();
		dto.setId(llave);
		return taskMapper.selectOne(dto);
	}

}