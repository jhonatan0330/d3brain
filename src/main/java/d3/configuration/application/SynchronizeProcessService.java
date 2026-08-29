package d3.configuration.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.process.application.ProcesoSvc;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoFilterDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class SynchronizeProcessService {

	private final ProcesoSvc processService;
	private final SynchronizePropertiesService propertiesSynchronizeService;

	public SynchronizeProcessService(@Lazy ProcesoSvc processService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService) {
		this.processService = processService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
	}

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<ProcesoDTO> localProcessToErase = processService.getFullToSynchronize(null);

		List<ProcesoDTO> processRemote = hierarchy.getProcess();
		if (processRemote != null && !processRemote.isEmpty()) {
			log.setRoot("SynchronizeProcessService");
			for (ProcesoDTO remoteProcess : processRemote) {
				ProcesoDTO local = findProcessInList(localProcessToErase, remoteProcess.getCodigo());
				// Creo el nuevo proceso
				if (local != null) {
					localProcessToErase.remove(local);
					log.info("EXIST PROCESS " + local.getCodigo() + " - " + local.getNombre());
					propertiesSynchronizeService.call(hierarchy, remoteProcess.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PROCESO, local.getLlaveTabla(), token, log, compare);
					changeMacroProcesoField(processRemote, remoteProcess.getLlaveTabla(), local.getLlaveTabla());
					changeProcessInStates(hierarchy.getStates(), remoteProcess.getLlaveTabla(), local.getLlaveTabla());
					changeProcessInTransition(hierarchy.getTransitions(), remoteProcess.getLlaveTabla(),
							local.getLlaveTabla());
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST PROCESS " + remoteProcess.getCodigo() + " - "
								+ remoteProcess.getNombre());
					} else {
						ProcesoDTO newProcess = new ProcesoDTO();
						newProcess.setCodigo(remoteProcess.getCodigo());
						newProcess.setImagen(remoteProcess.getImagen());
						// newProcess.setMacroproceso(remoteProcess.getMacroproceso());
						newProcess.setNombre(remoteProcess.getNombre());
						newProcess.setObjetivo(remoteProcess.getObjetivo());
						newProcess.setPrioridad(remoteProcess.getPrioridad());
						newProcess.setTipo(remoteProcess.getTipo());
						local = processService.save(newProcess);
						log.info("new process" + local.getCodigo() + " - " + local.getNombre());
						propertiesSynchronizeService.call(hierarchy, remoteProcess.getLlaveTabla(),
								PropiedadValorDefinidoDTO.PROCESO, local.getLlaveTabla(), token, log, compare);
					}

				}
				if (local != null) {
					changeMacroProcesoField(processRemote, remoteProcess.getLlaveTabla(), local.getLlaveTabla());
					changeProcessInStates(hierarchy.getStates(), remoteProcess.getLlaveTabla(), local.getLlaveTabla());
					changeProcessInTransition(hierarchy.getTransitions(), remoteProcess.getLlaveTabla(),
							local.getLlaveTabla());
					changeProcessInTemplates(hierarchy.getTemplates(), remoteProcess.getLlaveTabla(),
							local.getLlaveTabla());
				}
			}
		}
		synchronizeMacroprocessField(processRemote);

	}

	private void synchronizeMacroprocessField(List<ProcesoDTO> processRemote) throws ServerException {
		for (ProcesoDTO remote : processRemote) {
			if (remote.getMacroproceso() != null) {
				ProcesoFilterDTO filter = new ProcesoFilterDTO();
				filter.setCodigo(remote.getCodigo());
				filter.setEstado(SharedConstants.STATE_ACTIVE);
				ProcesoDTO db = null;
				try {
					db = processService.consultaUnica(filter);
				} catch (Exception e) {
					throw new ServerException("Corrige los codigos de los procesos no pueden ser duplicados, el codigo "
							+ remote.getCodigo() + " esta duplicado");
				}
				if (db != null) {
					db.setMacroproceso(remote.getMacroproceso());
					processService.update(db);
				}
			}
		}
	}

	private void changeProcessInTemplates(List<DocumentoPlantillaDTO> array, String remote, String local) {
		for (DocumentoPlantillaDTO item : array) {
			if (item.getProceso() != null && item.getProceso().compareTo(remote) == 0) {
				item.setProceso(local);
			}
		}
	}

	private void changeMacroProcesoField(List<ProcesoDTO> processRemote, String remote, String local) {
		for (ProcesoDTO remoteProcess : processRemote) {
			if (remoteProcess.getMacroproceso() != null && remoteProcess.getMacroproceso().compareTo(remote) == 0) {
				remoteProcess.setMacroproceso(local);
			}
		}
	}

	private void changeProcessInStates(List<ProcesoEstadoDTO> array, String remote, String local) {
		if (array == null)
			return;
		for (ProcesoEstadoDTO remoteProcess : array) {
			if (remoteProcess.getProceso() != null && remoteProcess.getProceso().compareTo(remote) == 0) {
				remoteProcess.setProceso(local);
			}
		}
	}

	private void changeProcessInTransition(List<ProcesoTransicionDTO> array, String remote, String local) {
		if (array == null)
			return;
		for (ProcesoTransicionDTO remoteProcess : array) {
			if (remoteProcess.getProceso() != null && remoteProcess.getProceso().compareTo(remote) == 0) {
				remoteProcess.setProceso(local);
			}
		}
	}

	/*
	 * public void callAfterRol(String token, HierarchyExporterDTO hierarchy) throws
	 * ServerException { List<ProcesoDTO> localProcessToErase =
	 * processService.getFullToSynchronize(); List<ProcesoDTO> processRemote =
	 * hierarchy.getProcess(); if (processRemote != null &&
	 * !processRemote.isEmpty()) { for (ProcesoDTO remoteProcess : processRemote) {
	 * ProcesoDTO localProcess = findProcessInList(localProcessToErase,
	 * remoteProcess.getCodigo()); // Creo el nuevo proceso if (localProcess!=null){
	 * localProcessToErase.remove(localProcess);
	 * propertiesSynchronizeService.call(hierarchy.getProperties(),
	 * remoteProcess.getLlaveTabla(), PropiedadValorDefinidoDTO.PROCESO,
	 * localProcess.getLlaveTabla(), token); } } } }
	 */

	private ProcesoDTO findProcessInList(List<ProcesoDTO> array, String code) {
		for (ProcesoDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

}
