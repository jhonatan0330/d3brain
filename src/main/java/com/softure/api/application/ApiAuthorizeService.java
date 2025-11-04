package com.softure.api.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.document_execution.application.field.Propiedades;

@Service
public class ApiAuthorizeService {

	@Autowired @Lazy  OrganizacionSvc organizationService;
	
	private String apiKeyOrganization;

	public void call(String apiKey, String token) throws ServerException {
		String user = null; //queda pendiente el tema de validar el usuario
		if(apiKey==null || apiKey.isEmpty()) throw new ServerException("Ingresa el codigo de la app asignado");
		if(token!=null) user = organizationService.getUserFlex(token);
		if(apiKeyOrganization==null) getApiKeyHeader(user);
		if(apiKeyOrganization==null) throw new ServerException("Notifica al administrador que no se encuentra configurada la clave de acceso x-api-key en la organizacion");
		if(apiKey.compareTo(apiKeyOrganization)!=0) throw new ServerException("El x-api-key que envias no concuerda con el registrado en la plataforma: " + apiKey);
		return;
	}
	
	private void getApiKeyHeader(String user) throws ServerException {
		OrganizacionDTO org = organizationService.obtenerPrincipalPropiedades(user);
		apiKeyOrganization = Propiedades.obtenerValor(org, Propiedades.API_KEY);
		if(apiKeyOrganization!=null && apiKeyOrganization.isEmpty()) apiKeyOrganization = null;
	}
}
