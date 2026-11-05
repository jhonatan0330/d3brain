package d3.configuration.application.sinchronize;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.NodoArbolDTO;
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

	public SynchronizeProcessTransitionService(@Lazy ProcesoTransicionSvc processTransitionService,
			@Lazy DocumentoPlantillaMapper documentoPlantillaMapper,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService, @Lazy NodoHijoMapper nodoHijoMapper) {
		this.processTransitionService = processTransitionService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
		this.documentoPlantillaMapper = documentoPlantillaMapper;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public void call(ContextoSincronizacion contexto, LogConfigurationDTO log, boolean compare) {
		List<NodoArbolDTO> locales = nodoHijoMapper.listarTransicionesFull(null);
		List<NodoArbolDTO> remotos = contexto.porCategoria(PropiedadValorDefinidoDTO.TRANSICION);
		if (remotos.isEmpty())
			return;

		for (NodoArbolDTO remoto : remotos) {
			NodoArbolDTO plantillaRemota = buscarPlantilla(contexto, remoto);
			String procesoLocal = resolverProcesoLocal(contexto, remoto, plantillaRemota);
			log.setRoot("Sincronizando Transition del proceso " + remoto.getNodoSuperiorNombre());
			if (procesoLocal == null) {
				log.error("TRANSICION SIN PROCESO LOCAL " + remoto.getCodigo() + " - " + remoto.getNombre());
				continue;
			}
			NodoArbolDTO local = buscarLocal(locales, procesoLocal, remoto.getCodigo());
			if (local != null) {
				locales.remove(local);
				contexto.registrarLocal(remoto, local.getLlaveTabla());
				log.info("EXIST TRANSITION " + remoto.getCodigo() + " - " + remoto.getNombre());
			} else {
				if (compare) {
					log.error("COMPARE NOT EXIST TRANSITION " + remoto.getCodigo() + " - " + remoto.getNombre());
				} else {
					crearTransicion(contexto, remoto, procesoLocal, plantillaRemota, log);
				}
			}
		}
	}

	private void crearTransicion(ContextoSincronizacion contexto, NodoArbolDTO remoto, String procesoLocal,
			NodoArbolDTO plantillaRemota, LogConfigurationDTO log) {
		String codigoPlantilla = remoto.getAtt1();
		String codigoPartida = remoto.getAtt2();
		String codigoLlegada = remoto.getAtt3();

		String plantillaLocal = plantillaRemota == null ? null : contexto.local(plantillaRemota);
		if (plantillaLocal == null && codigoPlantilla != null && !codigoPlantilla.isEmpty()) {
			DocumentoPlantillaDTO plantilla = documentoPlantillaMapper.getByCode(codigoPlantilla);
			if (plantilla == null) {
				log.error(" La transicion " + remoto.getNombre() + "(Cod. " + remoto.getCodigo()
						+ ") No se logro encontrar la plantilla con codigo: " + codigoPlantilla);
				return;
			}
			plantillaLocal = plantilla.getLlaveTabla();
		}

		String estadoPartida = resolverEstado(contexto, procesoLocal, codigoPartida);
		if (codigoPartida != null && !codigoPartida.isEmpty() && estadoPartida == null) {
			log.error(" La transicion " + remoto.getNombre() + "(Cod. " + remoto.getCodigo()
					+ ") No se logro encontrar el estado de partida con codigo: " + codigoPartida);
			return;
		}
		String estadoLlegada = resolverEstado(contexto, procesoLocal, codigoLlegada);
		if (codigoLlegada == null || codigoLlegada.isEmpty() || estadoLlegada == null) {
			log.error(" La transicion " + remoto.getNombre() + "(Cod. " + remoto.getCodigo()
					+ ") No se logro encontrar el estado de llegada con codigo: " + codigoLlegada);
			return;
		}

		ProcesoTransicionDTO nueva = new ProcesoTransicionDTO();
		nueva.setCodigo(remoto.getCodigo());
		nueva.setNodoSuperior(procesoLocal);
		nueva.setPlantilla(plantillaLocal);
		nueva.setEstadoLLegada(estadoLlegada);
		nueva.setEstadoPartida(estadoPartida);
		nueva.setNombre(remoto.getNombre());
		try {
			nueva = processTransitionService.save(nueva);
			contexto.registrarLocal(remoto, nueva.getLlaveTabla());
			log.info("NEW TRANSITION " + nueva.getCodigo() + " - " + nueva.getNombre());
		} catch (Exception e) {
			log.error(" La transicion " + remoto.getNombre() + "(Cod. " + remoto.getCodigo()
					+ ") Tiene el siguiente error: " + e.getMessage());
		}
	}

	private NodoArbolDTO buscarPlantilla(ContextoSincronizacion contexto, NodoArbolDTO remoto) {
		if (remoto == null || remoto.getAtt1() == null || remoto.getAtt1().isEmpty())
			return null;
		NodoArbolDTO padre = contexto.padreDe(remoto);
		String procesoRemoto = null;
		if (padre != null && PropiedadValorDefinidoDTO.PROCESO.equals(padre.getCategoria()))
			procesoRemoto = padre.getLlaveTabla();
		NodoArbolDTO encontrada = null;
		for (NodoArbolDTO plantilla : contexto.porCategoria(PropiedadValorDefinidoDTO.PLANTILLA)) {
			if (!remoto.getAtt1().equals(plantilla.getCodigo()))
				continue;
			if (procesoRemoto == null || procesoRemoto.equals(plantilla.getAtt1()))
				return plantilla;
			if (encontrada == null)
				encontrada = plantilla;
		}
		return encontrada;
	}

	private String resolverProcesoLocal(ContextoSincronizacion contexto, NodoArbolDTO remoto,
			NodoArbolDTO plantillaRemota) {
		NodoArbolDTO padre = contexto.padreDe(remoto);
		String local = null;
		if (padre != null && PropiedadValorDefinidoDTO.PROCESO.equals(padre.getCategoria()))
			local = contexto.local(padre);
		if (local == null && plantillaRemota != null && plantillaRemota.getAtt1() != null)
			local = contexto.localPorCategoria(PropiedadValorDefinidoDTO.PROCESO, plantillaRemota.getAtt1());
		return local;
	}

	private String resolverEstado(ContextoSincronizacion contexto, String procesoLocal, String codigo) {
		if (codigo == null || codigo.isEmpty())
			return null;
		return contexto.estadoLocal(procesoLocal, codigo);
	}

	public void callAfterCreateAll(ContextoSincronizacion contexto, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<NodoArbolDTO> remotos = contexto.porCategoria(PropiedadValorDefinidoDTO.TRANSICION);
		if (remotos.isEmpty())
			return;
		for (NodoArbolDTO remoto : remotos) {
			String local = contexto.local(remoto);
			if (local == null)
				continue;
			log.setRoot("Sincronizando el proceso " + remoto.getNodoSuperiorNombre()
					+ " la transicion de nombre" + remoto.getNombre());
			propertiesSynchronizeService.call(contexto.getPropiedades(), remoto.getLlaveTabla(),
					PropiedadValorDefinidoDTO.TRANSICION, local, log, compare);
		}
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
