package com.softure.logisticpymes.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.softure.java.dto.exception.ApiErrorResponse;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.ActividadDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.PedidoVentaAjusteDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.UsuarioAutenticacionDTO;
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.dto.filter.UsuarioFilterDTO;
import com.softure.logisticpymes.services.ActividadSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.PedidoVentaAjusteSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.UploadSvc;
import com.softure.logisticpymes.services.UsuarioAutenticacionSvc;
import com.softure.logisticpymes.services.UsuarioSvc;
import com.softure.logisticpymes.services.adapter.CampoAdaptador;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/rest")
public class APIController {

	@Autowired private ActividadSvc actividadService;
	@Autowired private UsuarioAutenticacionSvc usuarioAutenticacionService;
	@Autowired private DocumentoPlantillaSvc documentoplantillaService;
	@Autowired private PedidoVentaSvc pedidoVentaService;
	@Autowired private PedidoVentaAjusteSvc pedidoVentaAjusteService;
	@Autowired private UsuarioSvc usuarioService;
	@Autowired private UploadSvc uploadService;
	@Autowired private CampoAdaptador adaptador;
	
	@RequestMapping(value="/logOut", method=RequestMethod.POST)
	public UsuarioDTO logOut(@RequestBody UsuarioAutenticacionDTO autenticacion, @RequestHeader("Authorization") String token) throws ServerException {
		if(autenticacion==null) throw new ServerException("Los datos de autenticacion son nulos");
		usuarioAutenticacionService.inactivar(autenticacion, token);
		return null;
	}
	
	@RequestMapping(value="/consultarDocumento", method=RequestMethod.POST)
	public PedidoVentaDTO consultarDocumento(@RequestBody PedidoVentaFilterDTO documentoFiltro, @RequestHeader("Authorization") String token) throws ServerException  {
		documentoFiltro.setSecurityToken(token);
		return pedidoVentaService.consultaCompleta(documentoFiltro);
	}

	@RequestMapping(value="/guardarDocumento", method=RequestMethod.POST)
	public PedidoVentaDTO guardarDocumento(@RequestBody PedidoVentaDTO documento, @RequestHeader("Authorization") String token)  throws ServerException  {
		PedidoVentaDTO result = new PedidoVentaDTO();
		if(documento.getLlaveTabla()==null){
			documento = pedidoVentaService.guardar(documento, token);
		}else{
			documento = pedidoVentaService.actualizar(documento, token);
		}
		result.setNombre(documento.getNombre());
		result.setPlantilla(documento.getPlantilla());
		result.setLlaveTabla(documento.getLlaveTabla());
		result.setEstadoExpediente(documento.getEstadoExpediente());
		result.setEstadoNombre(documento.getEstadoNombre());
		return result;
	}
	
	/*@RequestMapping(value="/eliminarDocumento", method=RequestMethod.POST)
	public PedidoVentaDTO eliminarDocumento(@RequestBody PedidoVentaDTO documento)   throws ServerException  {
		return pedidoVentaService.inactivar(documento);
	}*/
	
	@RequestMapping(value="/consultarUsuario", method=RequestMethod.POST)
	public UsuarioDTO consultarUsuario(@RequestBody UsuarioFilterDTO dto)  throws ServerException  {
		return usuarioService.consultaUnica(dto);	
	}
		
	@RequestMapping(value="/consultarDatosBase", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(@RequestBody PedidoVentaCaracteristicaFilterDTO dto)  throws ServerException  {
		return adaptador.consultarDatosBase(dto);
	}
	
	
	@RequestMapping(value="/listarDocumentos", method=RequestMethod.POST)
	public List<PedidoVentaDTO> listarDocumentos(@RequestBody PedidoVentaFilterDTO documentoFiltro) throws ServerException {
		return pedidoVentaService.listarAvanzado(documentoFiltro);
	}
	
	@RequestMapping(value="/obtenerCampos", method=RequestMethod.POST)
	public DocumentoPlantillaDTO obtenerCampos(@RequestBody DocumentoPlantillaDTO documentoFiltro,  @RequestHeader("Authorization") String token) throws ServerException {
		return documentoplantillaService.obtenerCampos(documentoFiltro, token);
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public ApiErrorResponse handleFileUpload(@RequestParam("file") MultipartFile file,  @RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
        if (file.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
        	String url =uploadService.uploadFile(file.getBytes(), file.getOriginalFilename(), token); 
        	ApiErrorResponse response =new ApiErrorResponse.ApiErrorResponseBuilder()
     		        .withMessage(url).build();
			return response;
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	
	@RequestMapping(value="/uploadResponseString", method=RequestMethod.POST)
    public String handleFileUploadFlex(@RequestParam("file") MultipartFile file) throws ServerException {
        if (file.isEmpty()) throw new ServerException("You failed to upload because the file was empty.");
        try {
        	//En flex no es posible pasar los datos del header ver flash.net.FileReference.upload
        	return uploadService.uploadFile(file.getBytes(), file.getOriginalFilename(), null); 
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
    }
	
	@RequestMapping(value="/usuariosXRol", method=RequestMethod.POST)
	public List<UsuarioDTO> usuariosXRol(@RequestBody PedidoVentaFilterDTO document)  throws ServerException  {
		if(document==null) throw new ServerException("Porfavor envie el objeto documento");
		if(document.getLlaveTabla()==null) throw new ServerException("Porfavor envie la llave del documento");
		PedidoVentaDTO documento = pedidoVentaService.consultaXId(document.getLlaveTabla());
		if(documento==null) throw new ServerException("No se encuentra documento con esa llave");
		if(documento.getEstadoExpediente()==null) throw new ServerException("El documento no tiene estado");
		return usuarioService.getUsersState(documento.getEstadoExpediente(), document.getSecurityToken());
	}
	
	@RequestMapping(value="/reasignar", method=RequestMethod.POST)
	public ActividadDTO reasignar(@RequestBody ActividadDTO asignacion,@RequestHeader("Authorization") String token)  throws ServerException  {
		return actividadService.guardar(asignacion, token);	
	}
	
	@RequestMapping(value="/changeState", method=RequestMethod.POST)
	public PedidoVentaAjusteDTO changeState(@RequestBody PedidoVentaAjusteDTO ajuste,@RequestHeader("Authorization") String token)  throws ServerException  {
		return pedidoVentaAjusteService.guardar(ajuste, token);	
	}
	
}