package com.softure.api.application;

import org.springframework.stereotype.Service;

import com.softure.api.domain.LoginVO;
import com.softure.java.domain.IdResponse;
import com.softure.java.dto.exception.ServerException;

@Service
public class ApiLoginService {

	public IdResponse call(LoginVO login) throws ServerException {
		return null;
	}

}
