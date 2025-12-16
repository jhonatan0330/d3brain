package com.softure;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Component
public class CacheManager {

	private List<PropiedadValorDefinidoDTO> types;

	private Map<String, List<PropiedadDTO>> propByTypeMap = new HashMap<>();
	private Map<String, List<String>> userRoleMap = new HashMap<>();
	private Map<String, List<PropiedadDTO>> propByKey = new HashMap<>();

	private String mainOrganization;
	private String mainUser;
	private String mainUserMail;

	private Map<String, UsuarioSesionDTO> sessionMap = new HashMap<>();
	private Map<String, Integer> sessionTimeMap = new HashMap<>();

	private Map<String, DocumentoPlantillaCaracteristicaDTO> fieldsMap = new HashMap<>();

	private Map<String, RolAccesoDTO> rolesMap = new HashMap<>();

	// ---------------------------------------------
	// GETTERS & SETTERS
	// ---------------------------------------------

	public List<PropiedadValorDefinidoDTO> getTypes() {
		return types;
	}

	public void setTypes(List<PropiedadValorDefinidoDTO> types) {
		this.types = types;
	}

	public Map<String, List<PropiedadDTO>> getPropByTypeMap() {
		return propByTypeMap;
	}

	public void setPropByTypeMap(Map<String, List<PropiedadDTO>> propByTypeMap) {
		this.propByTypeMap = propByTypeMap;
	}

	public Map<String, List<String>> getUserRoleMap() {
		return userRoleMap;
	}

	public void setUserRoleMap(Map<String, List<String>> userRoleMap) {
		this.userRoleMap = userRoleMap;
	}

	public Map<String, List<PropiedadDTO>> getPropByKey() {
		return propByKey;
	}

	public void setPropByKey(Map<String, List<PropiedadDTO>> propByKey) {
		this.propByKey = propByKey;
	}

	public String getMainOrganization() {
		return mainOrganization;
	}

	public void setMainOrganization(String mainOrganization) {
		this.mainOrganization = mainOrganization;
	}

	public String getMainUser() {
		return mainUser;
	}

	public void setMainUser(String mainUser) {
		this.mainUser = mainUser;
	}

	public String getMainUserMail() {
		return mainUserMail;
	}

	public void setMainUserMail(String mainUserMail) {
		this.mainUserMail = mainUserMail;
	}

	public Map<String, UsuarioSesionDTO> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, UsuarioSesionDTO> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public Map<String, Integer> getSessionTimeMap() {
		return sessionTimeMap;
	}

	public void setSessionTimeMap(Map<String, Integer> sessionTimeMap) {
		this.sessionTimeMap = sessionTimeMap;
	}

	public Map<String, DocumentoPlantillaCaracteristicaDTO> getFieldsMap() {
		return fieldsMap;
	}

	public void setFieldsMap(Map<String, DocumentoPlantillaCaracteristicaDTO> fieldsMap) {
		this.fieldsMap = fieldsMap;
	}

	public Map<String, RolAccesoDTO> getRolesMap() {
		return rolesMap;
	}

	public void setRolesMap(Map<String, RolAccesoDTO> rolesMap) {
		this.rolesMap = rolesMap;
	}

	// ---------------------------------------------
	// CLEAR FUNCTIONS (por campo)
	// ---------------------------------------------

	public void clearTypes() {
		this.types = null;
	}

	public void clearPropByTypeMap() {
		this.propByTypeMap.clear();
	}

	public void clearUserRoleMap() {
		this.userRoleMap.clear();
	}

	public void clearPropByKey() {
		this.propByKey.clear();
	}

	public void clearMainOrganization() {
		this.mainOrganization = null;
	}

	public void clearMainUser() {
		this.mainUser = null;
	}

	public void clearMainUserMail() {
		this.mainUserMail = null;
	}

	public void clearSessionMap() {
		this.sessionMap.clear();
	}

	public void clearSessionTimeMap() {
		this.sessionTimeMap.clear();
	}

	public void clearFieldsMap() {
		this.fieldsMap.clear();
	}

	public void clearRolesMap() {
		this.rolesMap.clear();
	}

	// ----------------------------------------------------------
	// MÉTODOS PUT / GET PERSONALIZADOS
	// ----------------------------------------------------------

	// ---- propByTypeMap ----
	public void putPropByType(String type, List<PropiedadDTO> propiedades) {
		propByTypeMap.put(type, propiedades);
	}

	public List<PropiedadDTO> getPropByType(String type) {
		return propByTypeMap.get(type);
	}

	// ---- userRoleMap ----
	public void putUserRoles(String user, List<String> roles) {
		userRoleMap.put(user, roles);
	}

	public List<String> getUserRoles(String user) {
		return userRoleMap.get(user);
	}

	// ---- propByKey ----
	public void putPropByKey(String key, List<PropiedadDTO> propiedades) {
		propByKey.put(key, propiedades);
	}

	public List<PropiedadDTO> getPropByKeyValue(String key) {
		return propByKey.get(key);
	}

	// ---- sessionMap ----
	public void putSession(String sessionId, UsuarioSesionDTO session) {
		sessionMap.put(sessionId, session);
		System.out.println(new Date().toString() + "SESSION ***************** CACHE token: " + sessionMap.size());
	}

	public void removeSession(String sessionId) {
		sessionMap.remove(sessionId);
		System.out.println(new Date().toString() + "SESSION ***************** RETIRANDO token: " + sessionMap.size());
	}

	public UsuarioSesionDTO getSession(String sessionId) {
		return sessionMap.get(sessionId);
	}

	// ---- sessionTimeMap ----
	public void putSessionTime(String sessionId, Integer time) {
		sessionTimeMap.put(sessionId, time);
	}

	public Integer getSessionTime(String sessionId) {
		return sessionTimeMap.get(sessionId);
	}

	// ---- fieldsMap ----
	public void putField(String key, DocumentoPlantillaCaracteristicaDTO field) {
		fieldsMap.put(key, field);
	}

	public DocumentoPlantillaCaracteristicaDTO getField(String key) {
		return fieldsMap.get(key);
	}

	// ---- fieldsMap ----
	public void putRole(String key, RolAccesoDTO field) {
		rolesMap.put(key, field);
	}

	public RolAccesoDTO getRole(String key) {
		return rolesMap.get(key);
	}

	// ---------------------------------------------
	// CLEAR ALL (LIMPIA)
	// ---------------------------------------------

	public void clearAll() {
		this.types = null;

		this.propByTypeMap.clear();
		this.userRoleMap.clear();
		this.propByKey.clear();

		this.mainOrganization = null;
		this.mainUser = null;
		this.mainUserMail = null;

		this.sessionMap.clear();
		this.sessionTimeMap.clear();

		this.fieldsMap.clear();
		this.rolesMap.clear();
	}
}
