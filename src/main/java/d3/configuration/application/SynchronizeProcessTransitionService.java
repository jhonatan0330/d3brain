package d3.configuration.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.process.application.ProcesoTransicionSvc;
import d3.process.domain.ProcesoTransicionDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class SynchronizeProcessTransitionService {

	private final ProcesoTransicionSvc processTransitionService;
	private final SynchronizePropertiesService propertiesSynchronizeService;

	public SynchronizeProcessTransitionService(@Lazy ProcesoTransicionSvc processTransitionService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService) {
		this.processTransitionService = processTransitionService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
	}

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<ProcesoTransicionDTO> localToErase = processTransitionService.getFullToSynchronize(null);
		List<ProcesoTransicionDTO> remoteTocompare = hierarchy.getTransitions();
		if (remoteTocompare == null || localToErase == null)
			return;
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {

			for (ProcesoTransicionDTO remote : remoteTocompare) {
				log.setRoot("Sincronizando Transition del proceso " + remote.getProcesoNombre());
				ProcesoTransicionDTO local = findProcessInList(localToErase, remote);
				// Creo el nuevo proceso
				if (local != null) {
					localToErase.remove(local);
					log.info("EXIST TRANSITION " + remote.getCodigo() + " - " + remote.getNombre());
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST TRANSITION " + remote.getCodigo() + " - " + remote.getNombre());
					} else {
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
							log.info("NEW TRANSITION " + remote.getCodigo() + " - " + remote.getNombre());
						} catch (Exception e) {
							log.error(" La transicion " + remote.getNombre() + "(Cod. " + remote.getCodigo()
									+ ") Tiene el siguiente error: " + e.getMessage());
						}
					}
				}
			}
		}
	}

	public void callAfterCreateAll(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log,
			boolean compare) throws ServerException {
		List<ProcesoTransicionDTO> localToErase = processTransitionService.getFullToSynchronize(null);
		List<ProcesoTransicionDTO> remoteTocompare = hierarchy.getTransitions();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {
			for (ProcesoTransicionDTO remote : remoteTocompare) {
				ProcesoTransicionDTO local = findProcessInList(localToErase, remote);
				if (local != null) {
					localToErase.remove(local);
					log.setRoot("Sincronizando el proceso " + local.getProcesoNombre() + " la transicion de nombre"
							+ local.getNombre());
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.TRANSICION, local.getLlaveTabla(), token, log, compare);
				}
			}
		}
	}

	private ProcesoTransicionDTO findProcessInList(List<ProcesoTransicionDTO> array, ProcesoTransicionDTO remote) {
		if (array == null)
			return null;
		for (ProcesoTransicionDTO localProcess : array) {
			if (remote.getCodigo().compareTo(localProcess.getCodigo()) == 0
					&& remote.getProceso().compareTo(localProcess.getProceso()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

}
