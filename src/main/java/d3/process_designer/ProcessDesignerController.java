package d3.process_designer;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.process_designer.application.ProcessCopy;
import org.springframework.context.annotation.Lazy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/process_designer")
public class ProcessDesignerController {

	private final ProcessCopy copyService;

	public ProcessDesignerController(@Lazy ProcessCopy copyService) {
		this.copyService = copyService;
	}

	@PostMapping(value = "/copy")
	public SharedIdResponse copy(@RequestHeader("Authorization") String token, @RequestParam String processId)
			throws ServerException {
		return copyService.call(processId, token);
	}

}
