package d3.webservice.application;

import java.util.List;
import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.shared.application.D3Utils;
import d3.shared.domain.ServerException;

public class MailUtils {

	public static final String SEPARADOR = ";;";
	public static final int LONGITUD_MAXIMA_DESCRIPCION = 200;

	
	public static String getFromMail(String datosJson) throws JsonMappingException, JsonProcessingException {
		 ObjectMapper mapper = new ObjectMapper();
		    JsonNode servidor = mapper.readTree(datosJson);
		    String usuario = servidor.get("from").asText();
		    if(usuario==null) usuario = servidor.get("usuario").asText();
		return usuario;
	}
	public static JavaMailSenderImpl getMailSender(String datosJson) throws JsonMappingException, JsonProcessingException {
		
		 ObjectMapper mapper = new ObjectMapper();
		    JsonNode servidor = mapper.readTree(datosJson);

		    String host = servidor.get("url").asText();

		    int puerto = servidor.hasNonNull("puerto")
		            ? servidor.get("puerto").asInt()
		            : 587;

		    String usuario = servidor.get("usuario").asText();
		    String clave = servidor.get("clave").asText();

		    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		    mailSender.setHost(host);
		    mailSender.setPort(puerto);
		    mailSender.setUsername(usuario);
		    mailSender.setPassword(clave);
		    
		
		Properties prop = mailSender.getJavaMailProperties();
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.debug", "false");
		prop.put("mail.smtp.ssl.trust", host);
		prop.put("mail.smtp.connectiontimeout", "10000");
		prop.put("mail.smtp.timeout", "10000");
		prop.put("mail.smtp.writetimeout", "10000");
		return mailSender;
	}

	public static String replaceParameterInBodyMessage(String template, String parameters) {
		if (parameters == null)
			return "";
		String[] params = parameters.split(MailUtils.SEPARADOR);
		int posIgual = -1;
		String codigo = null;
		String textoReemplazar = null;
		for (String iParameter : params) {
			posIgual = iParameter.indexOf("=");
			codigo = iParameter.substring(0, posIgual);
			textoReemplazar = iParameter.substring(posIgual + 1, iParameter.length());
			template = template.replace("{" + codigo + "}", textoReemplazar);
		}
		template = template.replaceAll("\\{[A-Za-z0-9_]*\\}", "");
		return template;
	}

	public static String generateParameters(PedidoVentaDTO documento, String pResponsable, PedidoVentaDTO modificador,
			List<PedidoVentaCaracteristicaDTO> camposMensaje) throws ServerException {
		String parametros = generarParametros(documento, "D_");
		if (pResponsable != null)
			parametros = parametros + MailUtils.SEPARADOR + "D_RESPONSABLE=" + pResponsable;
		if (modificador != null)
			parametros = parametros + MailUtils.SEPARADOR + generarParametros(modificador, "M_");
		if (camposMensaje != null && !camposMensaje.isEmpty()) {
			for (PedidoVentaCaracteristicaDTO iCampo : camposMensaje) {
				if (iCampo.getValorText() != null) {
					parametros = parametros + MailUtils.SEPARADOR + "C_"
							+ D3Utils.formatFunction(iCampo.getCampo()).toUpperCase() + "="
							+ D3Utils.recortar(iCampo.getValorText(), MailUtils.LONGITUD_MAXIMA_DESCRIPCION);
				}
			}
		}
		return parametros;
	}

	private static String generarParametros(PedidoVentaDTO documento, String prefijo) {
		String parametros = prefijo + "CODE=" + documento.getNombre();
		if (documento.getDescripcion() != null)
			parametros = parametros + MailUtils.SEPARADOR + prefijo + "DESC="
					+ D3Utils.recortar(documento.getDescripcion(), MailUtils.LONGITUD_MAXIMA_DESCRIPCION);
		if (documento.getEstadoNombre() != null)
			parametros = parametros + MailUtils.SEPARADOR + prefijo + "ESTADO=" + documento.getEstadoNombre();
		if (documento.getFecha() != null)
			parametros = parametros + MailUtils.SEPARADOR + prefijo + "FECHA="
					+ D3Utils.formatDateTime(documento.getFecha());
		if (documento.getDinero() != null) {
			parametros = parametros + MailUtils.SEPARADOR + prefijo + "VALOR="
					+ D3Utils.formatNumber(documento.getDinero().getValorTotal());
			parametros = parametros + MailUtils.SEPARADOR + prefijo + "SALDO="
					+ D3Utils.formatNumber(documento.getDinero().getSaldo());
		}
		return parametros;
	}
}
