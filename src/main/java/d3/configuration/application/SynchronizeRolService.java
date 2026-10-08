package d3.configuration.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.authorization.application.RolAccesoSvc;
import d3.authorization.domain.RolAccesoDTO;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.PropiedadDTO;

@Service
public class SynchronizeRolService {

	private final RolAccesoSvc rolService;

	public SynchronizeRolService(@Lazy RolAccesoSvc rolService) {
		this.rolService = rolService;
	}

	public List<PropiedadDTO> call(HierarchyExporterDTO hierarchy, List<PropiedadDTO> propierties,
			LogConfigurationDTO log) {
		List<RolAccesoDTO> localListToErase = rolService.getFullToSynchronize(null);
		List<RolAccesoDTO> remoteList = hierarchy.getRoles();
		List<PropiedadDTO> propertiesWithReplaceRol = new ArrayList<>();
		if (remoteList != null && !remoteList.isEmpty()) {
			log.setRoot("SynchronizeRolService");
			for (RolAccesoDTO remote : remoteList) {
				if (remote.getCodigo() == null || remote.getCodigo().isEmpty()) {
					log.warn("SKIP ROL SIN CODIGO - " + remote.getNombre());
					continue;
				}
				RolAccesoDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				if (local != null) {
					localListToErase.remove(local);
					log.info("EXIST ROL " + remote.getCodigo() + " - " + remote.getNombre());
				} else {
					RolAccesoDTO nuevo = new RolAccesoDTO();
					nuevo.setCodigo(remote.getCodigo());
					nuevo.setNombre(remote.getNombre());
					nuevo.setImagen(remote.getImagen());
					nuevo.setPlantilla(remote.getPlantilla());
					try {
						local = rolService.guardar(nuevo);
						localListToErase.add(local);
						log.info("NEW ROL " + remote.getCodigo() + " - " + remote.getNombre());
					} catch (Exception e) {
						log.error("NEW ROL " + remote.getCodigo() + " - " + remote.getNombre() + " : "
								+ e.getMessage());
						continue;
					}
				}
				if (propierties != null) {
					for (PropiedadDTO iProperty : propierties) {
						boolean isToMigrate = false;
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
