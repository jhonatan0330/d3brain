package com.softure.configuration_file.application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.FileVO;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.logisticpymes.application.CambioSvc;
import com.softure.logisticpymes.domain.CambioDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.upload.application.UploadSvc;

@Service
public class ImportConfigurationFileService {

	@Autowired
	private CambioSvc changeService;
	@Autowired
	private SynchronizeTypePropertiesService sincronizeTypeService;
	@Autowired
	private SynchronizeMessageService sincronizeMessageService;
	@Autowired
	private SynchronizeApiService sincronizeApiService;
	@Autowired
	private SynchronizeOrganizationService sincronizeOrganizationService;
	@Autowired
	private SynchronizeProcessService sincronizeProcessService;
	@Autowired
	private SynchronizeProcessStateService sincronizeProcessStateService;
	@Autowired
	private SynchronizeProcessTransitionService sincronizeProcessTransitionService;
	@Autowired
	private SynchronizeTemplateService sincronizeTemplateService;
	@Autowired
	private SynchronizeRelationService sincronizeRelationService;
	@Autowired
	private SynchronizeRolService sincronizeRolService;
	@Autowired
	private UploadSvc uploadService;

	public FileVO call(String token, FileVO file) throws ServerException {

		ObjectMapper mapper = new ObjectMapper();

		// JSON from file to Object
		try {
			HierarchyExporterDTO hierarchy = mapper.readValue(new URL(file.getUrl()), HierarchyExporterDTO.class);
			return uploadFile(token, sincronize(token, hierarchy).getLogs());
		} catch (StreamReadException e) {
			throw new ServerException(e.getMessage());
		} catch (DatabindException e) {
			throw new ServerException(e.getMessage());
		} catch (MalformedURLException e) {
			throw new ServerException(e.getMessage());
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public FileVO compare(String token, FileVO file) throws ServerException {

		ObjectMapper mapper = new ObjectMapper();

		// JSON from file to Object
		try {
			HierarchyExporterDTO hierarchy = mapper.readValue(new URL(file.getUrl()), HierarchyExporterDTO.class);
			return uploadFile(token, compareFile(token, hierarchy).getLogs());
		} catch (StreamReadException e) {
			throw new ServerException(e.getMessage());
		} catch (DatabindException e) {
			throw new ServerException(e.getMessage());
		} catch (MalformedURLException e) {
			throw new ServerException(e.getMessage());
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	private LogConfigurationDTO sincronize(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		CambioDTO changeRequest = new CambioDTO();
		changeRequest.setMotivo("Importacion");
		changeService.guardar(changeRequest, token);
		// aparto las propiedades TIPO_ROL porque al sincronizar las propiedades no se
		// actuzlaiban los campos y salia un error de esta propiedad ya fue definida
		List<PropiedadDTO> propertiesToCreateRoles = hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor().compareTo("PROP_141") == 0))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor().compareTo("PROP_141") != 0))
				.collect(Collectors.toList()));

