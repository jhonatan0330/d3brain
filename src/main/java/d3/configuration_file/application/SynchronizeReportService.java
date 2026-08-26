package d3.configuration_file.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.configuration_file.domain.HierarchyExporterDTO;
import d3.configuration_file.domain.LogConfigurationDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import d3.report.application.ReporteBaseSvc;
import d3.report.domain.ReporteBaseDTO;

@Service
public class SynchronizeReportService {

	private final ReporteBaseSvc reportService;
	private final SynchronizePropertiesService propertiesSynchronizeService;

	public SynchronizeReportService(@Lazy ReporteBaseSvc reportService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService) {
		this.reportService = reportService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
	}

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<ReporteBaseDTO> localListToErase = reportService.getFullToSynchronize(null);
		List<ReporteBaseDTO> remoteList = hierarchy.getReports();

		if (remoteList != null && !remoteList.isEmpty()) {
			for (ReporteBaseDTO remote : remoteList) {
				log.setRoot("SynchronizeReport " + remote.getNombre());
				ReporteBaseDTO local = findTemplateInList(localListToErase, remote.getCodigo(), remote.getNombre());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					log.info("EXIST REPORT " + remote.getCodigo() + " - " + remote.getNombre());
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST REPORT " + remote.getCodigo() + " - " + remote.getNombre());
					} else {
						ReporteBaseDTO newReport = new ReporteBaseDTO();
						newReport.setPlantilla(remote.getPlantilla());
						newReport.setCodigo(remote.getCodigo());
						newReport.setDescripcion(remote.getDescripcion());
						newReport.setSoloExistente(remote.getSoloExistente());
						newReport.setNombre(remote.getNombre());
						newReport.setVariables(remote.getVariables());
						try {
							newReport = reportService.save(newReport);
							log.info("NEW REPORT " + remote.getCodigo() + " - " + remote.getNombre());
						} catch (Exception e) {
							log.error(remote.getCodigo() + " - " + remote.getNombre() + " : " + e.getMessage());
						}
					}

				}
			}
		}
		callAfterCreateAll(token, hierarchy, log, compare);
	}

	private void callAfterCreateAll(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log,
			boolean compare) throws ServerException {
		List<ReporteBaseDTO> localListToErase = reportService.getFullToSynchronize(null);
		List<ReporteBaseDTO> remoteList = hierarchy.getReports();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (ReporteBaseDTO remote : remoteList) {
				ReporteBaseDTO local = findTemplateInList(localListToErase, remote.getCodigo(), remote.getNombre());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.REPORTE, local.getLlaveTabla(), token, log, compare);
				}
			}
		}
	}

	/*
	 * private List<ReporteBaseDTO> getReportsFromTemplate(List<ReporteBaseDTO>
	 * fullToSynchronize, String template) { if(fullToSynchronize ==null ||
	 * fullToSynchronize.isEmpty()) return null; return fullToSynchronize.stream()
	 * .filter(report -> (report.getPlantilla().compareTo(template)==0))
	 * .collect(Collectors.toList()); }
	 */

	public void callAfterRol(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<ReporteBaseDTO> localToErase = reportService.getFullToSynchronize(null);
		List<ReporteBaseDTO> remoteList = hierarchy.getReports();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (ReporteBaseDTO remote : remoteList) {
				ReporteBaseDTO local = findTemplateInList(localToErase, remote.getCodigo(), remote.getNombre());
				// Creo el nuevo proceso
				if (local != null) {
					localToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.REPORTE, local.getLlaveTabla(), token, log, compare);
				}
			}
		}
	}

	private ReporteBaseDTO findTemplateInList(List<ReporteBaseDTO> array, String code, String nombre) {
		for (ReporteBaseDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0 && nombre.compareTo(localProcess.getNombre()) == 0) {
				return localProcess;
			}
		}
		return null;
	}
}
