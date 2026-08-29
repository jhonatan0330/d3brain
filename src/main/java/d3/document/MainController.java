package d3.document;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.domain.ServerException;
import d3.authentication.application.OrganizacionSvc;
import d3.authentication.application.UsuarioAutenticacionSvc;
import d3.authentication.application.UsuarioOrganizacionSvc;
import d3.authentication.application.UsuarioSesionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import d3.authentication.domain.UsuarioAutenticacionDTO;
import d3.authentication.domain.UsuarioAutenticacionFilterDTO;
import d3.authentication.domain.UsuarioOrganizacionDTO;
import d3.authentication.domain.UsuarioSesionDTO;
import d3.document.application.CallDocumentListWithFilters;
import d3.document.domain.PedidoVentaDTO;
import d3.document.domain.PedidoVentaFilterDTO;
import d3.shared.application.HttpUtils;
import d3.shared.application.D3Utils;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.DocumentoPlantillaFilterDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/main")
public class MainController {

	private final DocumentoPlantillaSvc plantillaService;
	private final UsuarioAutenticacionSvc usuarioAutenticacionService;
	private final UsuarioSesionSvc usuarioSessionService;
	private final CallDocumentListWithFilters listDocumentWithFiltersFunction;
	private final OrganizacionSvc organizacionService;
	private final UsuarioOrganizacionSvc organizacionUsuarioService;

	public MainController(@Lazy DocumentoPlantillaSvc plantillaService,
			@Lazy UsuarioAutenticacionSvc usuarioAutenticacionService, @Lazy UsuarioSesionSvc usuarioSessionService,
			@Lazy CallDocumentListWithFilters listDocumentWithFiltersFunction,
			@Lazy OrganizacionSvc organizacionService, @Lazy UsuarioOrganizacionSvc organizacionUsuarioService) {
		this.plantillaService = plantillaService;
		this.usuarioAutenticacionService = usuarioAutenticacionService;
		this.usuarioSessionService = usuarioSessionService;
		this.listDocumentWithFiltersFunction = listDocumentWithFiltersFunction;
		this.organizacionService = organizacionService;
		this.organizacionUsuarioService = organizacionUsuarioService;
	}

	@GetMapping(value = "/test")
	public String test() {
		return "OK";
	}

	@GetMapping(value = "/obtenerPrincipalOrganizacion")
	public OrganizacionDTO obtenerPrincipalOrganizacion(HttpServletRequest request) throws ServerException {
		// Este metodo se usa para obtener los datos de la organizacion pero despues se
		// vuelve a utilizar para obtener las propiedades
		return organizacionService.obtenerPrincipalPublic(HttpUtils.getRequestIP(request));
	}

	@PostMapping(value = "/autenticarUsuarioAutenticacion")
	public UsuarioAutenticacionDTO autenticarUsuarioAutenticacion(HttpServletRequest request,
			@RequestBody UsuarioAutenticacionFilterDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		return usuarioAutenticacionService.autenticar(filter, (filter.getClaveAnterior() == null),
				D3Utils.getRequestUrl(request));
	}

	@PostMapping(value = "/checkToken")
	public UsuarioAutenticacionDTO checkToken(HttpServletRequest request,
			@RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
		return usuarioAutenticacionService.checkToken(token, HttpUtils.getRequestIP(request));
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

	@PostMapping(value = "/cambiarClaveOtherSystem")
	public UsuarioOrganizacionDTO cambiarClaveOtherSystem(@RequestHeader("Authorization") String token,
			@RequestBody UsuarioOrganizacionDTO dto) throws ServerException {
		return organizacionUsuarioService.reloadPassword(dto, token);
	}

	@GetMapping(value = "/checkToken")
	public UsuarioSesionDTO checkToken(@RequestHeader("Authorization") String token) throws ServerException {
		return usuarioSessionService.checkToken(token);
	}

	@PostMapping(value = "/consultaUsuarioDocumentoPlantilla")
	public List<DocumentoPlantillaDTO> consultaUsuarioDocumentoPlantilla(
			@RequestBody DocumentoPlantillaFilterDTO filter) throws ServerException {
		return plantillaService.consultaUsuario(filter);
	}

	@PostMapping(value = "/listarUsuarioPedidoVenta")
	public List<PedidoVentaDTO> listarUsuarioPedidoVenta(@RequestBody PedidoVentaFilterDTO dto) throws ServerException {
		return listDocumentWithFiltersFunction.listarUsuario(dto);
	}

	/*
	 * @GetMapping(value="/getAdministratorTemplates") public
	 * List<DocumentoPlantillaDTO>
	 * consultaAdministrador(@RequestHeader("Authorization") String token) throws
	 * ServerException { DocumentoPlantillaFilterDTO filter = new
	 * DocumentoPlantillaFilterDTO(); filter.setSecurityToken(token); return
	 * plantillaService.consultaAdministrador(filter); }
	 */
}
