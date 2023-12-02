package com.softure.task.application;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.application.SharedCRUDService;
import com.shared.domain.ServerException;
import com.softure.task.domain.TaskTaskDTO;
import com.softure.task.domain.TaskTaskFilter;
import com.softure.task.infrastructure.TaskTaskMapper;

@Service("TaskService")
public class TaskCRUDTaskService extends SharedCRUDService<TaskTaskDTO, TaskTaskFilter> {

	@Autowired
	private TaskTaskMapper taskMapper;

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = taskMapper;
	}

	@Override
	public TaskTaskDTO findById(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Task");
		TaskTaskFilter dto = new TaskTaskFilter();
		dto.setKey(llave);
		return taskMapper.selectOne(dto);
	}

}