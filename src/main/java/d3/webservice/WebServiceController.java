package d3.webservice;

import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.domain.ServerException;
import d3.webservice.application.MailReleaseMessageQueueService;
import d3.webservice.application.WebServiceEjecucionSvc;

@RestController
@RequestMapping("/web-service")
public class WebServiceController {

	private final WebServiceEjecucionSvc apiService;
	private final MailReleaseMessageQueueService releaseQueueService;

	public WebServiceController(@Lazy WebServiceEjecucionSvc apiService, @Lazy MailReleaseMessageQueueService releaseQueueService) {
		this.apiService = apiService;
		this.releaseQueueService = releaseQueueService;
	}
	
	@GetMapping(value = "/ping_mail")
	public String sendMail() throws ServerException {
		return "******* CORREOS (" + releaseQueueService.call() + ") ***" + new Date().toString();
	}

	@GetMapping(value = "/ping_api")
	public String sendApi() {
		return apiService.apiToTransaction();
	}

}
