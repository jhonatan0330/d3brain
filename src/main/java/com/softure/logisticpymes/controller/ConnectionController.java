package com.softure.logisticpymes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.UsuarioOrganizacionDTO;
import com.softure.logisticpymes.services.UsuarioOrganizacionSvc;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/connection")
public class ConnectionController {
	
	@Autowired private UsuarioOrganizacionSvc usuarioOrganizacionService;
	
	@RequestMapping(value="/sincronizarUsuarios", method=RequestMethod.GET)
	public List<UsuarioOrganizacionDTO> sincronizarUsuarios()  throws ServerException  {
		return usuarioOrganizacionService.getHumanResource();
	}
	
	
}
