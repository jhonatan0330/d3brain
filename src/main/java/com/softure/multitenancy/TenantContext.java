package com.softure.multitenancy;

/**
 * Holds the current tenant id for the executing thread. Used by
 * {@link TenantRoutingDataSource} and must be cleared after each request.
 */
public final class TenantContext {

	private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

	private TenantContext() {
	}

	public static void setCurrentTenant(String tenantId) {
		CURRENT.set(tenantId);
	}

	public static String getCurrentTenant() {
		return CURRENT.get();
	}

	public static void clear() {
		CURRENT.remove();
	}
}
