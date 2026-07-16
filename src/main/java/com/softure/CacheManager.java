package com.softure;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.multitenancy.TenantContext;
import com.softure.multitenancy.TenantDataSourcesConfigurationProperties;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

/**
 * Tenant-scoped in-memory cache. Resolves the active bucket from
 * {@link TenantContext}; when absent (scheduled tasks, startup), uses
 * {@link TenantDataSourcesConfigurationProperties#getDefaultTenantId()}.
 */
@Component
public class CacheManager {

	private final ConcurrentHashMap<String, TenantCacheState> byTenant = new ConcurrentHashMap<>();
	private final String defaultTenantId;
	private final int maxCachedTenants;
	private final Object evictionLock = new Object();

	public CacheManager(TenantDataSourcesConfigurationProperties tenantProperties) {
		this.defaultTenantId = tenantProperties.getDefaultTenantId();
		this.maxCachedTenants = tenantProperties.getCacheMaxEntries();
	}

	private String resolveTenantId() {
		String tenantId = TenantContext.getCurrentTenant();
		if (tenantId == null || tenantId.isBlank()) {
			return defaultTenantId;
		}
		return tenantId;
	}

	private TenantCacheState current() {
		String tenantId = resolveTenantId();
		enforceCacheLimitBeforeNewTenant(tenantId);
		return byTenant.computeIfAbsent(tenantId, id -> new TenantCacheState());
	}

	private void enforceCacheLimitBeforeNewTenant(String tenantId) {
		if (maxCachedTenants <= 0 || byTenant.containsKey(tenantId)) {
			return;
		}
		while (maxCachedTenants > 0 && byTenant.size() >= maxCachedTenants && !byTenant.containsKey(tenantId)) {
			synchronized (evictionLock) {
				if (byTenant.size() < maxCachedTenants || byTenant.containsKey(tenantId)) {
					break;
				}
				if (!evictOneNonDefaultTenant()) {
					break;
				}
			}
		}
	}

	private boolean evictOneNonDefaultTenant() {
		for (String key : byTenant.keySet()) {
			if (!key.equals(defaultTenantId)) {
				byTenant.remove(key);
				return true;
			}
		}
		return false;
	}

	// ---------------------------------------------
	// GETTERS & SETTERS (tenant-scoped)
	// ---------------------------------------------

	public List<PropiedadValorDefinidoDTO> getTypes() {
		return current().getTypes();
	}

	public void setTypes(List<PropiedadValorDefinidoDTO> types) {
		current().setTypes(types);
	}

	public Map<String, List<PropiedadDTO>> getPropByTypeMap() {
		return current().getPropByTypeMap();
	}

	public void setPropByTypeMap(Map<String, List<PropiedadDTO>> propByTypeMap) {
		current().getPropByTypeMap().clear();
		if (propByTypeMap != null) {
			current().getPropByTypeMap().putAll(propByTypeMap);
		}
	}

	public Map<String, List<String>> getUserRoleMap() {
		return current().getUserRoleMap();
	}

	public void setUserRoleMap(Map<String, List<String>> userRoleMap) {
		current().getUserRoleMap().clear();
		if (userRoleMap != null) {
			current().getUserRoleMap().putAll(userRoleMap);
		}
	}

	public Map<String, List<PropiedadDTO>> getPropByKey() {
		return current().getPropByKey();
	}

	public void setPropByKey(Map<String, List<PropiedadDTO>> propByKey) {
		current().getPropByKey().clear();
		if (propByKey != null) {
			current().getPropByKey().putAll(propByKey);
		}
	}

	public String getMainOrganization() {
		return current().getMainOrganization();
	}

	public void setMainOrganization(String mainOrganization) {
		current().setMainOrganization(mainOrganization);
	}

	public String getMainUser() {
		return current().getMainUser();
	}

	public void setMainUser(String mainUser) {
		current().setMainUser(mainUser);
	}

	public String getMainUserMail() {
		return current().getMainUserMail();
	}

	public void setMainUserMail(String mainUserMail) {
		current().setMainUserMail(mainUserMail);
	}

	public Map<String, UsuarioSesionDTO> getSessionMap() {
		return current().getSessionMap();
	}

	public void setSessionMap(Map<String, UsuarioSesionDTO> sessionMap) {
		current().getSessionMap().clear();
		if (sessionMap != null) {
			current().getSessionMap().putAll(sessionMap);
		}
	}

	public Map<String, Integer> getSessionTimeMap() {
		return current().getSessionTimeMap();
	}

