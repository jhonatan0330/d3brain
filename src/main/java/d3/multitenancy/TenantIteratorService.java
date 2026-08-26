package d3.multitenancy;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class TenantIteratorService {

	private final TenantRegistry tenantRegistry;

	public TenantIteratorService(TenantRegistry tenantRegistry) {
		this.tenantRegistry = tenantRegistry;
	}

	public void executeForAllTenants(TenantTask task) {
		Set<String> tenants = tenantRegistry.getRegisteredTenants();
		for (String tenantId : tenants) {
			try {
				TenantContext.setCurrentTenant(tenantId);
				task.execute(tenantId);
			} catch (Exception e) {
				// Un tenant no detiene los demás
				System.err.println("Error en tenant " + tenantId + ": " + e.getMessage());
			} finally {
				TenantContext.clear();
			}
		}
	}

	@FunctionalInterface
	public interface TenantTask {
		void execute(String tenantId) throws Exception;
	}
}