package d3.configuration.application.sinchronize;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.shared.domain.ServerException;

@Service
public class SynchronizeTemplateService {

	private final DocumentoPlantillaSvc templateService;
	private final SynchronizeReportService reportSynchronizeService;
	private final SynchronizeTemplateFieldService fieldSynchronizeService;
	private final SynchronizePropertiesService propertiesSynchronizeService;
	private final NodoHijoMapper nodoHijoMapper;

	public SynchronizeTemplateService(@Lazy DocumentoPlantillaSvc templateService,
			@Lazy SynchronizeReportService reportSynchronizeService,
			@Lazy SynchronizeTemplateFieldService fieldSynchronizeService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService, @Lazy NodoHijoMapper nodoHijoMapper) {
		this.templateService = templateService;
		this.reportSynchronizeService = reportSynchronizeService;
		this.fieldSynchronizeService = fieldSynchronizeService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public void call(ContextoSincronizacion contexto, LogConfigurationDTO log, boolean compare) {
		List<NodoArbolDTO> locales = nodoHijoMapper.listarPlantillasFull(null);
		List<NodoArbolDTO> remotos = contexto.porCategoria(PropiedadValorDefinidoDTO.PLANTILLA);
		if (remotos.isEmpty())
			return;

		log.setRoot("SynchronizeTemplateService");
		for (NodoArbolDTO remoto : remotos) {
			String procesoLocal = contexto.localPorCategoria(PropiedadValorDefinidoDTO.PROCESO, remoto.getAtt1());
			String padreLocal = contexto.localPadre(remoto);
			NodoArbolDTO local = buscarLocal(locales, remoto.getCodigo(), procesoLocal);
			if (local != null) {
				locales.remove(local);
				contexto.registrarLocal(remoto, local.getLlaveTabla());
				log.info("EXIST TEMPLATE " + remoto.getCodigo() + " - " + remoto.getNombre());
			} else {
				if (compare) {
					log.error("COMPARE NOT EXIST TEMPLATE " + remoto.getCodigo() + " - " + remoto.getNombre());
				} else if (procesoLocal == null) {
					log.error("TEMPLATE SIN PROCESO LOCAL " + remoto.getCodigo() + " - " + remoto.getNombre());
				} else {
					DocumentoPlantillaDTO nueva = new DocumentoPlantillaDTO();
					nueva.setCodigo(remoto.getCodigo());
					nueva.setImagen(remoto.getImagen());
					nueva.setProceso(procesoLocal);
					nueva.setNombre(remoto.getNombre());
					nueva.setTipo(remoto.getTipo());
					nueva.setNodoSuperior(padreLocal != null ? padreLocal : procesoLocal);
					try {
						nueva = templateService.save(nueva);
						contexto.registrarLocal(remoto, nueva.getLlaveTabla());
						log.info("NEW TEMPLATE " + nueva.getCodigo() + " - " + nueva.getNombre());
					} catch (Exception e) {
						log.error(remoto.getCodigo() + " - " + remoto.getNombre() + " : " + e.getMessage());
					}
				}
			}
		}
	}

	public void callAfterCreateAllTemplate(ContextoSincronizacion contexto, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<NodoArbolDTO> remotos = contexto.porCategoria(PropiedadValorDefinidoDTO.PLANTILLA);
		if (remotos.isEmpty())
			return;

		reportSynchronizeService.call(contexto, log, compare);

		for (NodoArbolDTO remoto : remotos) {
			String local = contexto.local(remoto);
			if (local == null) {
				log.error("NOT FIND " + remoto.getCodigo() + "  - " + remoto.getAtt1());
				continue;
			}
			log.setRoot("Sincronizando los campos de la plantilla " + remoto.getNombre() + "(Cod:"
					+ remoto.getCodigo() + " )");
			fieldSynchronizeService.call(contexto, remoto, local, log, compare);
			log.setRoot("Sincronizando las propiedades de la plantilla " + remoto.getNombre() + "(Cod:"
					+ remoto.getCodigo() + " )");
			propertiesSynchronizeService.call(contexto.getPropiedades(), remoto.getLlaveTabla(),
					PropiedadValorDefinidoDTO.PLANTILLA, local, log, compare);
		}
	}

	public void callCreateRol(ContextoSincronizacion contexto, List<PropiedadDTO> propertiesToCreateRole,
			LogConfigurationDTO log, boolean compare) throws ServerException {

		if (propertiesToCreateRole == null || propertiesToCreateRole.isEmpty())
			return;

		for (PropiedadDTO propertyRole : propertiesToCreateRole) {
			NodoArbolDTO remoto = contexto.porLlave(propertyRole.getCampo());
			if (remoto == null || !PropiedadValorDefinidoDTO.PLANTILLA.equals(remoto.getCategoria())) {
				log.error("NOT FIND TO ROLE " + propertyRole.getCampo());
				continue;
			}
			String local = contexto.local(remoto);
			if (local == null) {
				log.error("NOT FIND TO ROLE" + remoto.getCodigo() + "  - " + remoto.getAtt1());
				continue;
			}
			propertiesSynchronizeService.call(propertiesToCreateRole, remoto.getLlaveTabla(),
					PropiedadValorDefinidoDTO.PLANTILLA, local, log, compare);
		}
	}

	private NodoArbolDTO buscarLocal(List<NodoArbolDTO> array, String codigo, String procesoLocal) {
		if (array == null || codigo == null)
			return null;
		for (NodoArbolDTO local : array) {
			if (codigo.equals(local.getCodigo()) && procesoLocal != null && procesoLocal.equals(local.getAtt1()))
				return local;
		}
		return null;
	}

}
