package com.softure.mail.application;

import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.domain.MensajeFilterDTO;
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
