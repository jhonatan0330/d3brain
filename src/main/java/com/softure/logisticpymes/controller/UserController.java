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
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired @Lazy  private RolAccesoSvc roleService;
	@Autowired @Lazy  private UsuarioSvc userService;

	@GetMapping(value="/getRole")
	public List<RolAccesoDTO> getRole(@RequestHeader(name="Authorization", required = false) String token) throws ServerException {
		RolAccesoFilterDTO _filter = new RolAccesoFilterDTO();
		_filter.setEstado(SharedConstants.STATE_ACTIVE);
		return roleService.listarConsulta(_filter);
	}
	
	@PostMapping(value="/getUsers")
	public List<UsuarioDTO> getUsers(@RequestHeader(name="Authorization", required = false) String token, @RequestBody UsuarioFilterDTO filter) throws ServerException {
		return userService.listarConsulta(filter);
	}
	
	@GetMapping("/{userId}")
	public UsuarioDTO getUserById(@RequestHeader(name="Authorization", required = false) String token, @PathVariable(name="userId") String pUserId) throws ServerException {
		return userService.consultaXId(pUserId);
	}
	
	
}
