package d3.multitenancy.application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantFilterDTO;
import d3.multitenancy.domain.TenantMetadataProvider;
import d3.multitenancy.domain.TenantRegistry;
import d3.multitenancy.infrastructure.TenantMapper;
import d3.shared.domain.SharedConstants;

@Component
public class TenantIteratorService {

	private final TenantRegistry tenantRegistry;
	private final TenantMapper tenantMapper;
	private final TenantMetadataProvider metadataProvider;

	public TenantIteratorService(TenantRegistry tenantRegistry, @Lazy TenantMapper tenantMapper,
			TenantMetadataProvider metadataProvider) {
		this.tenantRegistry = tenantRegistry;
		this.tenantMapper = tenantMapper;
		this.metadataProvider = metadataProvider;
	}

	public void executeForAllTenants(TenantTask task) {
		String corrida = UUID.randomUUID().toString().substring(0, 8);
		Set<String> visitados = ConcurrentHashMap.newKeySet();
		Set<String> urlsVistas = ConcurrentHashMap.newKeySet();
		Deque<String> pendientes = new ArrayDeque<>();
		for (String tenant : tenantRegistry.getRegisteredTenants()) {
			String normalizado = normalizarTenant(tenant);
			if (!normalizado.isEmpty() && !visitados.contains(normalizado)) {
				pendientes.add(normalizado);
			}
		}
		System.out.println("******* CRON [" + corrida + "] inicio, pendientes=" + pendientes.size() + " ***" + new Date());
		int ejecutados = 0;
		int omitidos = 0;
		while (!pendientes.isEmpty()) {
			String tenantId = pendientes.poll();
			if (!visitados.add(tenantId)) {
				continue;
			}
			String url = resolverUrl(tenantId);
			String urlNormalizada = normalizarUrl(url);
			if (urlNormalizada != null && !urlsVistas.add(urlNormalizada)) {
				omitidos++;
				System.out.println("******* CRON [" + corrida + "] OMITIDO duplicado tenant=" + tenantId
						+ " url=" + urlNormalizada + " ***" + new Date());
				continue;
			}
			try {
				TenantContext.setCurrentTenant(tenantId);
				task.execute(tenantId);
				ejecutados++;
				System.out.println("******* CRON [" + corrida + "] OK tenant=" + tenantId
						+ " url=" + urlNormalizada + " ***" + new Date());
				for (TenantDTO hijo : listarHijos()) {
					String composite = tenantId.equals("default") ? hijo.getKey()
							: tenantId + "/" + hijo.getKey();
					composite = normalizarTenant(composite);
					hijo.setKey(composite);
					registrar(composite, hijo);
					if (!visitados.contains(composite)) {
						pendientes.add(composite);
					}
				}
			} catch (Exception e) {
				System.err.println("Error en tenant " + tenantId + ": " + e.getMessage());
			} finally {
				TenantContext.clear();
			}
		}
		System.out.println("******* CRON [" + corrida + "] fin, ejecutados=" + ejecutados + " omitidos=" + omitidos
				+ " visitados=" + visitados.size() + " ***" + new Date());
	}

	private List<TenantDTO> listarHijos() {
		try {
			TenantFilterDTO filter = new TenantFilterDTO();
			filter.setState(SharedConstants.STATE_ACTIVE);
			List<TenantDTO> hijos = tenantMapper.getMany(filter);
			return hijos != null ? hijos : new ArrayList<>();
		} catch (Exception e) {
			System.err.println("No se pudieron listar hijos de " + TenantContext.getCurrentTenant() + ": "
					+ e.getMessage());
			return new ArrayList<>();
		}
	}

	private String resolverUrl(String tenantId) {
		try {
			return metadataProvider.resolve(tenantId).map(TenantDTO::getDatasourceUrl).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	private void registrar(String composite, TenantDTO tenant) {
		if (metadataProvider instanceof DatabaseTenantMetadataProvider provider) {
			provider.register(tenant);
		}
		if (tenantRegistry instanceof DatabaseTenantRegistry registry) {
			registry.register(composite);
		}
	}

	private String normalizarTenant(String tenant) {
		if (tenant == null) {
			return "";
		}
		return tenant.trim().replaceAll("/+", "/").replaceAll("^/|/$", "");
	}

	private String normalizarUrl(String url) {
		if (url == null) {
			return null;
		}
		String normalizada = url.trim();
		return normalizada.isEmpty() ? null : normalizada;
	}

	@FunctionalInterface
	public interface TenantTask {
		void execute(String tenantId) throws Exception;
	}
}