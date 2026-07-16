package com.softure.configuration_file.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeOrganizationService {

	private final OrganizacionSvc organizationService;
	private final SynchronizePropertiesService propertiesSynchronizeService;

	public SynchronizeOrganizationService(@Lazy OrganizacionSvc organizationService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService) {
		this.organizationService = organizationService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
	}

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
