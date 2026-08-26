package d3.task.application;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.task.application.base.TaskService;
import d3.task.domain.TaskDTO;
import d3.task.domain.TaskRequest;
import org.springframework.context.annotation.Lazy;

@Service
public class TaskCreateService {

	private final TaskService taskService;

	public TaskCreateService(@Lazy TaskService taskService) {
		this.taskService = taskService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(TaskRequest task, String user) throws ServerException {
		TaskDTO dto = task.toModel();
		dto.setUser(user);
		dto.setCreatedAt(new Date());
		taskService.save(dto);
		return new SharedIdResponse(dto.getKey());
	}

}
