package d3.massiveload;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import d3.shared.domain.ServerException;
import d3.massiveload.application.MassiveLoadOrchestratorService;
import d3.massiveload.domain.MasivaItemRequest;
import d3.massiveload.domain.MassiveMasterRequest;

import org.springframework.context.annotation.Lazy;

@RestController
@RequestMapping("massiveload")
public class MassiveRest {

	private final MassiveLoadOrchestratorService orchestratorService;

	public MassiveRest(@Lazy MassiveLoadOrchestratorService orchestratorService) {
		this.orchestratorService = orchestratorService;
	}

	@PostMapping("/upload")
	public MassiveMasterRequest upload(@RequestHeader(name = "Authorization") String token,
			@RequestPart("file") MultipartFile file, @RequestParam("template") String pTemplate)
			throws ServerException {
		return orchestratorService.uploadFile(file, pTemplate, token);
	}

	@PostMapping("/validate/{loadId}")
	public MassiveMasterRequest validate(@RequestHeader(name = "Authorization") String token,
			@PathVariable String loadId) throws ServerException {
		return orchestratorService.validateLoad(loadId, token);
	}

	@PostMapping("/execute/{loadId}")
	public MassiveMasterRequest execute(@RequestHeader(name = "Authorization") String token,
			@PathVariable String loadId) throws ServerException {
		return orchestratorService.executeLoad(loadId, token);
	}

	@GetMapping("/{loadId}")
	public MassiveMasterRequest getLoad(@RequestHeader(name = "Authorization") String token,
			@PathVariable String loadId) throws ServerException {
		return orchestratorService.getLoad(loadId, token);
	}

	@GetMapping("/{loadId}/items")
	public List<MasivaItemRequest> getItems(@RequestHeader(name = "Authorization") String token,
			@PathVariable String loadId) throws ServerException {
		return orchestratorService.getItems(loadId, token);
	}
}
