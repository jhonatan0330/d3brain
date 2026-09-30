package d3.multitenancy.application;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantFilterDTO;
import d3.multitenancy.domain.TenantMetadataProvider;
import d3.multitenancy.infrastructure.TenantMapper;

/**
 * Simulates an external tenant catalog. Replace with a DB-backed or remote
 * implementation in production; keep this class as a test double or local
 * bootstrap.
 */
@Component
@Order(1)
public class DatabaseTenantMetadataProvider implements TenantMetadataProvider, ApplicationRunner {

	private final Environment env;
	private final Map<String, TenantDTO> catalog = new ConcurrentHashMap<>();
	private final TenantMapper tenantRepository;
	private final TenantBaseAlias tenantBaseAlias;

	public DatabaseTenantMetadataProvider(Environment env, @Lazy TenantMapper tenantRepository,
			TenantBaseAlias tenantBaseAlias) {
		this.env = env;
		this.tenantRepository = tenantRepository;
		this.tenantBaseAlias = tenantBaseAlias;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception { // ✅ carga cuando todo está listo

		catalog.put("default", jdbcFromLegacyDbProperties());

		tenantRepository.getMany(new TenantFilterDTO()).forEach(tenant -> {
			catalog.put(tenant.getKey(), tenant);
		});
	}

	@Override
	public boolean isTenantKnown(String tenantId) {
		if (tenantId == null) {
			return false;
		}
		if (catalog.containsKey(tenantId)) {
			return true;
		}
		if (tenantBaseAlias != null) {
			String normalizado = tenantBaseAlias.normalizeCompositeToInternal(tenantId.trim());
			if (catalog.containsKey(normalizado)) {
				return true;
			}
			if (tenantBaseAlias.isBaseTenant(tenantId)) {
				return catalog.containsKey(tenantBaseAlias.getDefaultTenantId());
			}
		}
		return false;
	}

	@Override
	public Optional<TenantDTO> resolve(String tenantId) {
		if (tenantId == null) {
			return Optional.empty();
		}
		TenantDTO directo = catalog.get(tenantId);
		if (directo != null) {
			return Optional.of(directo);
		}
		if (tenantBaseAlias != null) {
			String normalizado = tenantBaseAlias.normalizeCompositeToInternal(tenantId.trim());
			TenantDTO normal = catalog.get(normalizado);
			if (normal != null) {
				return Optional.of(normal);
			}
			if (tenantBaseAlias.isBaseTenant(tenantId)) {
				return Optional.ofNullable(catalog.get(tenantBaseAlias.getDefaultTenantId()));
			}
		}
		return Optional.empty();
	}

	// ✅ método nuevo para registrar en caliente
	public void register(TenantDTO tenant) {
		catalog.put(tenant.getKey(), tenant);
	}

	// ✅ método nuevo para eliminar en caliente
	public void unregister(String tenantId) {
		catalog.remove(tenantId);
	}

	private TenantDTO jdbcFromLegacyDbProperties() {
		TenantDTO p = new TenantDTO();
		p.setDriver(env.getProperty("db.driver"));
		p.setDatasourceUrl(env.getProperty("db.url"));
		p.setDatasourceUsername(env.getProperty("db.username"));
		p.setDatasourcePassword(env.getProperty("db.password"));
		return p;
	}

}
