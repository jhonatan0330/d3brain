package d3.mail.application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.java.services.MailUtils;
import d3.java.services.ProcessTemplate;
import d3.logisticpymes.application.ServidorSvc;
import d3.logisticpymes.domain.ServidorDTO;
import d3.mail.domain.MensajeDTO;
import d3.mail.domain.MensajePlantillaCorreoDTO;
import d3.mail.infrastructure.MensajeMapper;
import d3.report.application.ReporteBaseSvc;
import d3.report.domain.ReportDTO;
import d3.report.domain.ReporteBaseDTO;

import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.context.annotation.Lazy;

@Service
public class MailSendMessageService {

	private final MensajeMapper mensajeMapper;
	private final ReporteBaseSvc reporteBaseService;
	private final MailSendMessageToAdminService sendToAdminService;
	private final MensajePlantillaCorreoSvc mailTemplateService;
	private final ServidorSvc servidorService;
	private final ProcessTemplate templatesService;

	public MailSendMessageService(@Lazy MensajeMapper mensajeMapper, @Lazy ReporteBaseSvc reporteBaseService,
			@Lazy MailSendMessageToAdminService sendToAdminService, @Lazy MensajePlantillaCorreoSvc mailTemplateService,
			@Lazy ServidorSvc servidorService, @Lazy ProcessTemplate templatesService) {
		this.mensajeMapper = mensajeMapper;
		this.reporteBaseService = reporteBaseService;
		this.sendToAdminService = sendToAdminService;
		this.mailTemplateService = mailTemplateService;
		this.servidorService = servidorService;
		this.templatesService = templatesService;
	}

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
			ServidorDTO servidor = obtenerServidorActivo(plantilla);

			String mailFrom = servidorService.getFromMail(servidor);
			String mailTo;
			String[] mailCc = null;

			if (dto.getCorreo().contains(";")) {
				String[] toMails = dto.getCorreo().split(";");
				mailTo = toMails[0];
				mailCc = Arrays.copyOfRange(toMails, 1, toMails.length);
			} else {
				mailTo = dto.getCorreo();
			}

			String mailSubject = dto.getTitulo();
			String mailText = construirCuerpoCorreo(dto, plantilla);
			Map<String, DataSource> attachmentsFiles = construirAdjuntos(dto, token);

			if (plantilla.getNombre().contains("ZIP") && attachmentsFiles != null && !attachmentsFiles.isEmpty()) {
				attachmentsFiles = comprimirAdjuntos(attachmentsFiles, dto.getLlaveTabla());
			}

