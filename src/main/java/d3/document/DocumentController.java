package d3.document;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import d3.document.application.CallDocumentCRUD;
import d3.document.application.CallDocumentListWithFilters;
import d3.document.application.PedidoVentaAjusteSvc;
import d3.document.application.PedidoVentaSvc;
import d3.document.application.field.CampoAdaptador;
import d3.document.domain.PedidoVentaAjusteDTO;
import d3.document.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.document.domain.PedidoVentaFilterDTO;
import d3.inventory.application.ProductoInventarioSvc;
import d3.inventory.domain.ProductoInventarioDTO;
import d3.mail.application.MailReleaseMessageQueueService;
import d3.notification.application.ActividadSvc;
import d3.notification.domain.ActividadDTO;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.application.ProcesoTransicionAutomaticaSvc;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.DocumentoPlantillaFilterDTO;
import d3.shared.application.D3Utils;
import d3.shared.application.HttpUtils;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedApiErrorResponse;
import d3.shared.domain.SharedIdResponse;
import d3.upload.application.UploadSvc;
import d3.users.application.UsuarioSvc;
import d3.users.domain.UsuarioDTO;
import d3.users.domain.UsuarioFilterDTO;
import d3.webservice.application.WebServiceEjecucionSvc;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/document")
public class DocumentController {

	private final PedidoVentaSvc pedidoVentaService;
	private final UploadSvc uploadService;
	private final CallDocumentCRUD saveUpdateDocumentFunction;
	private final CallDocumentListWithFilters listDocumentWithFiltersFunction;
	private final ActividadSvc actividadService;
	private final ProductoInventarioSvc inventoryService;
	private final MailReleaseMessageQueueService releaseQueueService;
	private final ProcesoTransicionAutomaticaSvc transicionservice;
	private final WebServiceEjecucionSvc apiService;
	private final DocumentoPlantillaSvc plantillaService;
	private final UsuarioAutenticacionSvc usuarioAutenticacionService;
	private final UsuarioSesionSvc usuarioSessionService;
	private final OrganizacionSvc organizacionService;
	private final UsuarioOrganizacionSvc organizacionUsuarioService;
	private final UsuarioSvc usuarioService;
	private final PedidoVentaAjusteSvc pedidoVentaAjusteService;
	private final CampoAdaptador adaptador;

	public DocumentController(
			@Lazy PedidoVentaSvc pedidoVentaService,
			@Lazy UploadSvc uploadService,
			@Lazy CallDocumentCRUD saveUpdateDocumentFunction,
			@Lazy CallDocumentListWithFilters listDocumentWithFiltersFunction,
			@Lazy ActividadSvc actividadService,
			@Lazy ProductoInventarioSvc inventoryService,
			@Lazy MailReleaseMessageQueueService releaseQueueService,
			@Lazy ProcesoTransicionAutomaticaSvc transicionservice,
			@Lazy WebServiceEjecucionSvc apiService,
			@Lazy DocumentoPlantillaSvc plantillaService,
			@Lazy UsuarioAutenticacionSvc usuarioAutenticacionService,
			@Lazy UsuarioSesionSvc usuarioSessionService,
			@Lazy OrganizacionSvc organizacionService,
			@Lazy UsuarioOrganizacionSvc organizacionUsuarioService,
			@Lazy UsuarioSvc usuarioService,
			@Lazy PedidoVentaAjusteSvc pedidoVentaAjusteService,
			@Lazy CampoAdaptador adaptador) {
		this.pedidoVentaService = pedidoVentaService;
		this.uploadService = uploadService;
		this.saveUpdateDocumentFunction = saveUpdateDocumentFunction;
		this.listDocumentWithFiltersFunction = listDocumentWithFiltersFunction;
		this.actividadService = actividadService;
		this.inventoryService = inventoryService;
		this.releaseQueueService = releaseQueueService;
		this.transicionservice = transicionservice;
		this.apiService = apiService;
		this.plantillaService = plantillaService;
		this.usuarioAutenticacionService = usuarioAutenticacionService;
		this.usuarioSessionService = usuarioSessionService;
		this.organizacionService = organizacionService;
		this.organizacionUsuarioService = organizacionUsuarioService;
		this.usuarioService = usuarioService;
		this.pedidoVentaAjusteService = pedidoVentaAjusteService;
		this.adaptador = adaptador;
	}

