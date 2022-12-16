package com.softure.authentication.application;

import java.util.List;

// BEGIN region interImport
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionErrorDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioAutenticacionMapper;
import com.softure.authorization.application.ModuloContratadoSvc;
import com.softure.authorization.domain.ModuloContratadoFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;

@Service("usuarioAutenticacionService")
public class UsuarioAutenticacionSvc extends BasicSvc<UsuarioAutenticacionDTO, UsuarioAutenticacionFilterDTO> {
	
	@Autowired
	private UsuarioAutenticacionMapper usuarioAutenticacionMapper;
	
	// BEGIN region servicesUsuarioAutenticacion
	
	@Autowired private UsuarioAutenticacionAutorizacionSvc authorizationService;
	@Autowired private ModuloContratadoSvc modulosService;
	@Autowired private OrganizacionSvc organizacionService;
	@Autowired private UsuarioSesionSvc usuarioSesionService;
	@Autowired private UsuarioSvc usuarioService;
	@Autowired private UsuarioSesionErrorSvc errorService;
	// END region servicesUsuarioAutenticacion

	@Override
	public UsuarioAutenticacionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. UsuarioAutenticacion");
		UsuarioAutenticacionFilterDTO dto = new UsuarioAutenticacionFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioAutenticacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioAutenticacionMapper;
	}
	
	@Override
	public UsuarioAutenticacionDTO activar(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_activar
		return super.activar(dto, token);
		// END UsuarioAutenticacion_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionDTO actualizar( UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_actualizar
		return super.actualizar(dto, token);
		// END UsuarioAutenticacion_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionDTO inactivar(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_inactivar
		return super.inactivar(dto, token);
		// END UsuarioAutenticacion_inactivar
	}
	
	@Override
	public UsuarioAutenticacionDTO consultaUnica(UsuarioAutenticacionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioAutenticacionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioAutenticacionDTO> listarConsulta(UsuarioAutenticacionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public UsuarioAutenticacionDTO autenticar(UsuarioAutenticacionFilterDTO dto)throws ServerException{
		// BEGIN region autenticar
		return autenticar(dto, false);
		// END region autenticar
	}
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionDTO cambiarClave(UsuarioAutenticacionDTO dto, String token)throws ServerException{
		// BEGIN region cambiarClave
		UsuarioAutenticacionAutorizacionDTO autho = null;
		UsuarioAutenticacionDTO user = null;
		// Se envia por el administrador por el momento solo flex
		if(dto.getClaveAnterior().compareTo("ADMIN$123")==0){
			if(dto.getLlaveTabla()!=null){
				user = consultaXId(dto.getLlaveTabla());
				if(user== null) throw new ServerException("El usuario no tiene una autenticacion");
				if(user.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("Por favor consulte con su administrador, sus credenciales se encuentran inactivas");
				// if(user.getClave().compareTo(dto.getClaveAnterior())!=0) throw new ServerException("No concuerda la clave anterior");
			}
		} else {
			// La validacion de la autorizacion la retiro para que funcione flex
			if(dto.getLlaveTabla()!=null)
				autho = authorizationService.validar(dto.getLlaveTabla(), dto.getClaveAnterior(), dto.getClave(),dto.getIp());
			UsuarioAutenticacionFilterDTO filtro = new UsuarioAutenticacionFilterDTO();
			if(autho!=null) {
				filtro.setUsuario(autho.getUsuario());
			} else {
				filtro.setUsuario(getUserFlex(token));
			}
			filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			user = consultaUnica(filtro);
			if(autho==null && user.getClave().compareTo(dto.getClaveAnterior())!=0) throw new ServerException("No concuerda la clave anterior");
		}
		
		
		
		if(autho!=null) user.setAutorizacionElimina(autho.getLlaveTabla());
		user.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		user = update(user);
		
		
		UsuarioAutenticacionDTO newAuth = new UsuarioAutenticacionDTO();
		newAuth.setUsuario(user.getUsuario());
		newAuth.setClave(dto.getClave());
		newAuth.setSesion(user.getSesion());
		if(autho!=null) newAuth.setAutorizacionCrea(autho.getLlaveTabla());
		newAuth.setIp(dto.getIp());
		newAuth.setFechaMaxima(getNewMaximunDate());
		newAuth = save(newAuth);
		
		UsuarioSesionFilterDTO filterSesion = new UsuarioSesionFilterDTO();
		filterSesion.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filterSesion.setUsuario(dto.getUsuario());
		List<UsuarioSesionDTO> sesiones = usuarioSesionService.listarConsulta(filterSesion);
		if(sesiones!=null && !sesiones.isEmpty()) {
			for (UsuarioSesionDTO usuarioSesionDTO : sesiones) {
				if(token == null) {
					usuarioSesionDTO.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
					usuarioSesionDTO.setFechaCierre(new Date());
					usuarioSesionService.update(usuarioSesionDTO);
				}else {
					if(usuarioSesionDTO.getLlaveTabla().compareTo(token)!=0)
						usuarioSesionService.inactivar(usuarioSesionDTO, token);					
				}
			}
		}
		return user;
		// END region cambiarClave
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioAutenticacionDTO guardar(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_guardar
		dto.setFechaMaxima(getNewMaximunDate());
		return super.guardar(dto, token);
		// END UsuarioAutenticacion_guardar
	}

// BEGIN region aditionalMethods
	public void crearAutenticacion(String usuario, String token) throws ServerException{
		UsuarioAutenticacionFilterDTO filtro1 = new UsuarioAutenticacionFilterDTO();
		filtro1.setUsuario(usuario);
		if(consultaUnica(filtro1) == null) {
			UsuarioDTO user = usuarioService.consultaXId(usuario);
			UsuarioAutenticacionDTO aut = new UsuarioAutenticacionDTO();
			aut.setUsuario(usuario);
			aut.setClave(user.getIdentificacion());
			aut.setSesion(user.getIdentificacion());
			guardar(aut, token);
		}
	}
	
	public UsuarioDTO getUserSystem() throws ServerException{
		OrganizacionDTO principal = organizacionService.obtenerPrincipal(null);
		if(principal==null ) throw new ServerException("No se tiene organizacion principal");
		if(principal.getUsuarioSystem()==null) throw new ServerException("No se tiene configurado el usuario sistem en la organizacion principal");
		UsuarioDTO usuarioSystem = usuarioService.consultaXId(principal.getUsuarioSystem());
		if(usuarioSystem==null || usuarioSystem.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("El usuario sistema no exisste o esta inactivo");
		return usuarioSystem;
	}
	
	public UsuarioSesionDTO generateAdministratorToken() throws ServerException{
		UsuarioDTO usuarioSystem = getUserSystem();
		UsuarioSesionDTO sesion = new UsuarioSesionDTO();
		sesion.setFecha(new Date());
		//sesion.setFechaCierre(usuarioSesionService.getFechaCierre(usuarioSystem.getLlaveTabla()));
		sesion.setUsuario(usuarioSystem.getLlaveTabla());
		sesion = usuarioSesionService.save(sesion);
		return sesion;
	}
	
	public void logout(String token)throws ServerException{
		UsuarioSesionDTO sesion = usuarioSesionService.consultaXId(token);
		if(sesion == null) throw new ServerException("Token incorrecto");
		if(sesion.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0) throw new ServerException("Se encuentra inactiva la sesion");
		usuarioSesionService.inactivar(sesion, null);
	}
	
	public String getFechaActualizacion() {
		return usuarioAutenticacionMapper.versionActual();
	}
	
	private void reportarError(UsuarioAutenticacionFilterDTO dto, String error) throws ServerException {
		UsuarioSesionErrorDTO use = new UsuarioSesionErrorDTO();
		use.setClave(dto.getClave());
		use.setIp(dto.getIp());
		use.setFecha(new Date());
		use.setSesion(dto.getSesion());
		use.setError(error);
		errorService.save(use);
		throw new ServerException(error);
	}
	
	private void errorDesdeNuevaClave(UsuarioDTO dto, String ip,  String error) throws ServerException {
		UsuarioAutenticacionFilterDTO uaf = new UsuarioAutenticacionFilterDTO();
		uaf.setClave(dto.getCorreo());
		uaf.setIp(ip);
		uaf.setSesion(dto.getIdentificacion());
		reportarError(uaf, error);
	}
	
	public void solicitarNuevaClave(UsuarioAutenticacionDTO dto) throws ServerException {
		if(dto == null || dto.getUsuarioDTO()==null ) throw new ServerException("Faltan los datos de recuperacion");
		if(dto.getUsuarioDTO().getCorreo() == null) errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), "No se envio el correo de recuperacion");
		if(dto.getUsuarioDTO().getIdentificacion() == null) errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), "No se envio la identificacion del usuario");
		UsuarioFilterDTO filter = new UsuarioFilterDTO();
		filter.setIdentificacion(dto.getUsuarioDTO().getIdentificacion());
		UsuarioDTO usuario = usuarioService.consultaUnica(filter) ;
		if(usuario==null) errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), "Revisa los datos de acceso. El numero de id no esta en la base de datos");
		if(usuario.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), "Revisa los datos de acceso. El usuario se encuentra inactivo");
		if(usuario.getCorreo()==null) errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), "No tienes correo registrado para enviarte la nueva clave");
		if(usuario.getCorreo().compareTo(dto.getUsuarioDTO().getCorreo())!=0) errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), "Revisa los datos de acceso. el correo electronico no es el mismo que tienes registrado");
		try {
			authorizationService.generarAutorizacion(usuario.getLlaveTabla(), usuario.getCorreo(), dto.getIp());	
		} catch (Exception e) {
			errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), e.getMessage());
		}
		
	}
	
	// PAra el api de flex despues lo puedo quitar
	public UsuarioAutenticacionDTO autenticar(UsuarioAutenticacionFilterDTO dto, boolean fromApi)throws ServerException{
		if (!fromApi) {
			if(dto.getClaveAnterior()==null) reportarError(dto, "Por favor actualice su version de software");
			String fechaMinima = null;
			try {
				fechaMinima = usuarioAutenticacionMapper.fechaMinima();
			}catch (Exception e) {
				reportarError(dto, e.getMessage());
			}
			if(fechaMinima!=null){
				int cliente = Integer.parseInt(dto.getClaveAnterior().replace(".", ""));
				int servidor = Integer.parseInt(fechaMinima.replace(".", ""));
				if(cliente < servidor) reportarError(dto, "Por favor actualice su version de software (Limpie cache o descargue una nueva app).\nCliente: " + String.valueOf(cliente) + "\nServidor:" + String.valueOf(servidor));
			}	
		}
		
		String fechaTrial = usuarioAutenticacionMapper.consultarValidez();
		if(fechaTrial==null) reportarError(dto,  "El sistema no tiene configurada la fecha de la licencia");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		long diasVigencia = 0;
		try {
			Date date = formatter.parse(fechaTrial);
			diasVigencia =(date.getTime() - new Date().getTime())/ (24 * 3600000);
			if(diasVigencia < 0)
				reportarError(dto, "Se ha vencido la licencia del sistema. " + fechaTrial);
		} catch (ParseException e) {
			reportarError(dto, "El formato de la fecha de licencia esta incorrecto");
		}
		UsuarioAutenticacionDTO autenticacion =null;
		UsuarioSesionDTO sesion  = null;
		if(dto.getSecurityToken()!=null && dto.getClave()==null){
			sesion = usuarioSesionService.consultaXId(dto.getSecurityToken());
			if(sesion == null) reportarError(dto, "Autenticacion incorrecta");
			if(sesion.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0)
				reportarError(dto, "Se encuentra inactiva la sesion");
			autenticacion = new UsuarioAutenticacionDTO();
			autenticacion.setUsuario(sesion.getUsuario());
		}else{
			if(dto.getClave()!=null && dto.getSesion()==null && dto.getSecurityToken()!=null) {
				autenticacion = consultaXId(dto.getClave());
			}
			if(autenticacion== null) {
				UsuarioAutenticacionFilterDTO autenticacionF = new UsuarioAutenticacionFilterDTO();
				if(dto.getSesion()==null) reportarError(dto, "La sesion no puede estar vacia");
				autenticacionF.setSesion(dto.getSesion());
				if(dto.getClave()==null) reportarError(dto, "La clave no puede estar vacia");
				autenticacionF.setClave(dto.getClave());
				autenticacionF.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				autenticacion = consultaUnica(autenticacionF);
			}
			if(autenticacion == null) reportarError(dto, "Autenticacion incorrecta");
			if(autenticacion.getFechaMaxima()!=null && autenticacion.getFechaMaxima().compareTo(new Date())<0)
				reportarError(dto, "Por seguridad, es necesario actualizar la clave de acceso");	
		}
		
		UsuarioDTO usuario = usuarioService.consultaXId(autenticacion.getUsuario());
		if(usuario.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0)reportarError(dto, "El usuario no se encuentra activo");
		autenticacion.setUsuarioDTO(usuario);
		
		if (!fromApi) {
			autenticacion.setOrganizacion(organizacionService.obtenerPrincipalPropiedades(usuario.getLlaveTabla()));
			autenticacion.setOrganizaciones(organizacionService.obtenerUsuario(autenticacion.getUsuario()));	
		}
		
		if(sesion==null) {
			sesion = new UsuarioSesionDTO();
			sesion.setFecha(new Date());
			sesion.setFechaCierre(usuarioSesionService.getFechaCierre(autenticacion.getUsuario()));
			sesion.setUsuario(autenticacion.getUsuario());
			sesion.setIp(dto.getIp());
			sesion = usuarioSesionService.guardar(sesion, dto.getSecurityToken());
		}
		autenticacion.setToken(sesion.getLlaveTabla());
		
		if (!fromApi) {
			if(diasVigencia <= 5 && usuarioAutenticacionMapper.ocultarLicencia(autenticacion.getUsuario())==0) autenticacion.setMensaje("Quedan "+ (diasVigencia +1) +" dias para que se cumpla el periodo de su licencia");
			autenticacion.setTableroControl(usuarioAutenticacionMapper.cantidadAsignaciones(autenticacion.getUsuario()));
			
			ModuloContratadoFilterDTO filterMod = new ModuloContratadoFilterDTO();
			filterMod.setSecurityToken(sesion.getLlaveTabla());
			autenticacion.setModulos(modulosService.modulosUsuario(filterMod));		
		}
		return autenticacion;
	}
	
	private Date getNewMaximunDate() {
		Calendar newDate = Calendar.getInstance();
		newDate.add(Calendar.MONTH, 2);
		return newDate.getTime();
	}
// END region aditionalMethods

}