package d3.multitenancy;

import java.util.Set;

/**
 * Reports whether a tenant id has a configured {@link javax.sql.DataSource}.
 */
public interface TenantRegistry {

	boolean isRegistered(String tenantId);

	Set<String> getRegisteredTenants();

}
