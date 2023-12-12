package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeProcessService {

	@Autowired private ProcesoSvc processService;
	@Autowired private SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log) throws ServerException {
		List<ProcesoDTO> localProcessToErase = processService.getFullToSynchronize(null);

		List<ProcesoDTO> processRemote = hierarchy.getProcess();
		if (processRemote != null && !processRemote.isEmpty()) {
			log.setRoot("SynchronizeProcessService");
			for (ProcesoDTO remoteProcess : processRemote) {
				ProcesoDTO localProcess = findProcessInList(localProcessToErase, remoteProcess.getCodigo());
				// Creo el nuevo proceso
				if (localProcess!=null){
					localProcessToErase.remove(localProcess);
					log.info("EXIST " +localProcess.getCodigo() + " - " + localProcess.getNombre());
					propertiesSynchronizeService.call(hierarchy, remoteProcess.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PROCESO, localProcess.getLlaveTabla(), token, log);
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
					log.info("new " +newProcess.getCodigo() + " - " + newProcess.getNombre());
					propertiesSynchronizeService.call(hierarchy, remoteProcess.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PROCESO, newProcess.getLlaveTabla(), token, log);
					changeMacroProcesoField(processRemote, remoteProcess.getLlaveTabla(), newProcess.getLlaveTabla());
					changeProcessInStates(hierarchy.getStates(), remoteProcess.getLlaveTabla(), newProcess.getLlaveTabla());
					changeProcessInTransition(hierarchy.getTransitions(), remoteProcess.getLlaveTabla(), newProcess.getLlaveTabla());
					changeProcessInTemplates(hierarchy.getTemplates(), remoteProcess.getLlaveTabla(), newProcess.getLlaveTabla());
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
				filter.setEstado(SharedConstants.STATE_ACTIVE);
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

	private void changeProcessInTemplates(List<DocumentoPlantillaDTO> array, String remote, String local) {
		for (DocumentoPlantillaDTO item : array) {
			if(item.getProceso()!=null && item.getProceso().compareTo(remote)==0) {
				item.setProceso(local);
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
		if(array ==null) return;
		for (ProcesoEstadoDTO remoteProcess : array) {
			if(remoteProcess.getProceso()!=null && remoteProcess.getProceso().compareTo(remote)==0) {
				remoteProcess.setProceso(local);
			}
		}
	}
	
	private void changeProcessInTransition(List<ProcesoTransicionDTO> array, String remote, String local) {
		if(array ==null) return;
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
