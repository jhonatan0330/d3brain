package com.softure.mail.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.mail.infrastructure.MensajeMapper;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReportDTO;
import com.softure.report.domain.ReporteBaseDTO;

@Service
public class MailSendMessageService {

	@Autowired private MensajeMapper mensajeMapper;
	@Autowired private ReporteBaseSvc reporteBaseService;
	@Autowired private MailSendMessageToAdminService sendToAdminService;
	@Autowired private MensajePlantillaCorreoSvc mailTemplateService;
	@Autowired private ServidorSvc servidorService;

	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public MensajeDTO call(MensajeDTO dto, String usuario, String token) throws ServerException {
		if(dto.getCorreo() == null || dto.getCorreo().isEmpty()) {
			dto.setCorreoError("No se envia correo debido a que no se tiene registrado el mail de correo");
			dto.setCorreoEnviado(new Date());
			mensajeMapper.actualizar(dto);
			return dto;
		}
		try {
			MensajePlantillaCorreoDTO plantilla = mailTemplateService.consultaXId(dto.getTemplate());
			ServidorDTO servidor = null;
			if(plantilla.getServidor()!=null){
				servidor = servidorService.consultaXId(plantilla.getServidor());
			} else {
				servidor = servidorService.obtenerServidorPrincipal(ServidorDTO.MAIL);
			}
			if(servidor == null) throw new ServerException("No se encuentra el servidor de correo configurado");
			if(servidor.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("El servidor de correo no se encuentra activo. " + servidor.getNombre());
			JavaMailSenderImpl mailSender = MailUtils.getMailSender(servidor);
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			boolean conReporte = (dto.getReporte()!=null);
			MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, conReporte);
			mailMsg.setFrom(servidor.getUsuario());
			if(dto.getCorreo().contains(";")) {
				String[] toMails = dto.getCorreo().split(";");
				mailMsg.setTo(toMails[0]);
				List<String> list = new ArrayList<String>(Arrays.asList(toMails));
				list.remove(toMails[0]);
				mailMsg.setCc(list.toArray(new String[0]));
			}else {
				mailMsg.setTo(dto.getCorreo());
			}
			mailMsg.setSubject(dto.getTitulo());
			mailMsg.setText(MailUtils.replaceParameterInBodyMessage(plantilla.getTexto(), dto.getParametros()),true);
			if(conReporte) {
				ReportDTO reporte = reporteBaseService.generarReporte(
						reporteBaseService.validateReport(dto.getReporte(), token), 
						dto.getDocumento(), null, token);
				if(reporte!=null) {
					ReporteBaseDTO base = reporteBaseService.consultaXId(dto.getReporte());
					mailMsg.addAttachment(base.getNombre() + ".pdf", new ByteArrayDataSource(reporte.getContent(), "application/pdf"));
				}
			}
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			dto.setCorreoError(e.getMessage());
			sendToAdminService.call("Error enviando correos electronicos " + dto.getTitulo(), e.getMessage());
		}
		dto.setCorreoEnviado(new Date());
		mensajeMapper.actualizar(dto);
		return dto;
	}
	
	
}
