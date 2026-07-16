package com.softure.api.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.api.domain.LoginRequest;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.java.services.SoftureUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ApiLoginService {

	private final UsuarioAutenticacionSvc authenticationService;

	public ApiLoginService(@Lazy UsuarioAutenticacionSvc authenticationService) {
		this.authenticationService = authenticationService;
	}

	public SharedIdResponse call(LoginRequest login, HttpServletRequest request) throws ServerException {
		if (login == null)
			throw new ServerException("No se enviaron datos");
		if (login.getUser() == null)
			throw new ServerException("Por favor ingrese el usuario");
		if (login.getPassword() == null)
			throw new ServerException("Por favor ingrese la clave");
		UsuarioAutenticacionFilterDTO user = new UsuarioAutenticacionFilterDTO();
		user.setSesion(login.getUser());
		user.setClave(login.getPassword());
		return new SharedIdResponse(
				authenticationService.autenticar(user, true, SoftureUtil.getRequestUrl(request)).getToken());
	}

}
