package com.softure.configuration_file.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeOrganizationService {

	@Autowired
	OrganizacionSvc organizationService;
	@Autowired
	SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		OrganizacionDTO mainOrganization = organizationService.obtenerPrincipalPropiedades(null);
		// hierarchy.getOrganization().setLlaveTabla(mainOrganization.getLlaveTabla());
		propertiesSynchronizeService.call(hierarchy.getProperties(), mainOrganization.getLlaveTabla(),
				PropiedadValorDefinidoDTO.ORGANIZACION, hierarchy.getOrganization().getLlaveTabla(), token);
	}


}
