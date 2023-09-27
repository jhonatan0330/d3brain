package com.softure.process_designer.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.ServerException;
import com.softure.process_designer.application.ProcessCopy;
import com.softure.shared.domain.SharedIdResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/process_designer")
public class ProcessDesignerController {
	
	@Autowired
	private ProcessCopy copyService;
	
	@PostMapping(value="/copy")
	public SharedIdResponse copy(@RequestHeader("Authorization") String token, @RequestParam String processId) throws ServerException {
		return copyService.call(processId, token);
	}
	
}
