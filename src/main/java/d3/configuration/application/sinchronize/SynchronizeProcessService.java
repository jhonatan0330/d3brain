package d3.configuration.application.sinchronize;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.process.application.ProcesoSvc;
import d3.process.domain.ProcesoDTO;
import d3.shared.domain.ServerException;

@Service
public class SynchronizeProcessService {

	private final ProcesoSvc processService;
	private final SynchronizePropertiesService propertiesSynchronizeService;
	private final NodoHijoMapper nodoHijoMapper;

	public SynchronizeProcessService(@Lazy ProcesoSvc processService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService, @Lazy NodoHijoMapper nodoHijoMapper) {
		this.processService = processService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public void call(ContextoSincronizacion contexto, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<NodoArbolDTO> locales = nodoHijoMapper.listarProcesosFull(null);

		List<NodoArbolDTO> remotos = contexto.porCategoria(PropiedadValorDefinidoDTO.PROCESO);
		if (remotos.isEmpty())
			return;

		log.setRoot("SynchronizeProcessService");
		for (NodoArbolDTO remoto : remotos) {
			validarCodigoUnico(locales, remoto.getCodigo());
			NodoArbolDTO local = buscarPorCodigo(locales, remoto.getCodigo());
			if (local != null) {
				locales.remove(local);
				contexto.registrarLocal(remoto, local.getLlaveTabla());
				log.info("EXIST PROCESS " + local.getCodigo() + " - " + local.getNombre());
			} else {
				if (compare) {
					log.error("COMPARE NOT EXIST PROCESS " + remoto.getCodigo() + " - " + remoto.getNombre());
				} else {
					ProcesoDTO nuevo = new ProcesoDTO();
					nuevo.setCodigo(remoto.getCodigo());
					nuevo.setImagen(remoto.getImagen());
					nuevo.setNombre(remoto.getNombre());
					nuevo.setPrioridad(remoto.getPrioridad());
					nuevo.setTipo(remoto.getTipo());
					nuevo.setNodoSuperior(llavePadreProceso(contexto, remoto));
					nuevo = processService.save(nuevo);
					contexto.registrarLocal(remoto, nuevo.getLlaveTabla());
					log.info("NEW PROCESS " + nuevo.getCodigo() + " - " + nuevo.getNombre());
				}
			}
		}

		sincronizarMacroproceso(contexto, remotos, log);

		for (NodoArbolDTO remoto : remotos) {
			String local = contexto.local(remoto);
			if (local == null)
				continue;
			propertiesSynchronizeService.call(contexto.getPropiedades(), remoto.getLlaveTabla(),
					PropiedadValorDefinidoDTO.PROCESO, local, log, compare);
		}
	}

	private void sincronizarMacroproceso(ContextoSincronizacion contexto, List<NodoArbolDTO> remotos,
			LogConfigurationDTO log) throws ServerException {
		for (NodoArbolDTO remoto : remotos) {
			String llaveLocal = contexto.local(remoto);
			String padreLocal = llavePadreProceso(contexto, remoto);
			if (llaveLocal == null || padreLocal == null)
				continue;
			ProcesoDTO local = processService.consultaXId(llaveLocal);
			if (local == null || padreLocal.equals(local.getNodoSuperior()))
				continue;
			local.setNodoSuperior(padreLocal);
			processService.update(local);
			log.info("PROCESS MACROPROCESS UPDATED " + local.getCodigo());
		}
	}

	private String llavePadreProceso(ContextoSincronizacion contexto, NodoArbolDTO remoto) {
		NodoArbolDTO padre = contexto.porLlave(remoto.getNodoSuperior());
		if (padre == remoto)
			return null;
		if (padre == null)
			padre = contexto.padreDe(remoto);
		if (padre == null || !PropiedadValorDefinidoDTO.PROCESO.equals(padre.getCategoria()))
			return null;
		return contexto.local(padre);
	}

	private void validarCodigoUnico(List<NodoArbolDTO> locales, String codigo) throws ServerException {
		if (codigo == null)
			return;
		int encontrados = 0;
		for (NodoArbolDTO local : locales) {
			if (codigo.equals(local.getCodigo()))
				encontrados++;
		}
		if (encontrados > 1)
			throw new ServerException("Corrige los codigos de los procesos no pueden ser duplicados, el codigo "
					+ codigo + " esta duplicado");
	}

	private NodoArbolDTO buscarPorCodigo(List<NodoArbolDTO> array, String codigo) {
		if (codigo == null)
			return null;
		for (NodoArbolDTO local : array) {
			if (codigo.equals(local.getCodigo()))
				return local;
		}
		return null;
	}

}
