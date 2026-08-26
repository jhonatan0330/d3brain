package d3.logisticpymes;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.authentication.application.UsuarioAutenticacionSvc;
import d3.authentication.domain.UsuarioAutenticacionDTO;
import d3.authorization.application.RolAccesoSvc;
import d3.authorization.domain.RolAccesoDTO;
import d3.authorization.domain.RolAccesoFilterDTO;
import d3.java.services.HttpUtils;
import d3.logisticpymes.application.UsuarioSvc;
import d3.logisticpymes.domain.UsuarioDTO;
import d3.logisticpymes.domain.UsuarioFilterDTO;
import d3.property.application.PropertyGetWithCacheService;
import d3.property.domain.PropiedadDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;

@RestController
@RequestMapping("/user")
public class UserController {

	private final RolAccesoSvc roleService;
	private final UsuarioSvc userService;
	private final UsuarioAutenticacionSvc usuarioAutenticacionService;
	private final RolAccesoSvc rolAccesoService;
	private final PropertyGetWithCacheService cacheService;

	public UserController(@Lazy RolAccesoSvc roleService, @Lazy UsuarioSvc userService,
			@Lazy UsuarioAutenticacionSvc usuarioAutenticacionService, @Lazy RolAccesoSvc rolAccesoService,
			@Lazy PropertyGetWithCacheService cacheService) {
		this.roleService = roleService;
		this.userService = userService;
		this.usuarioAutenticacionService = usuarioAutenticacionService;
		this.rolAccesoService = rolAccesoService;
		this.cacheService = cacheService;
	}

	@GetMapping(value = "/getRole")
	public List<RolAccesoDTO> getRole(@RequestHeader(name = "Authorization") String token) throws ServerException {
		RolAccesoFilterDTO _filter = new RolAccesoFilterDTO();
		_filter.setEstado(SharedConstants.STATE_ACTIVE);
		return roleService.listarConsulta(_filter);
	}

	@PostMapping(value = "/getUsers")
	public List<UsuarioDTO> getUsers(@RequestHeader(name = "Authorization") String token,
			@RequestBody UsuarioFilterDTO pFilter) throws ServerException {
		return userService.listarRol(pFilter);
	}

	@GetMapping("/{userId}")
	public UsuarioDTO getUserById(@RequestHeader(name = "Authorization") String token,
			@PathVariable(name = "userId") String pUserId) throws ServerException {
		return userService.consultaXId(pUserId);
	}

	@PostMapping("/dfa")
	public void validateDFA(HttpServletRequest request, @RequestBody UsuarioAutenticacionDTO pAuth)
			throws ServerException {
		usuarioAutenticacionService.dobleFactorAutenticacion(pAuth.getUsuario(), pAuth.getToken(),
				HttpUtils.getRequestIP(request));
	}

	@GetMapping("/document/{documentId}")
	public UsuarioDTO getUserByDocument(@RequestHeader(name = "Authorization") String token,
			@PathVariable(name = "documentId") String pDocumentId) throws ServerException {
		return userService.getUserByDocument(pDocumentId);
	}

	@PostMapping(value = "/cambiarClaveUsuarioAutenticacion")
	public UsuarioAutenticacionDTO cambiarClaveUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		return usuarioAutenticacionService.cambiarClave(dto, token);
	}

	@GetMapping(value = "/roles/{userId}")
	public List<RolAccesoDTO> consultaUsuarioDocumentoRolAcceso(@RequestHeader(name = "Authorization") String token,
			@PathVariable(name = "userId") String pUserId) throws ServerException {
		return rolAccesoService.consultaUsuarioDocumento(pUserId);
	}

	@GetMapping(value = "/properties/{userId}")
	public List<PropiedadDTO> getPropertiesToUser(@RequestHeader(name = "Authorization") String token,
			@PathVariable(name = "userId") String pUserId) throws ServerException {
		return cacheService.getToUser(pUserId);
	}

}
