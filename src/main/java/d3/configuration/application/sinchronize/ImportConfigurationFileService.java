package d3.configuration.application.sinchronize;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import d3.configuration.domain.LogConfigurationDTO;
import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.shared.domain.ServerException;
import d3.upload.application.UploadSvc;
import d3.upload.domain.CargaArchivoDTO;

@Service
public class ImportConfigurationFileService {

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

	public ImportConfigurationFileService(@Lazy SynchronizeApiService sincronizeApiService,
			@Lazy SynchronizeOrganizationService sincronizeOrganizationService,
			@Lazy SynchronizeProcessService sincronizeProcessService,
			@Lazy SynchronizeProcessStateService sincronizeProcessStateService,
			@Lazy SynchronizeProcessTransitionService sincronizeProcessTransitionService,
			@Lazy SynchronizeTemplateService sincronizeTemplateService,
			@Lazy SynchronizeRelationService sincronizeRelationService,
			@Lazy SynchronizeRolService sincronizeRolService, @Lazy UploadSvc uploadService, ObjectMapper mapper) {
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

			List<NodoArbolDTO> arboles = leerArboles(inputStream);
			return uploadFile(sincronize(arboles, false).getLogs());

		} catch (IOException | URISyntaxException e) {
			throw new ServerException(e.getMessage());
		}
	}

	public CargaArchivoDTO compare(CargaArchivoDTO file) throws ServerException {

		try (InputStream inputStream = new URI(file.getUrl()).toURL().openStream()) {

			List<NodoArbolDTO> arboles = leerArboles(inputStream);
			return uploadFile(sincronize(arboles, true).getLogs());

		} catch (IOException | URISyntaxException e) {
			throw new ServerException(e.getMessage());
		}
	}

	private List<NodoArbolDTO> leerArboles(InputStream inputStream) throws ServerException, IOException {
		JsonNode contenido = mapper.readTree(inputStream);
		if (contenido == null || contenido.isNull() || contenido.isMissingNode())
			throw new ServerException("El archivo de configuracion esta vacio");
		if (contenido.isObject()) {
			if (contenido.has("hierarchy") || contenido.has("arbol"))
				throw new ServerException(
						"El archivo usa el formato anterior de configuración; expórtelo nuevamente con la versión actual");
			throw new ServerException("El archivo de configuración debe ser un arreglo de árboles");
		}
		if (!contenido.isArray())
			throw new ServerException("El archivo de configuración debe ser un arreglo de árboles");
		List<NodoArbolDTO> arboles = mapper.convertValue(contenido, new TypeReference<List<NodoArbolDTO>>() {
		});
		if (arboles == null || arboles.isEmpty())
			throw new ServerException("El archivo de configuracion esta vacio");
		return arboles;
	}

	public LogConfigurationDTO sincronize(List<NodoArbolDTO> arboles, boolean onlyRead) throws ServerException {
		if (arboles == null || arboles.isEmpty())
			throw new ServerException("La configuracion a sincronizar se encuentra vacia");

		ContextoSincronizacion contexto = ContextoSincronizacion.crear(arboles);

		// aparto las propiedades TIPO_ROL porque al sincronizar las propiedades no se
		// actualizaban los campos y salia un error de esta propiedad ya fue definida
		List<PropiedadDTO> propertiesToCreateRoles = contexto.getPropiedades().stream()
				.filter(property -> property.getPropiedadValor() != null
						&& property.getPropiedadValor().compareTo("PROP_141") == 0)
				.collect(Collectors.toList());

		List<PropiedadDTO> propertiesWithoutRoles = contexto.getPropiedades().stream()
				.filter(property -> property.getPropiedadValor() == null
						|| property.getPropiedadValor().compareTo("PROP_141") != 0)
				.collect(Collectors.toList());

		List<PropiedadDTO> rolInProperties = propertiesWithoutRoles.stream()
				.filter(property -> (property.getRol() != null || property.getRolExcluyente() != null))
				.collect(Collectors.toList());

		List<PropiedadDTO> propertiesWithoutRol = propertiesWithoutRoles.stream()
				.filter(property -> (property.getRol() == null && property.getRolExcluyente() == null))
				.collect(Collectors.toList());

		List<PropiedadDTO> templateUpdateProperties = propertiesWithoutRol.stream()
				.filter(property -> property.getPropiedadValor() != null
						&& property.getPropiedadValor().compareTo("PROP_78") == 0)
				.collect(Collectors.toList());

		contexto.setPropiedades(propertiesWithoutRol.stream()
				.filter(property -> property.getPropiedadValor() == null
						|| property.getPropiedadValor().compareTo("PROP_78") != 0)
				.collect(Collectors.toList()));

		LogConfigurationDTO logs = new LogConfigurationDTO();
		sincronizeOrganizationService.call(contexto, logs, onlyRead);
		sincronizeProcessService.call(contexto, logs, onlyRead);
		sincronizeApiService.call(contexto, logs, onlyRead);
		sincronizeProcessStateService.call(contexto, logs, onlyRead);
		sincronizeTemplateService.call(contexto, logs, onlyRead);
		sincronizeProcessTransitionService.call(contexto, logs, onlyRead);
		sincronizeTemplateService.callAfterCreateAllTemplate(contexto, logs, onlyRead);
		sincronizeTemplateService.callCreateRol(contexto, propertiesToCreateRoles, logs, onlyRead);
		sincronizeProcessStateService.callAfter(contexto, logs, onlyRead);
		sincronizeProcessTransitionService.callAfterCreateAll(contexto, logs, onlyRead);
		logs.setRoot("");
		sincronizeRelationService.call(contexto, logs, onlyRead);
		rolInProperties = sincronizeRolService.call(rolInProperties, logs);
		rolInProperties.addAll(templateUpdateProperties);
		contexto.setPropiedades(rolInProperties);
		sincronizeOrganizationService.call(contexto, logs, onlyRead);
		sincronizeProcessService.call(contexto, logs, onlyRead);
		sincronizeApiService.call(contexto, logs, onlyRead);
		sincronizeProcessStateService.call(contexto, logs, onlyRead);
		sincronizeTemplateService.call(contexto, logs, onlyRead);
		sincronizeProcessTransitionService.callAfterCreateAll(contexto, logs, onlyRead);
		sincronizeTemplateService.callAfterCreateAllTemplate(contexto, logs, onlyRead);
		sincronizeProcessStateService.callAfter(contexto, logs, onlyRead);
		sincronizeTemplateService.call(contexto, logs, onlyRead);
		logs.setRoot("");
		sincronizeRelationService.call(contexto, logs, onlyRead);
		return logs;
	}

	private CargaArchivoDTO uploadFile(String logs) throws ServerException {
		return uploadService.uploadFileDTO(logs.getBytes(), "Entrada.txt", "import", "private");
	}

}
