package com.softure.mail.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.domain.MensajeFilterDTO;

@Service
public class MailUserSendMessage {

	@Autowired
	private MensajeSvc messageService;
	@Autowired
	private MailSendMessageService sendMessageService;

	public MensajeDTO call(MensajeFilterDTO dto) throws ServerException {
		MensajeDTO bd = messageService.consultaXId(dto.getLlaveTabla());
		if (bd.getCorreoEnviado() != null)
			throw new ServerException("Este mensaje ya fue enviado");
		String usuario = messageService.getUserFlex(dto.getSecurityToken());
		return sendMessageService.call(bd, usuario, dto.getSecurityToken());
	}
}
