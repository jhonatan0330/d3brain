package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeProcessTransitionService {

	@Autowired private ProcesoTransicionSvc processTransitionService;
	@Autowired private SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<ProcesoTransicionDTO> localToErase = processTransitionService.getFullToSynchronize();
		List<ProcesoTransicionDTO> remoteTocompare = hierarchy.getTransitions();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {
			for (ProcesoTransicionDTO remote : remoteTocompare) {
				ProcesoTransicionDTO local = findProcessInList(localToErase, remote);
				// Creo el nuevo proceso
				if (local!=null){
					localToErase.remove(local);
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
					newState = processTransitionService.save(newState);
				}
			}
		}
		callAfterCreateAll(token, hierarchy);
	}
	
	private void callAfterCreateAll(String token, HierarchyExporterDTO hierarchy)throws ServerException {
		List<ProcesoTransicionDTO> localToErase = processTransitionService.getFullToSynchronize();
		List<ProcesoTransicionDTO> remoteTocompare = hierarchy.getTransitions();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {
			for (ProcesoTransicionDTO remote : remoteTocompare) {
				System.out.println("Transicion : " + remote.getNombre() + "  --  " + remote.getProcesoNombre());
				ProcesoTransicionDTO local = findProcessInList(localToErase, remote);
				if (local!=null){
					localToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy.getProperties(), remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.TRANSICION, local.getLlaveTabla(), token);
				}
			}
		}
	}


	private ProcesoTransicionDTO findProcessInList(List<ProcesoTransicionDTO> array, ProcesoTransicionDTO remote) {
		for (ProcesoTransicionDTO localProcess : array) {
			if (remote.getNombre().compareTo(localProcess.getNombre()) == 0 && remote.getProceso().compareTo(localProcess.getProceso())==0) {
				return localProcess;
			}
		}
		return null;
	}

}
