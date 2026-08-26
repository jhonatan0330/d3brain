package d3.webservice;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedIdResponse;
import d3.webservice.application.WebServiceCopyAPI;
import org.springframework.context.annotation.Lazy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/webservice")
public class WebServiceController {

	private final WebServiceCopyAPI copyService;

	public WebServiceController(@Lazy WebServiceCopyAPI copyService) {
		this.copyService = copyService;
	}

	@PostMapping(value = "/copy")
	public SharedIdResponse copy(@RequestHeader("Authorization") String token, @RequestParam String apiId)
			throws ServerException {
		return copyService.call(apiId, token);
	}

}
