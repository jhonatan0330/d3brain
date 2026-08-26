package d3.task.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.task.application.base.TaskService;
import d3.task.domain.TaskDTO;
import d3.task.domain.TaskFilterDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class TaskGetByUserService {

	private final TaskService taskService;

	public TaskGetByUserService(@Lazy TaskService taskService) {
		this.taskService = taskService;
	}

	public List<TaskDTO> call(String user) throws ServerException {
		TaskFilterDTO filter = new TaskFilterDTO();
		filter.setUser(user);
		filter.setState(SharedConstants.STATE_ACTIVE);
		return taskService.getMany(filter);
	}
}
