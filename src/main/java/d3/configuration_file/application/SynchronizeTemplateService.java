package d3.configuration_file.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.configuration_file.domain.HierarchyExporterDTO;
import d3.configuration_file.domain.LogConfigurationDTO;
import d3.process_designer.domain.ProcesoTransicionDTO;
import d3.process_form.application.DocumentoPlantillaSvc;
import d3.process_form.domain.DocumentoPlantillaDTO;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import d3.property.domain.RelacionInternaDTO;
import d3.report.domain.ReporteBaseDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class SynchronizeTemplateService {

	private final DocumentoPlantillaSvc templateService;
	private final SynchronizeReportService reportSynchronizeService;
	private final SynchronizeTemplateFieldService fieldSynchronizeService;
	private final SynchronizePropertiesService propertiesSynchronizeService;

	public SynchronizeTemplateService(@Lazy DocumentoPlantillaSvc templateService,
			@Lazy SynchronizeReportService reportSynchronizeService,
			@Lazy SynchronizeTemplateFieldService fieldSynchronizeService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService) {
		this.templateService = templateService;
		this.reportSynchronizeService = reportSynchronizeService;
		this.fieldSynchronizeService = fieldSynchronizeService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
	}

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize(null);
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (remoteList != null && !remoteList.isEmpty()) {
			log.setRoot("SynchronizeTemplateService");
			for (DocumentoPlantillaDTO remote : remoteList) {
				DocumentoPlantillaDTO local = findTemplateInList(localListToErase, remote.getCodigo(),
						remote.getProceso());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					log.info("EXIST TEMPLATE " + remote.getCodigo() + " - " + remote.getNombre());
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST TEMPLATE " + remote.getCodigo() + " - " + remote.getNombre());
					} else {
						DocumentoPlantillaDTO newProcess = new DocumentoPlantillaDTO();
						newProcess.setCodigo(remote.getCodigo());
						newProcess.setImagen(remote.getImagen());
						newProcess.setProceso(remote.getProceso());
						newProcess.setNombre(remote.getNombre());
						newProcess.setObjetivo(remote.getObjetivo());
						try {
							local = templateService.save(newProcess);
							log.info("NEW TEMPLATE " + remote.getCodigo() + " - " + remote.getNombre());
						} catch (Exception e) {
							log.error(remote.getCodigo() + " - " + remote.getNombre() + " : " + e.getMessage());
						}
					}

				}
				if (local != null) {
					changeReportTemplateField(hierarchy.getReports(), remote.getLlaveTabla(), local.getLlaveTabla());
					changeTemplateInTransitions(hierarchy.getTransitions(), remote.getLlaveTabla(),
							local.getLlaveTabla());
					changeTemplateInRelations(hierarchy.getRelations(), remote.getLlaveTabla(), local.getLlaveTabla());
				}
			}
		}
		// Esto es para que se creen las transiciones
		// callAfterCreateAllTemplate(token, hierarchy, log);
	}

	private void changeReportTemplateField(List<ReporteBaseDTO> remoteList, String remote, String local) {
		for (ReporteBaseDTO remoteProcess : remoteList) {
			if (remoteProcess.getPlantilla() != null && remoteProcess.getPlantilla().compareTo(remote) == 0) {
				remoteProcess.setPlantilla(local);
			}
		}
	}

	private void changeTemplateInTransitions(List<ProcesoTransicionDTO> array, String remote, String local) {
		for (ProcesoTransicionDTO remoteProcess : array) {
			if (remoteProcess.getPlantilla() != null && remoteProcess.getPlantilla().compareTo(remote) == 0) {
				remoteProcess.setPlantilla(local);
			}
		}
	}

	private void changeTemplateInRelations(List<RelacionInternaDTO> array, String remote, String local) {
		for (RelacionInternaDTO remoteRelations : array) {
			if (remoteRelations.getPlantilla() != null && remoteRelations.getPlantilla().compareTo(remote) == 0) {
				remoteRelations.setPlantilla(local);
			}
		}
	}

	public void callAfterCreateAllTemplate(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log,
			boolean compare) throws ServerException {
		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize(null);
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (remoteList != null && !remoteList.isEmpty()) {
			reportSynchronizeService.call(token, hierarchy, log, compare);
			for (DocumentoPlantillaDTO remote : remoteList) {
				DocumentoPlantillaDTO local = findTemplateInList(localListToErase, remote.getCodigo(),
						remote.getProceso());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					log.setRoot("Sincronizando los campos de la plantilla " + local.getNombre() + "(Cod:"
							+ local.getCodigo() + " )");
					fieldSynchronizeService.call(token, hierarchy, remote.getLlaveTabla(), local.getLlaveTabla(), log,
							compare);
					log.setRoot("Sincronizando las propiedades de la plantilla " + local.getNombre() + "(Cod:"
							+ local.getCodigo() + " )");
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PLANTILLA, local.getLlaveTabla(), token, log, compare);
					// synchronizeFieldReport(token, hierarchy, remote.getLlaveTabla(),
					// local.getLlaveTabla(), log);
				} else {
					log.error("NOT FIND " + remote.getCodigo() + "  - " + remote.getProceso());
				}
			}
		}
	}

	/*
	 * private void synchronizeFieldReport(String token, HierarchyExporterDTO
	 * hierarchy, String remote, String local, LogConfigurationDTO log) throws
	 * ServerException {
	 * 
	 * }
	 */
	private DocumentoPlantillaDTO findTemplateInList(List<DocumentoPlantillaDTO> array, String code, String process) {
		if (array == null)
			return null;
		for (DocumentoPlantillaDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				if ((localProcess.getProceso() == null && process == null) || (localProcess.getProceso() != null
						&& process != null && localProcess.getProceso().compareTo(process) == 0)) {
					return localProcess;
				} else {
					return null;
				}
			}
		}
		return null;
	}

	public void callCreateRol(String token, HierarchyExporterDTO hierarchy, List<PropiedadDTO> propertiesToCreateRole,
			LogConfigurationDTO log, boolean compare) throws ServerException {

		if (propertiesToCreateRole == null || propertiesToCreateRole.isEmpty())
			return;

		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize(null);
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (localListToErase == null || remoteList == null)
			return;
		for (PropiedadDTO propertyRole : propertiesToCreateRole) {
			for (DocumentoPlantillaDTO remote : remoteList) {
				if (remote.getLlaveTabla().compareTo(propertyRole.getCampo()) == 0) {
					DocumentoPlantillaDTO local = findTemplateInList(localListToErase, remote.getCodigo(),
							remote.getProceso());
					if (local != null) {
						HierarchyExporterDTO hierarchyRole = new HierarchyExporterDTO();
						hierarchyRole.setRelations(hierarchy.getRelations());
						hierarchyRole.setProperties(propertiesToCreateRole);
						propertiesSynchronizeService.call(hierarchyRole, remote.getLlaveTabla(),
								PropiedadValorDefinidoDTO.PLANTILLA, local.getLlaveTabla(), token, log, compare);
					} else {
						log.error("NOT FIND TO ROLE" + remote.getCodigo() + "  - " + remote.getProceso());
					}

				}
			}
		}
	}
}
