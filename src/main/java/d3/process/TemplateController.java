package d3.process;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.configuration.application.RelacionInternaSvc;
import d3.configuration.domain.RelacionInternaDTO;
import d3.configuration.domain.RelacionInternaFilterDTO;
import d3.document.application.DocumentoRelacionGestorSvc;
import d3.document.application.PedidoVentaCaracteristicaSvc;
import d3.document.application.field.CampoAdaptador;
import d3.document.domain.DocumentoRelacionGestorDTO;
import d3.document.domain.DocumentoRelacionGestorFilterDTO;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.application.ProcessCopy;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import d3.process.domain.TemplateDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;

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
	private final ProcessCopy copyService;

	public TemplateController(@Lazy CampoAdaptador adaptador, @Lazy DocumentoPlantillaSvc documentoplantillaService,
			@Lazy DocumentoPlantillaCaracteristicaSvc campoService, @Lazy DocumentoRelacionGestorSvc gestionService,
			@Lazy RelacionInternaSvc relacionesService, @Lazy PedidoVentaCaracteristicaSvc fieldsService,
			@Lazy ProcessCopy copyService) {
		this.adaptador = adaptador;
		this.documentoplantillaService = documentoplantillaService;
		this.campoService = campoService;
		this.gestionService = gestionService;
		this.relacionesService = relacionesService;
		this.fieldsService = fieldsService;
		this.copyService = copyService;
	}

	@GetMapping(value = "/getTemplates/{profile}")
	public List<TemplateDTO> consultaUsuarioDocumentoPlantilla(
			@PathVariable(name = "profile") String pProfile) throws ServerException {
		switch (pProfile) {
		case "ADMIN": {
			return documentoplantillaService.consultaAdministrador();
		}
		case "READER": {
			return documentoplantillaService.consultaAuditor();
		}
		default:
			return documentoplantillaService.consultaUsuario();
		}
	}

	@GetMapping(value = "/getFields")
	public TemplateDTO obtenerCampos(@RequestParam String id) throws ServerException {
		TemplateDTO filterTemplate = new TemplateDTO();
		filterTemplate.setLlaveTabla(id);
		return documentoplantillaService.obtenerCampos(filterTemplate, true);
	}

	@PostMapping(value = "/getFieldData")
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(
			@RequestBody PedidoVentaCaracteristicaFilterDTO filterField) throws ServerException {
		return adaptador.consultarDatosBase(filterField);
	}

	@PostMapping(value = "/getTrace")
	public List<DocumentoRelacionGestorDTO> getTrace(@RequestBody DocumentoRelacionGestorFilterDTO filterField)
			throws ServerException {
		return gestionService.listarExpedientesGestionadores(filterField);
	}

	@GetMapping(value = "/getTraceFields/{documentId}/{transaction}")
	public List<PedidoVentaCaracteristicaDTO> getTraceFields(@PathVariable(name = "documentId") String pDocumentId,
			@PathVariable(name = "transaction") String pTransaction) {
		return fieldsService.listar2Gestor(pDocumentId, pTransaction);
	}

	@PostMapping(value = "/getPropertyRelations")
	public List<RelacionInternaDTO> getPropertyRelations(@RequestBody RelacionInternaFilterDTO filter)
			throws ServerException {
		return relacionesService.listarConsulta(filter);
	}

	@PostMapping(value = "/validateLoad")
	public DocumentoPlantillaCaracteristicaDTO validateLoad(
			@RequestBody DocumentoPlantillaCaracteristicaFilterDTO filter) throws ServerException {
		return campoService.listarCarga(filter);
	}

	// ==================== PROCESS DESIGNER (antes /process_designer/*) ====================

	@PostMapping(value = "/designer/copy")
	public SharedIdResponse copy(@RequestParam String processId) throws ServerException {
		return copyService.call(processId);
	}

}