			try {
				enviarCorreo(MailUtils.getMailSender(servidor), mailFrom, mailTo, mailCc, mailSubject, mailText,
						attachmentsFiles);
			} catch (MailException e) {
				manejarEnvioConRespaldo(dto, servidor, mailFrom, mailTo, mailCc, mailSubject, mailText,
						attachmentsFiles, e);
			}
		} catch (Exception e) {
			dto.setCorreoError(e.getMessage());
			sendToAdminService.call("Error enviando correos electronicos " + dto.getTitulo(), e.getMessage());
		}

		dto.setCorreoEnviado(new Date());
		mensajeMapper.actualizar(dto);
		return dto;
	}

	private ServidorDTO obtenerServidorActivo(MensajePlantillaCorreoDTO plantilla) throws ServerException {
		ServidorDTO servidor = plantilla.getServidor() != null ? servidorService.consultaXId(plantilla.getServidor())
				: servidorService.obtenerServidorPrincipal(ServidorDTO.MAIL);

		if (servidor == null)
			throw new ServerException("No se encuentra el servidor de correo configurado");
		if (servidor.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El servidor de correo no se encuentra activo. " + servidor.getNombre());

		return servidor;
	}

	private String construirCuerpoCorreo(MensajeDTO dto, MensajePlantillaCorreoDTO plantilla) {
		String mailText = templatesService.generateOutputFile(plantilla.getTexto(), dto.getParametros());
		return MailUtils.replaceParameterInBodyMessage(mailText, dto.getParametros());
	}

	private Map<String, DataSource> construirAdjuntos(MensajeDTO dto, String token) throws Exception {
		Map<String, DataSource> adjuntos = new HashMap<>();

		if (dto.getReporte() != null) {
			ReportDTO reporte = reporteBaseService.generarReporte(
					reporteBaseService.validateReport(dto.getReporte(), token), dto.getDocumento(), null, token);
			if (reporte != null) {
				ReporteBaseDTO base = reporteBaseService.consultaXId(dto.getReporte());
				adjuntos.put(base.getNombre() + ".pdf",
						new ByteArrayDataSource(reporte.getContent(), "application/pdf"));
			}
		}

		if (dto.getAdjuntoURL() != null) {
			for (String url : dto.getAdjuntoURL().split(SharedConstants.PUNTO_COMA_DOBLE)) {
				if (!url.isEmpty()) {
					try {
						String fileName = url.substring(url.lastIndexOf('/') + 1);
						File tempFile = File.createTempFile("file_", fileName);
						tempFile.deleteOnExit();
						FileUtils.copyURLToFile(new URI(url).toURL(), tempFile);
						adjuntos.put(fileName, new FileDataSource(tempFile));
					} catch (IOException e) {
						dto.setCorreoError(e.getLocalizedMessage());
						sendToAdminService.call("Error enviando correos electronicos (Adjunto)" + dto.getTitulo(),
								e.getMessage());
					}
				}
			}
		}

		return adjuntos.isEmpty() ? null : adjuntos;
	}

	private Map<String, DataSource> comprimirAdjuntos(Map<String, DataSource> adjuntos, String name)
			throws IOException {
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		for (Map.Entry<String, DataSource> entry : adjuntos.entrySet()) {
			InputStream is = entry.getValue().getInputStream();
			zipOut.putNextEntry(new ZipEntry(entry.getKey()));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				zipOut.write(buffer, 0, length);
			}
			is.close();
		}
		zipOut.close();
		fos.close();

		Map<String, DataSource> zipAttachment = new HashMap<>();
		zipAttachment.put(name + ".zip", new ByteArrayDataSource(fos.toByteArray(), "application/zip"));
		return zipAttachment;
	}

	private void enviarCorreo(JavaMailSenderImpl sender, String from, String to, String[] cc, String subject,
			String htmlBody, Map<String, DataSource> adjuntos) throws MessagingException {
		MimeMessage mimeMessage = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, (adjuntos != null));
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlBody, true);
		if (cc != null)
			helper.setCc(cc);
		if (adjuntos != null) {
			for (Map.Entry<String, DataSource> entry : adjuntos.entrySet()) {
				helper.addAttachment(entry.getKey(), entry.getValue());
			}
		}
		sender.send(mimeMessage);
	}

	private void manejarEnvioConRespaldo(MensajeDTO dto, ServidorDTO servidorPrincipal, String mailFrom, String mailTo,
			String[] mailCc, String subject, String htmlBody, Map<String, DataSource> adjuntos, MailException error)
			throws ServerException {
		if (servidorPrincipal != null && servidorPrincipal.getServidorRespaldo() != null) {
			sendToAdminService.call("Error enviando correo principal pasa a backup " + dto.getTitulo(),
					error.getMessage());
			ServidorDTO servidorBackup = servidorService.consultaXId(servidorPrincipal.getServidorRespaldo());
			if (servidorBackup == null)
				throw new ServerException("No se encuentra el servidor de correo backup configurado");
			if (servidorBackup.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException(
						"El servidor de correo backup no se encuentra activo. " + servidorBackup.getNombre());

			try {
				enviarCorreo(MailUtils.getMailSender(servidorBackup), mailFrom, mailTo, mailCc, subject, htmlBody,
						adjuntos);
			} catch (MailException | MessagingException ex) {
				dto.setCorreoError(ex.getMessage());
				sendToAdminService.call("Error enviando correo backup " + dto.getTitulo(), ex.getMessage());
			}
		} else {
			dto.setCorreoError(error.getMessage());
			sendToAdminService.call("Error enviando correos electronicos " + dto.getTitulo(), error.getMessage());
		}
	}

}
