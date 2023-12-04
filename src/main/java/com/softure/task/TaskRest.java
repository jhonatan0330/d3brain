package com.softure.task;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.softure.task.application.TaskCreateService;
import com.softure.task.application.TaskDeleteService;
import com.softure.task.application.TaskGetByIdService;
import com.softure.task.application.TaskGetByUserService;
import com.softure.task.application.TaskUpdateService;
import com.softure.task.domain.TaskTaskRequest;
import com.softure.task.domain.TaskTaskResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/task")
public class TaskRest {

	@Autowired private SharedAuthenticateService tokenService;
	
	@Autowired private TaskGetByUserService taskGetByUserService;
	@Autowired private TaskGetByIdService taskGetByIdService;
	@Autowired private TaskCreateService taskCreateService;
	@Autowired private TaskUpdateService taskUpdateService;
	@Autowired private TaskDeleteService taskDeleteService;
	
	@GetMapping(value="/")
	public List<TaskTaskResponse> getFromUser(HttpServletRequest request, @RequestHeader("Authorization") String token)  throws ServerException  {
		return taskGetByUserService.call(tokenService.getUser(token, request));
	}
	
	@GetMapping(value="/{id}")
	public TaskTaskResponse getById(HttpServletRequest request, @RequestHeader("Authorization") String token, @RequestParam String id)  throws ServerException  {
		tokenService.getUser(token, request);
		return taskGetByIdService.call(id);
	}
	
	@PostMapping(value="/create")
	public SharedIdResponse save(HttpServletRequest request, @RequestHeader("Authorization") String token, @RequestBody TaskTaskRequest task)  throws ServerException  {
		return taskCreateService.call(task, tokenService.getUser(token, request));
	}
	
	@PostMapping(value="/update")
	public SharedIdResponse update(HttpServletRequest request, @RequestHeader("Authorization") String token, @RequestBody TaskTaskRequest task)  throws ServerException  {
		return taskUpdateService.call(task, tokenService.getUser(token, request));
	}

	@PostMapping(value="/delete/{id}")
	public SharedIdResponse delete(HttpServletRequest request, @RequestHeader("Authorization") String token, @PathVariable("id") String id)  throws ServerException  {
		return taskDeleteService.call(id, tokenService.getUser(token, request));
	}
}
