package d3.shared.application;

import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.authentication.application.UsuarioSesionErrorSvc;
import d3.authentication.application.UsuarioSesionSvc;
import d3.authentication.domain.UsuarioSesionErrorDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedToken;
import jakarta.servlet.http.HttpServletRequest;

@Service("sharedTokenService")
public class SharedTokenService implements SharedAuthenticateService {

	private final UsuarioSesionSvc usuarioSesionService;
	private final UsuarioSesionErrorSvc errorService;

	public SharedTokenService(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy UsuarioSesionErrorSvc errorService) {
		this.usuarioSesionService = usuarioSesionService;
		this.errorService = errorService;
	}

	@Override
	public SharedToken validate(String token, HttpServletRequest request) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		if (request == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		try {
			return usuarioSesionService.getUserToken(token);
		} catch (ServerException e) {
			reportarError(token, HttpUtils.getRequestIP(request), e.getMessage());
			throw e;
		}
	}

	@Override
	public String getUser(String token, HttpServletRequest request) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		if (request == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		try {
			return usuarioSesionService.getUserToken(token).getUser();
		} catch (ServerException e) {
			reportarError(token, HttpUtils.getRequestIP(request), e.getMessage());
			throw e;
		}
	}

	public SharedToken getToken(String token) throws ServerException {
		return usuarioSesionService.getUserToken(token);
	}

	private void reportarError(String token, String ip, String error) throws ServerException {
		UsuarioSesionErrorDTO use = new UsuarioSesionErrorDTO();
		use.setIp(ip);
		use.setFecha(new Date());
		use.setSesion(token);
		use.setError(error);
		errorService.saveSimple(use);
	}
}