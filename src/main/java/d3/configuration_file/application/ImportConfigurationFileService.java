package d3.configuration_file.application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import d3.shared.domain.ServerException;
import d3.configuration_file.domain.FileVO;
import d3.configuration_file.domain.HierarchyExporterDTO;
import d3.configuration_file.domain.LogConfigurationDTO;
import d3.property.domain.PropiedadDTO;
import d3.upload.application.UploadSvc;

@Service
public class ImportConfigurationFileService {

	private final SynchronizeTypePropertiesService sincronizeTypeService;
	private final SynchronizeMessageService sincronizeMessageService;
	private final SynchronizeApiService sincronizeApiService;
	private final SynchronizeOrganizationService sincronizeOrganizationService;
	private final SynchronizeProcessService sincronizeProcessService;
	private final SynchronizeProcessStateService sincronizeProcessStateService;
	private final SynchronizeProcessTransitionService sincronizeProcessTransitionService;
	private final SynchronizeTemplateService sincronizeTemplateService;
	private final SynchronizeRelationService sincronizeRelationService;
	private final SynchronizeRolService sincronizeRolService;
	private final UploadSvc uploadService;
	private final ObjectMapper mapper;

	public ImportConfigurationFileService(@Lazy SynchronizeTypePropertiesService sincronizeTypeService,
			@Lazy SynchronizeMessageService sincronizeMessageService, @Lazy SynchronizeApiService sincronizeApiService,
			@Lazy SynchronizeOrganizationService sincronizeOrganizationService,
			@Lazy SynchronizeProcessService sincronizeProcessService,
			@Lazy SynchronizeProcessStateService sincronizeProcessStateService,
			@Lazy SynchronizeProcessTransitionService sincronizeProcessTransitionService,
			@Lazy SynchronizeTemplateService sincronizeTemplateService,
			@Lazy SynchronizeRelationService sincronizeRelationService,
			@Lazy SynchronizeRolService sincronizeRolService, @Lazy UploadSvc uploadService, ObjectMapper mapper) {
		this.sincronizeTypeService = sincronizeTypeService;
		this.sincronizeMessageService = sincronizeMessageService;
		this.sincronizeApiService = sincronizeApiService;
		this.sincronizeOrganizationService = sincronizeOrganizationService;
		this.sincronizeProcessService = sincronizeProcessService;
		this.sincronizeProcessStateService = sincronizeProcessStateService;
		this.sincronizeProcessTransitionService = sincronizeProcessTransitionService;
		this.sincronizeTemplateService = sincronizeTemplateService;
		this.sincronizeRelationService = sincronizeRelationService;
		this.sincronizeRolService = sincronizeRolService;
		this.uploadService = uploadService;
		this.mapper = mapper;
	}

	public FileVO call(String token, FileVO file) throws ServerException {

		try (InputStream inputStream = new URI(file.getUrl()).toURL().openStream()) {

			HierarchyExporterDTO hierarchy = mapper.readValue(inputStream, HierarchyExporterDTO.class);

			return uploadFile(token, sincronize(token, hierarchy).getLogs());

		} catch (IOException | URISyntaxException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public FileVO compare(String token, FileVO file) throws ServerException {

		ObjectMapper mapper = new ObjectMapper();

		try (InputStream inputStream = new URI(file.getUrl()).toURL().openStream()) {

			HierarchyExporterDTO hierarchy = mapper.readValue(inputStream, HierarchyExporterDTO.class);

			return uploadFile(token, compareFile(token, hierarchy).getLogs());

		} catch (IOException | URISyntaxException e) {
			throw new ServerException(e.getMessage());
		}
	}

	private LogConfigurationDTO sincronize(String token, HierarchyExporterDTO hierarchy) throws ServerException {
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
		logs.setRoot("");
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
		logs.setRoot("");
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
		logs.setRoot("");
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
		logs.setRoot("");
		sincronizeRelationService.call(token, hierarchy, logs, true);
		return logs;
	}

	private FileVO uploadFile(String token, String logs) throws ServerException {
		FileVO result = new FileVO();
		result.setUrl(uploadService.uploadFile(logs.getBytes(), "Entrada.txt", token, "import", "private"));
		return result;
	}

}
