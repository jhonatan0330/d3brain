package d3.configuration.application.sinchronize;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.process.application.ProcesoEstadoSvc;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.shared.domain.ServerException;

@Service
public class SynchronizeProcessStateService {

	private final ProcesoEstadoSvc processStateService;
	private final SynchronizePropertiesService propertiesSynchronizeService;
	private final NodoHijoMapper nodoHijoMapper;

	public SynchronizeProcessStateService(@Lazy ProcesoEstadoSvc processStateService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService, @Lazy NodoHijoMapper nodoHijoMapper) {
		this.processStateService = processStateService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public void call(HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare) throws ServerException {
		List<ProcesoEstadoDTO> localToErase = nodoHijoMapper.listarEstadosFull(null);
		List<ProcesoEstadoDTO> remoteTocompare = hierarchy.getStates();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {

			for (ProcesoEstadoDTO remote : remoteTocompare) {
				log.setRoot("SynchronizeProcessStateService " + remote.getNodoSuperiorNombre());
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
						newState.setNodoSuperior(remote.getNodoSuperior());
						newState.setPrioridad(remote.getPrioridad());
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

	public void callAfter(HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<ProcesoEstadoDTO> localToErase = nodoHijoMapper.listarEstadosFull(null);
		List<ProcesoEstadoDTO> remoteTocompare = hierarchy.getStates();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {
			for (ProcesoEstadoDTO remote : remoteTocompare) {
				log.setRoot("SynchronizeProcessStateAfterService " + remote.getNodoSuperiorNombre());
				ProcesoEstadoDTO local = findProcessInList(localToErase, remote);
				if (local != null) {
					localToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.ESTADO, local.getLlaveTabla(), log, compare);
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
			if (remote.getNodoSuperior().compareTo(localProcess.getNodoSuperior()) == 0
					&& remote.getNombre().compareTo(localProcess.getNombre()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

}
