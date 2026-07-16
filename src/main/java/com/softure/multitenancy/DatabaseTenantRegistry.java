package com.softure.multitenancy;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.softure.multitenancy.domain.TenantFilterDTO;
import com.softure.multitenancy.infrastructure.TenantMapper;

@Component
public class DatabaseTenantRegistry implements TenantRegistry {

	private final TenantMapper tenantMapper;
	private final Set<String> registeredTenants = ConcurrentHashMap.newKeySet();
	private final TenantMetadataProvider metadataProvider; // ✅ agregar dependencia
	private volatile boolean loaded = false;

	public DatabaseTenantRegistry(@Lazy TenantMapper tenantMapper, TenantMetadataProvider metadataProvider) {
		this.tenantMapper = tenantMapper;
		this.metadataProvider = metadataProvider;
	}

	private void loadIfNeeded() {
		if (!loaded) {
			synchronized (this) {
				if (!loaded) {
					registeredTenants.add("default");
					tenantMapper.getMany(new TenantFilterDTO()).forEach(tenant -> {
						registeredTenants.add(tenant.getKey());
						// ✅ sincroniza el catalog al mismo tiempo
						if (metadataProvider instanceof DatabaseTenantMetadataProvider imp) {
							imp.register(tenant);
						}
					});
					loaded = true;
				}
			}
		}
	}

	@Override
	public boolean isRegistered(String tenantId) {
		loadIfNeeded(); // ✅ carga lazy en el primer uso
		return registeredTenants.contains(tenantId);
	}

	@Override
	public Set<String> getRegisteredTenants() {
		loadIfNeeded(); // ✅ carga lazy en el primer uso
		return Collections.unmodifiableSet(registeredTenants);
	}

	public void register(String tenantId) {
		registeredTenants.add(tenantId);
	}

	public void unregister(String tenantId) {
		registeredTenants.remove(tenantId);
	}

}