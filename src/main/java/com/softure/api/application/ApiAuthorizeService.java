package com.softure.api.application;

import org.springframework.stereotype.Service;

import com.softure.java.dto.exception.ServerException;

@Service
public class ApiAuthorizeService {

	public void call(String apiKey) throws ServerException {
		return;
	}
	
	public void call(String apiKey, String token) throws ServerException {
		call(apiKey);
		return;
	}
}
