package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.DocumentoRelacionExpedienteDTO;
import com.softure.logisticpymes.dto.MensajePlantillaCorreoDTO;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.dto.ReporteBaseDTO;
import com.softure.logisticpymes.dto.ServidorDTO;
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.logisticpymes.dto.filter.DocumentoRelacionExpedienteFilterDTO;
import com.softure.logisticpymes.services.adapter.Propiedades;
import com.softure.java.services.SoftureUtil;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.MensajeDTO;
import com.softure.logisticpymes.dto.filter.MensajeFilterDTO;
import com.softure.logisticpymes.persistence.MensajeMapper;

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
		return enviarCorreo(bd, usuario);
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
	 		String usuario = autenticacionService.generateAdministratorToken().getUsuario();
	 		for (MensajeDTO tareaProgramadaDTO : tareasPendientes) {
	 			if(tareaProgramadaDTO.getCorreo()!=null) {
	 				tareaProgramadaDTO = enviarCorreo(tareaProgramadaDTO, usuario);
	 			}
			}
	 	}
	}
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public MensajeDTO enviarCorreo(MensajeDTO dto, String usuario) throws ServerException {
		try {
			MensajePlantillaCorreoDTO plantilla = mensajeTransicionService.consultaXId(dto.getTemplate());
			ServidorDTO servidor = servidorService.consultaXId(plantilla.getServidor());
			if(servidor ==null) throw new ServerException("No se encuentra el servidor de correo configurado");
			if(servidor.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("El servidor de correo no se encuentra activo. " + servidor.getNombre());
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
						reporteBaseService.validateReport(dto.getReporte(), usuario), 
						dto.getDocumento(), null, usuario);
				if(reporte!=null) {
					ReporteBaseDTO base = reporteBaseService.consultaXId(dto.getReporte());
					mailMsg.addAttachment(base.getNombre(), new ByteArrayDataSource(reporte, "application/pdf"));
				}
			}
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			dto.setCorreoError(e.getMessage());
		}
		dto.setCorreoEnviado(new Date());
		update(dto);
		return dto;
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
		try {
			if(mensajeFuncion!=null)destinatariosXFuncion = mensajeMapper.correosMensaje(SoftureUtil.formatFunction(mensajeFuncion.getLlaveTabla()), modificador.getLlaveTabla());
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Documento : " + documento.getNombre() + " - " +documento.getDescripcion() + "\nModificador : " + modificador.getNombre() + " - " +modificador.getDescripcion() + "\n" + mensajeFuncion.getKey() + " : " + mensajeFuncion.getMotivo());
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
			if(modificador==null) {
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
					UsuarioDTO pUser = usuarioService.consultaXId(iPropiedad.getValor());
					if(pUser!=null && pUser.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0) {
						fijos.add(pUser);
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

	
// END region aditionalMethods

}