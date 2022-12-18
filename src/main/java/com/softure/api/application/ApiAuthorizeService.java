package com.softure.api.application;

import org.springframework.stereotype.Service;

import com.softure.java.dto.exception.ServerException;

@Service
public class ApiAuthorizeService {

	public void call(String apiKey) throws ServerException {
		if(apiKey==null || apiKey.isEmpty()) throw new ServerException("Ingresa el codigo de la app asignado");
		if(apiKey.compareTo("123")!=0) throw new ServerException("Codigo incorrecto");
		return;
	}
	
	public void call(String apiKey, String token) throws ServerException {
		call(apiKey);
		return;
	}
}
