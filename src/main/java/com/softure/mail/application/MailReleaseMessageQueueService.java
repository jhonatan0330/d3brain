package com.softure.mail.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.authentication.application.UsuarioSesionSvc;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.infrastructure.MensajeMapper;
import org.springframework.context.annotation.Lazy;

@Service
public class MailReleaseMessageQueueService {

	private final MensajeMapper mensajeMapper;
	private final MailSendMessageService sendMessage;
	private final UsuarioSesionSvc autenticacionService;

	public MailReleaseMessageQueueService(@Lazy MensajeMapper mensajeMapper, @Lazy MailSendMessageService sendMessage,
			@Lazy UsuarioSesionSvc autenticacionService) {
		this.mensajeMapper = mensajeMapper;
		this.sendMessage = sendMessage;
		this.autenticacionService = autenticacionService;
	}

	public String call() throws ServerException {
		List<MensajeDTO> messageToSend = mensajeMapper.mensajesDisponibles();
		if (messageToSend == null || messageToSend.size() <= 0)
			return "0";
		UsuarioSesionDTO sessionAdmin = autenticacionService.generateAdministratorToken();
		for (MensajeDTO iMessage : messageToSend) {
			if (iMessage.getCorreo() != null) {
				iMessage = sendMessage.call(iMessage, sessionAdmin.getUsuario(), sessionAdmin.getLlaveTabla());
			}
		}
		return String.valueOf(messageToSend.size());

	}
}
