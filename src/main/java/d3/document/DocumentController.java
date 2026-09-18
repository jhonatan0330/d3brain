package d3.document;

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

import d3.document.application.CallDocumentCRUD;
import d3.document.application.CallDocumentListWithFilters;
import d3.document.application.DocumentoRelacionGestorSvc;
import d3.document.application.PedidoVentaAjusteSvc;
import d3.document.application.PedidoVentaCaracteristicaSvc;
import d3.document.application.PedidoVentaSvc;
import d3.document.application.field.CampoAdaptador;
import d3.document.domain.DocumentoRelacionGestorDTO;
import d3.document.domain.DocumentoRelacionGestorFilterDTO;
import d3.document.domain.PedidoVentaAjusteDTO;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.document.domain.PedidoVentaFilterDTO;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/document")
public class DocumentController {

	private final CallDocumentCRUD saveUpdateDocumentFunction;
	private final CallDocumentListWithFilters listDocumentWithFiltersFunction;
	private final CampoAdaptador adaptador;
	private final DocumentoRelacionGestorSvc gestionService;
	private final PedidoVentaSvc pedidoVentaService;
	private final PedidoVentaAjusteSvc pedidoVentaAjusteService;
	private final PedidoVentaCaracteristicaSvc fieldsService;

	public DocumentController(@Lazy PedidoVentaSvc pedidoVentaService,
			@Lazy CallDocumentCRUD saveUpdateDocumentFunction,
			@Lazy CallDocumentListWithFilters listDocumentWithFiltersFunction,
			@Lazy PedidoVentaCaracteristicaSvc fieldsService, @Lazy DocumentoRelacionGestorSvc gestionService,
			@Lazy PedidoVentaAjusteSvc pedidoVentaAjusteService, @Lazy CampoAdaptador adaptador) {
		this.pedidoVentaService = pedidoVentaService;
		this.saveUpdateDocumentFunction = saveUpdateDocumentFunction;
		this.listDocumentWithFiltersFunction = listDocumentWithFiltersFunction;
		this.pedidoVentaAjusteService = pedidoVentaAjusteService;
		this.adaptador = adaptador;
		this.fieldsService = fieldsService;
		this.gestionService = gestionService;
	}
	
	/*@PostMapping(value = "/saveDocument")
	public PedidoVentaDTO guardarDocumento(@RequestBody PedidoVentaDTO document) throws ServerException {
		PedidoVentaDTO result = new PedidoVentaDTO();
		if (document.getLlaveTabla() == null) {
			document = saveUpdateDocumentFunction.save(document, null);
		} else {
			document = saveUpdateDocumentFunction.update(document, null);
		}
		result.setNombre(document.getNombre());
		result.setPlantilla(document.getPlantilla());
		result.setLlaveTabla(document.getLlaveTabla());
		result.setDescripcion(document.getDescripcion());
		result.setEstadoExpediente(document.getEstadoExpediente());
		result.setEstadoNombre(document.getEstadoNombre());
		return result;
	}*/
	
	@PostMapping(value = "/save")
	public PedidoVentaDTO apiGuardarDocumento(@RequestBody PedidoVentaDTO documento,
			@RequestHeader(name = "non-duplicate", required = false) String session) throws ServerException {
		if (documento.getLlaveTabla() == null) {
			documento = saveUpdateDocumentFunction.save(documento, session);
		} else {
			documento = saveUpdateDocumentFunction.update(documento, null);
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
	
	@PostMapping(value = "/saveByMassive")
	public PedidoVentaDTO saveByMassive(@RequestBody PedidoVentaDTO documento,
			@RequestHeader(name = "non-duplicate", required = false) String session) throws ServerException {
		documento = saveUpdateDocumentFunction.massive(documento, session);
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
	
	@PostMapping(value = "/get")
	public List<PedidoVentaDTO> listarDocumentos(@RequestBody PedidoVentaFilterDTO filter) throws ServerException {
		return listDocumentWithFiltersFunction.listarAvanzado(filter);
	}

	@PostMapping(value = "/get-user")
	public List<PedidoVentaDTO> listarUsuarioPedidoVenta(@RequestBody PedidoVentaFilterDTO dto) throws ServerException {
		return listDocumentWithFiltersFunction.listarUsuario(dto);
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
	

	@PostMapping(value = "/getDocument")
	public PedidoVentaDTO consultarDocumento(@RequestBody PedidoVentaFilterDTO filter) throws ServerException {
		return pedidoVentaService.consultaCompleta(filter.getLlaveTabla());
	}

	@PostMapping(value = "/api/consultarDocumento")
	public PedidoVentaDTO apiConsultarDocumento(@RequestBody PedidoVentaFilterDTO documentoFiltro)
			throws ServerException {
		PedidoVentaDTO _result = pedidoVentaService.consultaCompleta(documentoFiltro.getLlaveTabla());
		pedidoVentaService.clearPedidoResponse(_result);
		return _result;
	}

	@PostMapping(value = "/api/validateBeforeNew")
	public PedidoVentaDTO validateBeforeNew(@RequestBody PedidoVentaFilterDTO documentoFiltro) throws ServerException {
		return pedidoVentaService.validateBeforeNew(documentoFiltro);
	}


	@GetMapping(value = "/api/getMessageToProcessField/{property}/{fieldValue}")
	public SharedIdResponse message(@PathVariable(name = "property") String pProperty,
			@PathVariable(name = "fieldValue") String pFieldValue) throws ServerException {
		return new SharedIdResponse(null, null, null,
				pedidoVentaService.getMessageToProcessField(pProperty, pFieldValue, SessionContext.getCurrentToken()));
	}

	@PostMapping(value = "/api/changeState")
	public PedidoVentaAjusteDTO changeState(@RequestBody PedidoVentaAjusteDTO ajuste) throws ServerException {
		return pedidoVentaAjusteService.guardar(ajuste);
	}

	@GetMapping(value = "/getTraceFields/{documentId}/{transaction}")
	public List<PedidoVentaCaracteristicaDTO> getTraceFields(@PathVariable(name = "documentId") String pDocumentId,
			@PathVariable(name = "transaction") String pTransaction) {
		return fieldsService.listar2Gestor(pDocumentId, pTransaction);
	}

}
