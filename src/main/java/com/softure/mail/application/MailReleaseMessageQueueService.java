package com.softure.mail.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.infrastructure.MensajeMapper;

@Service
public class MailReleaseMessageQueueService {

	@Autowired @Lazy 
	private MensajeMapper mensajeMapper;
	@Autowired @Lazy 
	private MailSendMessageService sendMessage;
	@Autowired @Lazy 
	private UsuarioAutenticacionSvc autenticacionService;

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
