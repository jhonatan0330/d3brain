package d3.configuration.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadFilterDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

import org.springframework.context.annotation.Lazy;

@Service
public class PropertyCRUDSvc {

	private final PropiedadSvc propertyService;

	public PropertyCRUDSvc(@Lazy PropiedadSvc propertyService) {
		this.propertyService = propertyService;
	}

	public void inactivateAllPropertiesOfUser(String userId, String token) throws ServerException {

		PropiedadFilterDTO filter = new PropiedadFilterDTO();
		filter.setUsuario(userId);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setPaginacionRegistroFinal(1000);
		List<PropiedadDTO> propertiesToInactivate = propertyService.listarConsulta(filter);
		if (propertiesToInactivate == null)
			propertiesToInactivate = new ArrayList<>();

		filter.setUsuario(null);
		filter.setUsuarioExcluyente(userId);

		propertiesToInactivate.addAll(propertyService.listarConsulta(filter));

		if (propertiesToInactivate == null || propertiesToInactivate.size() == 0)
			return;

		for (PropiedadDTO iProperty : propertiesToInactivate) {
			propertyService.inactivar(iProperty, token);
		}
	}

	public void inactivateAllPropertiesOfRol(String rolId, String token) throws ServerException {

		PropiedadFilterDTO filter = new PropiedadFilterDTO();
		filter.setRol(rolId);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setPaginacionRegistroFinal(1000);
		List<PropiedadDTO> propertiesToInactivate = propertyService.listarConsulta(filter);
		if (propertiesToInactivate == null)
			propertiesToInactivate = new ArrayList<>();

		filter.setRol(null);
		filter.setRolExcluyente(rolId);

		propertiesToInactivate.addAll(propertyService.listarConsulta(filter));

		if (propertiesToInactivate == null || propertiesToInactivate.size() == 0)
			return;

		for (PropiedadDTO iProperty : propertiesToInactivate) {
			propertyService.inactivar(iProperty, token);
		}
	}
}
