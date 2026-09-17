package d3.mail.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.authentication.application.UsuarioSesionSvc;
import d3.mail.domain.MensajeDTO;
import d3.mail.infrastructure.MensajeMapper;
import d3.shared.domain.ServerException;

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
		autenticacionService.generateAdministratorToken();
		for (MensajeDTO iMessage : messageToSend) {
			if (iMessage.getCorreo() != null) {
				iMessage = sendMessage.call(iMessage);
			}
		}
		return String.valueOf(messageToSend.size());

	}
}
