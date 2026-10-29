package d3.configuration.application.sinchronize;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.authorization.domain.RolAccesoDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.infrastructure.NodoHijoMapper;

@Service
public class SynchronizeRolService {

	private final NodoHijoMapper nodoHijoMapper;

	public SynchronizeRolService(@Lazy NodoHijoMapper nodoHijoMapper) {
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public List<PropiedadDTO> call( List<PropiedadDTO> propierties, LogConfigurationDTO log) {
		List<PropiedadDTO> propertiesWithReplaceRol = new ArrayList<>();
		if (propierties == null || propierties.isEmpty()) 
			return propertiesWithReplaceRol;
		List<RolAccesoDTO> localListToErase = nodoHijoMapper.listarRolesFull();
		//List<RolAccesoDTO> remoteList = hierarchy.getRoles();
		if (localListToErase != null && !localListToErase.isEmpty()) {
			log.setRoot("SynchronizeRolService");
			for (RolAccesoDTO remote : localListToErase) {
				/*if (remote.getCodigo() == null || remote.getCodigo().isEmpty()) {
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
				}*/
				
					for (PropiedadDTO iProperty : propierties) {
						boolean isToMigrate = false;
						if (iProperty.getRol() != null && iProperty.getRol().compareTo(remote.getCodigo()) == 0) {
							iProperty.setRol(remote.getLlaveTabla());
							isToMigrate = true;
						}
						if (iProperty.getRolExcluyente() != null
								&& iProperty.getRolExcluyente().compareTo(remote.getCodigo()) == 0) {
							iProperty.setRolExcluyente(remote.getLlaveTabla());
							isToMigrate = true;
						}
						if (isToMigrate && iProperty.getEstado() == null) {
							propertiesWithReplaceRol.add(iProperty);
							iProperty.setEstado("YA");
						}
					}
				
			}
		}
		return propertiesWithReplaceRol;
	}


}
