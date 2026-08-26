package d3.mail.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.authentication.application.UsuarioSesionSvc;
import d3.authentication.domain.UsuarioSesionDTO;
import d3.mail.domain.MensajeDTO;
import d3.mail.infrastructure.MensajeMapper;
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
