package com.softure.api.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.SharedIdResponse;
import com.softure.api.domain.LoginRequest;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.java.dto.exception.ServerException;

@Service
public class ApiLoginService {

	@Autowired UsuarioAutenticacionSvc authenticationService;
	
	public SharedIdResponse call(LoginRequest login) throws ServerException {
		if(login == null) throw new ServerException("No se enviaron datos");
		if(login.getUser() == null) throw new ServerException("Por favor ingrese el usuario");
		if(login.getPassword() == null) throw new ServerException("Por favor ingrese la clave");
		UsuarioAutenticacionFilterDTO user = new UsuarioAutenticacionFilterDTO();
		user.setSesion(login.getUser());
		user.setClave(login.getPassword());
		return new SharedIdResponse( authenticationService.autenticar(user, true).getToken());
	}

}
