package d3.multitenancy;

import java.util.Optional;

import d3.multitenancy.domain.TenantDTO;

/**
 * Resolves JDBC metadata for a tenant id. Production implementations may load
 * from a registry database, HTTP service, or config server; this module ships
 * an in-memory stub.
 */
public interface TenantMetadataProvider {

	boolean isTenantKnown(String tenantId);

	Optional<TenantDTO> resolve(String tenantId);

}
