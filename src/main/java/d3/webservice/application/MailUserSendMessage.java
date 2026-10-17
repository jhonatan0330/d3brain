package d3.webservice.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.webservice.domain.MensajeDTO;
import d3.webservice.domain.MensajeFilterDTO;

@Service
public class MailUserSendMessage {

	private final MensajeSvc messageService;
	private final MailSendMessageService sendMessageService;

	public MailUserSendMessage(@Lazy MensajeSvc messageService, @Lazy MailSendMessageService sendMessageService) {
		this.messageService = messageService;
		this.sendMessageService = sendMessageService;
	}

	public MensajeDTO call(MensajeFilterDTO dto) throws ServerException {
		MensajeDTO bd = messageService.consultaXId(dto.getLlaveTabla());
		if (bd.getCorreoEnviado() != null)
			throw new ServerException("Este mensaje ya fue enviado");
		return sendMessageService.call(bd);
	}
}
