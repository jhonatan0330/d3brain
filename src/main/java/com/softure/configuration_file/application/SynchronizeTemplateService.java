package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.report.domain.ReporteBaseDTO;

@Service
public class SynchronizeTemplateService {

	@Autowired private DocumentoPlantillaSvc templateService;
	@Autowired private SynchronizeReportService reportSynchronizeService;
	@Autowired private SynchronizeTemplateFieldService fieldSynchronizeService;
	@Autowired private SynchronizePropertiesService propertiesSynchronizeService;
	
	public void call(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize();
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (DocumentoPlantillaDTO remote : remoteList) {
				DocumentoPlantillaDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					localListToErase.remove(local);
					changeReportTemplateField(hierarchy.getReports(), remote.getLlaveTabla(), local.getLlaveTabla());
					changeTemplateInTransitions(hierarchy.getTransitions(), remote.getLlaveTabla(), local.getLlaveTabla());
				}
				else
				{
					DocumentoPlantillaDTO newProcess = new DocumentoPlantillaDTO();
					newProcess.setCodigo(remote.getCodigo());
					newProcess.setImagen(remote.getImagen());
					newProcess.setProceso(remote.getProceso());
					newProcess.setNombre(remote.getNombre());
					newProcess.setObjetivo(remote.getObjetivo());
					newProcess = templateService.save(newProcess);
					changeReportTemplateField(hierarchy.getReports(), remote.getLlaveTabla(), newProcess.getLlaveTabla());
					changeTemplateInTransitions(hierarchy.getTransitions(), remote.getLlaveTabla(), newProcess.getLlaveTabla());
				}
			}
		}
		callAfterCreateAllTemplate(token, hierarchy);
	}
	
	private void changeReportTemplateField(List<ReporteBaseDTO> remoteList, String remote,
			String local) {
		for (ReporteBaseDTO remoteProcess : remoteList) {
			if(remoteProcess.getPlantilla()!=null && remoteProcess.getPlantilla().compareTo(remote)==0) {
				remoteProcess.setPlantilla(local);
			}
		}	
	}
	
	private void changeTemplateInTransitions(List<ProcesoTransicionDTO> array, String remote, String local) {
		for (ProcesoTransicionDTO remoteProcess : array) {
			if(remoteProcess.getPlantilla()!=null && remoteProcess.getPlantilla().compareTo(remote)==0) {
				remoteProcess.setPlantilla(local);
			}
		}	
	}

	private void callAfterCreateAllTemplate(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize();
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (remoteList != null && !remoteList.isEmpty()) {
			reportSynchronizeService.call(token, hierarchy);
			for (DocumentoPlantillaDTO remote : remoteList) {
				DocumentoPlantillaDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					System.out.println(local.getNombre());
					localListToErase.remove(local);
					synchronizeFieldReport(token, hierarchy, remote.getLlaveTabla(), local.getLlaveTabla());
				}
			}
		}
	}

	private void synchronizeFieldReport(String token, HierarchyExporterDTO hierarchy, String remote,
			String local) throws ServerException {
		
		fieldSynchronizeService.call(token, hierarchy, remote, local);
		propertiesSynchronizeService.call(hierarchy.getProperties(), remote,
				PropiedadValorDefinidoDTO.PLANTILLA, local, token);
	}

	private DocumentoPlantillaDTO findTemplateInList(List<DocumentoPlantillaDTO> array, String code) {
		for (DocumentoPlantillaDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}
	
	public void callCreateRol(String token, HierarchyExporterDTO hierarchy, List<PropiedadDTO> propertiesToCreateRole) throws ServerException {
		
		if (propertiesToCreateRole == null || propertiesToCreateRole.isEmpty()) return; 
		
		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize();
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		
		for (PropiedadDTO propertyRole : propertiesToCreateRole) {
			for (DocumentoPlantillaDTO remote : remoteList) {
				if(remote.getLlaveTabla().compareTo(propertyRole.getCampo())==0) {
					DocumentoPlantillaDTO local = findTemplateInList(localListToErase, remote.getCodigo());
					propertiesSynchronizeService.call(propertiesToCreateRole, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.PLANTILLA, local.getLlaveTabla(), token);
				}
			}
		}
	}
}
