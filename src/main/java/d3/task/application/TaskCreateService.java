package d3.task.application;

import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.task.application.base.TaskService;
import d3.task.domain.TaskDTO;
import d3.task.domain.TaskRequest;

@Service
public class TaskCreateService {

	private final TaskService taskService;

	public TaskCreateService(@Lazy TaskService taskService) {
		this.taskService = taskService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(TaskRequest task) throws ServerException {
		TaskDTO dto = task.toModel();
		dto.setUser(SessionContext.getCurrentUser());
		dto.setCreatedAt(new Date());
		taskService.save(dto);
		return new SharedIdResponse(dto.getKey());
	}

}
