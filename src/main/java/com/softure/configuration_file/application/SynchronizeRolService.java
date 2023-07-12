package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeRolService {

	@Autowired RolAccesoSvc rolService;
	@Autowired SynchronizePropertiesService propertiesSynchronizeService;
	
	public void call(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<RolAccesoDTO> localListToErase = rolService.getFullToSynchronize();

		List<RolAccesoDTO> remoteList = hierarchy.getRoles();
		// Saco un listado de las propiedades nuevas
		// Saco un listado de las propiedades a borrar
		if (remoteList != null && !remoteList.isEmpty()) {
			for (RolAccesoDTO remote : remoteList) {
				RolAccesoDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					localListToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy.getProperties(), remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.ROL, local.getLlaveTabla(), token);
				}
				//Uy seria muy raro else
			}
		}


	}

	private RolAccesoDTO findTemplateInList(List<RolAccesoDTO> array, String code) {
		for (RolAccesoDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

}
