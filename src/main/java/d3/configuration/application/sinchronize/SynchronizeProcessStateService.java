package d3.configuration.application.sinchronize;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.process.application.ProcesoEstadoSvc;
import d3.process.domain.ProcesoEstadoDTO;
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

	public void call(ContextoSincronizacion contexto, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<NodoArbolDTO> locales = nodoHijoMapper.listarEstadosFull(null);
		List<NodoArbolDTO> remotos = contexto.porCategoria(PropiedadValorDefinidoDTO.ESTADO);
		if (remotos.isEmpty())
			return;

		for (NodoArbolDTO remoto : remotos) {
			String procesoLocal = resolverProcesoLocal(contexto, remoto);
			log.setRoot("SynchronizeProcessStateService " + remoto.getNodoSuperiorNombre());
			if (procesoLocal == null) {
				log.error("ESTADO SIN PROCESO LOCAL " + remoto.getCodigo() + " - " + remoto.getNombre());
				continue;
			}
			NodoArbolDTO local = buscarLocal(locales, procesoLocal, remoto.getCodigo());
			if (local != null) {
				locales.remove(local);
				contexto.registrarLocal(remoto, local.getLlaveTabla());
				contexto.registrarEstado(procesoLocal, remoto.getCodigo(), local.getLlaveTabla());
				log.info("EXIST STATE " + remoto.getCodigo() + " - " + remoto.getNombre());
			} else {
				if (compare) {
					log.error("COMPARE NOT EXIST STATE " + remoto.getCodigo() + " - " + remoto.getNombre());
				} else {
					ProcesoEstadoDTO nuevo = new ProcesoEstadoDTO();
					nuevo.setCodigo(remoto.getCodigo());
					nuevo.setEstadoDocumento(remoto.getAtt1());
					nuevo.setNodoSuperior(procesoLocal);
					nuevo.setPrioridad(remoto.getPrioridad());
					nuevo.setTipo(remoto.getTipo());
					nuevo.setNombre(remoto.getNombre());
					nuevo = processStateService.save(nuevo);
					contexto.registrarLocal(remoto, nuevo.getLlaveTabla());
					contexto.registrarEstado(procesoLocal, remoto.getCodigo(), nuevo.getLlaveTabla());
					log.info("NEW STATE " + nuevo.getCodigo() + " - " + nuevo.getNombre());
				}
			}
		}
	}

	public void callAfter(ContextoSincronizacion contexto, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<NodoArbolDTO> remotos = contexto.porCategoria(PropiedadValorDefinidoDTO.ESTADO);
		if (remotos.isEmpty())
			return;
		for (NodoArbolDTO remoto : remotos) {
			String local = contexto.local(remoto);
			if (local == null)
				continue;
			log.setRoot("SynchronizeProcessStateAfterService " + remoto.getNodoSuperiorNombre());
			propertiesSynchronizeService.call(contexto.getPropiedades(), remoto.getLlaveTabla(),
					PropiedadValorDefinidoDTO.ESTADO, local, log, compare);
		}
	}

	private String resolverProcesoLocal(ContextoSincronizacion contexto, NodoArbolDTO remoto) {
		NodoArbolDTO proceso = contexto.porLlave(remoto.getNodoSuperior());
		String local = proceso == null ? null : contexto.local(proceso);
		return local != null ? local : contexto.localPadre(remoto);
	}

	private NodoArbolDTO buscarLocal(List<NodoArbolDTO> array, String procesoLocal, String codigo) {
		if (array == null || codigo == null)
			return null;
		for (NodoArbolDTO local : array) {
			if (codigo.equals(local.getCodigo()) && procesoLocal.equals(local.getNodoSuperior()))
				return local;
		}
		return null;
	}

}
