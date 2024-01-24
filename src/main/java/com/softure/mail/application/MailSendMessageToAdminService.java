package com.softure.mail.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.java.services.MailUtils;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.logisticpymes.domain.ServidorFilterDTO;
import com.softure.logisticpymes.domain.UsuarioDTO;

@Service
public class MailSendMessageToAdminService {

	@Autowired private ServidorSvc servidorService;
	@Autowired private OrganizacionSvc organizacionService;
	
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	public void call(String messageTitle, String messageText) throws ServerException {
		UsuarioDTO userAdmin = autenticacionService.getUserSystem();
		if(userAdmin==null || userAdmin.getCorreo()==null ) return;
		ServidorFilterDTO filter = new ServidorFilterDTO();
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		filter.setTipo(ServidorDTO.MAIL);
		List<ServidorDTO> servidores = servidorService.listarConsulta(filter);
		if(servidores == null || servidores.isEmpty()) throw new ServerException("No se encuentra el servidor de correo configurado para enviar mensaje al administrador.\n " + messageTitle + "\n" +messageText);
		OrganizacionDTO principal = organizacionService.obtenerPrincipal();
		SimpleMailMessage message = new SimpleMailMessage();  
        message.setFrom(servidores.get(0).getUsuario());
	    message.setTo(userAdmin.getCorreo());
	    message.setSubject(messageTitle);  
	    message.setText(principal.getNombre() + " " + messageText);
	    try {
	    	JavaMailSenderImpl mailSender = MailUtils.getMailSender(servidores.get(0));
	    	mailSender.send(message);
		} catch (MailException e) {
			if(servidores.size()>1) {
				SimpleMailMessage messageBackup = new SimpleMailMessage();  
		        messageBackup.setFrom(servidores.get(1).getUsuario());
			    messageBackup.setTo(userAdmin.getCorreo());
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
