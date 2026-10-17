package d3.webservice.application;

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
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import d3.authentication.application.OrganizacionSvc;
import d3.authentication.domain.OrganizacionDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.document.application.field.Propiedades;
import d3.report.application.ReporteBaseSvc;
import d3.report.domain.ReportDTO;
import d3.report.domain.ReporteBaseDTO;
import d3.shared.application.ProcessTemplate;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.webservice.domain.MensajeDTO;
import d3.webservice.domain.WebServiceDTO;
import d3.webservice.infrastructure.MensajeMapper;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class MailSendMessageService {

	private final MensajeMapper mensajeMapper;
	private final ReporteBaseSvc reporteBaseService;
	private final MailSendMessageToAdminService sendToAdminService;
	private final WebServiceSvc mailTemplateService;
	private final ProcessTemplate templatesService;
	private final OrganizacionSvc organizacionService;

	public MailSendMessageService(@Lazy MensajeMapper mensajeMapper, @Lazy ReporteBaseSvc reporteBaseService,
			@Lazy MailSendMessageToAdminService sendToAdminService, @Lazy WebServiceSvc mailTemplateService,
			@Lazy ProcessTemplate templatesService, @Lazy OrganizacionSvc organizacionService) {
		this.mensajeMapper = mensajeMapper;
		this.reporteBaseService = reporteBaseService;
		this.sendToAdminService = sendToAdminService;
		this.mailTemplateService = mailTemplateService;
		this.templatesService = templatesService;
		this.organizacionService = organizacionService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO call(MensajeDTO dto) throws ServerException {
		if (dto.getCorreo() == null || dto.getCorreo().isEmpty()) {
			dto.setCorreoError("No se envia correo debido a que no se tiene registrado el mail de correo");
			dto.setCorreoEnviado(new Date());
			mensajeMapper.actualizar(dto);
			return dto;
		}

		try {
			WebServiceDTO plantilla = mailTemplateService.getByIdFullProperties(dto.getTemplate());
			String servidor = obtenerServidorActivo();

			String mailFrom = MailUtils.getFromMail(servidor);
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
			Map<String, DataSource> attachmentsFiles = construirAdjuntos(dto);

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

	private String obtenerServidorActivo() throws ServerException {
		OrganizacionDTO principal = organizacionService.obtenerPrincipalPropiedades(SessionContext.getCurrentUserOrNull());
		String servidor = Propiedades.obtenerValor(principal, Propiedades.MAIL_SERVER);

		if (servidor == null || servidor.isEmpty())
			throw new ServerException("No se encuentra el servidor de correo configurado");
		return servidor;
	}

	private String construirCuerpoCorreo(MensajeDTO dto, WebServiceDTO plantilla) {
		PropiedadDTO pTitle = Propiedades.obtenerParametro(plantilla, Propiedades.API_TEMPLATE);
		String title = (pTitle==null)?"Sin propiedad API TEMPLATE":pTitle.getValor();
		String mailText = templatesService.generateOutputFile(title, dto.getParametros());
		return MailUtils.replaceParameterInBodyMessage(mailText, dto.getParametros());
	}

	private Map<String, DataSource> construirAdjuntos(MensajeDTO dto) throws Exception {
		Map<String, DataSource> adjuntos = new HashMap<>();

		if (dto.getReporte() != null) {
			ReportDTO reporte = reporteBaseService.generarReporte(reporteBaseService.validateReport(dto.getReporte()),
					dto.getDocumento(), null);
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

	private void manejarEnvioConRespaldo(MensajeDTO dto, String servidorPrincipal, String mailFrom, String mailTo,
			String[] mailCc, String subject, String htmlBody, Map<String, DataSource> adjuntos, MailException error)
			throws ServerException {
		OrganizacionDTO principal = organizacionService.obtenerPrincipalPropiedades(SessionContext.getCurrentUserOrNull());
		String servidorBackup = Propiedades.obtenerValor(principal, Propiedades.MAIL_SERVER_BACKUP);
		if (servidorPrincipal != null && !servidorBackup.isEmpty()) {
			sendToAdminService.call("Error enviando correo principal pasa a backup " + dto.getTitulo(),
					error.getMessage());
			try {
				enviarCorreo(MailUtils.getMailSender(servidorBackup), mailFrom, mailTo, mailCc, subject, htmlBody,
						adjuntos);
			} catch (MailException | MessagingException | JsonProcessingException ex) {
				dto.setCorreoError(ex.getMessage());
				sendToAdminService.call("Error enviando correo backup " + dto.getTitulo(), ex.getMessage());
			}
		} else {
			dto.setCorreoError(error.getMessage());
			sendToAdminService.call("Error enviando correos electronicos " + dto.getTitulo(), error.getMessage());
		}
	}

}
