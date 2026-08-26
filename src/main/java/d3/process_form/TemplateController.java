package d3.process_form;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.domain.ServerException;
import d3.document_execution.application.PedidoVentaCaracteristicaSvc;
import d3.document_execution.application.field.CampoAdaptador;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document_transition.application.DocumentoRelacionGestorSvc;
import d3.document_transition.domain.DocumentoRelacionGestorDTO;
import d3.document_transition.domain.DocumentoRelacionGestorFilterDTO;
import d3.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process_form.application.DocumentoPlantillaSvc;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import d3.process_form.domain.DocumentoPlantillaDTO;
import d3.process_form.domain.DocumentoPlantillaFilterDTO;
import d3.property.application.RelacionInternaSvc;
import d3.property.domain.RelacionInternaDTO;
import d3.property.domain.RelacionInternaFilterDTO;
import org.springframework.context.annotation.Lazy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/template")
public class TemplateController {

	private final CampoAdaptador adaptador;
	private final DocumentoPlantillaSvc documentoplantillaService;
	private final DocumentoPlantillaCaracteristicaSvc campoService;
	private final DocumentoRelacionGestorSvc gestionService;
	private final RelacionInternaSvc relacionesService;
	private final PedidoVentaCaracteristicaSvc fieldsService;

	public TemplateController(@Lazy CampoAdaptador adaptador, @Lazy DocumentoPlantillaSvc documentoplantillaService,
			@Lazy DocumentoPlantillaCaracteristicaSvc campoService, @Lazy DocumentoRelacionGestorSvc gestionService,
			@Lazy RelacionInternaSvc relacionesService, @Lazy PedidoVentaCaracteristicaSvc fieldsService) {
		this.adaptador = adaptador;
		this.documentoplantillaService = documentoplantillaService;
		this.campoService = campoService;
		this.gestionService = gestionService;
		this.relacionesService = relacionesService;
		this.fieldsService = fieldsService;
	}

	@GetMapping(value = "/getTemplates/{profile}")
	public List<DocumentoPlantillaDTO> consultaUsuarioDocumentoPlantilla(@RequestHeader("Authorization") String token,
			@PathVariable(name = "profile") String pProfile) throws ServerException {
		DocumentoPlantillaFilterDTO filter = new DocumentoPlantillaFilterDTO();
		filter.setSecurityToken(token);
		switch (pProfile) {
		case "ADMIN": {
			return documentoplantillaService.consultaAdministrador(filter);
		}
		case "READER": {
			return documentoplantillaService.consultaAuditor(filter);
		}
		default:
			return documentoplantillaService.consultaUsuario(filter);
		}
	}

	@GetMapping(value = "/getFields")
	public DocumentoPlantillaDTO obtenerCampos(@RequestParam String id, @RequestHeader("Authorization") String token)
			throws ServerException {
		DocumentoPlantillaDTO filterTemplate = new DocumentoPlantillaDTO();
		filterTemplate.setLlaveTabla(id);
		return documentoplantillaService.obtenerCampos(filterTemplate, token, true);
	}

	@PostMapping(value = "/getFieldData")
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(
			@RequestBody PedidoVentaCaracteristicaFilterDTO filterField, @RequestHeader("Authorization") String token)
			throws ServerException {
		filterField.setSecurityToken(token);
		return adaptador.consultarDatosBase(filterField);
	}

	@PostMapping(value = "/getTrace")
	public List<DocumentoRelacionGestorDTO> getTrace(@RequestBody DocumentoRelacionGestorFilterDTO filterField,
			@RequestHeader("Authorization") String token) throws ServerException {
		filterField.setSecurityToken(token);
		return gestionService.listarExpedientesGestionadores(filterField);
	}

	@GetMapping(value = "/getTraceFields/{documentId}/{transaction}")
	public List<PedidoVentaCaracteristicaDTO> getTraceFields(@PathVariable(name = "documentId") String pDocumentId,
			@PathVariable(name = "transaction") String pTransaction, @RequestHeader("Authorization") String token)
			throws ServerException {
		return fieldsService.listar2Gestor(pDocumentId, pTransaction);
	}

	@PostMapping(value = "/getPropertyRelations")
	public List<RelacionInternaDTO> getPropertyRelations(@RequestBody RelacionInternaFilterDTO filter,
			@RequestHeader("Authorization") String token) throws ServerException {
		filter.setSecurityToken(token);
		return relacionesService.listarConsulta(filter);
	}

	@PostMapping(value = "/validateLoad")
	public DocumentoPlantillaCaracteristicaDTO validateLoad(
			@RequestBody DocumentoPlantillaCaracteristicaFilterDTO filter, @RequestHeader("Authorization") String token)
			throws ServerException {
		filter.setSecurityToken(token);
		return campoService.listarCarga(filter);
	}

}
