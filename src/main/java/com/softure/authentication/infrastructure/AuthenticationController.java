package com.softure.authentication.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadFilterDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/authentication")
public class AuthenticationController {
	
	@Autowired @Lazy  private OrganizacionSvc organizationSvc;
	@Autowired @Lazy  private PropiedadSvc propertiesService;
	
	@GetMapping(value = "getLinkedOrganizations")
	public List<OrganizacionDTO> getLinkedOrganizations(@RequestHeader("Authorization") String token)throws ServerException  {
		return organizationSvc.obtenerUsuario(organizationSvc.getUserFlex(token));
	}
	
	@GetMapping(value="/obtenerPrincipalOrganizacion")
	public OrganizacionDTO obtenerPrincipalOrganizacion() throws ServerException {
		// Este metodo se usa para obtener los datos de la organizacion pero despues se vuelve a utilizar para obtener las propiedades
		return organizationSvc.obtenerPrincipalPublic();
	}
	
	@GetMapping(value="/properties/{type}/{field}")
	public List<PropiedadDTO> getFullProperties(@RequestHeader("Authorization") String token, @PathVariable String type, @PathVariable String field) throws ServerException {
		PropiedadFilterDTO filter = new PropiedadFilterDTO();
		filter.setTipo(type);
		filter.setCampo(field);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propertiesService.listarConsulta(filter);
	}
}
