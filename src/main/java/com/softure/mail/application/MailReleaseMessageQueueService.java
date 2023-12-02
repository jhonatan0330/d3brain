package com.softure.mail.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.infrastructure.MensajeMapper;

@Service
public class MailReleaseMessageQueueService {

	@Autowired private MensajeMapper mensajeMapper;
	@Autowired private MailSendMessageService sendMessage;
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	
	public void call()throws ServerException{
	 	List<MensajeDTO> tareasPendientes = mensajeMapper.mensajesDisponibles();
	 	if(tareasPendientes!=null && tareasPendientes.size()>0){
	 		UsuarioSesionDTO sessionAdmin = autenticacionService.generateAdministratorToken();
	 		for (MensajeDTO tareaProgramadaDTO : tareasPendientes) {
	 			if(tareaProgramadaDTO.getCorreo()!=null) {
	 				tareaProgramadaDTO = sendMessage.call(tareaProgramadaDTO, sessionAdmin.getUsuario(), sessionAdmin.getLlaveTabla());
	 			}
			}
	 	}
	}
}
