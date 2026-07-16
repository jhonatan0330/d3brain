package com.softure.multitenancy;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Binds {@link TenantContext} from the {@code X-Tenant-ID} header
 * (configurable) for each request. Clears the context in a {@code finally}
 * block to avoid leaking tenant state across pooled threads.
 */
public class TenantFilter extends OncePerRequestFilter {

	private final TenantDataSourcesConfigurationProperties tenantProperties;
	private final TenantRegistry tenantRegistry;

	public TenantFilter(TenantDataSourcesConfigurationProperties tenantProperties, TenantRegistry tenantRegistry) {
		this.tenantProperties = tenantProperties;
		this.tenantRegistry = tenantRegistry;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.startsWith("/static/") || path.startsWith("/error");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String tenantId;
		try {
			tenantId = resolveTenantId(request);
		} catch (IllegalArgumentException ex) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
			return;
		}
		if (!tenantRegistry.isRegistered(tenantId)) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown tenant: " + tenantId);
			return;
		}
		TenantContext.setCurrentTenant(tenantId);
		try {
			filterChain.doFilter(request, response);
		} finally {
			TenantContext.clear();
		}
	}

	private String resolveTenantId(HttpServletRequest request) {
		String headerName = tenantProperties.getHeaderName();
		String raw = request.getHeader(headerName);
		if (raw == null || raw.isBlank()) {
			if (tenantProperties.isRequired()) {
				throw new IllegalArgumentException("Missing required header: " + headerName);
			}
			return tenantProperties.getDefaultTenantId();
		}
		return raw.trim();
	}
}
