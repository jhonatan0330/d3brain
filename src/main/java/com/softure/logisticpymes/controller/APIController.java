package com.softure.logisticpymes.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shared.domain.SharedApiErrorResponse;
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

	@Autowired private UsuarioAutenticacionSvc usuarioAutenticacionService;
	@Autowired private DocumentoPlantillaSvc documentoplantillaService;
	@Autowired private PedidoVentaSvc pedidoVentaService;
	@Autowired private CallDocumentCRUD saveUpdateDocumentFunction;
	@Autowired private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	@Autowired private PedidoVentaAjusteSvc pedidoVentaAjusteService;
	@Autowired private UsuarioSvc usuarioService;
	@Autowired private UploadSvc uploadService;
	@Autowired private CampoAdaptador adaptador;
	
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

	@PostMapping(value="/guardarDocumento")
	public PedidoVentaDTO guardarDocumento(@RequestBody PedidoVentaDTO documento, @RequestHeader("Authorization") String token, @RequestHeader(name = "non-duplicate", required = false) String session)  throws ServerException  {
		PedidoVentaDTO result = new PedidoVentaDTO();
		if(documento.getLlaveTabla()==null){
			documento = saveUpdateDocumentFunction.save(documento, token, session);
		}else{
			documento = saveUpdateDocumentFunction.update(documento, null, token);
		}
		result.setNombre(documento.getNombre());
		result.setPlantilla(documento.getPlantilla());
		result.setLlaveTabla(documento.getLlaveTabla());
		result.setEstadoExpediente(documento.getEstadoExpediente());
		result.setEstadoNombre(documento.getEstadoNombre());
		result.setDescripcion(documento.getDescripcion());
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
		return documentoplantillaService.obtenerCampos(documentoFiltro, token);
	}
	
	@PostMapping(value="/upload")
    public SharedApiErrorResponse handleFileUpload(@RequestParam("file") MultipartFile file,  @RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
        if (file.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
        	String url =uploadService.uploadFile(file.getBytes(), file.getOriginalFilename(), token, null); 
        	SharedApiErrorResponse response =new SharedApiErrorResponse.ApiErrorResponseBuilder()
     		        .withMessage(url).build();
			return response;
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	
	@PostMapping(value="/uploadResponseString")
    public String handleFileUploadFlex(@RequestParam("file") MultipartFile file) throws ServerException {
        if (file.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
        	//En flex no es posible pasar los datos del header ver flash.net.FileReference.upload
        	return uploadService.uploadFile(file.getBytes(), file.getOriginalFilename(), null, "config"); 
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	@PostMapping(value="/changePicture")
    public UsuarioDTO cambiarImagen(@RequestParam("file") MultipartFile file,  @RequestHeader("Authorization") String token) throws ServerException {
        if (file.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
        	String url =uploadService.uploadFile(file.getBytes(), file.getOriginalFilename(), token, "config");
			return usuarioService.changePicture(url, token);
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	@PostMapping(value="/changeState")
	public PedidoVentaAjusteDTO changeState(@RequestBody PedidoVentaAjusteDTO ajuste,@RequestHeader("Authorization") String token)  throws ServerException  {
		return pedidoVentaAjusteService.guardar(ajuste, token);	
	}
	
}