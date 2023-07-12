package com.softure.configuration_file.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeTemplateFieldService {

	@Autowired DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired SynchronizePropertiesService propertiesSynchronizeService;
	
	public void call(String token, HierarchyExporterDTO hierarchy, String remoteTemplate, String localTemplate) throws ServerException {
		List<DocumentoPlantillaCaracteristicaDTO> localListToErase = getFieldsFromTemplate(fieldService.getFullToSynchronize(), localTemplate) ;
		List<DocumentoPlantillaCaracteristicaDTO> remoteList = getFieldsFromTemplate(hierarchy.getFields(), remoteTemplate);
		if (remoteList != null && !remoteList.isEmpty()) {
			for (DocumentoPlantillaCaracteristicaDTO remote : remoteList) {
				DocumentoPlantillaCaracteristicaDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					localListToErase.remove(local);
				}
				else
				{
					DocumentoPlantillaCaracteristicaDTO newField = new DocumentoPlantillaCaracteristicaDTO();
					newField.setPlantilla(localTemplate);
					newField.setCodigo(remote.getCodigo());
					newField.setImagen(remote.getImagen());
					newField.setFormato(remote.getFormato());
					newField.setNombre(remote.getNombre());
					newField.setObjetivo(remote.getObjetivo());
					newField.setOrden(remote.getOrden());
					newField = fieldService.save(newField);
					
				}
			}
		}
		callAfterCreateAll(token, hierarchy, remoteTemplate, localTemplate);
	}
	
	private void callAfterCreateAll(String token, HierarchyExporterDTO hierarchy, String remoteTemplate, String localTemplate) throws ServerException {
		List<DocumentoPlantillaCaracteristicaDTO> localListToErase = getFieldsFromTemplate(fieldService.getFullToSynchronize(), localTemplate) ;
		List<DocumentoPlantillaCaracteristicaDTO> remoteList = getFieldsFromTemplate(hierarchy.getFields(), remoteTemplate);
		if (remoteList != null && !remoteList.isEmpty()) {
			for (DocumentoPlantillaCaracteristicaDTO remote : remoteList) {
				DocumentoPlantillaCaracteristicaDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					System.out.println(local.getPlantillaNombre() + " : " + local.getNombre());
					localListToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy.getProperties(), remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.CAMPO, local.getLlaveTabla(), token);
				}
			}
		}
	}

	public void callAfterRol(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		/*List<DocumentoPlantillaCaracteristicaDTO> localToErase = fieldService.getFullToSynchronize();
		List<DocumentoPlantillaCaracteristicaDTO> remoteList = hierarchy.getTemplates();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (DocumentoPlantillaCaracteristicaDTO remote : remoteList) {
				DocumentoPlantillaCaracteristicaDTO local = findTemplateInList(localToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					localToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy.getProperties(), remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.CAMPO, local.getLlaveTabla(), token);
				}
			}
		}*/
	}

	private DocumentoPlantillaCaracteristicaDTO findTemplateInList(List<DocumentoPlantillaCaracteristicaDTO> array, String code) {
		for (DocumentoPlantillaCaracteristicaDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}
	
	private List<DocumentoPlantillaCaracteristicaDTO> getFieldsFromTemplate(List<DocumentoPlantillaCaracteristicaDTO> fullToSynchronize, String template) {
		if(fullToSynchronize ==null || fullToSynchronize.isEmpty())	return null;
		return fullToSynchronize.stream()
			      .filter(field -> (field.getPlantilla().compareTo(template)==0))
			      .collect(Collectors.toList());
	}
}
