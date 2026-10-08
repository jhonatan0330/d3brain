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

import d3.configuration.domain.ConfigurationExportDTO;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.shared.domain.ServerException;
import d3.upload.application.UploadSvc;
import d3.upload.domain.CargaArchivoDTO;

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

	public CargaArchivoDTO call(CargaArchivoDTO file) throws ServerException {

		try (InputStream inputStream = new URI(file.getUrl()).toURL().openStream()) {

			Object payload = mapper.readValue(inputStream, Object.class);
			HierarchyExporterDTO hierarchy = parseHierarchy(payload);

			return uploadFile(sincronize(hierarchy).getLogs());

		} catch (IOException | URISyntaxException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public CargaArchivoDTO compare(CargaArchivoDTO file) throws ServerException {

		try (InputStream inputStream = new URI(file.getUrl()).toURL().openStream()) {

			Object payload = mapper.readValue(inputStream, Object.class);
			HierarchyExporterDTO hierarchy = parseHierarchy(payload);

			return uploadFile(compareFile(hierarchy).getLogs());

		} catch (IOException | URISyntaxException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public LogConfigurationDTO sincronize(HierarchyExporterDTO hierarchy) throws ServerException {
		if (hierarchy == null)
			throw new ServerException("La configuracion a sincronizar se encuentra vacia");
		if (hierarchy.getProperties() == null)
			hierarchy.setProperties(new java.util.ArrayList<>());
		// aparto las propiedades TIPO_ROL porque al sincronizar las propiedades no se
		// actuzlaiban los campos y salia un error de esta propiedad ya fue definida
		List<PropiedadDTO> propertiesToCreateRoles = hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor() != null
						&& property.getPropiedadValor().compareTo("PROP_141") == 0))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor() == null
						|| property.getPropiedadValor().compareTo("PROP_141") != 0))
				.collect(Collectors.toList()));

		List<PropiedadDTO> rolInProperties = hierarchy.getProperties().stream()
				.filter(property -> (property.getRol() != null || property.getRolExcluyente() != null))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getRol() == null && property.getRolExcluyente() == null))
				.collect(Collectors.toList()));

		List<PropiedadDTO> templateUpdateProperties = hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor() != null
						&& property.getPropiedadValor().compareTo("PROP_78") == 0))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor() == null
						|| property.getPropiedadValor().compareTo("PROP_78") != 0))
				.collect(Collectors.toList()));

		LogConfigurationDTO logs = new LogConfigurationDTO();
		sincronizeTypeService.call(hierarchy, logs, false);
		sincronizeMessageService.call(hierarchy, logs, false);
		sincronizeApiService.call(hierarchy, logs, false);
		sincronizeOrganizationService.call(hierarchy, logs, false);
		sincronizeProcessService.call(hierarchy, logs, false);
		sincronizeProcessStateService.call(hierarchy, logs, false);
		sincronizeTemplateService.call(hierarchy, logs, false);
		sincronizeProcessTransitionService.call(hierarchy, logs, false);
		sincronizeTemplateService.callAfterCreateAllTemplate(hierarchy, logs, false);
		sincronizeTemplateService.callCreateRol(hierarchy, propertiesToCreateRoles, logs, false);
		sincronizeProcessStateService.callAfter(hierarchy, logs, false);
		sincronizeProcessTransitionService.callAfterCreateAll(hierarchy, logs, false);
		logs.setRoot("");
		sincronizeRelationService.call(hierarchy, logs, false);
		rolInProperties = sincronizeRolService.call(hierarchy, rolInProperties, logs);
		rolInProperties.addAll(templateUpdateProperties);
		hierarchy.setProperties(rolInProperties);
		sincronizeApiService.call(hierarchy, logs, false);
		sincronizeOrganizationService.call(hierarchy, logs, false);
		sincronizeProcessService.call(hierarchy, logs, false);
		sincronizeProcessStateService.call(hierarchy, logs, false);
		sincronizeTemplateService.call(hierarchy, logs, false);
		sincronizeProcessTransitionService.callAfterCreateAll(hierarchy, logs, false);
		sincronizeTemplateService.callAfterCreateAllTemplate(hierarchy, logs, false);
		sincronizeProcessStateService.callAfter(hierarchy, logs, false);
		sincronizeTemplateService.call(hierarchy, logs, false);
		logs.setRoot("");
		sincronizeRelationService.call(hierarchy, logs, false);
		return logs;
	}

	public LogConfigurationDTO compareFile(HierarchyExporterDTO hierarchy) throws ServerException {
		if (hierarchy == null)
			throw new ServerException("La configuracion a comparar se encuentra vacia");
		if (hierarchy.getProperties() == null)
			hierarchy.setProperties(new java.util.ArrayList<>());
		List<PropiedadDTO> propertiesToCreateRoles = hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor() != null
						&& property.getPropiedadValor().compareTo("PROP_141") == 0))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor() == null
						|| property.getPropiedadValor().compareTo("PROP_141") != 0))
				.collect(Collectors.toList()));

		List<PropiedadDTO> rolInProperties = hierarchy.getProperties().stream()
				.filter(property -> (property.getRol() != null || property.getRolExcluyente() != null))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getRol() == null && property.getRolExcluyente() == null))
				.collect(Collectors.toList()));

		List<PropiedadDTO> templateUpdateProperties = hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor() != null
						&& property.getPropiedadValor().compareTo("PROP_78") == 0))
				.collect(Collectors.toList());

		hierarchy.setProperties(hierarchy.getProperties().stream()
				.filter(property -> (property.getPropiedadValor() == null
						|| property.getPropiedadValor().compareTo("PROP_78") != 0))
				.collect(Collectors.toList()));

		LogConfigurationDTO logs = new LogConfigurationDTO();
		sincronizeTypeService.call(hierarchy, logs, true);
		sincronizeMessageService.call(hierarchy, logs, true);
		sincronizeApiService.call(hierarchy, logs, true);
		sincronizeOrganizationService.call(hierarchy, logs, true);
		sincronizeProcessService.call(hierarchy, logs, true);
		sincronizeProcessStateService.call(hierarchy, logs, true);
		sincronizeTemplateService.call(hierarchy, logs, true);
		sincronizeProcessTransitionService.call(hierarchy, logs, true);
		sincronizeTemplateService.callAfterCreateAllTemplate(hierarchy, logs, true);
		sincronizeTemplateService.callCreateRol(hierarchy, propertiesToCreateRoles, logs, true);
		sincronizeProcessStateService.callAfter(hierarchy, logs, true);
		sincronizeProcessTransitionService.callAfterCreateAll(hierarchy, logs, true);
		logs.setRoot("");
		sincronizeRelationService.call(hierarchy, logs, true);
		rolInProperties = sincronizeRolService.call(hierarchy, rolInProperties, logs);
		rolInProperties.addAll(templateUpdateProperties);
		hierarchy.setProperties(rolInProperties);
		sincronizeApiService.call(hierarchy, logs, true);
		sincronizeOrganizationService.call(hierarchy, logs, true);
		sincronizeProcessService.call(hierarchy, logs, true);
		sincronizeProcessStateService.call(hierarchy, logs, true);
		sincronizeTemplateService.call(hierarchy, logs, true);
		sincronizeProcessTransitionService.callAfterCreateAll(hierarchy, logs, true);
		sincronizeTemplateService.callAfterCreateAllTemplate(hierarchy, logs, true);
		sincronizeProcessStateService.callAfter(hierarchy, logs, true);
		sincronizeTemplateService.call(hierarchy, logs, true);
		logs.setRoot("");
		sincronizeRelationService.call(hierarchy, logs, true);
		return logs;
	}

	public HierarchyExporterDTO parseHierarchy(Object payload) throws ServerException {
		if (payload == null)
			throw new ServerException("El archivo de configuracion esta vacio");
		if (payload instanceof HierarchyExporterDTO)
			return (HierarchyExporterDTO) payload;
		if (payload instanceof ConfigurationExportDTO) {
			ConfigurationExportDTO wrapper = (ConfigurationExportDTO) payload;
			if (wrapper.getHierarchy() != null)
				return wrapper.getHierarchy();
			throw new ServerException("El archivo exportado no contiene configuracion (hierarchy)");
		}
		if (payload instanceof com.fasterxml.jackson.databind.JsonNode) {
			com.fasterxml.jackson.databind.JsonNode node = (com.fasterxml.jackson.databind.JsonNode) payload;
			if (node.has("hierarchy") && node.get("hierarchy").isObject())
				return mapper.convertValue(node.get("hierarchy"), HierarchyExporterDTO.class);
			if (node.has("arbol") && node.get("arbol").isObject())
				throw new ServerException(
						"El archivo contiene un arbol; use el flujo de arbol (/configuration/tree) para sincronizar");
			return mapper.convertValue(node, HierarchyExporterDTO.class);
		}
		throw new ServerException("Formato de configuracion no reconocido");
	}

	private CargaArchivoDTO uploadFile(String logs) throws ServerException {
		return uploadService.uploadFileDTO(logs.getBytes(), "Entrada.txt", "import", "private");
	}

}
