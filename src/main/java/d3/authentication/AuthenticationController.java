package d3.authentication;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.shared.application.HttpUtils;
import d3.property.application.PropiedadSvc;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadFilterDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/authentication")
public class AuthenticationController {

	private final OrganizacionSvc organizationSvc;
	private final PropiedadSvc propertiesService;

	public AuthenticationController(@Lazy OrganizacionSvc organizationSvc, @Lazy PropiedadSvc propertiesService) {
		this.organizationSvc = organizationSvc;
		this.propertiesService = propertiesService;
	}

	@GetMapping(value = "getLinkedOrganizations")
	public List<OrganizacionDTO> getLinkedOrganizations(@RequestHeader("Authorization") String token)
			throws ServerException {
		return organizationSvc.obtenerUsuario(organizationSvc.getUserFlex(token));
	}

	@GetMapping(value = "/obtenerPrincipalOrganizacion")
	public OrganizacionDTO obtenerPrincipalOrganizacion(HttpServletRequest request) throws ServerException {
		// Este metodo se usa para obtener los datos de la organizacion pero despues se
		// vuelve a utilizar para obtener las propiedades
		return organizationSvc.obtenerPrincipalPublic(HttpUtils.getRequestIP(request));
	}

	@GetMapping(value = "/properties/{type}/{field}")
	public List<PropiedadDTO> getFullProperties(@RequestHeader("Authorization") String token,
			@PathVariable(name = "type") String pType, @PathVariable(name = "field") String pField)
			throws ServerException {
		PropiedadFilterDTO filter = new PropiedadFilterDTO();
		filter.setTipo(pType);
		filter.setCampo(pField);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		return propertiesService.listarConsulta(filter);
	}
}
