package com.softure.mail.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
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

	@Autowired
	private MensajeMapper mensajeMapper;
	@Autowired
	private ReporteBaseSvc reporteBaseService;
	@Autowired
	private MailSendMessageToAdminService sendToAdminService;
	@Autowired
	private MensajePlantillaCorreoSvc mailTemplateService;
	@Autowired
	private ServidorSvc servidorService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO call(MensajeDTO dto, String usuario, String token) throws ServerException {
		if (dto.getCorreo() == null || dto.getCorreo().isEmpty()) {
			dto.setCorreoError("No se envia correo debido a que no se tiene registrado el mail de correo");
			dto.setCorreoEnviado(new Date());
			mensajeMapper.actualizar(dto);
			return dto;
		}
		try {
			MensajePlantillaCorreoDTO plantilla = mailTemplateService.consultaXId(dto.getTemplate());
			ServidorDTO servidor = null;
			if (plantilla.getServidor() != null) {
				servidor = servidorService.consultaXId(plantilla.getServidor());
			} else {
				servidor = servidorService.obtenerServidorPrincipal(ServidorDTO.MAIL);
			}
			if (servidor == null)
				throw new ServerException("No se encuentra el servidor de correo configurado");
			if (servidor.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException("El servidor de correo no se encuentra activo. " + servidor.getNombre());
			JavaMailSenderImpl mailSender = MailUtils.getMailSender(servidor);
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage,
					(dto.getReporte() != null || dto.getAdjuntoURL() != null));
			mailMsg.setFrom(servidor.getUsuario());
			if (dto.getCorreo().contains(";")) {
				String[] toMails = dto.getCorreo().split(";");
				mailMsg.setTo(toMails[0]);
				List<String> list = new ArrayList<String>(Arrays.asList(toMails));
				list.remove(toMails[0]);
				mailMsg.setCc(list.toArray(new String[0]));
			} else {
				mailMsg.setTo(dto.getCorreo());
			}
			mailMsg.setSubject(dto.getTitulo());
			mailMsg.setText(MailUtils.replaceParameterInBodyMessage(plantilla.getTexto(), dto.getParametros()), true);
			if (dto.getReporte() != null) {
				ReportDTO reporte = reporteBaseService.generarReporte(
						reporteBaseService.validateReport(dto.getReporte(), token), dto.getDocumento(), null, token);
				if (reporte != null) {
					ReporteBaseDTO base = reporteBaseService.consultaXId(dto.getReporte());
					mailMsg.addAttachment(base.getNombre() + ".pdf",
							new ByteArrayDataSource(reporte.getContent(), "application/pdf"));
				}
			}
			if (dto.getAdjuntoURL() != null) {
				URL attachURL = new URL(dto.getAdjuntoURL());
				String urlName = dto.getAdjuntoURL().substring(dto.getAdjuntoURL().lastIndexOf('/') + 1);
				URLConnection conn = attachURL.openConnection();
				String type = conn.getContentType();
				try {
					InputStream in = conn.getInputStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] b = new byte[1024];
					int count;
					while ((count = in.read(b)) >= 0) {
						out.write(b, 0, count);
					}
					out.flush();
					out.close();
					in.close();
					mailMsg.addAttachment(urlName, new ByteArrayDataSource(out.toByteArray(), type));
				} catch (IOException e) {
					dto.setCorreoError(e.getLocalizedMessage());
					sendToAdminService.call("Error enviando correos electronicos (Adjunto)" + dto.getTitulo(),
							e.getMessage());
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
