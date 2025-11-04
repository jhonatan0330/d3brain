package com.softure.document_execution.infrastructure;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.shared.domain.ServerException;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.inventory.application.ProductoInventarioSvc;
import com.softure.inventory.domain.ProductoInventarioDTO;
import com.softure.mail.application.MailReleaseMessageQueueService;
import com.softure.notification.application.ActividadSvc;
import com.softure.notification.domain.ActividadDTO;
import com.softure.process_designer.application.ProcesoTransicionAutomaticaSvc;
import com.softure.upload.application.UploadSvc;
import com.softure.webservice.application.WebServiceEjecucionSvc;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/document")
public class DocumentController {

	@Autowired @Lazy private PedidoVentaSvc pedidoVentaService;
	@Autowired @Lazy private UploadSvc uploadService;
	@Autowired @Lazy private CallDocumentCRUD saveUpdateDocumentFunction;
	@Autowired @Lazy private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	@Autowired @Lazy private ActividadSvc actividadService;
	@Autowired @Lazy private ProductoInventarioSvc inventoryService;
	@Autowired @Lazy private MailReleaseMessageQueueService releaseQueueService;
	@Autowired @Lazy private ProcesoTransicionAutomaticaSvc transicionservice;
	@Autowired @Lazy private WebServiceEjecucionSvc apiService;

	
	@PostMapping(value="/getDocument")
	public PedidoVentaDTO consultarDocumento(@RequestBody PedidoVentaFilterDTO filter, String token) throws ServerException  {
		return pedidoVentaService.consultaCompleta(filter.getLlaveTabla(), token);
	}
	
	@PostMapping(value="/getDocuments")
	public List<PedidoVentaDTO> listarDocumentos(@RequestBody PedidoVentaFilterDTO filter, @RequestHeader("Authorization") String token) throws ServerException {
		filter.setSecurityToken(token);
		return listDocumentWithFiltersFunction.listarAvanzado(filter);
	}

	@PostMapping(value="/saveDocument")
	public PedidoVentaDTO guardarDocumento(@RequestBody PedidoVentaDTO document, @RequestBody String token)  throws ServerException  {
		PedidoVentaDTO result = new PedidoVentaDTO();
		if(document.getLlaveTabla()==null){
			document = saveUpdateDocumentFunction.save(document, token, null);
		}else{
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
	
	
	@PostMapping(value="/upload")
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile pFile,  @RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
        if (pFile.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
			return uploadService.uploadFile(pFile.getBytes(), pFile.getOriginalFilename(), token, null, "public");
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	@PostMapping(value="/readActivity")
	public ActividadDTO readActivity(@RequestBody ActividadDTO activity, @RequestHeader("Authorization") String token) throws ServerException {
		return actividadService.readActivity(activity.getLlaveTabla(), token);
	}
	
	@GetMapping(value="/getInventory/{id}")
	public List<ProductoInventarioDTO> getInventory(@PathVariable("id") String pId, @RequestHeader("Authorization") String token)  throws ServerException  {
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
		return "*******TAREAS (" + _launch +") ***  PROGRAMADAS ("+ _prepare +") ***"  + new Date().toString();
	}
	
	@GetMapping("/ping_api")
	public String sendApi() throws ServerException {
		return apiService.apiToTransaction();
	}

	
	
}
