package d3.multitenancy;

public class TenantNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String tenantId;

	public TenantNotFoundException(String tenantId) {
		super("Tenant not found: " + tenantId);
		this.tenantId = tenantId;
	}

	public String getTenantId() {
		return tenantId;
	}
}
