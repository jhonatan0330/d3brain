package com.softure.configuration_file.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Service
public class SynchronizeTemplateFieldService {

	@Autowired @Lazy 
	DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired @Lazy 
	SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy, String remoteTemplate, String localTemplate,
			LogConfigurationDTO log, boolean compare) throws ServerException {
		List<DocumentoPlantillaCaracteristicaDTO> localListToErase = getFieldsFromTemplate(
				fieldService.getFullToSynchronize(null), localTemplate);
		List<DocumentoPlantillaCaracteristicaDTO> remoteList = getFieldsFromTemplate(hierarchy.getFields(),
				remoteTemplate);
		if (remoteList != null && !remoteList.isEmpty()) {
			String templateRoot = log.getRoot();
			for (DocumentoPlantillaCaracteristicaDTO remote : remoteList) {
				log.setRoot(templateRoot + "...." + remote.getNombre());
				DocumentoPlantillaCaracteristicaDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					log.info("EXIST FIELD " + remote.getNombre() + " (Cod: " + remote.getCodigo() + ")");
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST FIELD " + remote.getNombre() + " (Cod: " + remote.getCodigo() + ")");
					} else {
						DocumentoPlantillaCaracteristicaDTO newField = new DocumentoPlantillaCaracteristicaDTO();
						newField.setPlantilla(localTemplate);
						newField.setCodigo(remote.getCodigo());
						newField.setImagen(remote.getImagen());
						newField.setFormato(remote.getFormato());
						newField.setNombre(remote.getNombre());
						newField.setObjetivo(remote.getObjetivo());
						newField.setOrden(remote.getOrden());
						try {
							local = fieldService.save(newField);
							log.info("NEW FIELD " + remote.getNombre() + " (Cod: " + remote.getCodigo() + ")");
						} catch (Exception e) {
							log.error(remote.getCodigo() + " - " + remote.getNombre() + " : " + e.getMessage());
						}
					}

				}
				if (local != null)
					changeTemplateInRelations(hierarchy.getRelations(), remote.getLlaveTabla(), local.getLlaveTabla());
			}
			log.setRoot(templateRoot);
		}
		callAfterCreateAll(token, hierarchy, remoteTemplate, localTemplate, log, compare);
	}

	private void changeTemplateInRelations(List<RelacionInternaDTO> array, String remote, String local) {
		for (RelacionInternaDTO remoteRelations : array) {
			if (remoteRelations.getCampo() != null && remoteRelations.getCampo().compareTo(remote) == 0) {
				remoteRelations.setCampo(local);
			}
		}
	}

	private void callAfterCreateAll(String token, HierarchyExporterDTO hierarchy, String remoteTemplate,
			String localTemplate, LogConfigurationDTO log, boolean compare) throws ServerException {
		List<DocumentoPlantillaCaracteristicaDTO> localListToErase = getFieldsFromTemplate(
				fieldService.getFullToSynchronize(null), localTemplate);
		List<DocumentoPlantillaCaracteristicaDTO> remoteList = getFieldsFromTemplate(hierarchy.getFields(),
				remoteTemplate);
		if (remoteList != null && !remoteList.isEmpty()) {
			String templateRoot = log.getRoot();
			for (DocumentoPlantillaCaracteristicaDTO remote : remoteList) {
				DocumentoPlantillaCaracteristicaDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local != null) {
					log.setRoot(templateRoot + ".... Campo ->" + remote.getNombre() );
					localListToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.CAMPO, local.getLlaveTabla(), token, log, compare);
				}
			}
		}
	}

	private DocumentoPlantillaCaracteristicaDTO findTemplateInList(List<DocumentoPlantillaCaracteristicaDTO> array,
			String code) {
		for (DocumentoPlantillaCaracteristicaDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

	private List<DocumentoPlantillaCaracteristicaDTO> getFieldsFromTemplate(
			List<DocumentoPlantillaCaracteristicaDTO> fullToSynchronize, String template) {
		if (fullToSynchronize == null || fullToSynchronize.isEmpty())
			return null;
		return fullToSynchronize.stream().filter(field -> (field.getPlantilla().compareTo(template) == 0))
				.collect(Collectors.toList());
	}
}
