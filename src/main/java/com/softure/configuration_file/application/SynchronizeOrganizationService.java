package com.softure.configuration_file.application;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeOrganizationService {

	@Autowired @Lazy 
	OrganizacionSvc organizationService;
	@Autowired @Lazy 
	SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		if (hierarchy.getOrganization() == null)
			return;
		log.setRoot("SynchronizeOrganization");
		OrganizacionDTO mainOrganization = organizationService.obtenerPrincipal();
		// hierarchy.getOrganization().setLlaveTabla(mainOrganization.getLlaveTabla());
		propertiesSynchronizeService.call(hierarchy, mainOrganization.getLlaveTabla(),
				PropiedadValorDefinidoDTO.ORGANIZACION, hierarchy.getOrganization().getLlaveTabla(), token, log,
				compare);
	}

}
