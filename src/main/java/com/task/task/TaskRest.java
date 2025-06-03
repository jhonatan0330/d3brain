package com.task.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shared.application.SharedAuthenticateService;
import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.task.task.application.TaskCreateService;
import com.task.task.application.TaskDeleteService;
import com.task.task.application.TaskGetByUserService;
import com.task.task.application.TaskUpdateService;
import com.task.task.application.base.TaskService;
import com.task.task.domain.TaskDTO;
import com.task.task.domain.TaskRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/task")
public class TaskRest {

	@Autowired @Lazy  private SharedAuthenticateService tokenService;
	
	@Autowired @Lazy  private TaskGetByUserService taskGetByUserService;
	@Autowired @Lazy  private TaskCreateService taskCreateService;
	@Autowired @Lazy  private TaskUpdateService taskUpdateService;
	@Autowired @Lazy  private TaskDeleteService taskDeleteService;
	
	@Autowired @Lazy  private TaskService taskService;
	
	@GetMapping(value="/")
	public List<TaskDTO> getFromUser(HttpServletRequest request, @RequestHeader("Authorization") String token)  throws ServerException  {
		return taskGetByUserService.call(tokenService.getUser(token, request));
	}
	
	@GetMapping(value="/{id}")
	public TaskDTO getById(HttpServletRequest request, @RequestHeader("Authorization") String token, @RequestParam String id)  throws ServerException  {
		tokenService.getUser(token, request);
		return taskService.getById(id);
	}
	
	@PostMapping(value="/create")
	public SharedIdResponse save(HttpServletRequest request, @RequestHeader("Authorization") String token, @RequestBody TaskRequest task)  throws ServerException  {
		return taskCreateService.call(task, tokenService.getUser(token, request));
	}
	
	@PostMapping(value="/update")
	public SharedIdResponse update(HttpServletRequest request, @RequestHeader("Authorization") String token, @RequestBody TaskRequest task)  throws ServerException  {
		return taskUpdateService.call(task, tokenService.getUser(token, request));
	}

	@PostMapping(value="/delete/{id}")
	public SharedIdResponse delete(HttpServletRequest request, @RequestHeader("Authorization") String token, @PathVariable String id)  throws ServerException  {
		return taskDeleteService.call(id, tokenService.getUser(token, request));
	}
}
