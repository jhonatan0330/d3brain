package d3.configuration.application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import d3.configuration.application.sinchronize.SynchronizeApiService;
import d3.configuration.application.sinchronize.SynchronizeOrganizationService;
import d3.configuration.application.sinchronize.SynchronizeProcessService;
import d3.configuration.application.sinchronize.SynchronizeProcessStateService;
import d3.configuration.application.sinchronize.SynchronizeProcessTransitionService;
import d3.configuration.application.sinchronize.SynchronizeRelationService;
import d3.configuration.application.sinchronize.SynchronizeRolService;
import d3.configuration.application.sinchronize.SynchronizeTemplateService;
import d3.configuration.application.sinchronize.SynchronizeTypePropertiesService;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.shared.domain.ServerException;
import d3.upload.application.UploadSvc;
import d3.upload.domain.CargaArchivoDTO;

@Service
public class ImportConfigurationFileService {

	private final SynchronizeTypePropertiesService sincronizeTypeService;
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
			@Lazy SynchronizeApiService sincronizeApiService,
			@Lazy SynchronizeOrganizationService sincronizeOrganizationService,
			@Lazy SynchronizeProcessService sincronizeProcessService,
			@Lazy SynchronizeProcessStateService sincronizeProcessStateService,
			@Lazy SynchronizeProcessTransitionService sincronizeProcessTransitionService,
			@Lazy SynchronizeTemplateService sincronizeTemplateService,
			@Lazy SynchronizeRelationService sincronizeRelationService,
			@Lazy SynchronizeRolService sincronizeRolService, @Lazy UploadSvc uploadService, ObjectMapper mapper) {
		this.sincronizeTypeService = sincronizeTypeService;
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

	public CargaArchivoDTO call(CargaArchivoDTO file) throws ServerException {

		try (InputStream inputStream = new URI(file.getUrl()).toURL().openStream()) {

			HierarchyExporterDTO hierarchy = mapper.readValue(inputStream, HierarchyExporterDTO.class);
			return uploadFile(sincronize(hierarchy, false).getLogs());

		} catch (IOException | URISyntaxException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public CargaArchivoDTO compare(CargaArchivoDTO file) throws ServerException {
		
		ObjectMapper mapper = new ObjectMapper();

		try (InputStream inputStream = new URI(file.getUrl()).toURL().openStream()) {

			HierarchyExporterDTO hierarchy = mapper.readValue(inputStream, HierarchyExporterDTO.class);
			
			return uploadFile(sincronize(hierarchy, true).getLogs());

		} catch (IOException | URISyntaxException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public LogConfigurationDTO sincronize(HierarchyExporterDTO hierarchy, boolean onlyRead) throws ServerException {
		if (hierarchy == null)
			throw new ServerException("La configuracion a sincronizar se encuentra vacia");
		if (hierarchy.getProperties() == null)
			hierarchy.setProperties(new java.util.ArrayList<>());
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
		sincronizeTypeService.call(hierarchy, logs, onlyRead);
		sincronizeOrganizationService.call(hierarchy, logs, onlyRead);
		sincronizeProcessService.call(hierarchy, logs, onlyRead);
		sincronizeApiService.call(hierarchy, logs, onlyRead);
		sincronizeProcessStateService.call(hierarchy, logs, onlyRead);
		sincronizeTemplateService.call(hierarchy, logs, onlyRead);
		sincronizeProcessTransitionService.call(hierarchy, logs, onlyRead);
		sincronizeTemplateService.callAfterCreateAllTemplate(hierarchy, logs, onlyRead);
		sincronizeTemplateService.callCreateRol(hierarchy, propertiesToCreateRoles, logs, onlyRead);
		sincronizeProcessStateService.callAfter(hierarchy, logs, onlyRead);
		sincronizeProcessTransitionService.callAfterCreateAll(hierarchy, logs, onlyRead);
		logs.setRoot("");
		sincronizeRelationService.call(hierarchy, logs, onlyRead);
		rolInProperties = sincronizeRolService.call(hierarchy, rolInProperties, logs);
		rolInProperties.addAll(templateUpdateProperties);
		hierarchy.setProperties(rolInProperties);
		sincronizeOrganizationService.call(hierarchy, logs, onlyRead);
		sincronizeProcessService.call(hierarchy, logs, onlyRead);
		sincronizeApiService.call(hierarchy, logs, onlyRead);
		sincronizeProcessStateService.call(hierarchy, logs, onlyRead);
		sincronizeTemplateService.call(hierarchy, logs, onlyRead);
		sincronizeProcessTransitionService.callAfterCreateAll(hierarchy, logs, onlyRead);
		sincronizeTemplateService.callAfterCreateAllTemplate(hierarchy, logs, onlyRead);
		sincronizeProcessStateService.callAfter(hierarchy, logs, onlyRead);
		sincronizeTemplateService.call(hierarchy, logs, onlyRead);
		logs.setRoot("");
		sincronizeRelationService.call(hierarchy, logs, onlyRead);
		return logs;
	}

	private CargaArchivoDTO uploadFile(String logs) throws ServerException {
		return uploadService.uploadFileDTO(logs.getBytes(), "Entrada.txt", "import", "private");
	}

}
