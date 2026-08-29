package d3.mail.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;
import d3.document.application.PedidoVentaCaracteristicaSvc;
import d3.document.application.field.Propiedades;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.shared.application.MailUtils;
import d3.shared.application.ProcessTemplate;
import d3.shared.application.D3Utils;
import d3.users.application.UsuarioSvc;
import d3.users.domain.UsuarioDTO;
import d3.mail.domain.MensajeDTO;
import d3.mail.domain.MensajePlantillaCorreoDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.property.application.PropiedadSvc;
import d3.property.application.PropertyGetWithCacheService;
import d3.property.application.PropertyNavigateIntoRelationsToFindFieldsService;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class MailGenerateMessageService {

	private final MensajeSvc messageService;
	private final PropiedadSvc propiedadService;
	private final PropertyGetWithCacheService cacheService;
	private final UsuarioSvc usuarioService;
	private final PedidoVentaCaracteristicaSvc campoService;
	private final MensajePlantillaCorreoSvc mailTemplateService;
	private final PropertyNavigateIntoRelationsToFindFieldsService findFieldService;
	private final ProcessTemplate templatesService;

	public MailGenerateMessageService(@Lazy MensajeSvc messageService, @Lazy PropiedadSvc propiedadService,
			@Lazy PropertyGetWithCacheService cacheService, @Lazy UsuarioSvc usuarioService,
			@Lazy PedidoVentaCaracteristicaSvc campoService, @Lazy MensajePlantillaCorreoSvc mailTemplateService,
			@Lazy PropertyNavigateIntoRelationsToFindFieldsService findFieldService,
			@Lazy ProcessTemplate templatesService) {
		this.messageService = messageService;
		this.propiedadService = propiedadService;
		this.cacheService = cacheService;
		this.usuarioService = usuarioService;
		this.campoService = campoService;
		this.mailTemplateService = mailTemplateService;
		this.findFieldService = findFieldService;
		this.templatesService = templatesService;
	}

	public void call(PedidoVentaDTO pedido, ProcesoTransicionDTO transicionDTO, UsuarioDTO responsable,
			PedidoVentaDTO modificador, String token) throws ServerException {
		// Se puede mejorar con un solo query
		PropiedadDTO mensaje = null;
		String campo = modificador.getPlantilla();
		String tipo = null;
		String usuarioToken = (token == null) ? null : usuarioService.getUserFlex(token);
		if (modificador.getLlaveTabla() == null || modificador.getLlaveTabla().compareTo(pedido.getLlaveTabla()) == 0) {
			// El tema es que si coloco mensaje a la plantilla, pero es parte de una
			// transicion se duplica el mensaje
			tipo = PropiedadValorDefinidoDTO.PLANTILLA;
			mensaje = cacheService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
			// Esto lo repitopero no se bien si deba hacerlo asi
			if (mensaje == null && transicionDTO != null && transicionDTO.getEstadoPartida() == null) {
				campo = transicionDTO.getLlaveTabla();
				tipo = PropiedadValorDefinidoDTO.TRANSICION;
				mensaje = cacheService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				if (mensaje == null && transicionDTO.getProceso() != null) {
					campo = transicionDTO.getProceso();
					tipo = PropiedadValorDefinidoDTO.PROCESO;
					mensaje = cacheService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				}
			}
		} else {
			if (transicionDTO != null) {
				campo = transicionDTO.getLlaveTabla();
				tipo = PropiedadValorDefinidoDTO.TRANSICION;
				mensaje = cacheService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				if (mensaje == null && transicionDTO.getProceso() != null) {
					campo = transicionDTO.getProceso();
					tipo = PropiedadValorDefinidoDTO.PROCESO;
					mensaje = cacheService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				}
			}
		}
		// Si encontro mensaje
		if (mensaje != null) {
			System.out.format("\n[%s] Mensaje ( %s )", pedido.getNombre(), mensaje.getTexto());
			List<PropiedadDTO> destinatariosFijos = cacheService.obtenerPropiedades(tipo, campo,
					Propiedades.MENSAJE_DESTINATARIO, usuarioToken);
			List<UsuarioDTO> fijos = null;
			List<String> correosFijos = null;
			if (destinatariosFijos != null && !destinatariosFijos.isEmpty()) {
				fijos = new ArrayList<UsuarioDTO>();
				for (PropiedadDTO iPropiedad : destinatariosFijos) {
					if (iPropiedad.getValor().compareTo("*") != 0) {
						UsuarioDTO pUser = usuarioService.consultaXId(iPropiedad.getValor());
						if (pUser != null && pUser.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0
								&& pUser.getCorreo() != null) {
							fijos.add(pUser);
						}
					}
					// Si tiene relaciones la propiedad entonces el busca los correos que se
					// encuentren en esas relaciones
					List<PedidoVentaCaracteristicaDTO> fieldsEmailToSend = findFieldService
							.call(iPropiedad.getLlaveTabla(), modificador.getCaracteristicas());
					correosFijos = new ArrayList<String>();
					if (fieldsEmailToSend != null && !fieldsEmailToSend.isEmpty()) {
						for (PedidoVentaCaracteristicaDTO iFieldsEmailToSend : fieldsEmailToSend) {
							if (iFieldsEmailToSend.getValorText() != null) {
								String[] externalMail = iFieldsEmailToSend.getValorText()
										.split(SharedConstants.PUNTO_COMA);
								for (String iMail : externalMail) {
									if (iMail != null && !iMail.isEmpty())
										correosFijos.add(iMail.toLowerCase());
								}
							}
						}
					}
				}
			}
			colocarMensajes(mensaje, pedido, responsable, modificador, fijos, correosFijos, token);
		}
	}

	private void colocarMensajes(PropiedadDTO plantillaCorreo, PedidoVentaDTO documento, UsuarioDTO responsable,
			PedidoVentaDTO modificador, List<UsuarioDTO> destinosFijos, List<String> destinatariosExternos,
			String token) throws ServerException {
		Map<String, String> destinatarios = new HashMap<String, String>();// PAra para evitar duplicados de usuarios
		List<MensajeDTO> destinatariosXFuncion = null;
		MensajePlantillaCorreoDTO formatosPlantilla = mailTemplateService.consultaXId(plantillaCorreo.getValor());
		if (formatosPlantilla == null)
			throw new ServerException("Revisa porque el identificador del mensaje no aparece en BD." + plantillaCorreo);
		// No envio mensaje al que creo el documento
		String usuarioGenerador = (token == null) ? null : usuarioService.getUserFlex(token);
		PropiedadDTO mensajeFuncion = cacheService.obtenerPropiedad(plantillaCorreo.getTipo(),
				plantillaCorreo.getCampo(), Propiedades.MENSAJE_DESTINATARIOS_SQL, usuarioGenerador);
		PropiedadDTO mensajeReporte = cacheService.obtenerPropiedad(plantillaCorreo.getTipo(),
				plantillaCorreo.getCampo(), Propiedades.MENSAJE_REPORTE, usuarioGenerador);
		PropiedadDTO mensajeAdjuntoURL = cacheService.obtenerPropiedad(plantillaCorreo.getTipo(),
				plantillaCorreo.getCampo(), Propiedades.MENSAJE_ADJUNTO_URL, usuarioGenerador);
		// Por algun motivo validaba esto del reporte creo que tiene que ver con algun
		// null
		// if (mensajeReporte == null)
		// usuarioGenerador = usuarioService.getUserFlex(token);
		if (responsable != null
				&& (usuarioGenerador == null || responsable.getLlaveTabla().compareTo(usuarioGenerador) != 0)) {
			// Evitar enviar correo al mismo que lo creo
			formatEmail(destinatarios, responsable.getLlaveTabla(), responsable.getCorreo());
		}
		// Debo dejarla fuera de la exception porque me la bloquea la transaccionalidad
		if (mensajeFuncion != null) {
			String propiedadUbicacion = propiedadService.ubicarPropiedad(mensajeFuncion);
			try {
				String keyF = D3Utils.formatFunction(mensajeFuncion.getLlaveTabla());
				String documentF = documento.getLlaveTabla();
				String modificadorF = modificador.getLlaveTabla();
				destinatariosXFuncion = messageService.correosMensaje(keyF, documentF, modificadorF, token);
			} catch (Exception e) {
				throw new ServerException(e.getMessage(),
						propiedadUbicacion + "\nDOCUMENTO : " + documento.getNombre() + " - "
								+ documento.getDescripcion().toLowerCase() + "\nMODIFICADOR : "
								+ modificador.getNombre() + " - " + modificador.getDescripcion().toLowerCase()
								+ "\n PROPIEDAD: " + mensajeFuncion.getKey().toLowerCase());
			}
		}

		if (destinatariosXFuncion != null && !destinatariosXFuncion.isEmpty()) {
			for (MensajeDTO iDestinatario : destinatariosXFuncion) {
				if (iDestinatario.getUsuario() == null) {
					if (iDestinatario.getCorreo() != null) {
						if (destinatariosExternos == null)
							destinatariosExternos = new ArrayList<String>();
						String[] externalMail = iDestinatario.getCorreo().split(SharedConstants.PUNTO_COMA);
						for (String iMail : externalMail) {
							if (iMail != null && !iMail.isEmpty())
								destinatariosExternos.add(iMail.toLowerCase());
						}
					}
				} else {
					if (usuarioGenerador == null || iDestinatario.getUsuario().compareTo(usuarioGenerador) != 0)
						formatEmail(destinatarios, iDestinatario.getUsuario(), iDestinatario.getCorreo());
				}
			}
		}

		if (destinosFijos != null) {
			for (UsuarioDTO iFijo : destinosFijos) {
				if (usuarioGenerador == null || iFijo.getLlaveTabla().compareTo(usuarioGenerador) != 0)
					formatEmail(destinatarios, iFijo.getLlaveTabla(), iFijo.getCorreo());
			}
		}
		if (destinatarios.isEmpty() && destinatariosExternos == null) {
			System.out.format("\n[%s] Mensaje ( %s ) no tiene destinatario", documento.getNombre(),
					formatosPlantilla.getNombre());
			return;
		}
		List<PedidoVentaCaracteristicaDTO> camposMensaje = campoService.listarParaMensaje(documento.getLlaveTabla(),
				documento.getPlantilla(), plantillaCorreo.getLlaveTabla(),
				(modificador == null) ? null : modificador.getLlaveTabla());

		String parametros = MailUtils.generateParameters(plantillaCorreo, documento,
				(responsable == null) ? null : responsable.getNombre(), modificador, camposMensaje);
		parametros = templatesService.extractParameterTypeR(null, documento, modificador, parametros, plantillaCorreo,
				null);

		String mensajeTitulo = templatesService.generateOutputFile(formatosPlantilla.getTitulo(), parametros);
		mensajeTitulo = MailUtils.replaceParameterInBodyMessage(mensajeTitulo, parametros);
		mensajeTitulo = D3Utils.recortar(mensajeTitulo, MailUtils.LONGITUD_MAXIMA_DESCRIPCION);

		String attachLink = null;
		if (mensajeAdjuntoURL != null) {
			List<PedidoVentaCaracteristicaDTO> fieldsEmailToSend = findFieldService
					.call(mensajeAdjuntoURL.getLlaveTabla(), modificador.getCaracteristicas());
			if (fieldsEmailToSend != null && !fieldsEmailToSend.isEmpty()) {
				for (PedidoVentaCaracteristicaDTO iFieldsEmailToSend : fieldsEmailToSend) {
					if (attachLink == null) {
						attachLink = iFieldsEmailToSend.getValorText();
					} else {
						attachLink = attachLink + SharedConstants.PUNTO_COMA_DOBLE + iFieldsEmailToSend.getValorText();
					}
				}
			}
		}

		String destinyMails = "";

		if (destinatariosExternos != null) {
			List<String> externalWithoutDuplicates = new ArrayList<>(new HashSet<>(destinatariosExternos));
			// Para evitar duplicados quito los destinatarios externos que ya se les envia
			// correo
			for (Map.Entry<String, String> entry : destinatarios.entrySet()) {
				for (String iDestinatario : externalWithoutDuplicates) {
					if (entry.getValue() != null && entry.getValue().compareTo(iDestinatario) == 0) {
						externalWithoutDuplicates.remove(iDestinatario);
						break;
					}
				}
			}
			for (String iDestinatario : externalWithoutDuplicates) {
				destinyMails = destinyMails + SharedConstants.PUNTO_COMA + iDestinatario;
			}
		}
		for (Map.Entry<String, String> entry : destinatarios.entrySet()) {
			destinyMails = destinyMails + SharedConstants.PUNTO_COMA + entry.getValue();
		}

		MensajeDTO mensaje = new MensajeDTO();
		mensaje.setFecha(new Date());
		mensaje.setTemplate(plantillaCorreo.getValor());
		// Sucedio que al asignar sin transaccion salia un error porque no hay
		// modificador
		if (modificador == null || modificador.getLlaveTabla() == null) {
			mensaje.setDocumento(documento.getLlaveTabla());
		} else {
			mensaje.setDocumento(modificador.getLlaveTabla());
		}
		mensaje.setTitulo(mensajeTitulo);
		// mensaje.setUsuario(entry.getKey());

		if (destinyMails.isEmpty()) {
			mensaje.setCorreoEnviado(new Date());
			mensaje.setCorreoError("Mensaje sin destinatarios");
		} else {
			if (destinyMails.startsWith(SharedConstants.PUNTO_COMA))
				destinyMails = destinyMails.substring(1);
			mensaje.setCorreo(destinyMails);
		}
		mensaje.setAdjuntoURL(attachLink);
		if (mensajeReporte != null)
			mensaje.setReporte(mensajeReporte.getValor());
		mensaje.setParametros(parametros);

		messageService.saveSimple(mensaje);
		System.out.format("\n[%s] Mensaje ( %s ) asignado a (%s) ", documento.getNombre(),
				formatosPlantilla.getNombre(), destinyMails);
	}

	private void formatEmail(Map<String, String> map, String key, String email) {
		if (email == null)
			return;
		map.put(key, email.toLowerCase());
	}

}