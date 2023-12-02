package com.softure.authentication.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/authentication")
public class AuthenticationController {
	@Autowired private OrganizacionSvc organizationSvc;
	
	@GetMapping(value = "getLinkedOrganizations")
	public List<OrganizacionDTO> getLinkedOrganizations(@RequestHeader("Authorization") String token)throws ServerException  {
		return organizationSvc.obtenerUsuario(organizationSvc.getUserFlex(token));
	}
}
