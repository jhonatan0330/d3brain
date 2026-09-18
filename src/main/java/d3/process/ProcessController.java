package d3.process;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.application.DocumentoPlantillaSvc;
import d3.process.application.ProcesoTransicionAutomaticaSvc;
import d3.process.application.ProcessCopy;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import d3.process.domain.TemplateDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/process")
public class ProcessController {

	private final DocumentoPlantillaSvc documentoplantillaService;
	private final DocumentoPlantillaCaracteristicaSvc campoService;
	private final ProcessCopy copyService;
	private final ProcesoTransicionAutomaticaSvc transicionservice;

	public ProcessController(@Lazy DocumentoPlantillaSvc documentoplantillaService,
			@Lazy DocumentoPlantillaCaracteristicaSvc campoService, @Lazy ProcessCopy copyService,
			@Lazy ProcesoTransicionAutomaticaSvc transicionservice) {
		this.transicionservice = transicionservice;
		this.documentoplantillaService = documentoplantillaService;
		this.campoService = campoService;
		this.copyService = copyService;
	}

	// reemplazalo con getTemplates
	/*@GetMapping(value = "/main/consultaUsuarioDocumentoPlantilla")
	public List<TemplateDTO> consultaUsuarioDocumentoPlantilla() throws ServerException {
		return documentoplantillaService.consultaUsuario();
	}*/
	
	@GetMapping(value = "/getTemplates/{profile}")
	public List<TemplateDTO> consultaUsuarioDocumentoPlantilla(@PathVariable(name = "profile") String pProfile)
			throws ServerException {
		switch (pProfile) {
		case "ADMIN": {
			return documentoplantillaService.consultaAdministrador();
		}
		case "READER": {
			return documentoplantillaService.consultaAuditor();
		}
		default:
			return documentoplantillaService.consultaUsuario();
		}
	}

	@GetMapping(value = "/getFields")
	public TemplateDTO obtenerCampos(@RequestParam String id) throws ServerException {
		TemplateDTO filterTemplate = new TemplateDTO();
		filterTemplate.setLlaveTabla(id);
		return documentoplantillaService.obtenerCampos(filterTemplate, true);
	}
	
	@PostMapping(value = "/obtenerCampos")
	public TemplateDTO obtenerCampos(@RequestBody TemplateDTO documentoFiltro) throws ServerException {
		return documentoplantillaService.obtenerCampos(documentoFiltro, true);
	}

	@PostMapping(value = "/validateLoad")
	public DocumentoPlantillaCaracteristicaDTO validateLoad(
			@RequestBody DocumentoPlantillaCaracteristicaFilterDTO filter) throws ServerException {
		return campoService.listarCarga(filter);
	}

	@PostMapping(value = "/designer/copy")
	public SharedIdResponse copy(@RequestParam String processId) throws ServerException {
		return copyService.call(processId);
	}
	
	@GetMapping(value = "/ping_task")
	public String sendTemporizer() throws ServerException {
		int _launch = transicionservice.lanzarTransaccionesTemporizadas();
		int _prepare = transicionservice.programateAll();
		return "*******TAREAS (" + _launch + ") ***  PROGRAMADAS (" + _prepare + ") ***" + new Date().toString();
	}
	
	

}
