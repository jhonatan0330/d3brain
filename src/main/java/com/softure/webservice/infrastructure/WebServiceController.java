package com.softure.webservice.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.ServerException;
import com.softure.shared.domain.SharedIdResponse;
import com.softure.webservice.application.WebServiceCopyAPI;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/webservice")
public class WebServiceController {
	
	@Autowired
	private WebServiceCopyAPI copyService;
	
	@PostMapping(value="/copy")
	public SharedIdResponse autenticarUsuarioAutenticacion(@RequestHeader("Authorization") String token, @RequestParam String apiId) throws ServerException {
		return copyService.call(apiId, token);
	}
	
}
