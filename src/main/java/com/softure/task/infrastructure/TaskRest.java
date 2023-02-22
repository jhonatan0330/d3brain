package com.softure.task.infrastructure;

import java.util.List;

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

import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.task.application.TaskCreateService;
import com.softure.task.application.TaskDeleteService;
import com.softure.task.application.TaskGetByIdService;
import com.softure.task.application.TaskGetByUserService;
import com.softure.task.application.TaskUpdateService;
import com.softure.task.domain.TaskRequest;
import com.softure.task.domain.TaskResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/task")
public class TaskRest {

	@Autowired private TaskGetByUserService taskGetByUserService;
	@Autowired private TaskGetByIdService taskGetByIdService;
	@Autowired private TaskCreateService taskCreateService;
	@Autowired private TaskUpdateService taskUpdateService;
	@Autowired private TaskDeleteService taskDeleteService;
	
	@GetMapping(value="/")
	public List<TaskResponse> getFromUser(@RequestHeader("Authorization") String token)  throws ServerException  {
		return taskGetByUserService.call(token);
	}
	
	@GetMapping(value="/{id}")
	public TaskResponse getById(@RequestHeader("Authorization") String token, @RequestParam String id)  throws ServerException  {
		return taskGetByIdService.call(id);
	}
	
	@PostMapping(value="/create")
	public IdResponse save(@RequestHeader("Authorization") String token, @RequestBody TaskRequest task)  throws ServerException  {
		return taskCreateService.call(task, token);
	}
	
	/*@PostMapping(value="/order")
	public IdResponse order(@RequestHeader("Authorization") String token, @RequestBody TaskRequest task)  throws ServerException  {
		return taskOrderService.call(tasks, token);
	}*/
	
	@PostMapping(value="/update")
	public IdResponse update(@RequestHeader("Authorization") String token, @RequestBody TaskRequest task)  throws ServerException  {
		return taskUpdateService.call(task, token);
	}

	@PostMapping(value="/delete/{id}")
	public IdResponse delete(@RequestHeader("Authorization") String token, @PathVariable("id") String id)  throws ServerException  {
		return taskDeleteService.call(id, token);
	}
}
