package com.softure.mail.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.java.services.MailUtils;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.logisticpymes.domain.ServidorFilterDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailRecoverPasswordService {

	@Autowired
	@Lazy
	private ServidorSvc servidorService;
	@Autowired
	@Lazy
	private OrganizacionSvc organizacionService;

	private ServidorDTO getServer() throws ServerException {
		ServidorFilterDTO filter = new ServidorFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setTipo(ServidorDTO.MAIL);
		List<ServidorDTO> servidores = servidorService.listarConsulta(filter);
		if (servidores == null || servidores.isEmpty())
			throw new ServerException("No se encuentra el servidor de correo configurado");
		return servidores.get(0);
	}

	public void callNumber(String correo, String key, String code, String urlServer) throws ServerException {
		ServidorDTO _server = getServer();

		JavaMailSenderImpl mailSender = MailUtils.getMailSender(_server);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);

		try {
			mailMsg.setFrom(servidorService.getFromMail(_server));
			mailMsg.setTo(correo);
			mailMsg.setSubject(urlServer + " Codigo de autorizacion");
			mailMsg.setText(
					"<table style=\"height: 164px;\" width=\"600\" bgcolor=\"#0d47a1\"><tbody><tr style=\"height: 18px;\"><td style=\"height: 18px; width: 590px;\" bgcolor=\"#0d47a1\">&nbsp;</td></tr><tr style=\"text-align: center;\"><td style=\"height: 132px; width: 590px; text-align: center;\" bgcolor=\"#E4E4E4\">"
							+ "<p>El codigo de seguridad es : <strong>" + code
							+ "</strong></p><p>El codigo se vencera en 15 minutos</p></td></tr></tbody></table>",
					true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new ServerException(e.getMessage());
		} catch (MailAuthenticationException mae) {
			throw new ServerException("Mail Authentication error : " + mae.getMessage());
		}
	}

	public void callLink(String correo, String key, String code, String urlServer) throws ServerException {
		ServidorDTO _server = getServer();

		JavaMailSenderImpl mailSender = MailUtils.getMailSender(_server);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);

		try {
			mailMsg.setFrom(servidorService.getFromMail(_server));
			mailMsg.setTo(correo);
			mailMsg.setSubject(urlServer + " Autorizacion de acceso");
			mailMsg.setText(
					"<table style=\"height: 164px;\" width=\"600\" bgcolor=\"#0d47a1\"><tbody><tr style=\"height: 18px;\"><td style=\"height: 18px; width: 590px;\" bgcolor=\"#0d47a1\">&nbsp;</td></tr><tr style=\"text-align: center;\"><td style=\"height: 132px; width: 590px; text-align: center;\" bgcolor=\"#E4E4E4\"><a style=\"border-radius: 4px; display: inline-block; font-weight: bold; padding: 12px 24px; !important; color: #ffffff !important; background-color: #80bf2e;\" href=\""
							+ urlServer + "/sessions/new/" + key + "\" target=\"_blank\">PRESIONA PARA INGRESAR</a></td></tr></tbody></table>",
					true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new ServerException(e.getMessage());
		} catch (MailAuthenticationException mae) {
			throw new ServerException("Mail Authentication error : " + mae.getMessage());
		}
	}
}
