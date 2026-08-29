package d3.configuration.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.authorization.application.RolAccesoSvc;
import d3.authorization.domain.RolAccesoDTO;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.property.domain.PropiedadDTO;

@Service
public class SynchronizeRolService {

	private final RolAccesoSvc rolService;

	public SynchronizeRolService(@Lazy RolAccesoSvc rolService) {
		this.rolService = rolService;
	}

	public List<PropiedadDTO> call(String token, HierarchyExporterDTO hierarchy, List<PropiedadDTO> propierties,
			LogConfigurationDTO log, boolean compare) throws ServerException {
		List<RolAccesoDTO> localListToErase = rolService.getFullToSynchronize(null);
		List<RolAccesoDTO> remoteList = hierarchy.getRoles();
		List<PropiedadDTO> propertiesWithReplaceRol = new ArrayList<>();
		if (remoteList != null && !remoteList.isEmpty()) {
			log.setRoot("SynchronizeRolService");
			for (RolAccesoDTO remote : remoteList) {
				RolAccesoDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					log.info("EXIST ROL " + remote.getCodigo() + " - " + remote.getNombre());
					// propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
					// PropiedadValorDefinidoDTO.ROL, local.getLlaveTabla(), token, log, compare);
					boolean isToMigrate = false;
					for (PropiedadDTO iProperty : propierties) {
						isToMigrate = false;
						if (iProperty.getRol() != null && iProperty.getRol().compareTo(remote.getLlaveTabla()) == 0) {
							iProperty.setRol(local.getLlaveTabla());
							isToMigrate = true;
						}
						if (iProperty.getRolExcluyente() != null
								&& iProperty.getRolExcluyente().compareTo(remote.getLlaveTabla()) == 0) {
							iProperty.setRolExcluyente(local.getLlaveTabla());
							isToMigrate = true;
						}

						if (isToMigrate && iProperty.getEstado() == null) {
							propertiesWithReplaceRol.add(iProperty);
							iProperty.setEstado("YA");
						}
					}
				}
				// Uy seria muy raro else
			}
		}
		return propertiesWithReplaceRol;
	}

	private RolAccesoDTO findTemplateInList(List<RolAccesoDTO> array, String code) {
		if (array == null)
			return null;
		for (RolAccesoDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

}
