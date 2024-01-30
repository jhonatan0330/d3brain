package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeProcessTransitionService {

	@Autowired private ProcesoTransicionSvc processTransitionService;
	@Autowired private SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log) throws ServerException {
		List<ProcesoTransicionDTO> localToErase = processTransitionService.getFullToSynchronize(null);
		List<ProcesoTransicionDTO> remoteTocompare = hierarchy.getTransitions();
		if (remoteTocompare == null || localToErase ==null) return;
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {
			log.setRoot("SynchronizeProcessTransitionService");
			for (ProcesoTransicionDTO remote : remoteTocompare) {
				ProcesoTransicionDTO local = findProcessInList(localToErase, remote);
				// Creo el nuevo proceso
				if (local!=null){
					localToErase.remove(local);
					log.info("EXIST " + remote.getCodigo() + " - " + remote.getNombre());
				}
				else
				{
					ProcesoTransicionDTO newState = new ProcesoTransicionDTO();
					newState.setAfectaSaldo(remote.getAfectaSaldo());
					newState.setCodigo(remote.getCodigo());
					newState.setDocumentador(remote.getDocumentador());
					newState.setProceso(remote.getProceso());
					newState.setPlantilla(remote.getPlantilla());
					newState.setEstadoLLegada(remote.getEstadoLLegada());
					newState.setEstadoPartida(remote.getEstadoPartida());
					newState.setNombre(remote.getNombre());
					newState.setRapida(remote.getRapida());
					try {
						newState = processTransitionService.save(newState);						
						log.info("NEW " + remote.getCodigo() + " - " + remote.getNombre());
					} catch (Exception e) {
						log.error(remote.getCodigo() + " - " + remote.getNombre() + " : " + e.getMessage());
					}
				}
			}
		}
	}
	
	public void callAfterCreateAll(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log)throws ServerException {
		List<ProcesoTransicionDTO> localToErase = processTransitionService.getFullToSynchronize(null);
		List<ProcesoTransicionDTO> remoteTocompare = hierarchy.getTransitions();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {
			for (ProcesoTransicionDTO remote : remoteTocompare) {
				ProcesoTransicionDTO local = findProcessInList(localToErase, remote);
				if (local!=null){
					localToErase.remove(local);
					log.setRoot("SynchronizeProcessTransitionAfterService " + local.getProcesoNombre() + "..." + local.getNombre());
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.TRANSICION, local.getLlaveTabla(), token, log);
				}
			}
		}
	}


	private ProcesoTransicionDTO findProcessInList(List<ProcesoTransicionDTO> array, ProcesoTransicionDTO remote) {
		if (array == null) return null;
		for (ProcesoTransicionDTO localProcess : array) {
			if (remote.getCodigo().compareTo(localProcess.getCodigo()) == 0 && remote.getProceso().compareTo(localProcess.getProceso())==0 ) {
				return localProcess;
			}
		}
		return null;
	}

}
