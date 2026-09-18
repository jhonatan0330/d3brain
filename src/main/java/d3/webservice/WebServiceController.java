package d3.webservice;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.domain.ServerException;
import d3.webservice.application.WebServiceEjecucionSvc;

@RestController
@RequestMapping("/web-service")
public class WebServiceController {

	private final WebServiceEjecucionSvc apiService;

	public WebServiceController(@Lazy WebServiceEjecucionSvc apiService) {
		this.apiService = apiService;
	}

	@GetMapping(value = "/ping_api")
	public String sendApi() throws ServerException {
		return apiService.apiToTransaction();
	}

}
