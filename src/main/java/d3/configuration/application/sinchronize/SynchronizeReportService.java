package d3.configuration.application.sinchronize;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.infrastructure.NodoHijoMapper;
import d3.report.application.ReporteBaseSvc;
import d3.report.domain.ReporteBaseDTO;
import d3.shared.domain.ServerException;

@Service
public class SynchronizeReportService {

	private final ReporteBaseSvc reportService;
	private final SynchronizePropertiesService propertiesSynchronizeService;
	private final NodoHijoMapper nodoHijoMapper;

	public SynchronizeReportService(@Lazy ReporteBaseSvc reportService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService, @Lazy NodoHijoMapper nodoHijoMapper) {
		this.reportService = reportService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
		this.nodoHijoMapper = nodoHijoMapper;
	}

	public void call(ContextoSincronizacion contexto, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<NodoArbolDTO> locales = nodoHijoMapper.listarReportesFull(null);
		List<NodoArbolDTO> remotos = contexto.porCategoria(PropiedadValorDefinidoDTO.REPORTE);
		if (remotos.isEmpty())
			return;

		for (NodoArbolDTO remoto : remotos) {
			log.setRoot("SynchronizeReport " + remoto.getNombre());
			String localTemplate = resolverPlantillaLocal(contexto, remoto);
			if (localTemplate == null) {
				log.error(" El reporte " + remoto.getNombre() + "(Cod. " + remoto.getCodigo()
						+ ") No se logro encontrar la plantilla local");
				continue;
			}
			NodoArbolDTO local = buscarLocal(locales, remoto.getCodigo(), localTemplate);
			if (local != null) {
				locales.remove(local);
				contexto.registrarLocal(remoto, local.getLlaveTabla());
				log.info("EXIST REPORT " + remoto.getCodigo() + " - " + remoto.getNombre());
			} else {
				if (compare) {
					log.error("COMPARE NOT EXIST REPORT " + remoto.getCodigo() + " - " + remoto.getNombre());
				} else {
					ReporteBaseDTO nuevo = new ReporteBaseDTO();
					nuevo.setNodoSuperior(localTemplate);
					nuevo.setCodigo(remoto.getCodigo());
					nuevo.setNombre(remoto.getNombre());
					try {
						nuevo = reportService.save(nuevo);
						contexto.registrarLocal(remoto, nuevo.getLlaveTabla());
						log.info("NEW REPORT " + nuevo.getCodigo() + " - " + nuevo.getNombre());
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
			log.setRoot("SynchronizeReport " + remoto.getNombre());
			propertiesSynchronizeService.call(contexto.getPropiedades(), remoto.getLlaveTabla(),
					PropiedadValorDefinidoDTO.REPORTE, local, log, compare);
		}
	}

	private String resolverPlantillaLocal(ContextoSincronizacion contexto, NodoArbolDTO remoto) {
		NodoArbolDTO plantilla = contexto.porLlave(remoto.getNodoSuperior());
		if (plantilla == null)
			plantilla = contexto.padreDe(remoto);
		if (plantilla == null || !PropiedadValorDefinidoDTO.PLANTILLA.equals(plantilla.getCategoria()))
			return null;
		return contexto.local(plantilla);
	}

	private NodoArbolDTO buscarLocal(List<NodoArbolDTO> array, String codigo, String localTemplate) {
		if (array == null || codigo == null)
			return null;
		for (NodoArbolDTO local : array) {
			if (codigo.equals(local.getCodigo()) && localTemplate.equals(local.getNodoSuperior()))
				return local;
		}
		return null;
	}
}
