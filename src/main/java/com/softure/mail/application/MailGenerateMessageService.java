package com.softure.mail.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.document_execution.application.DocumentoRelacionExpedienteSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Service
public class MailGenerateMessageService {

	@Autowired
	private MensajeSvc messageService;
	@Autowired
	private PropiedadSvc propiedadService;
	@Autowired
	private RelacionInternaSvc relacionService;
	@Autowired
	private UsuarioSvc usuarioService;
	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	private DocumentoRelacionExpedienteSvc relacionExpedienteService;
	@Autowired
	private MensajePlantillaCorreoSvc mailTemplateService;

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
			mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
			// Esto lo repitopero no se bien si deba hacerlo asi
			if (mensaje == null && transicionDTO != null && transicionDTO.getEstadoPartida() == null) {
				campo = transicionDTO.getLlaveTabla();
				tipo = PropiedadValorDefinidoDTO.TRANSICION;
				mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				if (mensaje == null && transicionDTO.getProceso() != null) {
					campo = transicionDTO.getProceso();
					tipo = PropiedadValorDefinidoDTO.PROCESO;
					mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				}
			}
		} else {
			if (transicionDTO != null) {
				campo = transicionDTO.getLlaveTabla();
				tipo = PropiedadValorDefinidoDTO.TRANSICION;
				mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				if (mensaje == null && transicionDTO.getProceso() != null) {
					campo = transicionDTO.getProceso();
					tipo = PropiedadValorDefinidoDTO.PROCESO;
					mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				}
			}
		}
		// Si encontro mensaje
		if (mensaje != null) {
			System.out.format("\n[%s] Mensaje ( %s )", pedido.getNombre(), mensaje.getTexto());
			List<PropiedadDTO> destinatariosFijos = propiedadService.obtenerPropiedades(tipo, campo,
					Propiedades.MENSAJE_DESTINATARIO, usuarioToken);
			List<UsuarioDTO> fijos = null;
			List<String> correosFijos = null;
			if (destinatariosFijos != null && !destinatariosFijos.isEmpty()) {
				fijos = new ArrayList<UsuarioDTO>();
				for (PropiedadDTO iPropiedad : destinatariosFijos) {
					if (iPropiedad.getValor().compareTo("*") != 0) {
						UsuarioDTO pUser = usuarioService.consultaXId(iPropiedad.getValor());
						if (pUser != null && pUser.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO) == 0) {
							fijos.add(pUser);
						}
					}
					// Si tiene relaciones la propiedad entonces el busca los correos que se
					// encuentren en esas relaciones
					correosFijos = searchMail(relacionService.relacionesPropiedad(iPropiedad.getLlaveTabla()),
							modificador.getCaracteristicas());
				}
			}
			colocarMensajes(mensaje, pedido, responsable, modificador, fijos, correosFijos, token);
		}
	}

	private List<String> searchMail(List<RelacionInternaDTO> relaciones, List<PedidoVentaCaracteristicaDTO> fields)
			throws ServerException {
		if (relaciones == null || relaciones.isEmpty() || fields == null || fields.isEmpty())
			return null;
		List<String> correosFijos = null;
		List<PedidoVentaCaracteristicaDTO> fieldsInternal = null;
		List<RelacionInternaDTO> relacionesValidadas = new ArrayList<RelacionInternaDTO>();
		// relacionesSinRepetir.addAll(relaciones.);//SEparado al contructor creo que
		// para que funcione el remove
		for (RelacionInternaDTO iRelacion : relaciones) {
			for (PedidoVentaCaracteristicaDTO iField : fields) {
				if (iRelacion.getCampo().compareTo(iField.getCampo()) == 0) {
					relacionesValidadas.add(iRelacion);
					if (iField.getValorOpcion() != null) {
						if (fieldsInternal == null)
							fieldsInternal = new ArrayList<PedidoVentaCaracteristicaDTO>();
						fieldsInternal.add(iField);
					} else {
						// En caso que sea multiple se debe evaluar todos los expedientes internos
						if (iField.getCampoDTO() != null
								&& DocumentoPlantillaCaracteristicaDTO.PROCESO
										.compareTo(iField.getCampoDTO().getFormato()) == 0
								&& Propiedades.obtenerValor(iField.getCampoDTO(), Propiedades.MULTIPLE) != null) {
							DocumentoRelacionExpedienteFilterDTO relacionExpedienteFilter = new DocumentoRelacionExpedienteFilterDTO();
							relacionExpedienteFilter.setCampoMaestro(iField.getLlaveTabla());
							relacionExpedienteFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
							List<DocumentoRelacionExpedienteDTO> expedientesAnidados = relacionExpedienteService
									.listarConsulta(relacionExpedienteFilter);
							if (expedientesAnidados != null && !expedientesAnidados.isEmpty()) {
								if (fieldsInternal == null)
									fieldsInternal = new ArrayList<PedidoVentaCaracteristicaDTO>();
								for (DocumentoRelacionExpedienteDTO iAnidado : expedientesAnidados) {
									PedidoVentaCaracteristicaDTO newField = new PedidoVentaCaracteristicaDTO();
									newField.setValorOpcion(iAnidado.getExpedienteDetalle());
									fieldsInternal.add(newField);
								}
							}
						} else {
							if (correosFijos == null)
								correosFijos = new ArrayList<String>();
							correosFijos.add(iField.getValorText());
						}
					}
					// break; //Lo reitre para que valide todos los campos
				}
			}
		}
		if (fieldsInternal != null) {
			// Esto me toco hacerlo porque se descuadranban los array al remove la relacion
			List<RelacionInternaDTO> relacionesSinRepetir = new ArrayList<RelacionInternaDTO>();
			relacionesSinRepetir.addAll(relaciones);
			for (RelacionInternaDTO iRelacion : relacionesValidadas) {
				relacionesSinRepetir.remove(iRelacion);
			}
			fieldsInternal = campoService.listar2getMessageMailDestiny(fieldsInternal, relacionesSinRepetir);
			List<String> mailInternal = searchMail(relacionesSinRepetir, fieldsInternal);
			if (mailInternal != null) {
				if (correosFijos == null)
					correosFijos = new ArrayList<String>();
				correosFijos.addAll(mailInternal);
			}
		}
		return correosFijos;
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
		String usuarioGenerador = null;
		String usuarioToken = (token == null) ? null : usuarioService.getUserFlex(token);
		PropiedadDTO mensajeFuncion = propiedadService.obtenerPropiedad(plantillaCorreo.getTipo(),
				plantillaCorreo.getCampo(), Propiedades.MENSAJE_DESTINATARIOS_SQL, usuarioToken);
		PropiedadDTO mensajeReporte = propiedadService.obtenerPropiedad(plantillaCorreo.getTipo(),
				plantillaCorreo.getCampo(), Propiedades.MENSAJE_REPORTE, usuarioToken);
		if (mensajeReporte == null)
			usuarioGenerador = usuarioService.getUserFlex(token);
		if (responsable != null
				&& (usuarioGenerador == null || responsable.getLlaveTabla().compareTo(usuarioGenerador) != 0)) {
			destinatarios.put(responsable.getLlaveTabla(), responsable.getCorreo());// Evitar enviar correo al mismo que
																					// lo creo
		}

		if (mensajeFuncion != null) {
			String propiedadUbicacion = propiedadService.ubicarPropiedad(mensajeFuncion);// Debo dejarla fuera de la
																							// exception porque me la
																							// bloquea la
																							// transaccionalidad
			try {
				String keyF = SoftureUtil.formatFunction(mensajeFuncion.getLlaveTabla());
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
						destinatariosExternos.add(iDestinatario.getCorreo());
					}
				} else {
					if (usuarioGenerador == null || iDestinatario.getUsuario().compareTo(usuarioGenerador) != 0)
						destinatarios.put(iDestinatario.getUsuario(), iDestinatario.getCorreo());
				}
			}
		}

		if (destinosFijos != null) {
			for (UsuarioDTO iFijo : destinosFijos) {
				if (usuarioGenerador == null || iFijo.getLlaveTabla().compareTo(usuarioGenerador) != 0)
					destinatarios.put(iFijo.getLlaveTabla(), iFijo.getCorreo());
			}
		}
		if (destinatarios.isEmpty() && destinatariosExternos == null) {
			System.out.format("\n[%s] Mensaje ( %s ) no tiene destinatario", documento.getNombre(),
					formatosPlantilla.getNombre());
			return;
		}
		String parametros = generarParametros(documento, "D_");
		if (responsable != null)
			parametros = parametros + MailUtils.SEPARADOR + "D_RESPONSABLE=" + responsable.getNombre();
		if (modificador != null)
			parametros = parametros + MailUtils.SEPARADOR + generarParametros(modificador, "M_");
		List<PedidoVentaCaracteristicaDTO> camposMensaje = campoService.listarParaMensaje(documento.getLlaveTabla(),
				documento.getPlantilla(), plantillaCorreo.getLlaveTabla(),
				(modificador == null) ? null : modificador.getLlaveTabla());
		if (camposMensaje != null && !camposMensaje.isEmpty()) {
			for (PedidoVentaCaracteristicaDTO iCampo : camposMensaje) {
				if (iCampo.getValorText() != null) {
					parametros = parametros + MailUtils.SEPARADOR + "C_"
							+ SoftureUtil.formatFunction(iCampo.getCampo()).toUpperCase() + "="
							+ SoftureUtil.recortar(iCampo.getValorText(), MailUtils.LONGITUD_MAXIMA_DESCRIPCION);
				}
			}
		}
		String mensajeTitulo = SoftureUtil.recortar(
				MailUtils.replaceParameterInBodyMessage(formatosPlantilla.getTitulo(), parametros),
				MailUtils.LONGITUD_MAXIMA_DESCRIPCION);

		for (Map.Entry<String, String> entry : destinatarios.entrySet()) {
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
			mensaje.setUsuario(entry.getKey());
			mensaje.setCorreo(entry.getValue());
			if (mensajeReporte != null)
				mensaje.setReporte(mensajeReporte.getValor());
			mensaje.setParametros(parametros);
			messageService.save(mensaje);
			System.out.format("\n[%s] Mensaje ( %s ) asignado a (%s) con correo (%s)", documento.getNombre(),
					formatosPlantilla.getNombre(), entry.getKey(), entry.getValue());
		}

		if (destinatariosExternos != null) {
			// Para evitar duplicados quito los destinatarios externos que ya se les envia
			// correo
			for (Map.Entry<String, String> entry : destinatarios.entrySet()) {
				for (String iDestinatario : destinatariosExternos) {
					if (entry.getValue() != null && entry.getValue().compareTo(iDestinatario) == 0) {
						destinatariosExternos.remove(iDestinatario);
						break;
					}
				}
			}
			for (String iDestinatario : destinatariosExternos) {
				MensajeDTO mensaje = new MensajeDTO();
				mensaje.setFecha(new Date());
				mensaje.setTemplate(plantillaCorreo.getValor());
				if (modificador == null) {
					mensaje.setDocumento(documento.getLlaveTabla());
				} else {
					mensaje.setDocumento(modificador.getLlaveTabla());
				}
				mensaje.setTitulo(mensajeTitulo);
				mensaje.setCorreo(iDestinatario);
				if (mensajeReporte != null)
					mensaje.setReporte(mensajeReporte.getValor());
				mensaje.setParametros(parametros);
				messageService.save(mensaje);
				System.out.format("\n[%s] Mensaje ( %s ) asignado a correo externo (%s)", documento.getNombre(),
						formatosPlantilla.getNombre(), iDestinatario);
			}
		}
	}

	private String generarParametros(PedidoVentaDTO documento, String prefijo) throws ServerException {
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
					+ SoftureUtil.formatMoney(documento.getDinero().getValorTotal());
			parametros = parametros + MailUtils.SEPARADOR + prefijo + "SALDO="
					+ SoftureUtil.formatMoney(documento.getDinero().getSaldo());
		}
		return parametros;
	}

}
