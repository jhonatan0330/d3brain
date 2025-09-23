package com.softure.logisticpymes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired @Lazy  private RolAccesoSvc roleService;
	@Autowired @Lazy  private UsuarioSvc userService;
	@Autowired @Lazy  private UsuarioAutenticacionSvc usuarioAutenticacionService;
	@Autowired @Lazy  private RolAccesoSvc rolAccesoService;
	@Autowired @Lazy  private PropiedadSvc propertyService;

	@GetMapping(value="/getRole")
	public List<RolAccesoDTO> getRole(@RequestHeader(name="Authorization") String token) throws ServerException {
		RolAccesoFilterDTO _filter = new RolAccesoFilterDTO();
		_filter.setEstado(SharedConstants.STATE_ACTIVE);
		return roleService.listarConsulta(_filter);
	}
	
	@PostMapping(value="/getUsers")
	public List<UsuarioDTO> getUsers(@RequestHeader(name="Authorization") String token, @RequestBody UsuarioFilterDTO pFilter) throws ServerException {
		return userService.listarRol(pFilter);
	}
	
	@GetMapping("/{userId}")
	public UsuarioDTO getUserById(@RequestHeader(name="Authorization") String token, @PathVariable(name="userId") String pUserId) throws ServerException {
		return userService.consultaXId(pUserId);
	}
	
	@GetMapping("/document/{documentId}")
	public UsuarioDTO getUserByDocument(@RequestHeader(name="Authorization") String token, @PathVariable(name="documentId") String pDocumentId) throws ServerException {
		return userService.getUserByDocument(pDocumentId);
	}
	
	@PostMapping(value="/cambiarClaveUsuarioAutenticacion")
	public UsuarioAutenticacionDTO cambiarClaveUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token)throws ServerException {
		return usuarioAutenticacionService.cambiarClave(dto, token);
	}
	
	@GetMapping(value="/roles/{userId}")
	public List<RolAccesoDTO> consultaUsuarioDocumentoRolAcceso(@RequestHeader(name="Authorization") String token, @PathVariable(name="userId") String pUserId)throws ServerException {
			return rolAccesoService.consultaUsuarioDocumento(pUserId);
	}
	
	@GetMapping(value="/properties/{userId}")
	public List<PropiedadDTO> getPropertiesToUser(@RequestHeader(name="Authorization") String token, @PathVariable(name="userId") String pUserId)throws ServerException {
		return propertyService.getToUser(pUserId);
	}
	
}
