package d3.configuration.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.shared.domain.ServerException;

@Service
public class SynchronizeOrganizationService {

	private final OrganizacionSvc organizationService;
	private final SynchronizePropertiesService propertiesSynchronizeService;

	public SynchronizeOrganizationService(@Lazy OrganizacionSvc organizationService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService) {
		this.organizationService = organizationService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
	}

	public void call(HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare) throws ServerException {
		if (hierarchy.getOrganization() == null)
			return;
		log.setRoot("SynchronizeOrganization");
		OrganizacionDTO mainOrganization = organizationService.obtenerPrincipal();
		if (mainOrganization == null || mainOrganization.getLlaveTabla() == null)
			return;
		propertiesSynchronizeService.call(hierarchy, hierarchy.getOrganization().getLlaveTabla(),
				PropiedadValorDefinidoDTO.ORGANIZACION, mainOrganization.getLlaveTabla(), log, compare);
	}

}
