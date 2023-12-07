package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeProcessStateService {

	@Autowired private ProcesoEstadoSvc processStateService;
	@Autowired private SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log) throws ServerException {
		List<ProcesoEstadoDTO> localToErase = processStateService.getFullToSynchronize(null);
		List<ProcesoEstadoDTO> remoteTocompare = hierarchy.getStates();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {
			log.setRoot("SynchronizeProcessStateService");
			for (ProcesoEstadoDTO remote : remoteTocompare) {
				ProcesoEstadoDTO local = findProcessInList(localToErase, remote);
				if (local!=null){
					localToErase.remove(local);
					log.info("EXIST " + remote.getCodigo() + " - " + remote.getNombre());
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.ESTADO, local.getLlaveTabla(), token, log);
					changeStatesInTransitions(hierarchy.getTransitions(), remote.getLlaveTabla(), local.getLlaveTabla());
				}
				else
				{
					ProcesoEstadoDTO newState = new ProcesoEstadoDTO();
					newState.setCodigo(remote.getCodigo());
					newState.setEstadoDocumento(remote.getEstadoDocumento());
					newState.setProceso(remote.getProceso());
					newState.setAvance(remote.getAvance());
					newState.setTipo(remote.getTipo());
					newState.setNombre(remote.getNombre());
					newState = processStateService.save(newState);
					log.info("NEW " + remote.getCodigo() + " - " + remote.getNombre());
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.ESTADO, newState.getLlaveTabla(), token, log);
					changeStatesInTransitions(hierarchy.getTransitions(), remote.getLlaveTabla(), newState.getLlaveTabla());
				}
			}
		}
	}

	private void changeStatesInTransitions(List<ProcesoTransicionDTO> arrayToSync, String remote, String local) {
		for (ProcesoTransicionDTO remoteTransition : arrayToSync) {
			if(remoteTransition.getEstadoPartida()!=null && remoteTransition.getEstadoPartida().compareTo(remote)==0) {
				remoteTransition.setEstadoPartida(local);
			}
			if(remoteTransition.getEstadoLLegada()!=null && remoteTransition.getEstadoLLegada().compareTo(remote)==0) {
				remoteTransition.setEstadoLLegada(local);
			}
		}
	}

	private ProcesoEstadoDTO findProcessInList(List<ProcesoEstadoDTO> array, ProcesoEstadoDTO remote) {
		if (array == null) return null;
		for (ProcesoEstadoDTO localProcess : array) {
			if (remote.getProceso().compareTo(localProcess.getProceso()) == 0 && remote.getNombre().compareTo(localProcess.getNombre()) == 0 ) {
				return localProcess;
			}
		}
		return null;
	}

}
