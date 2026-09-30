package d3.authentication.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.CacheManager;
import d3.authentication.domain.OrganizacionDTO;
import d3.authentication.domain.OrganizacionFilterDTO;
import d3.authentication.infrastructure.OrganizacionMapper;
import d3.configuration.application.PropertyGetWithCacheService;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.document.application.field.Propiedades;
import d3.shared.application.BasicSvc;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import jakarta.annotation.PostConstruct;

@Service("organizacionService")
public class OrganizacionSvc extends BasicSvc<OrganizacionDTO, OrganizacionFilterDTO> {

	private final OrganizacionMapper organizacionMapper;
	private final PropertyGetWithCacheService cacheService;
	private final CacheManager cacheManager;
	private final d3.multitenancy.application.TenantBaseAlias tenantBaseAlias;

	public OrganizacionSvc(@Lazy OrganizacionMapper organizacionMapper,
			@Lazy PropertyGetWithCacheService cacheService, @Lazy CacheManager cacheManager,
			@Lazy d3.multitenancy.application.TenantBaseAlias tenantBaseAlias) {
		this.organizacionMapper = organizacionMapper;
		this.cacheService = cacheService;
		this.cacheManager = cacheManager;
		this.tenantBaseAlias = tenantBaseAlias;
	}

	@Override
	public OrganizacionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Organizacion");
		OrganizacionFilterDTO dto = new OrganizacionFilterDTO();
		dto.setLlaveTabla(llave);
		return organizacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = organizacionMapper;
	}

	@Override
	public OrganizacionDTO actualizar(OrganizacionDTO dto) throws ServerException {
		cacheManager.clearMainOrganization();
		if (tenantBaseAlias != null) {
			tenantBaseAlias.invalidateCache();
		}
		return super.actualizar(dto);
	}

	@Override
	public List<OrganizacionDTO> listarConsulta(OrganizacionFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	public OrganizacionDTO obtenerPrincipalPublic() throws ServerException {
		return obtenerPrincipalPropiedades(SessionContext.getCurrentUserOrNull());
	}

	public OrganizacionDTO obtenerPrincipal() throws ServerException {
		OrganizacionDTO mainOrganization = cacheManager.getMainOrganization();
		if (mainOrganization != null)
			return mainOrganization;
		try {
			mainOrganization = organizacionMapper.obtenerPrincipal();
			cacheManager.setMainOrganization(mainOrganization);
			return mainOrganization;
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}


	public OrganizacionDTO obtenerPrincipalPropiedades(String user) throws ServerException {
		OrganizacionDTO result = obtenerPrincipal();
		if (result != null) {
			result.setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.ORGANIZACION,
					result.getLlaveTabla(), null, user));
		}
		return result;
	}

	public boolean permisosCompletos(String user) throws ServerException {
		OrganizacionDTO _main = obtenerPrincipal();
		return (cacheService.obtenerPropiedad(PropiedadValorDefinidoDTO.ORGANIZACION, _main.getLlaveTabla(),
				Propiedades.APP_ADMIN, user) != null);
	}

	// Esto toca unirlo con lo anterior lo estoy haciendo rapido
	public boolean permisosAuditor(String user) throws ServerException {
		OrganizacionDTO _main = obtenerPrincipal();
		return (cacheService.obtenerPropiedad(PropiedadValorDefinidoDTO.ORGANIZACION, _main.getLlaveTabla(),
				Propiedades.APP_READER, user) != null);
	}

}