package d3.mail.application;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.mail.domain.MensajeDTO;
import d3.mail.domain.MensajeFilterDTO;
import org.springframework.context.annotation.Lazy;

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
		String usuario = messageService.getUserFlex(dto.getSecurityToken());
		return sendMessageService.call(bd, usuario, dto.getSecurityToken());
	}
}
