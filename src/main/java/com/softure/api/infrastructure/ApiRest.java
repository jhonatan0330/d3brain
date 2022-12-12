package com.softure.api.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softure.api.application.IApiSendService;
import com.softure.api.domain.ApiVO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

@RestController
@RequestMapping("api")
public class ApiRest {

	@Autowired IApiSendService apiSendService;
	
	@PostMapping("/send")
	public IdResponse sendApi(@RequestHeader(name = "Authorization") String token
			,@RequestBody ApiVO api
		) throws ServerException {
		return apiSendService.call(token, api);
	}
}
