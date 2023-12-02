package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeProcessService {

	@Autowired private ProcesoSvc processService;
	@Autowired private SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<ProcesoDTO> localProcessToErase = processService.getFullToSynchronize();

		List<ProcesoDTO> processRemote = hierarchy.getProcess();
		if (processRemote != null && !processRemote.isEmpty()) {
			for (ProcesoDTO remoteProcess : processRemote) {
				System.out.println("Proceso : " + remoteProcess.getNombre());
				ProcesoDTO localProcess = findProcessInList(localProcessToErase, remoteProcess.getCodigo());
				// Creo el nuevo proceso
				if (localProcess!=null){
					localProcessToErase.remove(localProcess);
					propertiesSynchronizeService.call(hierarchy.getProperties(), remoteProcess.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PROCESO, localProcess.getLlaveTabla(), token);
					changeMacroProcesoField(processRemote, remoteProcess.getLlaveTabla(), localProcess.getLlaveTabla());
					changeProcessInStates(hierarchy.getStates(), remoteProcess.getLlaveTabla(), localProcess.getLlaveTabla());
					changeProcessInTransition(hierarchy.getTransitions(), remoteProcess.getLlaveTabla(), localProcess.getLlaveTabla());
				}
				else
				{
					ProcesoDTO newProcess = new ProcesoDTO();
					newProcess.setCodigo(remoteProcess.getCodigo());
					newProcess.setImagen(remoteProcess.getImagen());
					//newProcess.setMacroproceso(remoteProcess.getMacroproceso());
					newProcess.setNombre(remoteProcess.getNombre());
					newProcess.setObjetivo(remoteProcess.getObjetivo());
					newProcess.setPrioridad(remoteProcess.getPrioridad());
					newProcess.setTipo(remoteProcess.getTipo());
					newProcess = processService.save(newProcess);
					propertiesSynchronizeService.call(hierarchy.getProperties(), remoteProcess.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PROCESO, newProcess.getLlaveTabla(), token);
					changeMacroProcesoField(processRemote, remoteProcess.getLlaveTabla(), newProcess.getLlaveTabla());
					changeProcessInStates(hierarchy.getStates(), remoteProcess.getLlaveTabla(), newProcess.getLlaveTabla());
					changeProcessInTransition(hierarchy.getTransitions(), remoteProcess.getLlaveTabla(), newProcess.getLlaveTabla());
				}
			}
		}
		synchronizeMacroprocessField(processRemote);

	}

	private void synchronizeMacroprocessField(List<ProcesoDTO> processRemote) throws ServerException {
		for (ProcesoDTO remote : processRemote) {
			if(remote.getMacroproceso()!=null) {
				ProcesoFilterDTO filter = new ProcesoFilterDTO();
				filter.setCodigo(remote.getCodigo());
				filter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				ProcesoDTO db = null;
				try {
					db = processService.consultaUnica(filter);
				} catch (Exception e) {
					throw new ServerException("Corrige los codigos de los procesos no pueden ser duplicados");
				}	
				db.setMacroproceso(remote.getMacroproceso());
				processService.update(db);
			}
		}
	}

	private void changeMacroProcesoField(List<ProcesoDTO> processRemote, String remote, String local) {
		for (ProcesoDTO remoteProcess : processRemote) {
			if(remoteProcess.getMacroproceso()!=null && remoteProcess.getMacroproceso().compareTo(remote)==0) {
				remoteProcess.setMacroproceso(local);
			}
		}
	}
	
	private void changeProcessInStates(List<ProcesoEstadoDTO> array, String remote, String local) {
		for (ProcesoEstadoDTO remoteProcess : array) {
			if(remoteProcess.getProceso()!=null && remoteProcess.getProceso().compareTo(remote)==0) {
				remoteProcess.setProceso(local);
			}
		}
	}
	
	private void changeProcessInTransition(List<ProcesoTransicionDTO> array, String remote, String local) {
		for (ProcesoTransicionDTO remoteProcess : array) {
			if(remoteProcess.getProceso()!=null && remoteProcess.getProceso().compareTo(remote)==0) {
				remoteProcess.setProceso(local);
			}
		}
	}

	/*public void callAfterRol(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<ProcesoDTO> localProcessToErase = processService.getFullToSynchronize();
		List<ProcesoDTO> processRemote = hierarchy.getProcess();
		if (processRemote != null && !processRemote.isEmpty()) {
			for (ProcesoDTO remoteProcess : processRemote) {
				ProcesoDTO localProcess = findProcessInList(localProcessToErase, remoteProcess.getCodigo());
				// Creo el nuevo proceso
				if (localProcess!=null){
					localProcessToErase.remove(localProcess);
					propertiesSynchronizeService.call(hierarchy.getProperties(), remoteProcess.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PROCESO, localProcess.getLlaveTabla(), token);
				}
			}
		}
	}*/

	private ProcesoDTO findProcessInList(List<ProcesoDTO> array, String code) {
		for (ProcesoDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

}
