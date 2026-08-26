package d3.multitenancy;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tenant")
public class TenantDataSourcesConfigurationProperties {

	/**
	 * Logical id used when the header is absent and {@link #required} is false, and
	 * for the eagerly created default pool.
	 */
	private String defaultTenantId = "default";

	private String headerName = "X-Tenant-ID";

	/**
	 * If true, requests without a tenant header receive HTTP 400 (except paths
	 * skipped by {@link TenantFilter}).
	 */
	private boolean required = false;

	/**
	 * Maximum number of tenant {@link javax.sql.DataSource} instances kept in
	 * memory. {@code 0} means unlimited. When full, least recently added
	 * non-default tenants may be evicted before caching a new id.
	 */
	private int cacheMaxEntries = 0;

	public String getDefaultTenantId() {
		return defaultTenantId;
	}

	public void setDefaultTenantId(String defaultTenantId) {
		this.defaultTenantId = defaultTenantId;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getCacheMaxEntries() {
		return cacheMaxEntries;
	}

	public void setCacheMaxEntries(int cacheMaxEntries) {
		this.cacheMaxEntries = cacheMaxEntries;
	}
}
