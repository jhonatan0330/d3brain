package d3.massiveload;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import d3.massiveload.application.MassiveBaseFileService;
import d3.massiveload.application.MassiveLoadOrchestratorService;
import d3.massiveload.application.MassiveParsePreviewService;
import d3.massiveload.domain.MasivaItemRequest;
import d3.massiveload.domain.MassiveMasterRequest;
import d3.massiveload.domain.MassiveParseResponse;
import d3.massiveload.domain.PlantillaBaseRequest;
import d3.massiveload.domain.PlantillaBaseResponse;
import d3.shared.domain.ServerException;

@RestController
@RequestMapping("massive-load")
public class MassiveController {

	private final MassiveLoadOrchestratorService orchestratorService;
	private final MassiveBaseFileService baseFileService;
	private final MassiveParsePreviewService parsePreviewService;

	public MassiveController(@Lazy MassiveLoadOrchestratorService orchestratorService,
			@Lazy MassiveBaseFileService baseFileService, @Lazy MassiveParsePreviewService parsePreviewService) {
		this.orchestratorService = orchestratorService;
		this.baseFileService = baseFileService;
		this.parsePreviewService = parsePreviewService;
	}

	@PostMapping("/upload")
	public MassiveMasterRequest upload(@RequestPart("file") MultipartFile file,
			@RequestParam("template") String pTemplate) throws ServerException {
		return orchestratorService.uploadFile(file, pTemplate);
	}

	@PostMapping("/template")
	public PlantillaBaseResponse generarBase(@RequestBody PlantillaBaseRequest request) throws ServerException {
		return baseFileService.generarBase(request.getTemplateId(), request.getFormat());
	}

	@PostMapping("/parse")
	public MassiveParseResponse parse(@RequestPart("file") MultipartFile file,
			@RequestParam("template") String templateId) throws ServerException {
		return parsePreviewService.parseFile(file, templateId);
	}

	@PostMapping("/validate/{loadId}")
	public MassiveMasterRequest validate(@PathVariable String loadId) throws ServerException {
		return orchestratorService.validateLoad(loadId);
	}

	@PostMapping("/execute/{loadId}")
	public MassiveMasterRequest execute(@PathVariable String loadId) throws ServerException {
		return orchestratorService.executeLoad(loadId);
	}

	@GetMapping("/{loadId}")
	public MassiveMasterRequest getLoad(@PathVariable String loadId) throws ServerException {
		return orchestratorService.getLoad(loadId);
	}

	@GetMapping("/{loadId}/items")
	public List<MasivaItemRequest> getItems(@PathVariable String loadId) throws ServerException {
		return orchestratorService.getItems(loadId);
	}
}
