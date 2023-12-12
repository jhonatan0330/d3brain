package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.report.domain.ReporteBaseDTO;

@Service
public class SynchronizeTemplateService {

	@Autowired
	private DocumentoPlantillaSvc templateService;
	@Autowired
	private SynchronizeReportService reportSynchronizeService;
	@Autowired
	private SynchronizeTemplateFieldService fieldSynchronizeService;
	@Autowired
	private SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log) throws ServerException {
		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize(null);
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (remoteList != null && !remoteList.isEmpty()) {
			log.setRoot("SynchronizeTemplateService");
			for (DocumentoPlantillaDTO remote : remoteList) {
				DocumentoPlantillaDTO local = findTemplateInList(localListToErase, remote.getCodigo(), remote.getProceso());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					log.info("EXIST " + remote.getCodigo() + " - " + remote.getNombre());
				} else {
					DocumentoPlantillaDTO newProcess = new DocumentoPlantillaDTO();
					newProcess.setCodigo(remote.getCodigo());
					newProcess.setImagen(remote.getImagen());
					newProcess.setProceso(remote.getProceso());
					newProcess.setNombre(remote.getNombre());
					newProcess.setObjetivo(remote.getObjetivo());
					local = templateService.save(newProcess);
					log.info("NEW " + remote.getCodigo() + " - " + remote.getNombre());
				}
				changeReportTemplateField(hierarchy.getReports(), remote.getLlaveTabla(), local.getLlaveTabla());
				changeTemplateInTransitions(hierarchy.getTransitions(), remote.getLlaveTabla(), local.getLlaveTabla());
				changeTemplateInRelations(hierarchy.getRelations(), remote.getLlaveTabla(), local.getLlaveTabla());
			}
		}
		callAfterCreateAllTemplate(token, hierarchy, log);
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

	private void callAfterCreateAllTemplate(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log)
			throws ServerException {
		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize(null);
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (remoteList != null && !remoteList.isEmpty()) {
			reportSynchronizeService.call(token, hierarchy, log);
			for (DocumentoPlantillaDTO remote : remoteList) {
				DocumentoPlantillaDTO local = findTemplateInList(localListToErase, remote.getCodigo(), remote.getProceso());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					log.setRoot("SynchronizeTemplateServiceAfter " + local.getNombre());
					fieldSynchronizeService.call(token, hierarchy, remote.getLlaveTabla(), local.getLlaveTabla(), log);
					log.setRoot("SynchronizeTemplateServiceAfter " + local.getNombre());
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PLANTILLA, local.getLlaveTabla(), token, log);
					// synchronizeFieldReport(token, hierarchy, remote.getLlaveTabla(),
					// local.getLlaveTabla(), log);
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
				}
			}
		}
		return null;
	}

	public void callCreateRol(String token, HierarchyExporterDTO hierarchy, List<PropiedadDTO> propertiesToCreateRole,
			LogConfigurationDTO log) throws ServerException {

		if (propertiesToCreateRole == null || propertiesToCreateRole.isEmpty())
			return;

		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize(null);
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (localListToErase == null || remoteList == null)
			return;
		for (PropiedadDTO propertyRole : propertiesToCreateRole) {
			for (DocumentoPlantillaDTO remote : remoteList) {
				if (remote.getLlaveTabla().compareTo(propertyRole.getCampo()) == 0) {
					DocumentoPlantillaDTO local = findTemplateInList(localListToErase, remote.getCodigo(), remote.getProceso());
					HierarchyExporterDTO hierarchyRole = new HierarchyExporterDTO();
					hierarchyRole.setRelations(hierarchy.getRelations());
					hierarchyRole.setProperties(propertiesToCreateRole);
					propertiesSynchronizeService.call(hierarchyRole, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PLANTILLA, local.getLlaveTabla(), token, log);
				}
			}
		}
	}
}
