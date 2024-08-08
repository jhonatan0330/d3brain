package com.softure.configuration_file.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.configuration_file.domain.ExportListRequest;
import com.softure.configuration_file.domain.FileVO;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.mail.application.MensajePlantillaCorreoSvc;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
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

	@Autowired @Lazy 
	private PropiedadValorDefinidoSvc typePropertiesService;
	@Autowired @Lazy 
	private OrganizacionSvc organizationService;
	@Autowired @Lazy 
	private UploadSvc uploadService;
	@Autowired @Lazy 
	private PropiedadSvc propertyService;
	@Autowired @Lazy 
	private RelacionInternaSvc relationService;
	@Autowired @Lazy 
	private RolAccesoSvc rolService;
	@Autowired @Lazy 
	private ProcesoSvc procesoService;
	@Autowired @Lazy 
	private ProcesoEstadoSvc stateService;
	@Autowired @Lazy 
	private ProcesoTransicionSvc transitionService;
	@Autowired @Lazy 
	private DocumentoPlantillaSvc templateService;
	@Autowired @Lazy 
	private ReporteBaseSvc reportService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired @Lazy 
	private MensajePlantillaCorreoSvc messageService;
	@Autowired @Lazy 
	private WebServiceSvc apiService;

	public FileVO call(String token) throws ServerException {
		HierarchyExporterDTO hierarchy = new HierarchyExporterDTO();
		hierarchy.setPropertyTypes(typePropertiesService.getFullToSynchronize());
		hierarchy.setMessages(messageService.getFullToSynchronize(null));
		hierarchy.setApis(apiService.getFullToSynchronize(null));
		hierarchy.setOrganization(organizationService.obtenerPrincipal());
		hierarchy.setProperties(propertyService.getFullPropertiesToConfiguration());
		hierarchy.setRelations(relationService.getRelationsFullToSynchronize());
		hierarchy.setProcess(procesoService.getFullToSynchronize(null));
		hierarchy.setStates(stateService.getFullToSynchronize(null));
		hierarchy.setTransitions(transitionService.getFullToSynchronize(null));
		hierarchy.setTemplates(templateService.getFullToSynchronize(null));
		hierarchy.setRoles(rolService.getFullToSynchronize(null));
		hierarchy.setReports(reportService.getFullToSynchronize(null));
		hierarchy.setFields(fieldService.getFullToSynchronize(null));

		return uploadFile(token, hierarchy);
	}

	public FileVO call(String token, ExportListRequest modules) throws ServerException {

		if (modules == null || modules.getModulesCode() == null || modules.getModulesCode().isEmpty())
			throw new ServerException("No hay modulos");
		List<String> processToInclude = new ArrayList<>();

		for (String iModule : modules.getModulesCode()) {
			ProcesoFilterDTO filterProcess = new ProcesoFilterDTO();
			filterProcess.setEstado(SharedConstants.STATE_ACTIVE);
			filterProcess.setCodigo(iModule);
			List<ProcesoDTO> processModule = procesoService.listarConsulta(filterProcess);
			if (processModule != null && !processModule.isEmpty())
				processToInclude.addAll(getProcessFromMacro(processModule.get(0).getLlaveTabla()));
		}
		if (processToInclude.isEmpty())
			throw new ServerException("No se identifica un modulo con el codigos elegidos ");
		HierarchyExporterDTO hierarchy = new HierarchyExporterDTO();
		hierarchy.setMessages(messageService.getFullToSynchronize(processToInclude));
		hierarchy.setApis(apiService.getFullToSynchronize(processToInclude));
		hierarchy.setProperties(propertyService.getFullPropertiesToConfiguration());
		hierarchy.setRelations(relationService.getRelationsFullToSynchronize());
		hierarchy.setProcess(procesoService.getFullToSynchronize(processToInclude));
		hierarchy.setStates(stateService.getFullToSynchronize(processToInclude));
		hierarchy.setTransitions(transitionService.getFullToSynchronize(processToInclude));
		hierarchy.setTemplates(templateService.getFullToSynchronize(processToInclude));
		hierarchy.setRoles(rolService.getFullToSynchronize(processToInclude));
		hierarchy.setReports(reportService.getFullToSynchronize(processToInclude));
		hierarchy.setFields(fieldService.getFullToSynchronize(processToInclude));

		return uploadFile(token, hierarchy);
	}

	private FileVO uploadFile(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		FileVO result = new FileVO();
		result.setUrl(uploadService.uploadFile(convert(hierarchy), "Entrada.txt", token, "webservice"));
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

	private List<String> getProcessFromMacro(String parentProcess) throws ServerException {
		List<String> processToInclude = new ArrayList<>();
		ProcesoFilterDTO filterProcess = new ProcesoFilterDTO();
		filterProcess.setEstado(SharedConstants.STATE_ACTIVE);
		filterProcess.setMacroproceso(parentProcess);
		List<ProcesoDTO> processModule = procesoService.listarConsulta(filterProcess);
		if (processModule != null && !processModule.isEmpty()) {
			for (ProcesoDTO procesoDTO : processModule) {
				processToInclude.addAll(getProcessFromMacro(procesoDTO.getLlaveTabla()));
				processToInclude.add(parentProcess);
			}
		}
		processToInclude.add(parentProcess);
		return processToInclude;
	}

}
