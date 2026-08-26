package d3.task;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.application.SharedAuthenticateService;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.task.application.TaskCreateService;
import d3.task.application.TaskDeleteService;
import d3.task.application.TaskGetByUserService;
import d3.task.application.TaskUpdateService;
import d3.task.application.base.TaskService;
import d3.task.domain.TaskDTO;
import d3.task.domain.TaskRequest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/task")
public class TaskRest {

	private final SharedAuthenticateService tokenService;
	private final TaskGetByUserService taskGetByUserService;
	private final TaskCreateService taskCreateService;
	private final TaskUpdateService taskUpdateService;
	private final TaskDeleteService taskDeleteService;
	private final TaskService taskService;

	public TaskRest(@Lazy SharedAuthenticateService tokenService, @Lazy TaskGetByUserService taskGetByUserService,
			@Lazy TaskCreateService taskCreateService, @Lazy TaskUpdateService taskUpdateService,
			@Lazy TaskDeleteService taskDeleteService, @Lazy TaskService taskService) {
		this.tokenService = tokenService;
		this.taskGetByUserService = taskGetByUserService;
		this.taskCreateService = taskCreateService;
		this.taskUpdateService = taskUpdateService;
		this.taskDeleteService = taskDeleteService;
		this.taskService = taskService;
	}

	@GetMapping(value = "/")
	public List<TaskDTO> getFromUser(HttpServletRequest request, @RequestHeader("Authorization") String token)
			throws ServerException {
		return taskGetByUserService.call(tokenService.getUser(token, request));
	}

	@GetMapping(value = "/{id}")
	public TaskDTO getById(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@RequestParam String id) throws ServerException {
		tokenService.getUser(token, request);
		return taskService.getById(id);
	}

	@PostMapping(value = "/create")
	public SharedIdResponse save(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@RequestBody TaskRequest task) throws ServerException {
		return taskCreateService.call(task, tokenService.getUser(token, request));
	}

	@PostMapping(value = "/update")
	public SharedIdResponse update(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@RequestBody TaskRequest task) throws ServerException {
		return taskUpdateService.call(task, tokenService.getUser(token, request));
	}

	@PostMapping(value = "/delete/{id}")
	public SharedIdResponse delete(HttpServletRequest request, @RequestHeader("Authorization") String token,
			@PathVariable(name = "id") String pId) throws ServerException {
		return taskDeleteService.call(pId, tokenService.getUser(token, request));
	}
}
