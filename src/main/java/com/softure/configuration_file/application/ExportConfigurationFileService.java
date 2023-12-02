package com.softure.configuration_file.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shared.domain.ServerException;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.configuration_file.domain.FileVO;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.mail.application.MensajePlantillaCorreoSvc;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.PropiedadValorDefinidoSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.upload.application.UploadSvc;
import com.softure.webservice.application.WebServiceSvc;

@Service
public class ExportConfigurationFileService {

	@Autowired	private PropiedadValorDefinidoSvc typePropertiesService;
	@Autowired	private OrganizacionSvc organizationService;
	@Autowired	private UploadSvc uploadService;
	@Autowired	private PropiedadSvc propertyService;
	@Autowired	private RelacionInternaSvc relationService;
	@Autowired  private RolAccesoSvc rolService;
	@Autowired	private ProcesoSvc procesoService;
	@Autowired	private ProcesoEstadoSvc stateService;
	@Autowired	private ProcesoTransicionSvc transitionService;
	@Autowired	private DocumentoPlantillaSvc templateService;
	@Autowired	private ReporteBaseSvc reportService;
	@Autowired	private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired	private MensajePlantillaCorreoSvc messageService;
	@Autowired	private WebServiceSvc apiService;

	public FileVO call(String token) throws ServerException {
		HierarchyExporterDTO hierarchy = new HierarchyExporterDTO();
		hierarchy.setPropertyTypes(typePropertiesService.getFullToSynchronize());
		hierarchy.setMessages(messageService.getFullToSynchronize());
		hierarchy.setApis(apiService.getFullToSynchronize());
		hierarchy.setOrganization(organizationService.obtenerPrincipal());
		hierarchy.setProperties(propertyService.getFullPropertiesToConfiguration());
		hierarchy.setRelations(relationService.getRelationsFullToSynchronize());
		hierarchy.setProcess(procesoService.getFullToSynchronize());
		hierarchy.setStates(stateService.getFullToSynchronize());
		hierarchy.setTransitions(transitionService.getFullToSynchronize());
		hierarchy.setTemplates(templateService.getFullToSynchronize());
		hierarchy.setRoles(rolService.getFullToSynchronize());
		hierarchy.setReports(reportService.getFullToSynchronize());
		hierarchy.setFields(fieldService.getFullToSynchronize());
		
		FileVO result = new FileVO();
		result.setUrl( uploadService.uploadFile(convert(hierarchy), "Entrada.txt", token, "webservice"));
		return result;
	}

	private byte[] convert(HierarchyExporterDTO hierarchy) throws ServerException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsBytes(hierarchy);
		} catch (JsonProcessingException e) {
			throw new ServerException(e.getMessage());
		}
		
	}

}