	@GetMapping(value = "/test")
	public String test() {
		return "OK";
	}

	@GetMapping(value = "/ping_mail")
	public String sendMail() throws ServerException {
		return "******* CORREOS (" + releaseQueueService.call() + ") ***" + new Date().toString();
	}

	@GetMapping(value = "/ping_task")
	public String sendTemporizer() throws ServerException {
		int _launch = transicionservice.lanzarTransaccionesTemporizadas();
		int _prepare = transicionservice.programateAll();
		return "*******TAREAS (" + _launch + ") ***  PROGRAMADAS (" + _prepare + ") ***" + new Date().toString();
	}

	@GetMapping(value = "/ping_api")
	public String sendApi() throws ServerException {
		return apiService.apiToTransaction();
	}

	@PostMapping(value = "/getDocument")
	public PedidoVentaDTO consultarDocumento(@RequestBody PedidoVentaFilterDTO filter, String token)
			throws ServerException {
		return pedidoVentaService.consultaCompleta(filter.getLlaveTabla(), token);
	}

	@PostMapping(value = "/getDocuments")
	public List<PedidoVentaDTO> listarDocumentos(@RequestBody PedidoVentaFilterDTO filter,
			@RequestHeader("Authorization") String token) throws ServerException {
		filter.setSecurityToken(token);
		return listDocumentWithFiltersFunction.listarAvanzado(filter);
	}

	@PostMapping(value = "/saveDocument")
	public PedidoVentaDTO guardarDocumento(@RequestBody PedidoVentaDTO document, @RequestBody String token)
			throws ServerException {
		PedidoVentaDTO result = new PedidoVentaDTO();
		if (document.getLlaveTabla() == null) {
			document = saveUpdateDocumentFunction.save(document, token, null);
		} else {
			document = saveUpdateDocumentFunction.update(document, null, token);
		}
		result.setNombre(document.getNombre());
		result.setPlantilla(document.getPlantilla());
		result.setLlaveTabla(document.getLlaveTabla());
		result.setDescripcion(document.getDescripcion());
		result.setEstadoExpediente(document.getEstadoExpediente());
		result.setEstadoNombre(document.getEstadoNombre());
		return result;
	}

