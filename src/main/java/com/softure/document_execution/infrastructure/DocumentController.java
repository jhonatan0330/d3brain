package com.softure.document_execution.infrastructure;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.softure.notification.application.ActividadSvc;
import com.softure.notification.domain.ActividadDTO;
import com.softure.upload.application.UploadSvc;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/document")
public class DocumentController {

	@Autowired @Lazy  private PedidoVentaSvc pedidoVentaService;
	@Autowired @Lazy  private UploadSvc uploadService;
	@Autowired @Lazy  private CallDocumentCRUD saveUpdateDocumentFunction;
	@Autowired @Lazy  private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	@Autowired @Lazy  private ActividadSvc actividadService;
	
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
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file,  @RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
        if (file.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
			return uploadService.uploadFile(file.getBytes(), file.getOriginalFilename(), token, null);
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	@PostMapping(value="/readActivity")
	public ActividadDTO readActivity(@RequestBody ActividadDTO activity, @RequestHeader("Authorization") String token) throws ServerException {
		return actividadService.readActivity(activity.getLlaveTabla(), token);
	}
}
