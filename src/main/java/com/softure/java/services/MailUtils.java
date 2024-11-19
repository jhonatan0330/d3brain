package com.softure.java.services;

import java.util.List;
import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.shared.domain.ServerException;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.property.domain.PropiedadDTO;

public class MailUtils {
	
	public static final String SEPARADOR = ";;";
	public static final int LONGITUD_MAXIMA_DESCRIPCION = 200;

	public static JavaMailSenderImpl getMailSender(ServidorDTO servidor) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(servidor.getUrl());
		mailSender.setPort((servidor.getPuerto()==null)?587:Integer.parseInt(servidor.getPuerto()));
		mailSender.setUsername(servidor.getUsuario());
		mailSender.setPassword(servidor.getClave());
		Properties prop = mailSender.getJavaMailProperties();
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.debug", "false");
		prop.put("mail.smtp.ssl.trust", servidor.getUrl());
		return mailSender;
	}
	
	public static  String replaceParameterInBodyMessage(String template , String parameters){
		if(parameters==null) return "";
		String[] params  = parameters.split(MailUtils.SEPARADOR);
		int posIgual = -1;
		String codigo = null;
		String textoReemplazar = null;
		for (String iParameter : params) {
			posIgual = iParameter.indexOf("=");
			codigo = iParameter.substring(0,posIgual);
			textoReemplazar = iParameter.substring(posIgual+1, iParameter.length());
			template = template.replace("{"+codigo +"}", textoReemplazar);
		}
		template = template.replaceAll("\\{[A-Za-z0-9_]*\\}", "");
		return template;
	}
	
	public static String generateParameters(PropiedadDTO plantillaCorreo, PedidoVentaDTO documento, UsuarioDTO responsable,
			PedidoVentaDTO modificador, List<PedidoVentaCaracteristicaDTO> camposMensaje) throws ServerException {
		String parametros = generarParametros(documento, "D_");
        if (responsable != null)
            parametros = parametros + MailUtils.SEPARADOR + "D_RESPONSABLE=" + responsable.getNombre();
        if (modificador != null)
            parametros = parametros + MailUtils.SEPARADOR + generarParametros(modificador, "M_");
        if (camposMensaje != null && !camposMensaje.isEmpty()) {
            for (PedidoVentaCaracteristicaDTO iCampo : camposMensaje) {
                if (iCampo.getValorText() != null) {
                    parametros = parametros + MailUtils.SEPARADOR + "C_"
                            + SoftureUtil.formatFunction(iCampo.getCampo()).toUpperCase() + "="
                            + SoftureUtil.recortar(iCampo.getValorText(), MailUtils.LONGITUD_MAXIMA_DESCRIPCION);
                }
            }
        }
		return parametros;
	}

    private static String generarParametros(PedidoVentaDTO documento, String prefijo) throws ServerException {
        String parametros = prefijo + "CODE=" + documento.getNombre();
        if (documento.getDescripcion() != null)
            parametros = parametros + MailUtils.SEPARADOR + prefijo + "DESC="
                    + SoftureUtil.recortar(documento.getDescripcion(), MailUtils.LONGITUD_MAXIMA_DESCRIPCION);
        if (documento.getEstadoNombre() != null)
            parametros = parametros + MailUtils.SEPARADOR + prefijo + "ESTADO=" + documento.getEstadoNombre();
        if (documento.getFecha() != null)
            parametros = parametros + MailUtils.SEPARADOR + prefijo + "FECHA="
                    + SoftureUtil.formatDateTime(documento.getFecha());
        if (documento.getDinero() != null) {
            parametros = parametros + MailUtils.SEPARADOR + prefijo + "VALOR="
                    + SoftureUtil.formatNumber(documento.getDinero().getValorTotal());
            parametros = parametros + MailUtils.SEPARADOR + prefijo + "SALDO="
                    + SoftureUtil.formatNumber(documento.getDinero().getSaldo());
        }
        return parametros;
    }
}
