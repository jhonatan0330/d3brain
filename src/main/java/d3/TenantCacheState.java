package d3;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import d3.authentication.domain.UsuarioSesionDTO;
import d3.authorization.domain.RolAccesoDTO;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;

/**
 * In-memory cache state for a single tenant. One instance per tenant id in
 * {@link CacheManager}.
 */
final class TenantCacheState {

	private volatile List<PropiedadValorDefinidoDTO> types;

	private final Map<String, List<PropiedadDTO>> propByTypeMap = new ConcurrentHashMap<>();
	private final Map<String, List<String>> userRoleMap = new ConcurrentHashMap<>();
	private final Map<String, List<PropiedadDTO>> propByKey = new ConcurrentHashMap<>();

	private volatile String mainOrganization;
	private volatile String mainUser;
	private volatile String mainUserMail;

	private final Map<String, UsuarioSesionDTO> sessionMap = new ConcurrentHashMap<>();
	private final Map<String, Integer> sessionTimeMap = new ConcurrentHashMap<>();

	private final Map<String, DocumentoPlantillaCaracteristicaDTO> fieldsMap = new ConcurrentHashMap<>();
	private final Map<String, RolAccesoDTO> rolesMap = new ConcurrentHashMap<>();

	List<PropiedadValorDefinidoDTO> getTypes() {
		return types;
	}

	void setTypes(List<PropiedadValorDefinidoDTO> types) {
		this.types = types;
	}

	Map<String, List<PropiedadDTO>> getPropByTypeMap() {
		return propByTypeMap;
	}

	Map<String, List<String>> getUserRoleMap() {
		return userRoleMap;
	}

	Map<String, List<PropiedadDTO>> getPropByKey() {
		return propByKey;
	}

	String getMainOrganization() {
		return mainOrganization;
	}

	void setMainOrganization(String mainOrganization) {
		this.mainOrganization = mainOrganization;
	}

	String getMainUser() {
		return mainUser;
	}

	void setMainUser(String mainUser) {
		this.mainUser = mainUser;
	}

	String getMainUserMail() {
		return mainUserMail;
	}

	void setMainUserMail(String mainUserMail) {
		this.mainUserMail = mainUserMail;
	}

	Map<String, UsuarioSesionDTO> getSessionMap() {
		return sessionMap;
	}

	Map<String, Integer> getSessionTimeMap() {
		return sessionTimeMap;
	}

	Map<String, DocumentoPlantillaCaracteristicaDTO> getFieldsMap() {
		return fieldsMap;
	}

	Map<String, RolAccesoDTO> getRolesMap() {
		return rolesMap;
	}

	void clearAll() {
		types = null;
		propByTypeMap.clear();
		userRoleMap.clear();
		propByKey.clear();
		mainOrganization = null;
		mainUser = null;
		mainUserMail = null;
		sessionMap.clear();
		sessionTimeMap.clear();
		fieldsMap.clear();
		rolesMap.clear();
	}
}
