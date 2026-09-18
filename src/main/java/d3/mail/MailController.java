package d3.mail;

import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.mail.application.MailReleaseMessageQueueService;
import d3.shared.domain.ServerException;

@RestController
@RequestMapping("/mail")
public class MailController {

	private final MailReleaseMessageQueueService releaseQueueService;

	public MailController(@Lazy MailReleaseMessageQueueService releaseQueueService) {
		this.releaseQueueService = releaseQueueService;
	}


	@GetMapping(value = "/ping_mail")
	public String sendMail() throws ServerException {
		return "******* CORREOS (" + releaseQueueService.call() + ") ***" + new Date().toString();
	}

	
}