		List<PropiedadDTO> rolInProperties = hierarchy.getProperties().stream()
				.filter(property -> (property.getRol() != null || property.getRolExcluyente() != null))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getRol() == null && property.getRolExcluyente() == null))
				.collect(Collectors.toList()));

		List<PropiedadDTO> templateUpdateProperties = hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor().compareTo("PROP_78") == 0))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor().compareTo("PROP_78") != 0))
				.collect(Collectors.toList()));

		LogConfigurationDTO logs = new LogConfigurationDTO();
		sincronizeTypeService.call(token, hierarchy, logs, false);
		sincronizeMessageService.call(token, hierarchy, logs, false);
		sincronizeApiService.call(token, hierarchy, logs, false);
		sincronizeOrganizationService.call(token, hierarchy, logs, false);
		sincronizeProcessService.call(token, hierarchy, logs, false);
		sincronizeProcessStateService.call(token, hierarchy, logs, false);
		sincronizeTemplateService.call(token, hierarchy, logs, false);
		sincronizeProcessTransitionService.call(token, hierarchy, logs, false);
		sincronizeTemplateService.callAfterCreateAllTemplate(token, hierarchy, logs, false);
		sincronizeTemplateService.callCreateRol(token, hierarchy, propertiesToCreateRoles, logs, false);
		sincronizeProcessStateService.callAfter(token, hierarchy, logs, false);
		sincronizeProcessTransitionService.callAfterCreateAll(token, hierarchy, logs, false);
		sincronizeRelationService.call(token, hierarchy, logs, false);
		rolInProperties = sincronizeRolService.call(token, hierarchy, rolInProperties, logs, false);
		rolInProperties.addAll(templateUpdateProperties);
		hierarchy.setProperties(rolInProperties);
		sincronizeApiService.call(token, hierarchy, logs, false);
		sincronizeOrganizationService.call(token, hierarchy, logs, false);
		sincronizeProcessService.call(token, hierarchy, logs, false);
		sincronizeProcessStateService.call(token, hierarchy, logs, false);
		sincronizeTemplateService.call(token, hierarchy, logs, false);
		sincronizeProcessTransitionService.callAfterCreateAll(token, hierarchy, logs, false);
		sincronizeTemplateService.callAfterCreateAllTemplate(token, hierarchy, logs, false);
		sincronizeProcessStateService.callAfter(token, hierarchy, logs, false);
		sincronizeTemplateService.call(token, hierarchy, logs, false);
		sincronizeRelationService.call(token, hierarchy, logs, false);
		// No sincornizamos propiedades de rol
		// sincronizeMessageService.call(token, hierarchy);
		// sincronizeRolService.callAfterRol(token, hierarchy);
		return logs;
	}

	private LogConfigurationDTO compareFile(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		// aparto las propiedades TIPO_ROL porque al sincronizar las propiedades no se
		// actuzlaiban los campos y salia un error de esta propiedad ya fue definida
		List<PropiedadDTO> propertiesToCreateRoles = hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor().compareTo("PROP_141") == 0))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor().compareTo("PROP_141") != 0))
				.collect(Collectors.toList()));

		List<PropiedadDTO> rolInProperties = hierarchy.getProperties().stream()
				.filter(property -> (property.getRol() != null || property.getRolExcluyente() != null))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getRol() == null && property.getRolExcluyente() == null))
				.collect(Collectors.toList()));

		List<PropiedadDTO> templateUpdateProperties = hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor().compareTo("PROP_78") == 0))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor().compareTo("PROP_78") != 0))
				.collect(Collectors.toList()));

		LogConfigurationDTO logs = new LogConfigurationDTO();
		sincronizeTypeService.call(token, hierarchy, logs, true);
		sincronizeMessageService.call(token, hierarchy, logs, true);
		sincronizeApiService.call(token, hierarchy, logs, true);
		sincronizeOrganizationService.call(token, hierarchy, logs, true);
		sincronizeProcessService.call(token, hierarchy, logs, true);
		sincronizeProcessStateService.call(token, hierarchy, logs, true);
		sincronizeTemplateService.call(token, hierarchy, logs, true);
		sincronizeProcessTransitionService.call(token, hierarchy, logs, true);
		sincronizeTemplateService.callAfterCreateAllTemplate(token, hierarchy, logs, true);
		sincronizeTemplateService.callCreateRol(token, hierarchy, propertiesToCreateRoles, logs, true);
		sincronizeProcessStateService.callAfter(token, hierarchy, logs, true);
		sincronizeProcessTransitionService.callAfterCreateAll(token, hierarchy, logs, true);
		sincronizeRelationService.call(token, hierarchy, logs, true);
		rolInProperties = sincronizeRolService.call(token, hierarchy, rolInProperties, logs, true);
		rolInProperties.addAll(templateUpdateProperties);
		hierarchy.setProperties(rolInProperties);
		sincronizeApiService.call(token, hierarchy, logs, true);
		sincronizeOrganizationService.call(token, hierarchy, logs, true);
		sincronizeProcessService.call(token, hierarchy, logs, true);
		sincronizeProcessStateService.call(token, hierarchy, logs, true);
		sincronizeTemplateService.call(token, hierarchy, logs, true);
		sincronizeProcessTransitionService.callAfterCreateAll(token, hierarchy, logs, true);
		sincronizeTemplateService.callAfterCreateAllTemplate(token, hierarchy, logs, true);
		sincronizeProcessStateService.callAfter(token, hierarchy, logs, true);
		sincronizeTemplateService.call(token, hierarchy, logs, true);
		sincronizeRelationService.call(token, hierarchy, logs, true);
		return logs;
	}

	private FileVO uploadFile(String token, String logs) throws ServerException {
		FileVO result = new FileVO();
		result.setUrl(uploadService.uploadFile(logs.getBytes(), "Entrada.txt", token, "webservice"));
		return result;
	}

}
