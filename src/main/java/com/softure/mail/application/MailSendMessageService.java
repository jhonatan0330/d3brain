package com.softure.mail.application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.java.services.MailUtils;
import com.softure.java.services.ProcessTemplate;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.mail.infrastructure.MensajeMapper;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReportDTO;
import com.softure.report.domain.ReporteBaseDTO;

import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class MailSendMessageService {

	@Autowired @Lazy 
	private MensajeMapper mensajeMapper;
	@Autowired @Lazy 
	private ReporteBaseSvc reporteBaseService;
	@Autowired @Lazy 
	private MailSendMessageToAdminService sendToAdminService;
	@Autowired @Lazy 
	private MensajePlantillaCorreoSvc mailTemplateService;
	@Autowired @Lazy 
	private ServidorSvc servidorService;
	@Autowired @Lazy 
	private ProcessTemplate templatesService;

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
			String mailFrom = servidorService.getFromMail(servidor);
			String mailTo = null;
			String[] mailCc = null;
			if (dto.getCorreo().contains(";")) {
				String[] toMails = dto.getCorreo().split(";");
				mailTo = toMails[0];
				List<String> list = new ArrayList<String>(Arrays.asList(toMails));
				list.remove(toMails[0]);
				mailCc = list.toArray(new String[0]);
			} else {
				mailTo = dto.getCorreo();
			}
			String mailSubject = dto.getTitulo();
			String mailText = templatesService.generateOutputFile(plantilla.getTexto(), dto.getParametros());
			mailText = MailUtils.replaceParameterInBodyMessage(mailText, dto.getParametros());
			Map<String, DataSource> attachmentsFiles = null;
			if (dto.getReporte() != null) {
				ReportDTO reporte = reporteBaseService.generarReporte(
						reporteBaseService.validateReport(dto.getReporte(), token), dto.getDocumento(), null, token);
				if (reporte != null) {
					ReporteBaseDTO base = reporteBaseService.consultaXId(dto.getReporte());
					if (attachmentsFiles == null)
						attachmentsFiles = new HashMap<String, DataSource>();
					attachmentsFiles.put(base.getNombre() + ".pdf",
							new ByteArrayDataSource(reporte.getContent(), "application/pdf"));
				}
			}

			if (dto.getAdjuntoURL() != null) {
				String[] attachments = dto.getAdjuntoURL().split(SharedConstants.PUNTO_COMA_DOBLE);
				String urlName = "attach";
				for (String string : attachments) {
					if (!string.isEmpty()) {
						try {
							urlName = string.substring(string.lastIndexOf('/') + 1);
							File file = File.createTempFile("file_", urlName);
							FileUtils.copyURLToFile(new URI(string).toURL(), file);
							if (attachmentsFiles == null)
								attachmentsFiles = new HashMap<String, DataSource>();
							attachmentsFiles.put(urlName, new FileDataSource(file));
						} catch (IOException e) {
							dto.setCorreoError(e.getLocalizedMessage());
							sendToAdminService.call("Error enviando correos electronicos (Adjunto)" + dto.getTitulo(),
									e.getMessage());
						}
					}
				}
			}
			
			if(plantilla.getNombre().contains("ZIP") && attachmentsFiles != null && attachmentsFiles.size() > 0) {
				final ByteArrayOutputStream fos = new ByteArrayOutputStream();
				ZipOutputStream zipOut = new ZipOutputStream(fos);
				for (Map.Entry<String, DataSource> entry : attachmentsFiles.entrySet()) {
					File fileToZip = new File(entry.getValue().getName());
					FileInputStream fis = new FileInputStream(fileToZip);
					ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
					zipOut.putNextEntry(zipEntry);
					byte[] bytes = new byte[1024];
					int length;
					while ((length = fis.read(bytes)) >= 0) {
						zipOut.write(bytes, 0, length);
					}
					fis.close();
				}
				zipOut.close();
				fos.close();
				attachmentsFiles.clear();
				attachmentsFiles.put("files.zip", new ByteArrayDataSource(fos.toByteArray(), "application/zip"));
				
			}
			try {
				JavaMailSenderImpl mailSender = MailUtils.getMailSender(servidor);
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, (attachmentsFiles != null));
				mailMsg.setFrom(mailFrom);
				mailMsg.setSubject(mailSubject);
				mailMsg.setText(mailText, true);
				mailMsg.setTo(mailTo);
				if (mailCc != null)
					mailMsg.setCc(mailCc);
				if (attachmentsFiles != null) {
					for (Map.Entry<String, DataSource> entry : attachmentsFiles.entrySet()) {
						mailMsg.addAttachment(entry.getKey(), entry.getValue());
					}
				}
				mailSender.send(mimeMessage);
			} catch (MailException e) {
				if (servidor != null && servidor.getServidorRespaldo() != null) {
					sendToAdminService.call("Error enviando correo principal pasa a backup " + dto.getTitulo(),
							e.getMessage());
					servidor = servidorService.consultaXId(servidor.getServidorRespaldo());
					if (servidor == null)
						throw new ServerException("No se encuentra el servidor de correo backup configurado");
					if (servidor.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
						throw new ServerException(
								"El servidor de correo backup no se encuentra activo. " + servidor.getNombre());

					JavaMailSenderImpl mailSenderBackup = MailUtils.getMailSender(servidor);
					MimeMessage mimeMessageBackup = mailSenderBackup.createMimeMessage();
					MimeMessageHelper mailMsgBackup = new MimeMessageHelper(mimeMessageBackup,
							(attachmentsFiles != null));
					mailMsgBackup.setFrom(mailFrom);
					mailMsgBackup.setSubject(mailSubject);
					mailMsgBackup.setText(mailText, true);
					mailMsgBackup.setTo(mailTo);
					if (mailCc != null)
						mailMsgBackup.setCc(mailCc);
					if (attachmentsFiles != null) {
						for (Map.Entry<String, DataSource> entry : attachmentsFiles.entrySet()) {
							mailMsgBackup.addAttachment(entry.getKey(), entry.getValue());
						}
					}
					mailSenderBackup.send(mimeMessageBackup);
				} else {
					dto.setCorreoError(e.getMessage());
					sendToAdminService.call("Error enviando correos electronicos " + dto.getTitulo(), e.getMessage());
				}
			}
		} catch (Exception e) {
			dto.setCorreoError(e.getMessage());
			sendToAdminService.call("Error enviando correos electronicos " + dto.getTitulo(), e.getMessage());
		}
		dto.setCorreoEnviado(new Date());
		mensajeMapper.actualizar(dto);
		return dto;
	}

}
