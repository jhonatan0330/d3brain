package d3.document_execution;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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

import d3.shared.domain.ServerException;
import d3.document_execution.application.CallDocumentCRUD;
import d3.document_execution.application.CallDocumentListWithFilters;
import d3.document_execution.application.PedidoVentaSvc;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.document_execution.domain.PedidoVentaFilterDTO;
import d3.inventory.application.ProductoInventarioSvc;
import d3.inventory.domain.ProductoInventarioDTO;
import d3.mail.application.MailReleaseMessageQueueService;
import d3.notification.application.ActividadSvc;
import d3.notification.domain.ActividadDTO;
import d3.process_designer.application.ProcesoTransicionAutomaticaSvc;
import d3.upload.application.UploadSvc;
import d3.webservice.application.WebServiceEjecucionSvc;
import org.springframework.context.annotation.Lazy;

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

	public DocumentController(@Lazy PedidoVentaSvc pedidoVentaService, @Lazy UploadSvc uploadService,
			@Lazy CallDocumentCRUD saveUpdateDocumentFunction,
			@Lazy CallDocumentListWithFilters listDocumentWithFiltersFunction, @Lazy ActividadSvc actividadService,
			@Lazy ProductoInventarioSvc inventoryService, @Lazy MailReleaseMessageQueueService releaseQueueService,
			@Lazy ProcesoTransicionAutomaticaSvc transicionservice, @Lazy WebServiceEjecucionSvc apiService) {
		this.pedidoVentaService = pedidoVentaService;
		this.uploadService = uploadService;
		this.saveUpdateDocumentFunction = saveUpdateDocumentFunction;
		this.listDocumentWithFiltersFunction = listDocumentWithFiltersFunction;
		this.actividadService = actividadService;
		this.inventoryService = inventoryService;
		this.releaseQueueService = releaseQueueService;
		this.transicionservice = transicionservice;
		this.apiService = apiService;
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

	@GetMapping("/ping_mail")
	public String sendMail() throws ServerException {
		return "******* CORREOS (" + releaseQueueService.call() + ") ***" + new Date().toString();
	}

	@GetMapping("/ping_task")
	public String sendTemporizer() throws ServerException {
		int _launch = transicionservice.lanzarTransaccionesTemporizadas();
		int _prepare = transicionservice.programateAll();
		return "*******TAREAS (" + _launch + ") ***  PROGRAMADAS (" + _prepare + ") ***" + new Date().toString();
	}

	@GetMapping("/ping_api")
	public String sendApi() throws ServerException {
		return apiService.apiToTransaction();
	}

}
