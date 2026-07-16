package com.softure.multitenancy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.softure.multitenancy.domain.TenantDTO;

/**
 * Routes JDBC connections using a {@link ConcurrentHashMap} cache of per-tenant
 * pools. Pools are created on first use (lazy). Optional
 * {@link TenantDataSourcesConfigurationProperties#getCacheMaxEntries()}
 * enforces an upper bound with eviction of non-default tenants. Shuts down
 * pools on container destroy to avoid leaks.
 */
public class TenantRoutingDataSource extends AbstractRoutingDataSource implements DisposableBean {

	private final ConcurrentHashMap<String, DataSource> cache = new ConcurrentHashMap<>();
	private final Object evictionLock = new Object();
	private final TenantMetadataProvider metadataProvider;
	private final TenantDataSourceFactory factory;
	private final String defaultTenantId;
	private final DataSource defaultTenantPhysicalDataSource;
	private final int maxCacheEntries;

	public TenantRoutingDataSource(TenantMetadataProvider metadataProvider, TenantDataSourceFactory factory,
			TenantDataSourcesConfigurationProperties props, DataSource defaultTenantPhysicalDataSource) {
		this.metadataProvider = metadataProvider;
		this.factory = factory;
		this.defaultTenantId = props.getDefaultTenantId();
		this.defaultTenantPhysicalDataSource = defaultTenantPhysicalDataSource;
		this.maxCacheEntries = props.getCacheMaxEntries();
		setTargetDataSources(Collections.emptyMap());
		setDefaultTargetDataSource(defaultTenantPhysicalDataSource);
		try {
			afterPropertiesSet();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		cache.put(defaultTenantId, defaultTenantPhysicalDataSource);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return TenantContext.getCurrentTenant();
	}

	@Override
	protected DataSource determineTargetDataSource() {
		Object lookupKey = determineCurrentLookupKey();
		if (lookupKey == null) {
			return super.determineTargetDataSource();
		}
		return resolveDataSource(lookupKey.toString());
	}

	private DataSource resolveDataSource(String tenantId) {
		enforceCacheLimitBeforeNewTenant(tenantId);
		return cache.computeIfAbsent(tenantId, this::createPoolForTenant);
	}

	private void enforceCacheLimitBeforeNewTenant(String tenantId) {
		if (maxCacheEntries <= 0 || cache.containsKey(tenantId)) {
			return;
		}
		while (maxCacheEntries > 0 && cache.size() >= maxCacheEntries && !cache.containsKey(tenantId)) {
			synchronized (evictionLock) {
				if (cache.size() < maxCacheEntries || cache.containsKey(tenantId)) {
					break;
				}
				if (!evictOneNonDefaultTenant()) {
					throw new IllegalStateException(
							"Tenant cache exhausted (max=" + maxCacheEntries + "). Cannot cache tenant: " + tenantId);
				}
			}
		}
	}

	private boolean evictOneNonDefaultTenant() {
		for (String key : cache.keySet()) {
			if (!key.equals(defaultTenantId)) {
				DataSource removed = cache.remove(key);
				closeQuietly(removed);
				return true;
			}
		}
		return false;
	}

	private DataSource createPoolForTenant(String tenantId) {
		if (defaultTenantId.equals(tenantId)) {
			return defaultTenantPhysicalDataSource;
		}
		TenantDTO jdbc = metadataProvider.resolve(tenantId)
				.orElseThrow(() -> new IllegalStateException("Unknown tenant: " + tenantId));
		return factory.createPooledDataSource(jdbc);
	}

	private static void closeQuietly(DataSource ds) {
		if (ds instanceof PooledDataSource pooled) {
			pooled.forceCloseAll();
		}
	}

	@Override
	public void destroy() {
		Set<DataSource> closed = Collections.newSetFromMap(new IdentityHashMap<>());
		for (DataSource ds : new ArrayList<>(cache.values())) {
			if (closed.add(ds)) {
				closeQuietly(ds);
			}
		}
		cache.clear();
	}
}
