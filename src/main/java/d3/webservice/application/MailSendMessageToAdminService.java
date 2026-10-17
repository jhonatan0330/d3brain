package d3.webservice.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import d3.authentication.application.OrganizacionSvc;
import d3.authentication.application.UsuarioSesionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.document.application.field.Propiedades;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;

@Service
public class MailSendMessageToAdminService {

	private final OrganizacionSvc organizacionService;
	private final UsuarioSesionSvc autenticacionService;

	public MailSendMessageToAdminService( @Lazy OrganizacionSvc organizacionService,
			@Lazy UsuarioSesionSvc autenticacionService) {
		this.organizacionService = organizacionService;
		this.autenticacionService = autenticacionService;
	}

	public void call(String messageTitle, String messageText) throws ServerException {
		String userAdmin = autenticacionService.getUserSystemMail();
		if (userAdmin == null)
			return;
		call(messageTitle, messageText, userAdmin);
	}

	public void call(String messageTitle, String messageText, String adminMail) throws ServerException {
		OrganizacionDTO principal = organizacionService.obtenerPrincipalPropiedades(SessionContext.getCurrentUserOrNull());
		String _server = Propiedades.obtenerValor(principal, Propiedades.MAIL_SERVER);
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(MailUtils.getFromMail(_server));
			message.setTo(adminMail);
			message.setSubject(messageTitle);
			message.setText(principal.getNombre() + " " + messageText);
			JavaMailSenderImpl mailSender = MailUtils.getMailSender(_server);
			mailSender.send(message);
		} catch (MailException | JsonProcessingException e) {
			String _serverBackup = Propiedades.obtenerValor(principal, Propiedades.MAIL_SERVER_BACKUP);
			if (_serverBackup!=null && !_serverBackup.isEmpty()) {
				try {
					SimpleMailMessage messageBackup = new SimpleMailMessage();
					messageBackup.setFrom( MailUtils.getFromMail(_serverBackup));
					messageBackup.setTo(adminMail);
					messageBackup.setSubject(messageTitle);
					messageBackup.setText(principal.getNombre() + " " + messageText);
					JavaMailSenderImpl mailSenderBackup = MailUtils.getMailSender(_serverBackup);
					mailSenderBackup.send(messageBackup);
				} catch (MailException | JsonProcessingException eBackup) {
					System.out.println(eBackup.getMessage());
				}
			}
		}
	}
	
	
}
