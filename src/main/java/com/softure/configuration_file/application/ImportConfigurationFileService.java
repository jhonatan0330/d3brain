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
import com.softure.configuration_file.domain.FileVO;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.CambioSvc;
import com.softure.logisticpymes.domain.CambioDTO;
import com.softure.property.domain.PropiedadDTO;

@Service
public class ImportConfigurationFileService {
	
	@Autowired CambioSvc changeService;
	@Autowired SynchronizeTypePropertiesService sincronizeTypeService;
	@Autowired SynchronizeMessageService sincronizeMessageService;
	@Autowired SynchronizeApiService sincronizeApiService;
	@Autowired SynchronizeOrganizationService sincronizeOrganizationService;
	@Autowired SynchronizeProcessService sincronizeProcessService;
	@Autowired SynchronizeProcessStateService sincronizeProcessStateService;
	@Autowired SynchronizeProcessTransitionService sincronizeProcessTransitionService;
	@Autowired SynchronizeTemplateService sincronizeTemplateService;
	@Autowired SynchronizeRolService sincronizeRolService;
	
	public FileVO call(String token, FileVO file) throws ServerException{
		
		ObjectMapper mapper = new ObjectMapper();

		//JSON from file to Object
		try {
			HierarchyExporterDTO hierarchy = mapper.readValue( new URL(file.getUrl()), HierarchyExporterDTO.class);
			sincronize(token, hierarchy);
			System.out.println(hierarchy.getOrganization().getNombre());
		} catch (StreamReadException e) {
			throw new ServerException(e.getMessage());
		} catch (DatabindException e) {
			throw new ServerException(e.getMessage());
		} catch (MalformedURLException e) {
			throw new ServerException(e.getMessage());
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
		return file;
	}

	private void sincronize(String token, HierarchyExporterDTO hierarchy) throws ServerException{
		CambioDTO changeRequest = new CambioDTO();
		changeRequest.setMotivo("Importacion");
		changeService.guardar(changeRequest, token);
		List<PropiedadDTO> propertiesToCreateRoles = hierarchy.getProperties().stream()
			      .filter(property -> (property.getPropiedadValor()=="PROP_141"))
			      .collect(Collectors.toList());
		
		hierarchy.setProperties(hierarchy.getProperties().stream()
			      .filter(property -> (property.getPropiedadValor()!="PROP_141"))
			      .collect(Collectors.toList()));
		
		List<PropiedadDTO> rolInProperties = hierarchy.getProperties().stream()
			      .filter(property -> (property.getRol()!=null || property.getRolExcluyente()!=null))
			      .collect(Collectors.toList());
		
		hierarchy.setProperties(hierarchy.getProperties().stream()
			      .filter(property -> (property.getRol()==null && property.getRolExcluyente()==null))
			      .collect(Collectors.toList()));
		
		sincronizeTypeService.call(token, hierarchy);
		sincronizeMessageService.call(token, hierarchy);
		sincronizeApiService.call(token, hierarchy);
		sincronizeOrganizationService.call(token, hierarchy);
		sincronizeProcessService.call(token, hierarchy);
		sincronizeTemplateService.call(token, hierarchy);
		sincronizeProcessStateService.call(token, hierarchy);
		sincronizeProcessTransitionService.call(token, hierarchy);
		rolInProperties = sincronizeRolService.call(token, hierarchy, rolInProperties);
		hierarchy.setProperties(rolInProperties);
		sincronizeApiService.call(token, hierarchy);
		sincronizeOrganizationService.call(token, hierarchy);
		sincronizeProcessService.call(token, hierarchy);
		sincronizeTemplateService.call(token, hierarchy);
		sincronizeProcessStateService.call(token, hierarchy);
		sincronizeProcessTransitionService.call(token, hierarchy);;
		//No sincornizamos propiedades de rol
		//sincronizeMessageService.call(token, hierarchy);
		//sincronizeRolService.callAfterRol(token, hierarchy);
	}

}
