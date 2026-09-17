package d3.authentication;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/authentication")
public class AuthenticationController {

	private final OrganizacionSvc organizationSvc;

	public AuthenticationController(@Lazy OrganizacionSvc organizationSvc) {
		this.organizationSvc = organizationSvc;
	}

	@GetMapping(value = "getLinkedOrganizations")
	public List<OrganizacionDTO> getLinkedOrganizations() throws ServerException {
		return organizationSvc.obtenerUsuario(SessionContext.getCurrentUser());
	}

	@GetMapping(value = "/obtenerPrincipalOrganizacion")
	public OrganizacionDTO obtenerPrincipalOrganizacion() throws ServerException {
		// Este metodo se usa para obtener los datos de la organizacion pero despues se
		// vuelve a utilizar para obtener las propiedades
		return organizationSvc.obtenerPrincipalPublic();
	}

}
