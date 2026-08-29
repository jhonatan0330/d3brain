package d3.configuration.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.process.application.ProcesoEstadoSvc;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoTransicionDTO;

import org.springframework.context.annotation.Lazy;

@Service
public class SynchronizeProcessStateService {

	private final ProcesoEstadoSvc processStateService;
	private final SynchronizePropertiesService propertiesSynchronizeService;

	public SynchronizeProcessStateService(@Lazy ProcesoEstadoSvc processStateService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService) {
		this.processStateService = processStateService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
	}

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<ProcesoEstadoDTO> localToErase = processStateService.getFullToSynchronize(null);
		List<ProcesoEstadoDTO> remoteTocompare = hierarchy.getStates();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {

			for (ProcesoEstadoDTO remote : remoteTocompare) {
				log.setRoot("SynchronizeProcessStateService " + remote.getProcesoNombre());
				ProcesoEstadoDTO local = findProcessInList(localToErase, remote);
				if (local != null) {
					localToErase.remove(local);
					log.info("EXIST STATE " + remote.getCodigo() + " - " + remote.getNombre());
					changeStatesInTransitions(hierarchy.getTransitions(), remote.getLlaveTabla(),
							local.getLlaveTabla());
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST STATE " + remote.getCodigo() + " - " + remote.getNombre());
					} else {
						ProcesoEstadoDTO newState = new ProcesoEstadoDTO();
						newState.setCodigo(remote.getCodigo());
						newState.setEstadoDocumento(remote.getEstadoDocumento());
						newState.setProceso(remote.getProceso());
						newState.setAvance(remote.getAvance());
						newState.setTipo(remote.getTipo());
						newState.setNombre(remote.getNombre());
						newState = processStateService.save(newState);
						log.info("NEW STATE " + remote.getCodigo() + " - " + remote.getNombre());
						changeStatesInTransitions(hierarchy.getTransitions(), remote.getLlaveTabla(),
								newState.getLlaveTabla());
					}

				}
			}
		}
	}

	public void callAfter(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<ProcesoEstadoDTO> localToErase = processStateService.getFullToSynchronize(null);
		List<ProcesoEstadoDTO> remoteTocompare = hierarchy.getStates();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {
			for (ProcesoEstadoDTO remote : remoteTocompare) {
				log.setRoot("SynchronizeProcessStateAfterService " + remote.getProcesoNombre());
				ProcesoEstadoDTO local = findProcessInList(localToErase, remote);
				if (local != null) {
					localToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.ESTADO, local.getLlaveTabla(), token, log, compare);
				}
			}
		}
	}

	private void changeStatesInTransitions(List<ProcesoTransicionDTO> arrayToSync, String remote, String local) {
		for (ProcesoTransicionDTO remoteTransition : arrayToSync) {
			if (remoteTransition.getEstadoPartida() != null
					&& remoteTransition.getEstadoPartida().compareTo(remote) == 0) {
				remoteTransition.setEstadoPartida(local);
			}
			if (remoteTransition.getEstadoLLegada() != null
					&& remoteTransition.getEstadoLLegada().compareTo(remote) == 0) {
				remoteTransition.setEstadoLLegada(local);
			}
		}
	}

	private ProcesoEstadoDTO findProcessInList(List<ProcesoEstadoDTO> array, ProcesoEstadoDTO remote) {
		if (array == null)
			return null;
		for (ProcesoEstadoDTO localProcess : array) {
			if (remote.getProceso().compareTo(localProcess.getProceso()) == 0
					&& remote.getNombre().compareTo(localProcess.getNombre()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

}
