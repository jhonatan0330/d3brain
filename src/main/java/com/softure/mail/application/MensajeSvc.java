package com.softure.mail.application;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.document_execution.application.DocumentoRelacionExpedienteSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.logisticpymes.domain.ServidorFilterDTO;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.domain.MensajeFilterDTO;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.mail.infrastructure.MensajeMapper;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReporteBaseDTO;
import com.softure.java.services.SoftureUtil;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;

@Service("mensajeService")
public class MensajeSvc extends BasicSvc<MensajeDTO, MensajeFilterDTO> {
	
	@Autowired
	private MensajeMapper mensajeMapper;
	
	// BEGIN region servicesMensaje
	private static final String SEPARADOR = ";;";
	private static final int LONGITUD_MAXIMA_DESCRIPCION = 200;
	@Autowired private ReporteBaseSvc reporteBaseService;
	@Autowired private MensajePlantillaCorreoSvc mensajeTransicionService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private RelacionInternaSvc relacionService;
	@Autowired private UsuarioSvc usuarioService;
	@Autowired private ServidorSvc servidorService;
	@Autowired private OrganizacionSvc organizacionService;
	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	@Autowired private DocumentoRelacionExpedienteSvc relacionExpedienteService;
	// END region servicesMensaje

	@Override
	public MensajeDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Mensaje");
		MensajeFilterDTO dto = new MensajeFilterDTO();
		dto.setLlaveTabla(llave);
		return mensajeMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = mensajeMapper;
	}
	
	@Override
	public MensajeDTO activar(MensajeDTO dto, String token) throws ServerException {
		// BEGIN Mensaje_activar
		return super.activar(dto, token);
		// END Mensaje_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public MensajeDTO actualizar( MensajeDTO dto, String token) throws ServerException {
		// BEGIN Mensaje_actualizar
		return super.actualizar(dto, token);
		// END Mensaje_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public MensajeDTO inactivar(MensajeDTO dto, String token) throws ServerException {
		// BEGIN Mensaje_inactivar
		return super.inactivar(dto, token);
		// END Mensaje_inactivar
	}
	
	@Override
	public MensajeDTO consultaUnica(MensajeFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(MensajeFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<MensajeDTO> listarConsulta(MensajeFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<MensajeDTO> mensajesUsuario(MensajeFilterDTO dto)throws ServerException{
		// BEGIN region mensajesUsuario
		if(dto.getUsuario()==null) throw new ServerException("Identifique el usuario");
		paginar(dto);
		return mensajeMapper.mensajesUsuario(dto);
		// END region mensajesUsuario
	}
	public MensajeDTO enviarMensaje(MensajeFilterDTO dto)throws ServerException{
		// BEGIN region enviarMensaje
		MensajeDTO bd = consultaXId(dto.getLlaveTabla());
		if(bd.getCorreoEnviado()!=null) throw new ServerException("Este mensaje ya fue enviado");
		String usuario = getUserFlex(dto.getSecurityToken());
		return enviarCorreo(bd, usuario, dto.getSecurityToken());
		// END region enviarMensaje
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public MensajeDTO guardar(MensajeDTO dto, String token) throws ServerException {
		// BEGIN Mensaje_guardar
		return super.guardar(dto, token);
		// END Mensaje_guardar
	}

// BEGIN region aditionalMethods
	
	public void tareaCorreoElectronico()throws ServerException{
	 	List<MensajeDTO> tareasPendientes = mensajeMapper.mensajesDisponibles();
	 	if(tareasPendientes!=null && tareasPendientes.size()>0){
	 		UsuarioSesionDTO sessionAdmin = autenticacionService.generateAdministratorToken();
	 		for (MensajeDTO tareaProgramadaDTO : tareasPendientes) {
	 			if(tareaProgramadaDTO.getCorreo()!=null) {
	 				tareaProgramadaDTO = enviarCorreo(tareaProgramadaDTO, sessionAdmin.getUsuario(), sessionAdmin.getLlaveTabla());
	 			}
			}
	 	}
	}
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public MensajeDTO enviarCorreo(MensajeDTO dto, String usuario, String token) throws ServerException {
		try {
			MensajePlantillaCorreoDTO plantilla = mensajeTransicionService.consultaXId(dto.getTemplate());
			ServidorDTO servidor = null;
			if(plantilla.getServidor()!=null){
				servidor = servidorService.consultaXId(plantilla.getServidor());
			} else {
				servidor = servidorService.obtenerServidorPrincipal(ServidorDTO.MAIL);
			}
			if(servidor == null) throw new ServerException("No se encuentra el servidor de correo configurado");
			if(servidor.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("El servidor de correo no se encuentra activo. " + servidor.getNombre());
			JavaMailSenderImpl mailSender = getMailSender(servidor);
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
			mailMsg.setText(crearMensaje(plantilla.getTexto(), dto.getParametros()),true);
			if(conReporte) {
				byte[] reporte = reporteBaseService.generarReporte(
						reporteBaseService.validateReport(dto.getReporte(), token), 
						dto.getDocumento(), null, token);
				if(reporte!=null) {
					ReporteBaseDTO base = reporteBaseService.consultaXId(dto.getReporte());
					mailMsg.addAttachment(base.getNombre(), new ByteArrayDataSource(reporte, "application/pdf"));
				}
			}
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			dto.setCorreoError(e.getMessage());
			mensaje2Administrator("Error enviando correos electronicos " + dto.getTitulo(), e.getMessage());
		}
		dto.setCorreoEnviado(new Date());
		update(dto);
		return dto;
	}
	
	private JavaMailSenderImpl getMailSender(ServidorDTO servidor) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(servidor.getUrl());
		mailSender.setPort((servidor.getPuerto()==null)?587:Integer.parseInt(servidor.getPuerto()));
		mailSender.setUsername(servidor.getUsuario());
		mailSender.setPassword(servidor.getClave());
		Properties prop = mailSender.getJavaMailProperties();
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.debug", "true");
		prop.put("mail.smtp.ssl.trust", servidor.getUrl());
		return mailSender;
	}
	
	private String crearMensaje(String plantilla , String parametros){
		if(parametros==null) return "";
		String[] params  = parametros.split(SEPARADOR);
		int posIgual = -1;
		String codigo = null;
		String textoReemplazar = null;
		for (String iParametro : params) {
			posIgual = iParametro.indexOf("=");
			codigo = iParametro.substring(0,posIgual);
			textoReemplazar = iParametro.substring(posIgual+1, iParametro.length());
			plantilla = plantilla.replace("{"+codigo +"}", textoReemplazar);
		}
		plantilla = plantilla.replaceAll("\\{[A-Za-z0-9_]*\\}", "");
		return plantilla;
	}
	
	public void colocarMensajes(
			PropiedadDTO plantillaCorreo,
			PedidoVentaDTO documento, 
			UsuarioDTO responsable, 
			PedidoVentaDTO modificador, 
			List<UsuarioDTO> destinosFijos,
			List<String> destinatariosExternos,
			String token) throws ServerException{
		Map<String, String> destinatarios = new HashMap<String, String>();//PAra para evitar duplicados de usuarios
		List<MensajeDTO> destinatariosXFuncion =null;
		MensajePlantillaCorreoDTO formatosPlantilla = mensajeTransicionService.consultaXId(plantillaCorreo.getValor());
		if(formatosPlantilla==null) throw new ServerException("Revisa porque el identificador del mensaje no aparece en BD." + plantillaCorreo);
		//No envio mensaje al que creo el documento
		String usuarioGenerador = null;
		String usuarioToken = (token==null)?null:getUserFlex(token);
		PropiedadDTO mensajeFuncion = propiedadService.obtenerPropiedad(plantillaCorreo.getTipo(), plantillaCorreo.getCampo(), Propiedades.MENSAJE_DESTINATARIOS_SQL, usuarioToken);
		PropiedadDTO mensajeReporte = propiedadService.obtenerPropiedad(plantillaCorreo.getTipo(), plantillaCorreo.getCampo(), Propiedades.MENSAJE_REPORTE, usuarioToken);
		if(mensajeReporte==null)usuarioGenerador =  getUserFlex(token);
		if(responsable!=null && (usuarioGenerador==null || responsable.getLlaveTabla().compareTo(usuarioGenerador)!=0)) {
			destinatarios.put(responsable.getLlaveTabla(), responsable.getCorreo());//Evitar enviar correo al mismo que lo creo
		}
		
		if(mensajeFuncion!=null) {
			String propiedadUbicacion = propiedadService.ubicarPropiedad(mensajeFuncion);// Debo dejarla fuera de la exception porque me la bloquea la transaccionalidad 
			try {
				String keyF= SoftureUtil.formatFunction(mensajeFuncion.getLlaveTabla());
				String documentF = documento.getLlaveTabla();
				String modificadorF = modificador.getLlaveTabla();
				destinatariosXFuncion = mensajeMapper.correosMensaje( keyF, documentF, modificadorF, token);
			} catch (Exception e) {
				throw new ServerException(e.getMessage(), propiedadUbicacion + "\nDOCUMENTO : " + documento.getNombre() + " - " +documento.getDescripcion().toLowerCase() + "\nMODIFICADOR : " + modificador.getNombre() + " - " +modificador.getDescripcion().toLowerCase() + "\n PROPIEDAD: " + mensajeFuncion.getKey().toLowerCase());
			}
		}
		
		if(destinatariosXFuncion!=null && !destinatariosXFuncion.isEmpty()) {
			for (MensajeDTO iDestinatario : destinatariosXFuncion) {
				if(iDestinatario.getUsuario()==null) {
					if(iDestinatario.getCorreo()!=null) {
						if(destinatariosExternos==null) destinatariosExternos = new ArrayList<String>();
						destinatariosExternos.add(iDestinatario.getCorreo());	
					}
				}else {
					if(usuarioGenerador==null || iDestinatario.getUsuario().compareTo(usuarioGenerador)!=0) destinatarios.put(iDestinatario.getUsuario(), iDestinatario.getCorreo());
				}
			}
		}
		
		if(destinosFijos!=null) {
			for (UsuarioDTO iFijo : destinosFijos) {
				if(usuarioGenerador==null || iFijo.getLlaveTabla().compareTo(usuarioGenerador)!=0) destinatarios.put(iFijo.getLlaveTabla(), iFijo.getCorreo());
			}
		}
		if(destinatarios.isEmpty() && destinatariosExternos==null) {
			System.out.format("\n[%s] Mensaje ( %s ) no tiene destinatario", documento.getNombre(), formatosPlantilla.getNombre());
			return;
		}
		String parametros = generarParametros(documento, "D_");
		if(responsable!=null) parametros = parametros + SEPARADOR + "D_RESPONSABLE=" + responsable.getNombre();
		if(modificador!=null) parametros = parametros + SEPARADOR + generarParametros(modificador, "M_");
		List<PedidoVentaCaracteristicaDTO> camposMensaje = campoService.listarParaMensaje(documento.getLlaveTabla(), documento.getPlantilla(), plantillaCorreo.getLlaveTabla(), (modificador==null)?null:modificador.getLlaveTabla());
		if(camposMensaje!=null && !camposMensaje.isEmpty()) {
			for (PedidoVentaCaracteristicaDTO iCampo: camposMensaje) {
				if(iCampo.getValorText()!=null) {
					parametros = parametros + SEPARADOR + "C_" +SoftureUtil.formatFunction(iCampo.getCampo()).toUpperCase() +"="+ SoftureUtil.recortar(iCampo.getValorText(), LONGITUD_MAXIMA_DESCRIPCION);
				}
			}
		}
		String mensajeTitulo = SoftureUtil.recortar(crearMensaje(formatosPlantilla.getTitulo(), parametros), LONGITUD_MAXIMA_DESCRIPCION);
		
		for (Map.Entry<String, String> entry : destinatarios.entrySet()) {
			MensajeDTO mensaje = new MensajeDTO();
			mensaje.setFecha(new Date());
			mensaje.setTemplate(plantillaCorreo.getValor());
			// Sucedio que al asignar sin transaccion salia un error porque no hay modificador
			if(modificador==null || modificador.getLlaveTabla()==null) {
				mensaje.setDocumento(documento.getLlaveTabla());				
			}else {
				mensaje.setDocumento(modificador.getLlaveTabla());
			}
			mensaje.setTitulo(mensajeTitulo);
			mensaje.setUsuario(entry.getKey());
			mensaje.setCorreo(entry.getValue());
			if(mensajeReporte!=null)mensaje.setReporte(mensajeReporte.getValor());
			mensaje.setParametros(parametros);
			save(mensaje);
			System.out.format("\n[%s] Mensaje ( %s ) asignado a (%s) con correo (%s)", documento.getNombre(), formatosPlantilla.getNombre(), entry.getKey(), entry.getValue());
		}
		
		if(destinatariosExternos!=null) {
			//Para evitar duplicados quito los destinatarios externos que ya se les envia correo
			for (Map.Entry<String, String> entry : destinatarios.entrySet()) {
				for (String iDestinatario : destinatariosExternos) {
					if(entry.getValue()!=null && entry.getValue().compareTo(iDestinatario)==0) {
						destinatariosExternos.remove(iDestinatario);
						break;
					}
				}
			}
			for (String iDestinatario : destinatariosExternos) {
				MensajeDTO mensaje = new MensajeDTO();
				mensaje.setFecha(new Date());
				mensaje.setTemplate(plantillaCorreo.getValor());
				if(modificador==null) {
					mensaje.setDocumento(documento.getLlaveTabla());				
				}else {
					mensaje.setDocumento(modificador.getLlaveTabla());
				}
				mensaje.setTitulo(mensajeTitulo);
				mensaje.setCorreo(iDestinatario);
				if(mensajeReporte!=null)mensaje.setReporte(mensajeReporte.getValor());
				mensaje.setParametros(parametros);
				save(mensaje);
				System.out.format("\n[%s] Mensaje ( %s ) asignado a correo externo (%s)", documento.getNombre(), formatosPlantilla.getNombre(), iDestinatario);
			}
		}
	}
	
	private String generarParametros(PedidoVentaDTO documento, String prefijo) throws ServerException {
		String parametros = prefijo + "CODE=" + documento.getNombre();
		if(documento.getDescripcion()!=null) parametros = parametros + SEPARADOR + prefijo + "DESC=" + SoftureUtil.recortar(documento.getDescripcion(), LONGITUD_MAXIMA_DESCRIPCION);
		if(documento.getEstadoNombre()!=null) parametros = parametros + SEPARADOR + prefijo + "ESTADO=" + documento.getEstadoNombre();
		if(documento.getFecha()!=null) parametros = parametros + SEPARADOR + prefijo + "FECHA=" + SoftureUtil.formatDateTime(documento.getFecha());
		if(documento.getDinero()!=null) {
			parametros = parametros + SEPARADOR + prefijo + "VALOR=" + SoftureUtil.formatMoney(documento.getDinero().getValorTotal());
			parametros = parametros + SEPARADOR + prefijo + "SALDO=" + SoftureUtil.formatMoney(documento.getDinero().getSaldo());
		}
		return parametros;
	}

	public void gestionarMensajes(
			PedidoVentaDTO pedido, 
			ProcesoTransicionDTO transicionDTO, 
			UsuarioDTO responsable, 
			PedidoVentaDTO modificador,
			String token) throws ServerException {
		//Se puede mejorar con un solo query
		PropiedadDTO mensaje = null;
		String campo = modificador.getPlantilla();
		String tipo = null;
		String usuarioToken = (token==null)?null:getUserFlex(token);
		if(modificador.getLlaveTabla()==null || modificador.getLlaveTabla().compareTo(pedido.getLlaveTabla())==0) {
			//El tema es que si coloco mensaje a la plantilla, pero es parte de una transicion se duplica el mensaje
			tipo =  PropiedadValorDefinidoDTO.PLANTILLA;
			mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
			if(mensaje ==null && transicionDTO!=null && transicionDTO.getEstadoPartida()==null) {//Esto lo repito pero no se bien si deba hacerlo asi
				campo = transicionDTO.getLlaveTabla();
				tipo = PropiedadValorDefinidoDTO.TRANSICION;
				mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				if(mensaje==null && transicionDTO.getProceso()!=null) {
					campo = transicionDTO.getProceso();
					tipo = PropiedadValorDefinidoDTO.PROCESO;
					mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				}
			}
		}else {
			if(transicionDTO!=null) {
				campo = transicionDTO.getLlaveTabla();
				tipo = PropiedadValorDefinidoDTO.TRANSICION;
				mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				if(mensaje==null && transicionDTO.getProceso()!=null) {
					campo = transicionDTO.getProceso();
					tipo = PropiedadValorDefinidoDTO.PROCESO;
					mensaje = propiedadService.obtenerPropiedad(tipo, campo, Propiedades.MENSAJE, usuarioToken);
				}
			}
		}
		//Si encontro mensaje 
		if(mensaje!=null) {
			System.out.format("\n[%s] Mensaje ( %s )", pedido.getNombre(), mensaje.getTexto());
			List<PropiedadDTO> destinatariosFijos = propiedadService.obtenerPropiedades(tipo, campo, Propiedades.MENSAJE_DESTINATARIO, usuarioToken);
			List<UsuarioDTO> fijos = null;
			List<String> correosFijos = null;
			if(destinatariosFijos!=null && !destinatariosFijos.isEmpty()) {
				fijos = new ArrayList<UsuarioDTO>();
				for (PropiedadDTO iPropiedad : destinatariosFijos) {
					if(iPropiedad.getValor().compareTo("*")!=0) {
						UsuarioDTO pUser = usuarioService.consultaXId(iPropiedad.getValor());
						if(pUser!=null && pUser.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0) {
							fijos.add(pUser);
						}	
					}
					//Si tiene relaciones la propiedad entonces el busca los correos que se encuentren en esas relaciones
					correosFijos = searchMail(relacionService.relacionesPropiedad(iPropiedad.getLlaveTabla()), modificador.getCaracteristicas());					
				}
			}
			colocarMensajes(mensaje, pedido, responsable, modificador, fijos, correosFijos, token);
		}
	}
	
	private List<String> searchMail(List<RelacionInternaDTO> relaciones, List<PedidoVentaCaracteristicaDTO> fields) throws ServerException {
		if(relaciones==null || relaciones.isEmpty() || fields == null || fields.isEmpty()) return null; 
		List<String> correosFijos = null;
		List<PedidoVentaCaracteristicaDTO> fieldsInternal = null;
		List<RelacionInternaDTO> relacionesValidadas = new ArrayList<RelacionInternaDTO>();
		// relacionesSinRepetir.addAll(relaciones.);//SEparado al contructor creo que para que funcione el remove
		for (RelacionInternaDTO iRelacion : relaciones) {
			for (PedidoVentaCaracteristicaDTO iField : fields) {
				if(iRelacion.getCampo().compareTo(iField.getCampo())==0) {
					relacionesValidadas.add(iRelacion);
					if(iField.getValorOpcion()!=null) {
						if(fieldsInternal==null) fieldsInternal = new ArrayList<PedidoVentaCaracteristicaDTO>();
						fieldsInternal.add(iField);
					}else {
						// En caso que sea multiple se debe evaluar todos los expedientes internos
						if(iField.getCampoDTO()!=null && DocumentoPlantillaCaracteristicaDTO.PROCESO.compareTo(iField.getCampoDTO().getFormato())==0
								&& Propiedades.obtenerValor(iField.getCampoDTO(), Propiedades.MULTIPLE)!=null) {
							DocumentoRelacionExpedienteFilterDTO relacionExpedienteFilter = new DocumentoRelacionExpedienteFilterDTO();
							relacionExpedienteFilter.setCampoMaestro(iField.getLlaveTabla());
							relacionExpedienteFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
							List<DocumentoRelacionExpedienteDTO> expedientesAnidados = relacionExpedienteService.listarConsulta(relacionExpedienteFilter);
							if(expedientesAnidados !=null && !expedientesAnidados.isEmpty()) {
								if(fieldsInternal==null) fieldsInternal = new ArrayList<PedidoVentaCaracteristicaDTO>();
								for (DocumentoRelacionExpedienteDTO iAnidado : expedientesAnidados) {
									PedidoVentaCaracteristicaDTO newField = new PedidoVentaCaracteristicaDTO();
									newField.setValorOpcion(iAnidado.getExpedienteDetalle());
									fieldsInternal.add(newField);	
								}
							}
						}else {
							if(correosFijos==null) correosFijos = new ArrayList<String>();
							correosFijos.add(iField.getValorText());	
						}
					}
					// break; //Lo reitre para que valide todos los campos
				}
			}
		}
		if(fieldsInternal!=null) {
			//Esto me toco hacerlo porque se descuadranban los array al remove la relacion
			List<RelacionInternaDTO> relacionesSinRepetir = new ArrayList<RelacionInternaDTO>();
			relacionesSinRepetir.addAll(relaciones);
			for (RelacionInternaDTO iRelacion : relacionesValidadas) {
				relacionesSinRepetir.remove(iRelacion);
			}
			fieldsInternal = campoService.listar2getMessageMailDestiny(fieldsInternal, relacionesSinRepetir);
			List<String> mailInternal = searchMail(relacionesSinRepetir, fieldsInternal);
			if(mailInternal!=null) {
				if(correosFijos==null) correosFijos = new ArrayList<String>();
				correosFijos.addAll(mailInternal);
			}
		}
		return correosFijos;
	}
	
	public void mensaje2Administrator(String messageTitle, String messageText) throws ServerException {
		UsuarioDTO userAdmin = autenticacionService.getUserSystem();
		if(userAdmin==null || userAdmin.getCorreo()==null ) return;
		ServidorFilterDTO filter = new ServidorFilterDTO();
		filter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filter.setTipo(ServidorDTO.MAIL);
		List<ServidorDTO> servidores = servidorService.listarConsulta(filter);
		if(servidores == null || servidores.isEmpty()) throw new ServerException("No se encuentra el servidor de correo configurado para enviar mensaje al administrador.\n " + messageTitle + "\n" +messageText);
		JavaMailSenderImpl mailSender = getMailSender(servidores.get(0));
		OrganizacionDTO principal = organizacionService.obtenerPrincipal(null);
		SimpleMailMessage message = new SimpleMailMessage();  
        message.setFrom(servidores.get(0).getUsuario());
	    message.setTo(userAdmin.getCorreo());
	    message.setSubject(messageTitle);  
	    message.setText(principal.getNombre() + " " + messageText);  
	    mailSender.send(message);
	}
	
	
	public void mensaje2Recover(String correo, String key, String code) throws ServerException {
		ServidorFilterDTO filter = new ServidorFilterDTO();
		filter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filter.setTipo(ServidorDTO.MAIL);
		List<ServidorDTO> servidores = servidorService.listarConsulta(filter);
		if(servidores == null || servidores.isEmpty()) throw new ServerException("No se encuentra el servidor de correo configurado");
		
		JavaMailSenderImpl mailSender = getMailSender(servidores.get(0));
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		OrganizacionDTO principal = organizacionService.obtenerPrincipal(null);
		if (principal.getServidorUrl()==null) throw new ServerException("Se debe configurar la url del servidor principal para la organizacion " + principal.getNombre());
		try {
			mailMsg.setFrom(servidores.get(0).getUsuario());
			mailMsg.setTo(correo);
			mailMsg.setSubject(principal.getNombre() +  " Recuperacion de clave de acceso");  
			mailMsg.setText("<table style=\"height: 164px;\" width=\"600\" bgcolor=\"#0d47a1\"><tbody><tr style=\"height: 18px;\"><td style=\"height: 18px; width: 590px;\" bgcolor=\"#0d47a1\">&nbsp;</td></tr><tr style=\"text-align: center;\"><td style=\"height: 132px; width: 590px; text-align: center;\" bgcolor=\"#E4E4E4\"><a style=\"border-radius: 4px; display: inline-block; font-weight: bold; padding: 12px 24px; !important; color: #ffffff !important; background-color: #80bf2e;\" href=\"" + principal.getServidorUrl() + "/sessions/new/"+key+"\" target=\"_blank\">PRESIONA PARA NUEVA CLAVE</a>"
					+ "<p>El codigo de seguridad es : <strong>"+code+"</strong></p><p>El codigo se vencera en 15 minutos</p></td></tr><tr><td style=\"font-size: 11px; color: #eeeeee;\" align=\"center\">"+ principal.getNombre() + "  " + principal.getSlogan() +"</td></tr></tbody></table>"
					, true); 
		    mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new ServerException(e.getMessage());
		}
	}
	
	public void sendMailToTransaction(String transactionId, String userId, String token) throws ServerException {
		List<MensajeDTO> mensajes = mensajeMapper.mensajesTransaccion(transactionId);
		if(mensajes!=null) {
			for (MensajeDTO iMessage : mensajes) {
				enviarCorreo(iMessage, userId, token);
			}
		}
	}

	
// END region aditionalMethods

}