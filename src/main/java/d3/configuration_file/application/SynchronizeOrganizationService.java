package d3.configuration_file.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.configuration_file.domain.HierarchyExporterDTO;
import d3.configuration_file.domain.LogConfigurationDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;

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
