package com.softure.document_execution;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shared.domain.SharedApiErrorResponse;
import com.shared.domain.SharedIdResponse;
import com.shared.domain.ServerException;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.application.field.CampoAdaptador;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.document_transition.application.PedidoVentaAjusteSvc;
import com.softure.document_transition.domain.PedidoVentaAjusteDTO;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.upload.application.UploadSvc;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/rest")
public class APIController {

	@Autowired @Lazy  private UsuarioAutenticacionSvc usuarioAutenticacionService;
	@Autowired @Lazy  private DocumentoPlantillaSvc documentoplantillaService;
	@Autowired @Lazy  private PedidoVentaSvc pedidoVentaService;
	@Autowired @Lazy  private CallDocumentCRUD saveUpdateDocumentFunction;
	@Autowired @Lazy  private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	@Autowired @Lazy  private PedidoVentaAjusteSvc pedidoVentaAjusteService;
	@Autowired @Lazy  private UsuarioSvc usuarioService;
	@Autowired @Lazy  private UploadSvc uploadService;
	@Autowired @Lazy  private CampoAdaptador adaptador;
	
	@PostMapping(value="/logOut")
	public UsuarioDTO logOut(@RequestBody UsuarioAutenticacionDTO autenticacion, @RequestHeader("Authorization") String token) throws ServerException {
		if(autenticacion==null) throw new ServerException("Los datos de autenticacion son nulos");
		usuarioAutenticacionService.inactivar(autenticacion, token);
		return null;
	}
	
	@PostMapping(value="/consultarDocumento")
	public PedidoVentaDTO consultarDocumento(@RequestBody PedidoVentaFilterDTO documentoFiltro, @RequestHeader("Authorization") String token) throws ServerException  {
		documentoFiltro.setSecurityToken(token);
		return pedidoVentaService.consultaCompleta(documentoFiltro.getLlaveTabla(), token);
	}
	
	@PostMapping(value="/validateBeforeNew")
	public PedidoVentaDTO validateBeforeNew(@RequestBody PedidoVentaFilterDTO documentoFiltro, @RequestHeader("Authorization") String token) throws ServerException  {
		documentoFiltro.setSecurityToken(token);
		return pedidoVentaService.validateBeforeNew(documentoFiltro);
	}

	@PostMapping(value="/guardarDocumento")
	public PedidoVentaDTO guardarDocumento(@RequestBody PedidoVentaDTO documento, @RequestHeader("Authorization") String token, @RequestHeader(name = "non-duplicate", required = false) String session)  throws ServerException  {
		if(documento.getLlaveTabla()==null){
			documento = saveUpdateDocumentFunction.save(documento, token, session);
		}else{
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
	
	@PostMapping(value="/saveByMassive")
	public PedidoVentaDTO saveByMassive(@RequestBody PedidoVentaDTO documento, @RequestHeader("Authorization") String token, @RequestHeader(name = "non-duplicate", required = false) String session)  throws ServerException  {
		//Este metodo es igual al de guardar pero debi colocar una logica del modificar
		// La idea es despues mejorar las cargas masivas 
		// Para almacenar el archivo y crear los registros desde el back
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
	
	@PostMapping(value="/consultarUsuario")
	public UsuarioDTO consultarUsuario(@RequestBody UsuarioFilterDTO dto, @RequestHeader("Authorization") String token)  throws ServerException  {
		dto.setSecurityToken(token);
		return usuarioService.consultaUnica(dto);	
	}
		
	@PostMapping(value="/consultarDatosBase")
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(@RequestBody PedidoVentaCaracteristicaFilterDTO dto, @RequestHeader("Authorization") String token)  throws ServerException  {
		dto.setSecurityToken(token);
		return adaptador.consultarDatosBase(dto);
	}
	
	
	@PostMapping(value="/listarDocumentos")
	public List<PedidoVentaDTO> listarDocumentos(@RequestBody PedidoVentaFilterDTO documentoFiltro, @RequestHeader("Authorization") String token) throws ServerException {
		documentoFiltro.setSecurityToken(token);
		return listDocumentWithFiltersFunction.listarAvanzado(documentoFiltro);
	}
	
	@PostMapping(value="/obtenerCampos")
	public DocumentoPlantillaDTO obtenerCampos(@RequestBody DocumentoPlantillaDTO documentoFiltro,  @RequestHeader("Authorization") String token) throws ServerException {
		return documentoplantillaService.obtenerCampos(documentoFiltro, token, true);
	}
	
	@PostMapping(value="/upload")
    public SharedApiErrorResponse handleFileUpload(@RequestParam("file") MultipartFile pFile,  @RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
        if (pFile.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
        	String url =uploadService.uploadFile(pFile.getBytes(), pFile.getOriginalFilename(), token, null, "public"); 
        	SharedApiErrorResponse response =new SharedApiErrorResponse.ApiErrorResponseBuilder()
     		        .withMessage(url).build();
			return response;
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	
	@PostMapping(value="/uploadResponseString")
    public String handleFileUploadFlex(@RequestParam("file") MultipartFile pFile) throws ServerException {
        if (pFile.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
        	//En flex no es posible pasar los datos del header ver flash.net.FileReference.upload
        	return uploadService.uploadFile(pFile.getBytes(), pFile.getOriginalFilename(), null, "config", "public"); 
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	@PostMapping(value="/changePicture")
    public UsuarioDTO cambiarImagen(@RequestParam("file") MultipartFile pFile,  @RequestHeader("Authorization") String token) throws ServerException {
        if (pFile.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
        	String url =uploadService.uploadFile(pFile.getBytes(), pFile.getOriginalFilename(), token, "config", "public");
			return usuarioService.changePicture(url, token);
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	@PostMapping(value="/changeState")
	public PedidoVentaAjusteDTO changeState(@RequestBody PedidoVentaAjusteDTO ajuste,@RequestHeader("Authorization") String token)  throws ServerException  {
		return pedidoVentaAjusteService.guardar(ajuste, token);	
	}
	
	@GetMapping(value="/getMessageToProcessField/{property}/{fieldValue}")
	public SharedIdResponse message(@PathVariable(name="property") String pProperty,@PathVariable(name="fieldValue") String pFieldValue, @RequestHeader("Authorization") String token)  throws ServerException  {
		return new SharedIdResponse(null, null, null, pedidoVentaService.getMessageToProcessField(pProperty, pFieldValue, token))  ;
	}
	
}