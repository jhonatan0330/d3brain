package d3.configuration.application.sinchronize;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.shared.domain.ServerException;

@Service
public class SynchronizeTemplateFieldService {

	private final DocumentoPlantillaCaracteristicaSvc fieldService;
	private final SynchronizePropertiesService propertiesSynchronizeService;
	private final NodoHijoMapper nodoHijoMapper;

	public SynchronizeTemplateFieldService(@Lazy DocumentoPlantillaCaracteristicaSvc fieldService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService, @Lazy NodoHijoMapper nodoHijoMapper) {
		this.fieldService = fieldService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public void call(ContextoSincronizacion contexto, NodoArbolDTO remotoPlantilla, String localTemplate,
			LogConfigurationDTO log, boolean compare) throws ServerException {
		List<NodoArbolDTO> locales = camposDePlantilla(nodoHijoMapper.listarCamposFull(null), localTemplate);
		List<NodoArbolDTO> remotos = camposDePlantilla(contexto.porCategoria(PropiedadValorDefinidoDTO.CAMPO),
				remotoPlantilla.getLlaveTabla());
		if (remotos.isEmpty())
			return;

		String templateRoot = log.getRoot();
		for (NodoArbolDTO remoto : remotos) {
			log.setRoot(templateRoot + "...." + remoto.getNombre());
			NodoArbolDTO local = buscarPorCodigo(locales, remoto.getCodigo());
			if (local != null) {
				locales.remove(local);
				contexto.registrarLocal(remoto, local.getLlaveTabla());
				log.info("EXIST FIELD " + remoto.getNombre() + " (Cod: " + remoto.getCodigo() + ")");
			} else {
				if (compare) {
					log.error("COMPARE NOT EXIST FIELD " + remoto.getNombre() + " (Cod: " + remoto.getCodigo() + ")");
				} else {
					DocumentoPlantillaCaracteristicaDTO nuevo = new DocumentoPlantillaCaracteristicaDTO();
					nuevo.setNodoSuperior(localTemplate);
					nuevo.setCodigo(remoto.getCodigo());
					nuevo.setImagen(remoto.getImagen());
					nuevo.setTipo(remoto.getTipo());
					nuevo.setNombre(remoto.getNombre());
					nuevo.setPrioridad(remoto.getPrioridad());
					try {
						nuevo = fieldService.save(nuevo);
						contexto.registrarLocal(remoto, nuevo.getLlaveTabla());
						log.info("NEW FIELD " + nuevo.getNombre() + " (Cod: " + nuevo.getCodigo() + ")");
					} catch (Exception e) {
						log.error(remoto.getCodigo() + " - " + remoto.getNombre() + " : " + e.getMessage());
					}
				}
			}
		}

		for (NodoArbolDTO remoto : remotos) {
			String local = contexto.local(remoto);
			if (local == null)
				continue;
			log.setRoot(templateRoot + ".... Campo ->" + remoto.getNombre());
			propertiesSynchronizeService.call(contexto.getPropiedades(), remoto.getLlaveTabla(),
					PropiedadValorDefinidoDTO.CAMPO, local, log, compare);
		}
		log.setRoot(templateRoot);
	}

	private List<NodoArbolDTO> camposDePlantilla(List<NodoArbolDTO> full, String llavePlantilla) {
		List<NodoArbolDTO> campos = new ArrayList<>();
		if (full == null || llavePlantilla == null)
			return campos;
		for (NodoArbolDTO campo : full) {
			if (llavePlantilla.equals(campo.getNodoSuperior()))
				campos.add(campo);
		}
		return campos;
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
