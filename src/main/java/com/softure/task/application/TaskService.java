package com.softure.task.application;

import java.util.Date;
import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.task.domain.TaskDTO;
import com.softure.task.domain.TaskFilterDTO;
import com.softure.task.infrastructure.TaskMapper;

@Service("TaskService")
public class TaskService extends BasicSvc<TaskDTO, TaskFilterDTO> {
	
	@Autowired
	private TaskMapper taskMapper;
	
	// BEGIN region servicesTask
	// END region servicesTask

	@Override
	public TaskDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Task");
		TaskFilterDTO dto = new TaskFilterDTO();
		dto.setLlaveTabla(llave);
		return taskMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = taskMapper;
	}
	
	@Override
	public TaskDTO activar(TaskDTO dto, String token) throws ServerException {
		// BEGIN Task_activar
		return super.activar(dto, token);
		// END Task_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TaskDTO actualizar( TaskDTO dto, String token) throws ServerException {
		// BEGIN Task_actualizar
		dto.setUpdatedAt(new Date());
		return super.actualizar(dto, token);
		// END Task_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TaskDTO inactivar(TaskDTO dto, String token) throws ServerException {
		// BEGIN Task_inactivar
		return super.inactivar(dto, token);
		// END Task_inactivar
	}
	
	@Override
	public TaskDTO consultaUnica(TaskFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(TaskFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<TaskDTO> listarConsulta(TaskFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TaskDTO guardar(TaskDTO dto, String token) throws ServerException {
		// BEGIN Task_guardar
		dto.setUser(getUserFlex(token));
		dto.setCreatedAt(new Date());
		return super.guardar(dto, token);
		// END Task_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}