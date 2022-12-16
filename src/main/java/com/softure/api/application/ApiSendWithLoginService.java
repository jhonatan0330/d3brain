package com.softure.api.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.api.domain.DocumentWithLoginVO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

@Service
public class ApiSendWithLoginService {

	@Autowired ApiSendService sendService;
	@Autowired ApiLoginService loginService;
	
	public IdResponse call(DocumentWithLoginVO item) throws ServerException {
		IdResponse token = loginService.call(item.getLoginVO());
		return sendService.call(token.getId(), item.getDocument());
	}

}
