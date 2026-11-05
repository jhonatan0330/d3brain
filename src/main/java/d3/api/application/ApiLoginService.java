package d3.api.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.api.domain.LoginRequest;
import d3.authentication.application.UsuarioAutenticacionSvc;
import d3.authentication.application.UsuarioSesionSvc;
import d3.authentication.domain.UsuarioAutenticacionDTO;
import d3.authentication.domain.UsuarioAutenticacionFilterDTO;
import d3.shared.application.D3Utils;
import d3.shared.application.SessionContext;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ApiLoginService {

	private final UsuarioAutenticacionSvc authenticationService;
	private final UsuarioSesionSvc sesionService;

	public ApiLoginService(@Lazy UsuarioAutenticacionSvc authenticationService, @Lazy UsuarioSesionSvc sesionService) {
		this.authenticationService = authenticationService;
		this.sesionService = sesionService;
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
		UsuarioAutenticacionDTO auth = authenticationService.autenticar(user, true, D3Utils.getRequestUrl(request));
		SessionContext.setCurrent(sesionService.getUserToken(auth.getToken()));
		return new SharedIdResponse(auth.getLlaveTabla());
	}

}
