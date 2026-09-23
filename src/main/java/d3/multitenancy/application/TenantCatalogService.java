package d3.multitenancy.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.CacheManager;
import d3.multitenancy.domain.TenantFilterDTO;
import d3.multitenancy.domain.TenantPublicDTO;
import d3.multitenancy.infrastructure.TenantMapper;
import d3.shared.domain.SharedConstants;

@Service("tenantCatalogService")
public class TenantCatalogService {

	private final TenantMapper tenantMapper;
	private final CacheManager cacheManager;

	@Value("${tenant.default-name:Principal}")
	private String defaultName;

	public TenantCatalogService(@Lazy TenantMapper tenantMapper, @Lazy CacheManager cacheManager) {
		this.tenantMapper = tenantMapper;
		this.cacheManager = cacheManager;
	}

	/**
	 * Lista los tenants activos del catálogo maestro. La consulta siempre se
	 * ejecuta contra el tenant "default" (catálogo), sin importar el tenant del
	 * request, y garantiza incluir el tenant "default" aunque no exista fila.
	 */
	public List<TenantPublicDTO> listarActivos() {
		List<TenantPublicDTO> cached = cacheManager.getTenantCatalog();
		if (cached != null) {
			return new ArrayList<>(cached);
		}
		String previous = TenantContext.getCurrentTenant();
		try {
			TenantContext.setCurrentTenant("default");
			TenantFilterDTO filter = new TenantFilterDTO();
			filter.setState(SharedConstants.STATE_ACTIVE);
			List<TenantPublicDTO> tenants = new ArrayList<>();
			tenantMapper.getMany(filter).forEach(tenant -> {
				TenantPublicDTO dto = new TenantPublicDTO();
				dto.setKey(tenant.getKey());
				dto.setName(tenant.getName());
				dto.setImagen(tenant.getImagen());
				tenants.add(dto);
			});
			if (tenants.stream().noneMatch(tenant -> "default".equals(tenant.getKey()))) {
				TenantPublicDTO dto = new TenantPublicDTO();
				dto.setKey("default");
				dto.setName(defaultName);
				dto.setImagen(null);
				tenants.add(dto);
			}
			cacheManager.setTenantCatalog(new ArrayList<>(tenants));
			return tenants;
		} finally {
			TenantContext.setCurrentTenant(previous);
		}
	}

	public void invalidarCache() {
		cacheManager.clearTenantCatalog();
	}

}