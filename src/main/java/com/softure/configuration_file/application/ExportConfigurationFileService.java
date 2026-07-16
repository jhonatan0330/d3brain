package com.softure.configuration_file.application;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.context.annotation.Lazy;

@Service
public class ExportConfigurationFileService {

	private final PropiedadValorDefinidoSvc typePropertiesService;
	private final OrganizacionSvc organizationService;
	private final UploadSvc uploadService;
	private final PropiedadSvc propertyService;
	private final RelacionInternaSvc relationService;
	private final RolAccesoSvc rolService;
	private final ProcesoSvc procesoService;
	private final ProcesoEstadoSvc stateService;
	private final ProcesoTransicionSvc transitionService;
	private final DocumentoPlantillaSvc templateService;
	private final ReporteBaseSvc reportService;
	private final DocumentoPlantillaCaracteristicaSvc fieldService;
	private final MensajePlantillaCorreoSvc messageService;
	private final WebServiceSvc apiService;

	public ExportConfigurationFileService(@Lazy PropiedadValorDefinidoSvc typePropertiesService,
			@Lazy OrganizacionSvc organizationService, @Lazy UploadSvc uploadService,
			@Lazy PropiedadSvc propertyService, @Lazy RelacionInternaSvc relationService, @Lazy RolAccesoSvc rolService,
			@Lazy ProcesoSvc procesoService, @Lazy ProcesoEstadoSvc stateService,
			@Lazy ProcesoTransicionSvc transitionService, @Lazy DocumentoPlantillaSvc templateService,
			@Lazy ReporteBaseSvc reportService, @Lazy DocumentoPlantillaCaracteristicaSvc fieldService,
			@Lazy MensajePlantillaCorreoSvc messageService, @Lazy WebServiceSvc apiService) {
		this.typePropertiesService = typePropertiesService;
		this.organizationService = organizationService;
		this.uploadService = uploadService;
		this.propertyService = propertyService;
		this.relationService = relationService;
		this.rolService = rolService;
		this.procesoService = procesoService;
		this.stateService = stateService;
		this.transitionService = transitionService;
		this.templateService = templateService;
		this.reportService = reportService;
		this.fieldService = fieldService;
		this.messageService = messageService;
		this.apiService = apiService;
	}

	public FileVO call(String token) throws ServerException {
		rolService.getUserFlex(token);
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

		rolService.getUserFlex(token);

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
		result.setUrl(uploadService.uploadFile(convert(hierarchy), "Entrada.txt", token, "export", "private"));
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
