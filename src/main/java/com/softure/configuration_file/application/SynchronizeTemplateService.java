package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

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
				}
			}
		}
		callAfterCreateAllTemplate(token, hierarchy);
	}
	
	private void callAfterCreateAllTemplate(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<DocumentoPlantillaDTO> localListToErase = templateService.getFullToSynchronize();
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (remoteList != null && !remoteList.isEmpty()) {
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
		reportSynchronizeService.call(token, hierarchy, remote, local);
		fieldSynchronizeService.call(token, hierarchy, remote, local);
		propertiesSynchronizeService.call(hierarchy.getProperties(), remote,
				PropiedadValorDefinidoDTO.PLANTILLA, local, token);
	}

	public void callAfterRol(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<DocumentoPlantillaDTO> localToErase = templateService.getFullToSynchronize();
		List<DocumentoPlantillaDTO> remoteList = hierarchy.getTemplates();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (DocumentoPlantillaDTO remote : remoteList) {
				DocumentoPlantillaDTO local = findTemplateInList(localToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					localToErase.remove(local);
					synchronizeFieldReport(token, hierarchy, remote.getLlaveTabla(), local.getLlaveTabla());
				}
			}
		}
	}

	private DocumentoPlantillaDTO findTemplateInList(List<DocumentoPlantillaDTO> array, String code) {
		for (DocumentoPlantillaDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}
}
