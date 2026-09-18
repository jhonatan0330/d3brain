package d3.authentication;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.authentication.application.OrganizacionSvc;
import d3.authentication.application.UsuarioAutenticacionSvc;
import d3.authentication.application.UsuarioSesionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import d3.authentication.domain.UsuarioAutenticacionDTO;
import d3.authentication.domain.UsuarioAutenticacionFilterDTO;
import d3.authentication.domain.UsuarioSesionDTO;
import d3.authorization.application.RolAccesoSvc;
import d3.authorization.domain.RolAccesoDTO;
import d3.authorization.domain.RolAccesoFilterDTO;
import d3.shared.application.D3Utils;
import d3.shared.application.HttpUtils;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.users.domain.UsuarioDTO;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/authentication")
public class AuthenticationController {

	private final OrganizacionSvc organizationSvc;
	private final UsuarioAutenticacionSvc usuarioAutenticacionService;
	private final RolAccesoSvc rolAccesoService;
	private final UsuarioSesionSvc usuarioSessionService;

	public AuthenticationController(@Lazy OrganizacionSvc organizationSvc,
			@Lazy UsuarioAutenticacionSvc usuarioAutenticacionService, @Lazy RolAccesoSvc rolAccesoService,
			@Lazy UsuarioSesionSvc usuarioSessionService) {
		this.organizationSvc = organizationSvc;
		this.usuarioAutenticacionService = usuarioAutenticacionService;
		this.rolAccesoService = rolAccesoService;
		this.usuarioSessionService = usuarioSessionService;
	}

	@GetMapping(value = "/obtenerPrincipalOrganizacion")
	public OrganizacionDTO obtenerPrincipalOrganizacion() throws ServerException {
		// Este metodo se usa para obtener los datos de la organizacion pero despues se
		// vuelve a utilizar para obtener las propiedades
		return organizationSvc.obtenerPrincipalPublic();
	}

	@PostMapping("/dfa")
	public void validateDFA(HttpServletRequest request, @RequestBody UsuarioAutenticacionDTO pAuth)
			throws ServerException {
		usuarioAutenticacionService.dobleFactorAutenticacion(pAuth.getUsuario(), pAuth.getToken(),
				HttpUtils.getRequestIP(request));
	}

	@GetMapping(value = "/getRole")
	public List<RolAccesoDTO> getRole() throws ServerException {
		RolAccesoFilterDTO _filter = new RolAccesoFilterDTO();
		_filter.setEstado(SharedConstants.STATE_ACTIVE);
		return rolAccesoService.listarConsulta(_filter);
	}

	@GetMapping(value = "/roles/{userId}")
	public List<RolAccesoDTO> consultaUsuarioDocumentoRolAcceso(@PathVariable(name = "userId") String pUserId)
			throws ServerException {
		return rolAccesoService.consultaUsuarioDocumento(pUserId);
	}
	

	@PostMapping(value = "/autenticarUsuarioAutenticacion")
	public UsuarioAutenticacionDTO autenticarUsuarioAutenticacion(HttpServletRequest request,
			@RequestBody UsuarioAutenticacionFilterDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		return usuarioAutenticacionService.autenticar(filter, (filter.getClaveAnterior() == null),
				D3Utils.getRequestUrl(request));
	}

	@PostMapping(value = "/checkToken")
	public UsuarioAutenticacionDTO checkToken(HttpServletRequest request) throws ServerException {
		return usuarioAutenticacionService.checkToken(HttpUtils.getRequestIP(request));
	}
	
	@GetMapping(value = "/checkSession")
	public UsuarioSesionDTO checkToken() throws ServerException {
		return usuarioSessionService.checkToken();
	}

	@PostMapping(value = "/cambiarClave")
	public UsuarioAutenticacionDTO cambiarClave(HttpServletRequest request,
			@RequestHeader(name = "Authorization", required = false) String token,
			@RequestBody UsuarioAutenticacionDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		return usuarioAutenticacionService.cambiarClave(filter, token);
	}

	@PostMapping(value = "/solicitarNuevaClave")
	public UsuarioAutenticacionAutorizacionDTO solicitarNuevaClave(HttpServletRequest request,
			@RequestBody UsuarioAutenticacionDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		return usuarioAutenticacionService.solicitarNuevaClave(filter, D3Utils.getRequestUrl(request));
	}
	
	@PostMapping(value = "/logOut")
	public UsuarioDTO logOut(@RequestBody UsuarioAutenticacionDTO autenticacion) throws ServerException {
		if (autenticacion == null)
			throw new ServerException("Los datos de autenticacion son nulos");
		usuarioAutenticacionService.inactivar(autenticacion);
		return null;
	}


}