	public void setSessionTimeMap(Map<String, Integer> sessionTimeMap) {
		current().getSessionTimeMap().clear();
		if (sessionTimeMap != null) {
			current().getSessionTimeMap().putAll(sessionTimeMap);
		}
	}

	public Map<String, DocumentoPlantillaCaracteristicaDTO> getFieldsMap() {
		return current().getFieldsMap();
	}

	public void setFieldsMap(Map<String, DocumentoPlantillaCaracteristicaDTO> fieldsMap) {
		current().getFieldsMap().clear();
		if (fieldsMap != null) {
			current().getFieldsMap().putAll(fieldsMap);
		}
	}

	public Map<String, RolAccesoDTO> getRolesMap() {
		return current().getRolesMap();
	}

	public void setRolesMap(Map<String, RolAccesoDTO> rolesMap) {
		current().getRolesMap().clear();
		if (rolesMap != null) {
			current().getRolesMap().putAll(rolesMap);
		}
	}

	// ---------------------------------------------
	// CLEAR FUNCTIONS (current tenant)
	// ---------------------------------------------

	public void clearTypes() {
		current().setTypes(null);
	}

	public void clearPropByTypeMap() {
		current().getPropByTypeMap().clear();
	}

	public void clearUserRoleMap() {
		current().getUserRoleMap().clear();
	}

	public void clearPropByKey() {
		current().getPropByKey().clear();
	}

	public void clearMainOrganization() {
		current().setMainOrganization(null);
	}

	public void clearMainUser() {
		current().setMainUser(null);
	}

	public void clearMainUserMail() {
		current().setMainUserMail(null);
	}

	public void clearSessionMap() {
		current().getSessionMap().clear();
	}

	public void clearSessionTimeMap() {
		current().getSessionTimeMap().clear();
	}

	public void clearFieldsMap() {
		current().getFieldsMap().clear();
	}

	public void clearRolesMap() {
		current().getRolesMap().clear();
	}

	// ----------------------------------------------------------
	// MÉTODOS PUT / GET PERSONALIZADOS
	// ----------------------------------------------------------

	public void putPropByType(String type, List<PropiedadDTO> propiedades) {
		current().getPropByTypeMap().put(type, propiedades);
	}

	public List<PropiedadDTO> getPropByType(String type) {
		return current().getPropByTypeMap().get(type);
	}

	public void putUserRoles(String user, List<String> roles) {
		current().getUserRoleMap().put(user, roles);
	}

	public List<String> getUserRoles(String user) {
		return current().getUserRoleMap().get(user);
	}

	public void putPropByKey(String key, List<PropiedadDTO> propiedades) {
		current().getPropByKey().put(key, propiedades);
	}

	public List<PropiedadDTO> getPropByKeyValue(String key) {
		return current().getPropByKey().get(key);
	}

	public void putSession(String sessionId, UsuarioSesionDTO session) {
		TenantCacheState state = current();
		state.getSessionMap().put(sessionId, session);
		System.out.println(new Date().toString() + " SESSION tenant=" + resolveTenantId() + " CACHE token: "
				+ state.getSessionMap().size());
	}

	public void removeSession(String sessionId) {
		TenantCacheState state = current();
		state.getSessionMap().remove(sessionId);
		System.out.println(new Date().toString() + " SESSION tenant=" + resolveTenantId() + " RETIRANDO token: "
				+ state.getSessionMap().size());
	}

	public UsuarioSesionDTO getSession(String sessionId) {
		return current().getSessionMap().get(sessionId);
	}

	public void putSessionTime(String sessionId, Integer time) {
		current().getSessionTimeMap().put(sessionId, time);
	}

	public Integer getSessionTime(String sessionId) {
		return current().getSessionTimeMap().get(sessionId);
	}

	public void putField(String key, DocumentoPlantillaCaracteristicaDTO field) {
		current().getFieldsMap().put(key, field);
	}

	public DocumentoPlantillaCaracteristicaDTO getField(String key) {
		return current().getFieldsMap().get(key);
	}

	public void putRole(String key, RolAccesoDTO field) {
		current().getRolesMap().put(key, field);
	}

	public RolAccesoDTO getRole(String key) {
		return current().getRolesMap().get(key);
	}

	// ---------------------------------------------
	// CLEAR ALL
	// ---------------------------------------------

	/** Clears cache for the current tenant only. */
	public void clearAll() {
		current().clearAll();
	}

	/** Clears cache for a specific tenant (e.g. after metadata sync). */
	public void clearTenant(String tenantId) {
		if (tenantId != null) {
			byTenant.remove(tenantId);
		}
	}

	/** Clears in-memory cache for every tenant. */
	public void clearAllTenants() {
		byTenant.clear();
	}
}
