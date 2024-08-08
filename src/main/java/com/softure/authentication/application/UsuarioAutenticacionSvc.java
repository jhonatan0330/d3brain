package com.softure.authentication.application;

import java.util.List;

// BEGIN region interImport
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.shared.domain.SharedConstants;
import com.shared.domain.SharedToken;
import com.shared.application.SharedAuthenticateService;
import com.shared.domain.ServerException;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionErrorDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioAutenticacionMapper;
import com.softure.authorization.application.ModuloSvc;
import com.softure.authorization.domain.ModuloFilterDTO;
import com.softure.java.services.HttpUtils;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;


import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("usuarioAutenticacionService")
public class UsuarioAutenticacionSvc extends BasicSvc<UsuarioAutenticacionDTO, UsuarioAutenticacionFilterDTO>
		implements SharedAuthenticateService {

	@Autowired @Lazy 
	private UsuarioAutenticacionMapper usuarioAutenticacionMapper;

	// BEGIN region servicesUsuarioAutenticacion

	@Autowired @Lazy 
	private UsuarioAutenticacionAutorizacionSvc authorizationService;
	@Autowired @Lazy 
	private ModuloSvc modulosService;
	@Autowired @Lazy 
	private OrganizacionSvc organizacionService;
	@Autowired @Lazy 
	private UsuarioSesionSvc usuarioSesionService;
	@Autowired @Lazy 
	private UsuarioSvc usuarioService;
	@Autowired @Lazy 
	private UsuarioSesionErrorSvc errorService;
	// END region servicesUsuarioAutenticacion

	@Override
	public UsuarioAutenticacionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. UsuarioAutenticacion");
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
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioAutenticacionDTO actualizar(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_actualizar
		return super.actualizar(dto, token);
		// END UsuarioAutenticacion_actualizar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
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
	public List<UsuarioAutenticacionDTO> listarConsulta(UsuarioAutenticacionFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	public UsuarioAutenticacionDTO autenticar(UsuarioAutenticacionFilterDTO dto) throws ServerException {
		// BEGIN region autenticar
		return autenticar(dto, false);
		// END region autenticar
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioAutenticacionDTO cambiarClave(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN region cambiarClave
		UsuarioAutenticacionAutorizacionDTO autho = null;
		UsuarioAutenticacionDTO user = null;
		// Se envia por el administrador por el momento solo flex
		if (dto.getClaveAnterior().compareTo("ADMIN$123") == 0) {
			if (dto.getLlaveTabla() != null) {
				user = consultaXId(dto.getLlaveTabla());
				if (user == null)
					throw new ServerException("El usuario no tiene una autenticacion");
				if (user.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
					throw new ServerException(
							"Por favor consulte con su administrador, sus credenciales se encuentran inactivas");
				// if(user.getClave().compareTo(dto.getClaveAnterior())!=0) throw new
				// ServerException("No concuerda la clave anterior");
			}
		} else {
			// La validacion de la autorizacion la retiro para que funcione flex
			if (dto.getLlaveTabla() != null)
				autho = authorizationService.validar(dto.getLlaveTabla(), dto.getClaveAnterior(), dto.getClave(),
						dto.getIp());
			UsuarioAutenticacionFilterDTO filtro = new UsuarioAutenticacionFilterDTO();
			if (autho != null) {
				filtro.setUsuario(autho.getUsuario());
			} else {
				filtro.setUsuario(getUserFlex(token));
			}
			filtro.setEstado(SharedConstants.STATE_ACTIVE);
			user = consultaUnica(filtro);
			if (autho == null && user.getClave().compareTo(dto.getClaveAnterior()) != 0)
				throw new ServerException("No concuerda la clave anterior");
		}

		if (autho != null)
			user.setAutorizacionElimina(autho.getLlaveTabla());
		user.setEstado(SharedConstants.STATE_INACTIVE);
		user = update(user);

		UsuarioAutenticacionFilterDTO filterPassword = new UsuarioAutenticacionFilterDTO();
		filterPassword.setUsuario(user.getUsuario());
		filterPassword.setClave(dto.getClave());
		
		List<UsuarioAutenticacionDTO> repeatPassword = listarConsulta(filterPassword);
		
		if(repeatPassword!=null && !repeatPassword.isEmpty())
			throw new ServerException("La clave que estas usando ya la habias usado y la cambiaste. Por seguridad no vuelvas a usar las mismas claves");
		
		UsuarioAutenticacionDTO newAuth = new UsuarioAutenticacionDTO();
		newAuth.setUsuario(user.getUsuario());
		newAuth.setClave(dto.getClave());
		newAuth.setSesion(user.getSesion());
		if (autho != null)
			newAuth.setAutorizacionCrea(autho.getLlaveTabla());
		newAuth.setIp(dto.getIp());
		newAuth.setFechaMaxima(getNewMaximunDate(user.getUsuario()));
		newAuth = save(newAuth);

		UsuarioSesionFilterDTO filterSesion = new UsuarioSesionFilterDTO();
		filterSesion.setEstado(SharedConstants.STATE_ACTIVE);
		filterSesion.setUsuario(dto.getUsuario());
		List<UsuarioSesionDTO> sesiones = usuarioSesionService.listarConsulta(filterSesion);
		if (sesiones != null && !sesiones.isEmpty()) {
			for (UsuarioSesionDTO usuarioSesionDTO : sesiones) {
				if (token == null) {
					usuarioSesionDTO.setEstado(SharedConstants.STATE_INACTIVE);
					usuarioSesionDTO.setFechaCierre(new Date());
					usuarioSesionService.update(usuarioSesionDTO);
				} else {
					if (usuarioSesionDTO.getLlaveTabla().compareTo(token) != 0)
						usuarioSesionService.inactivar(usuarioSesionDTO, token);
				}
			}
		}
		return user;
		// END region cambiarClave
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioAutenticacionDTO guardar(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioAutenticacion_guardar
		dto.setFechaMaxima(getNewMaximunDate(dto.getUsuario()));
		return super.guardar(dto, token);
		// END UsuarioAutenticacion_guardar
	}

// BEGIN region aditionalMethods
	public void crearAutenticacion(String usuario, String token) throws ServerException {
		UsuarioAutenticacionFilterDTO filtro1 = new UsuarioAutenticacionFilterDTO();
		filtro1.setUsuario(usuario);
		filtro1.setEstado(SharedConstants.STATE_ACTIVE);
		if (consultaUnica(filtro1) == null) {
			UsuarioDTO user = usuarioService.consultaXId(usuario);
			UsuarioAutenticacionDTO aut = new UsuarioAutenticacionDTO();
			aut.setUsuario(usuario);
			aut.setClave(user.getIdentificacion());
			aut.setSesion(user.getIdentificacion());
			guardar(aut, token);
		}
	}

	public UsuarioDTO getUserSystem() throws ServerException {
		OrganizacionDTO principal = organizacionService.obtenerPrincipal();
		if (principal == null)
			throw new ServerException("No se tiene organizacion principal");
		if (principal.getUsuarioSystem() == null)
			throw new ServerException("No se tiene configurado el usuario sistem en la organizacion principal");
		UsuarioDTO usuarioSystem = usuarioService.consultaXId(principal.getUsuarioSystem());
		if (usuarioSystem == null || usuarioSystem.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El usuario sistema no exisste o esta inactivo");
		return usuarioSystem;
	}

	public UsuarioSesionDTO generateAdministratorToken() throws ServerException {
		UsuarioDTO usuarioSystem = getUserSystem();
		UsuarioSesionDTO sesion = new UsuarioSesionDTO();
		sesion.setFecha(new Date());
		// sesion.setFechaCierre(usuarioSesionService.getFechaCierre(usuarioSystem.getLlaveTabla()));
		sesion.setUsuario(usuarioSystem.getLlaveTabla());
		sesion = usuarioSesionService.save(sesion);
		return sesion;
	}

	public void logout(String token) throws ServerException {
		UsuarioSesionDTO sesion = usuarioSesionService.consultaXId(token);
		if (sesion == null)
			throw new ServerException("Token incorrecto");
		if (sesion.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0)
			throw new ServerException("Se encuentra inactiva la sesion");
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

	private void errorDesdeNuevaClave(UsuarioDTO dto, String ip, String error) throws ServerException {
		UsuarioAutenticacionFilterDTO uaf = new UsuarioAutenticacionFilterDTO();
		uaf.setClave(dto.getCorreo());
		uaf.setIp(ip);
		uaf.setSesion(dto.getIdentificacion());
		reportarError(uaf, error);
	}

	public void solicitarNuevaClave(UsuarioAutenticacionDTO dto) throws ServerException {
		if (dto == null || dto.getUsuarioDTO() == null)
			throw new ServerException("Faltan los datos de recuperacion");
		if (dto.getUsuarioDTO().getCorreo() == null)
			errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), "No se envio el correo de recuperacion");
		if (dto.getUsuarioDTO().getIdentificacion() == null)
			errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), "No se envio la identificacion del usuario");
		UsuarioFilterDTO filter = new UsuarioFilterDTO();
		filter.setIdentificacion(dto.getUsuarioDTO().getIdentificacion());
		UsuarioDTO usuario = usuarioService.consultaUnica(filter);
		if (usuario == null)
			errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(),
					"Revisa los datos de acceso. El numero de id no esta en la base de datos");
		if (usuario.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(),
					"Revisa los datos de acceso. El usuario se encuentra inactivo");
		if (usuario.getCorreo() == null)
			errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(),
					"No tienes correo registrado para enviarte la nueva clave");
		if (usuario.getCorreo().toLowerCase().compareTo(dto.getUsuarioDTO().getCorreo().toLowerCase()) != 0)
			errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(),
					"Revisa los datos de acceso. el correo electronico no es el mismo que tienes registrado");
		try {
			authorizationService.generarAutorizacion(usuario.getLlaveTabla(), usuario.getCorreo(), dto.getIp());
		} catch (Exception e) {
			errorDesdeNuevaClave(dto.getUsuarioDTO(), dto.getIp(), e.getMessage());
		}

	}

	// PAra el api de flex despues lo puedo quitar
	public UsuarioAutenticacionDTO autenticar(UsuarioAutenticacionFilterDTO dto, boolean fromApi)
			throws ServerException {
		long diasVigencia = 0;
		if (!fromApi) {
			if (dto.getClaveAnterior() == null)
				reportarError(dto, "Por favor actualice su version de software");
			String fechaMinima = null;
			try {
				fechaMinima = usuarioAutenticacionMapper.fechaMinima();
			} catch (Exception e) {
				reportarError(dto, e.getMessage());
			}
			if (fechaMinima != null) {
				if (dto.getClaveAnterior().length() > 13)
					dto.setClaveAnterior(dto.getClaveAnterior().substring(0, 13));
				int cliente = Integer.parseInt(dto.getClaveAnterior().replace(".", ""));
				int servidor = Integer.parseInt(fechaMinima.replace(".", ""));
				if (cliente < servidor)
					reportarError(dto,
							"Por favor actualice su version de software (Limpie cache o descargue una nueva app).\nCliente: "
									+ String.valueOf(cliente) + "\nServidor:" + String.valueOf(servidor));
			}
		}

		UsuarioAutenticacionDTO autenticacion = null;

		UsuarioSesionDTO sesion = null;
		if (dto.getSecurityToken() != null && dto.getClave() == null) {
			sesion = usuarioSesionService.consultaXId(dto.getSecurityToken());
			if (sesion == null)
				reportarError(dto, "Autenticacion incorrecta");
			if (sesion.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				reportarError(dto, "Se encuentra inactiva la sesion");
			if (sesion.getFechaCierre() != null && sesion.getFecha().compareTo(new Date()) > 0)
				reportarError(dto, "Usuario perdio autenticacion.");
			autenticacion = new UsuarioAutenticacionDTO();
			autenticacion.setUsuario(sesion.getUsuario());
		} else {
			if (dto.getClave() != null && dto.getSesion() == null && dto.getSecurityToken() != null) {
				autenticacion = consultaXId(dto.getClave());
			}
			if (autenticacion == null) {
				UsuarioAutenticacionFilterDTO autenticacionF = new UsuarioAutenticacionFilterDTO();
				if (dto.getSesion() == null || dto.getSesion().isEmpty())
					reportarError(dto, "La sesion no puede estar vacia");
				if (dto.getClave() == null || dto.getClave().isEmpty())
					reportarError(dto, "La clave no puede estar vacia");
				autenticacionF.setSesion(dto.getSesion());
				autenticacionF.setClave(dto.getClave());
				autenticacionF.setEstado(SharedConstants.STATE_ACTIVE);
				autenticacion = consultaUnica(autenticacionF);
			}
			if (autenticacion == null)
				reportarError(dto, "Autenticacion incorrecta");
			if (autenticacion.getFechaMaxima() != null && autenticacion.getFechaMaxima().compareTo(new Date()) < 0)
				reportarError(dto, "Por seguridad, es necesario actualizar la clave de acceso");
		}

		UsuarioDTO usuario = usuarioService.consultaXId(autenticacion.getUsuario());
		if (usuario.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			reportarError(dto, "El usuario no se encuentra activo");
		autenticacion.setUsuarioDTO(usuario);

		if (!fromApi) {
			autenticacion.setOrganizacion(organizacionService.obtenerPrincipalPropiedades(usuario.getLlaveTabla()));
			// autenticacion.setOrganizaciones(organizacionService.obtenerUsuario(autenticacion.getUsuario()));
		}

		if (sesion == null) {
			sesion = new UsuarioSesionDTO();
			sesion.setFecha(new Date());
			sesion.setFechaCierre(usuarioSesionService.getFechaCierre(autenticacion.getUsuario()));
			if (sesion.getFechaCierre() == null && autenticacion.getFechaMaxima() != null)
				sesion.setFechaCierre(autenticacion.getFechaMaxima());
			sesion.setUsuario(autenticacion.getUsuario());
			sesion.setIp(dto.getIp());
			sesion = usuarioSesionService.guardar(sesion, dto.getSecurityToken());
		}
		autenticacion.setToken(sesion.getLlaveTabla());

		if (!fromApi) {
			String fechaTrial = usuarioAutenticacionMapper.consultarValidez();
			if (fechaTrial == null)
				reportarError(dto, "El sistema no tiene configurada la fecha de la licencia");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = formatter.parse(fechaTrial);
				diasVigencia = (date.getTime() - new Date().getTime()) / (24 * 3600000);
				if (diasVigencia < 0)
					reportarError(dto, "Se ha vencido la licencia del sistema. " + fechaTrial);
			} catch (ParseException e) {
				reportarError(dto, "El formato de la fecha de licencia esta incorrecto");
			}

			if (diasVigencia >= 0 && diasVigencia <= 5
					&& usuarioAutenticacionMapper.ocultarLicencia(autenticacion.getUsuario()) == 0)
				autenticacion.setMensaje(
						"Quedan " + (diasVigencia + 1) + " dias para que se cumpla el periodo de su licencia");
			autenticacion
					.setTableroControl(usuarioAutenticacionMapper.cantidadAsignaciones(autenticacion.getUsuario()));

			ModuloFilterDTO filterMod = new ModuloFilterDTO();
			filterMod.setSecurityToken(sesion.getLlaveTabla());
			autenticacion.setModulos(modulosService.modulosUsuario(filterMod));
		}


		return autenticacion;
	}

	public UsuarioAutenticacionDTO checkToken(String token, String ip) throws ServerException {
		UsuarioSesionDTO sesion = usuarioSesionService.consultaXId(token);
		if (sesion == null || sesion.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0
				|| (sesion.getFechaCierre() != null && sesion.getFecha().compareTo(new Date()) > 0)) {
			UsuarioSesionErrorDTO use = new UsuarioSesionErrorDTO();
			use.setIp(ip);
			use.setFecha(new Date());
			use.setSesion(token);
			use.setError("Error validando Token");
			errorService.save(use);
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		}

		UsuarioAutenticacionDTO autenticacion = new UsuarioAutenticacionDTO();
		autenticacion.setToken(sesion.getLlaveTabla());
		autenticacion.setUsuario(sesion.getUsuario());
		return autenticacion;
	}

	private Date getNewMaximunDate(String user) throws ServerException {
		if(user ==null) throw new ServerException("No se identifica el usuario para calcular el tiempo de recuperacion de clave");
		
		String timeToNewPassword = usuarioAutenticacionMapper.timeToNewPassword(user);
		if (timeToNewPassword==null) {
			Calendar newDate = Calendar.getInstance();
			newDate.add(Calendar.MONTH, 2);
			return newDate.getTime();	
		} else {
			try {
				int days = Integer.parseInt(timeToNewPassword);
				if(days == 0) {
					return null;
				}
				Calendar newDate = Calendar.getInstance();
				newDate.add(Calendar.DAY_OF_MONTH, days);
				return newDate.getTime();
			} catch (NumberFormatException e) {
				throw new ServerException("Existe un error en la propiedad TIEMPO DE SOLICITAR NUEVA CLAVE, el valor no es numerico : " + timeToNewPassword);
			}
		}
		
	}

	@Override
	public SharedToken validate(String token, HttpServletRequest request) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		if (request == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		UsuarioAutenticacionDTO auth = checkToken(token, HttpUtils.getRequestIP(request));
		UsuarioDTO user = usuarioService.consultaXId(auth.getUsuario());
		if (user == null || user.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		SharedToken st = new SharedToken();
		st.setToken(token);
		st.setUser(user.getLlaveTabla());
		st.setUserId(user.getIdentificacion());
		st.setUserName(user.getNombre());
		return st;
	}

	@Override
	public String getUser(String token, HttpServletRequest request) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		if (request == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		return checkToken(token, HttpUtils.getRequestIP(request)).getUsuario();

	}
// END region aditionalMethods

}