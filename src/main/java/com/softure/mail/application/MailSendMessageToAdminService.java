package com.softure.mail.application;

import java.util.List;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.application.UsuarioSesionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.java.services.MailUtils;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.logisticpymes.domain.ServidorFilterDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class MailSendMessageToAdminService {

	private final ServidorSvc servidorService;
	private final OrganizacionSvc organizacionService;
	private final UsuarioSesionSvc autenticacionService;

	public MailSendMessageToAdminService(@Lazy ServidorSvc servidorService, @Lazy OrganizacionSvc organizacionService,
			@Lazy UsuarioSesionSvc autenticacionService) {
		this.servidorService = servidorService;
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
		ServidorFilterDTO filter = new ServidorFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setTipo(ServidorDTO.MAIL);
		List<ServidorDTO> servidores = servidorService.listarConsulta(filter);
		if (servidores == null || servidores.isEmpty())
			throw new ServerException(
					"No se encuentra el servidor de correo configurado para enviar mensaje al administrador.\n "
							+ messageTitle + "\n" + messageText);
		OrganizacionDTO principal = organizacionService.obtenerPrincipal();
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(servidorService.getFromMail(servidores.get(0)));
		message.setTo(adminMail);
		message.setSubject(messageTitle);
		message.setText(principal.getNombre() + " " + messageText);
		try {
			JavaMailSenderImpl mailSender = MailUtils.getMailSender(servidores.get(0));
			mailSender.send(message);
		} catch (MailException e) {
			if (servidores.size() > 1) {
				SimpleMailMessage messageBackup = new SimpleMailMessage();
				messageBackup.setFrom(servidores.get(1).getUsuario());
				messageBackup.setTo(adminMail);
				messageBackup.setSubject(messageTitle);
				messageBackup.setText(principal.getNombre() + " " + messageText);
				try {
					JavaMailSenderImpl mailSenderBackup = MailUtils.getMailSender(servidores.get(1));
					mailSenderBackup.send(messageBackup);
				} catch (MailException eBackup) {
					System.out.println(eBackup.getMessage());
				}
			}
		}
	}
}