	@PostMapping(value = "/upload")
	public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile pFile,
			@RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
		if (pFile.isEmpty())
			throw new ServerException("You failed to upload because the file was empty.");
		try {
			return uploadService.uploadFile(pFile.getBytes(), pFile.getOriginalFilename(), token, null, "public");
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	@PostMapping(value = "/readActivity")
	public ActividadDTO readActivity(@RequestBody ActividadDTO activity, @RequestHeader("Authorization") String token)
			throws ServerException {
		return actividadService.readActivity(activity.getLlaveTabla(), token);
	}

	@GetMapping(value = "/getInventory/{id}")
	public List<ProductoInventarioDTO> getInventory(@PathVariable("id") String pId,
			@RequestHeader("Authorization") String token) throws ServerException {
		return inventoryService.getByProducto(pId);
	}

	// ==================== MAIN ENDPOINTS (antes /main/*) ====================

	@GetMapping(value = "/main/obtenerPrincipalOrganizacion")
	public OrganizacionDTO obtenerPrincipalOrganizacion(HttpServletRequest request) throws ServerException {
		return organizacionService.obtenerPrincipalPublic(HttpUtils.getRequestIP(request));
	}

	@PostMapping(value = "/main/autenticarUsuarioAutenticacion")
	public UsuarioAutenticacionDTO autenticarUsuarioAutenticacion(HttpServletRequest request,
			@RequestBody UsuarioAutenticacionFilterDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		return usuarioAutenticacionService.autenticar(filter, (filter.getClaveAnterior() == null),
				D3Utils.getRequestUrl(request));
	}

	@PostMapping(value = "/main/checkToken")
	public UsuarioAutenticacionDTO checkToken(HttpServletRequest request,
			@RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
		return usuarioAutenticacionService.checkToken(token, HttpUtils.getRequestIP(request));
	}

	@PostMapping(value = "/main/cambiarClave")
	public UsuarioAutenticacionDTO cambiarClave(HttpServletRequest request,
			@RequestHeader(name = "Authorization", required = false) String token,
			@RequestBody UsuarioAutenticacionDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		return usuarioAutenticacionService.cambiarClave(filter, token);
	}

	@PostMapping(value = "/main/solicitarNuevaClave")
	public UsuarioAutenticacionAutorizacionDTO solicitarNuevaClave(HttpServletRequest request,
			@RequestBody UsuarioAutenticacionDTO filter) throws ServerException {
		filter.setIp(HttpUtils.getRequestIP(request));
		return usuarioAutenticacionService.solicitarNuevaClave(filter, D3Utils.getRequestUrl(request));
	}

	@PostMapping(value = "/main/cambiarClaveOtherSystem")
	public UsuarioOrganizacionDTO cambiarClaveOtherSystem(@RequestHeader("Authorization") String token,
			@RequestBody UsuarioOrganizacionDTO dto) throws ServerException {
		return organizacionUsuarioService.reloadPassword(dto, token);
	}

	@GetMapping(value = "/main/checkSession")
	public UsuarioSesionDTO checkToken(@RequestHeader("Authorization") String token) throws ServerException {
		return usuarioSessionService.checkToken(token);
	}

	@PostMapping(value = "/main/consultaUsuarioDocumentoPlantilla")
	public List<DocumentoPlantillaDTO> consultaUsuarioDocumentoPlantilla(
			@RequestBody DocumentoPlantillaFilterDTO filter) throws ServerException {
		return plantillaService.consultaUsuario(filter);
	}

	@PostMapping(value = "/main/listarUsuarioPedidoVenta")
	public List<PedidoVentaDTO> listarUsuarioPedidoVenta(@RequestBody PedidoVentaFilterDTO dto) throws ServerException {
		return listDocumentWithFiltersFunction.listarUsuario(dto);
	}

	// ==================== API ENDPOINTS (antes /rest/*) ====================

	@PostMapping(value = "/api/logOut")
	public UsuarioDTO logOut(@RequestBody UsuarioAutenticacionDTO autenticacion,
			@RequestHeader("Authorization") String token) throws ServerException {
		if (autenticacion == null)
			throw new ServerException("Los datos de autenticacion son nulos");
		usuarioAutenticacionService.inactivar(autenticacion, token);
		return null;
	}

	@PostMapping(value = "/api/consultarDocumento")
	public PedidoVentaDTO apiConsultarDocumento(@RequestBody PedidoVentaFilterDTO documentoFiltro,
			@RequestHeader("Authorization") String token) throws ServerException {
		documentoFiltro.setSecurityToken(token);
		PedidoVentaDTO _result = pedidoVentaService.consultaCompleta(documentoFiltro.getLlaveTabla(), token);
		pedidoVentaService.clearPedidoResponse(_result);
		return _result;
	}

	@PostMapping(value = "/api/validateBeforeNew")
	public PedidoVentaDTO validateBeforeNew(@RequestBody PedidoVentaFilterDTO documentoFiltro,
			@RequestHeader("Authorization") String token) throws ServerException {
		documentoFiltro.setSecurityToken(token);
		return pedidoVentaService.validateBeforeNew(documentoFiltro);
	}

	@PostMapping(value = "/api/guardarDocumento")
	public PedidoVentaDTO apiGuardarDocumento(@RequestBody PedidoVentaDTO documento,
			@RequestHeader("Authorization") String token,
			@RequestHeader(name = "non-duplicate", required = false) String session) throws ServerException {
		if (documento.getLlaveTabla() == null) {
			documento = saveUpdateDocumentFunction.save(documento, token, session);
		} else {
			documento = saveUpdateDocumentFunction.update(documento, null, token);
		}
		PedidoVentaDTO result = new PedidoVentaDTO();
		result.setNombre(documento.getNombre());
		result.setPlantilla(documento.getPlantilla());
		result.setLlaveTabla(documento.getLlaveTabla());
		result.setEstadoExpediente(documento.getEstadoExpediente());
		result.setEstadoNombre(documento.getEstadoNombre());
		result.setDescripcion(documento.getDescripcion());
		result.setMessages(documento.getMessages());
		return result;
	}

	@PostMapping(value = "/api/saveByMassive")
	public PedidoVentaDTO saveByMassive(@RequestBody PedidoVentaDTO documento,
			@RequestHeader("Authorization") String token,
			@RequestHeader(name = "non-duplicate", required = false) String session) throws ServerException {
		documento = saveUpdateDocumentFunction.massive(documento, token, session);
		PedidoVentaDTO result = new PedidoVentaDTO();
		result.setNombre(documento.getNombre());
		result.setPlantilla(documento.getPlantilla());
		result.setLlaveTabla(documento.getLlaveTabla());
		result.setEstadoExpediente(documento.getEstadoExpediente());
		result.setEstadoNombre(documento.getEstadoNombre());
		result.setDescripcion(documento.getDescripcion());
		result.setMessages(documento.getMessages());
		return result;
	}

	@PostMapping(value = "/api/consultarUsuario")
	public UsuarioDTO consultarUsuario(@RequestBody UsuarioFilterDTO dto, @RequestHeader("Authorization") String token)
			throws ServerException {
		dto.setSecurityToken(token);
		return usuarioService.consultaUnica(dto);
	}

	@PostMapping(value = "/api/consultarDatosBase")
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(@RequestBody PedidoVentaCaracteristicaFilterDTO dto,
			@RequestHeader("Authorization") String token) throws ServerException {
		dto.setSecurityToken(token);
		return adaptador.consultarDatosBase(dto);
	}

	@PostMapping(value = "/api/listarDocumentos")
	public List<PedidoVentaDTO> apiListarDocumentos(@RequestBody PedidoVentaFilterDTO documentoFiltro,
			@RequestHeader("Authorization") String token) throws ServerException {
		documentoFiltro.setSecurityToken(token);
		return listDocumentWithFiltersFunction.listarAvanzado(documentoFiltro);
	}

	@PostMapping(value = "/api/obtenerCampos")
	public DocumentoPlantillaDTO obtenerCampos(@RequestBody DocumentoPlantillaDTO documentoFiltro,
			@RequestHeader("Authorization") String token) throws ServerException {
		return plantillaService.obtenerCampos(documentoFiltro, token, true);
	}

	@PostMapping(value = "/api/upload")
	public SharedApiErrorResponse handleFileUploadApi(@RequestParam("file") MultipartFile pFile,
			@RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
		if (pFile.isEmpty())
			throw new ServerException("You failed to upload because the file was empty.");
		try {
			String url = uploadService.uploadFile(pFile.getBytes(), pFile.getOriginalFilename(), token, null, "public");
			SharedApiErrorResponse response = new SharedApiErrorResponse.ApiErrorResponseBuilder().withMessage(url)
					.build();
			return response;
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	@PostMapping(value = "/api/uploadResponseString")
	public String handleFileUploadFlex(@RequestParam("file") MultipartFile pFile) throws ServerException {
		if (pFile.isEmpty())
			throw new ServerException("You failed to upload because the file was empty.");
		try {
			return uploadService.uploadFile(pFile.getBytes(), pFile.getOriginalFilename(), null, "config", "public");
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	@PostMapping(value = "/api/changePicture")
	public UsuarioDTO cambiarImagen(@RequestParam("file") MultipartFile pFile,
			@RequestHeader("Authorization") String token) throws ServerException {
		if (pFile.isEmpty())
			throw new ServerException("You failed to upload because the file was empty.");
		try {
			String url = uploadService.uploadFile(pFile.getBytes(), pFile.getOriginalFilename(), token, "config",
					"public");
			return usuarioService.changePicture(url, token);
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	@PostMapping(value = "/api/changeState")
	public PedidoVentaAjusteDTO changeState(@RequestBody PedidoVentaAjusteDTO ajuste,
			@RequestHeader("Authorization") String token) throws ServerException {
		return pedidoVentaAjusteService.guardar(ajuste, token);
	}

	@GetMapping(value = "/api/getMessageToProcessField/{property}/{fieldValue}")
	public SharedIdResponse message(@PathVariable(name = "property") String pProperty,
			@PathVariable(name = "fieldValue") String pFieldValue, @RequestHeader("Authorization") String token)
			throws ServerException {
		return new SharedIdResponse(null, null, null,
				pedidoVentaService.getMessageToProcessField(pProperty, pFieldValue, token));
	}

}
