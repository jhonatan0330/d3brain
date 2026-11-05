package d3.configuration.application.sinchronize;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.shared.domain.ServerException;
import d3.webservice.application.WebServiceSvc;
import d3.webservice.domain.WebServiceDTO;

@Service
public class SynchronizeApiService {

	private final WebServiceSvc apisService;
	private final SynchronizePropertiesService propertiesSynchronizeService;
	private final NodoHijoMapper nodoHijoMapper;

	public SynchronizeApiService(@Lazy WebServiceSvc apisService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService, @Lazy NodoHijoMapper nodoHijoMapper) {
		this.apisService = apisService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public void call(ContextoSincronizacion contexto, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<NodoArbolDTO> locales = nodoHijoMapper.listarApisFull(null);
		List<NodoArbolDTO> remotos = contexto.porCategoria(PropiedadValorDefinidoDTO.API_SERVICE);
		if (remotos.isEmpty())
			return;

		log.setRoot("SynchronizeApi");
		for (NodoArbolDTO remoto : remotos) {
			if (remoto.getCodigo() == null || remoto.getCodigo().isEmpty()) {
				log.error("API CODE IS NULL " + remoto.getNombre());
				continue;
			}
			NodoArbolDTO local = buscarPorCodigo(locales, remoto.getCodigo());
			if (local != null) {
				locales.remove(local);
				contexto.registrarLocal(remoto, local.getLlaveTabla());
				log.info("EXIST API " + remoto.getCodigo() + " - " + remoto.getNombre());
			} else {
				if (compare) {
					log.error("COMPARE NOT EXIST API " + remoto.getCodigo() + " - " + remoto.getNombre());
				} else {
					WebServiceDTO nueva = new WebServiceDTO();
					nueva.setCodigo(remoto.getCodigo());
					nueva.setNombre(remoto.getNombre());
					nueva.setNodoSuperior(llavePadreApi(contexto, remoto));
					nueva = apisService.save(nueva);
					contexto.registrarLocal(remoto, nueva.getLlaveTabla());
					log.info("NEW API " + nueva.getCodigo() + " - " + nueva.getNombre());
				}
			}
		}

		for (NodoArbolDTO remoto : remotos) {
			String local = contexto.local(remoto);
			if (local == null)
				continue;
			log.setRoot("SynchronizeApi " + remoto.getNombre());
			propertiesSynchronizeService.call(contexto.getPropiedades(), remoto.getLlaveTabla(),
					PropiedadValorDefinidoDTO.API_SERVICE, local, log, compare);
		}
	}

	private String llavePadreApi(ContextoSincronizacion contexto, NodoArbolDTO remoto) {
		NodoArbolDTO padre = contexto.porLlave(remoto.getNodoSuperior());
		if (padre == null)
			padre = contexto.padreDe(remoto);
		if (padre == null || !PropiedadValorDefinidoDTO.PROCESO.equals(padre.getCategoria()))
			return null;
		return contexto.local(padre);
	}

	private NodoArbolDTO buscarPorCodigo(List<NodoArbolDTO> array, String codigo) {
		if (array == null || array.isEmpty() || codigo == null)
			return null;
		for (NodoArbolDTO local : array) {
			if (local != null && codigo.equals(local.getCodigo()))
				return local;
		}
		return null;
	}

}
