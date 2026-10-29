package d3.configuration.application.sinchronize;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.process.application.ProcesoTransicionSvc;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.process.infrastructure.DocumentoPlantillaMapper;
import d3.shared.domain.ServerException;

@Service
public class SynchronizeProcessTransitionService {

	private final DocumentoPlantillaMapper documentoPlantillaMapper;
	private final ProcesoTransicionSvc processTransitionService;
	private final SynchronizePropertiesService propertiesSynchronizeService;
	private final NodoHijoMapper nodoHijoMapper;

	public SynchronizeProcessTransitionService(@Lazy ProcesoTransicionSvc processTransitionService,@Lazy DocumentoPlantillaMapper documentoPlantillaMapper,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService, @Lazy NodoHijoMapper nodoHijoMapper) {
		this.processTransitionService = processTransitionService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
		this.documentoPlantillaMapper = documentoPlantillaMapper;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public void call(HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare) {
		List<ProcesoTransicionDTO> localToErase = nodoHijoMapper.listarTransicionesFull(null);
		List<ProcesoTransicionDTO> remoteTocompare = hierarchy.getTransitions();
		if (remoteTocompare == null || localToErase == null)
			return;
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {

			for (ProcesoTransicionDTO remote : remoteTocompare) {
				log.setRoot("Sincronizando Transition del proceso " + remote.getNodoSuperiorNombre());
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
						newState.setCodigo(remote.getCodigo());
						newState.setNodoSuperior(remote.getNodoSuperior());
						newState.setPlantilla(remote.getPlantilla());
						newState.setEstadoLLegada(remote.getEstadoLLegada());
						newState.setEstadoPartida(remote.getEstadoPartida());
						newState.setNombre(remote.getNombre());
						
						try {
							DocumentoPlantillaDTO template = documentoPlantillaMapper.getByCode(remote.getPlantilla());
							if(template==null) {
								log.error(" La transicion " + remote.getNombre() + "(Cod. " + remote.getCodigo()
								+ ") No se logro encontrar la platilla con codigo: " + remote.getPlantilla());
							} else {
								newState.setPlantilla(template.getLlaveTabla());
								newState = processTransitionService.save(newState);
								log.info("NEW TRANSITION " + remote.getCodigo() + " - " + remote.getNombre());								
							}
						} catch (Exception e) {
							log.error(" La transicion " + remote.getNombre() + "(Cod. " + remote.getCodigo()
									+ ") Tiene el siguiente error: " + e.getMessage());
						}
					}
				}
			}
		}
	}

	public void callAfterCreateAll(HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<ProcesoTransicionDTO> localToErase = nodoHijoMapper.listarTransicionesFull(null);
		List<ProcesoTransicionDTO> remoteTocompare = hierarchy.getTransitions();
		if (remoteTocompare != null && !remoteTocompare.isEmpty()) {
			for (ProcesoTransicionDTO remote : remoteTocompare) {
				ProcesoTransicionDTO local = findProcessInList(localToErase, remote);
				if (local != null) {
					localToErase.remove(local);
					log.setRoot("Sincronizando el proceso " + local.getNodoSuperiorNombre() + " la transicion de nombre"
							+ local.getNombre());
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.TRANSICION, local.getLlaveTabla(), log, compare);
				}
			}
		}
	}

	private ProcesoTransicionDTO findProcessInList(List<ProcesoTransicionDTO> array, ProcesoTransicionDTO remote) {
		if (array == null)
			return null;
		for (ProcesoTransicionDTO localProcess : array) {
			if (remote.getCodigo().compareTo(localProcess.getCodigo()) == 0
					&& remote.getNodoSuperior().compareTo(localProcess.getNodoSuperior()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

}
