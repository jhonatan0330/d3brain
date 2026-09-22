package d3.authentication.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.shared.domain.SharedToken;
import d3.authentication.domain.OrganizacionDTO;
import d3.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import d3.authentication.domain.UsuarioAutenticacionDTO;
import d3.authentication.domain.UsuarioAutenticacionFilterDTO;
import d3.authentication.domain.UsuarioSesionDTO;
import d3.authentication.domain.UsuarioSesionErrorDTO;
import d3.authentication.infrastructure.UsuarioAutenticacionMapper;
import d3.configuration.domain.PropiedadDTO;
import d3.document.application.field.Propiedades;
import d3.shared.application.BasicSvc;
import d3.shared.application.SessionContext;
import d3.users.application.UsuarioSvc;
import d3.users.domain.UsuarioDTO;
import d3.users.domain.UsuarioFilterDTO;
import jakarta.annotation.PostConstruct;

@Service("usuarioAutenticacionService")
public class UsuarioAutenticacionSvc extends BasicSvc<UsuarioAutenticacionDTO, UsuarioAutenticacionFilterDTO> {

	private final UsuarioAutenticacionMapper usuarioAutenticacionMapper;
	private final UsuarioAutenticacionAutorizacionSvc authorizationService;
	private final OrganizacionSvc organizacionService;
	private final UsuarioSesionSvc usuarioSesionService;
	private final UsuarioSvc usuarioService;
	private final UsuarioSesionErrorSvc errorService;

	public UsuarioAutenticacionSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy UsuarioAutenticacionMapper usuarioAutenticacionMapper,
			@Lazy UsuarioAutenticacionAutorizacionSvc authorizationService, @Lazy OrganizacionSvc organizacionService,
			@Lazy UsuarioSvc usuarioService, @Lazy UsuarioSesionErrorSvc errorService) {
		this.usuarioAutenticacionMapper = usuarioAutenticacionMapper;
		this.authorizationService = authorizationService;
		this.organizacionService = organizacionService;
		this.usuarioSesionService = usuarioSesionService;
		this.usuarioService = usuarioService;
		this.errorService = errorService;
	}

	// Cache
	private String fechaMinima;

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

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioAutenticacionDTO cambiarClave(UsuarioAutenticacionDTO dto, String token) throws ServerException {
		UsuarioAutenticacionAutorizacionDTO autho = null;
		UsuarioAutenticacionDTO user = null;
		// Se envia por el administrador por el momento solo flex
		/*
		 * if (dto.getClaveAnterior().compareTo("ADMIN$123") == 0) {
		 * UsuarioAutenticacionFilterDTO filtro = new UsuarioAutenticacionFilterDTO();
		 * filtro.setEstado(SharedConstants.STATE_ACTIVE);
		 * filtro.setUsuario(dto.getUsuario()); user = consultaUnica(filtro); if (user
		 * == null) throw new ServerException("El usuario no tiene una autenticacion");
		 * if (user.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0) throw new
		 * ServerException(
		 * "Por favor consulte con su administrador, sus credenciales se encuentran inactivas"
		 * ); // if(user.getClave().compareTo(dto.getClaveAnterior())!=0) throw new //
		 * ServerException("No concuerda la clave anterior");
		 * 
		 * } else {
		 */
		// La validacion de la autorizacion la retiro para que funcione flex
		if (dto.getLlaveTabla() != null)
			autho = authorizationService.validateLink(dto.getLlaveTabla(), dto.getIp());
		UsuarioAutenticacionFilterDTO filtro = new UsuarioAutenticacionFilterDTO();
		if (autho != null) {
			filtro.setUsuario(autho.getUsuario());
		} else {
			filtro.setUsuario(SessionContext.getCurrentUser());
		}
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		user = consultaUnica(filtro);
		if (autho == null && user.getClave().compareTo(dto.getClaveAnterior()) != 0)
			throw new ServerException("No concuerda la clave anterior");
		// }

		if (autho != null)
			user.setAutorizacionElimina(autho.getLlaveTabla());
		user.setEstado(SharedConstants.STATE_INACTIVE);
		user = update(user);

		UsuarioAutenticacionFilterDTO filterPassword = new UsuarioAutenticacionFilterDTO();
		filterPassword.setUsuario(user.getUsuario());
		filterPassword.setClave(dto.getClave());
		filterPassword.setFechaCreacionMin(null);

		List<UsuarioAutenticacionDTO> repeatPassword = listarConsulta(filterPassword);

		if (repeatPassword != null && !repeatPassword.isEmpty())
			throw new ServerException(
					"La clave que estas usando ya la habias usado y la cambiaste. Por seguridad no vuelvas a usar las mismas claves");

		UsuarioAutenticacionDTO newAuth = new UsuarioAutenticacionDTO();
		newAuth.setUsuario(user.getUsuario());
		newAuth.setClave(dto.getClave());
		newAuth.setSesion(user.getSesion());
		if (autho != null)
			newAuth.setAutorizacionCrea(autho.getLlaveTabla());
		newAuth.setIp(dto.getIp());
		newAuth.setFechaMaxima(getNewMaximunDate(user.getUsuario()));
		newAuth.setFechaCreacion(new Date());
		newAuth = save(newAuth);

		usuarioSesionService.closeAllSession(user.getUsuario(), token);
		return user;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioAutenticacionDTO guardar(UsuarioAutenticacionDTO dto) throws ServerException {
		dto.setFechaMaxima(getNewMaximunDate(dto.getUsuario()));
		dto.setFechaCreacion(new Date());
		return super.guardar(dto);
	}

	public void crearAutenticacion(String usuario) throws ServerException {
		UsuarioAutenticacionFilterDTO filtro1 = new UsuarioAutenticacionFilterDTO();
		filtro1.setUsuario(usuario);
		filtro1.setEstado(SharedConstants.STATE_ACTIVE);
		if (consultaUnica(filtro1) == null) {
			UsuarioDTO user = usuarioService.consultaXId(usuario);
			UsuarioAutenticacionDTO aut = new UsuarioAutenticacionDTO();
			aut.setUsuario(usuario);
			aut.setClave(user.getIdentificacion());
			aut.setSesion(user.getIdentificacion());
			guardar(aut);
		}
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
		errorService.saveSimple(use);
		throw new ServerException(error);
	}

	private void errorDesdeNuevaClave(String correo, String id, String ip, String error) throws ServerException {
		UsuarioAutenticacionFilterDTO uaf = new UsuarioAutenticacionFilterDTO();
		uaf.setClave(correo);
		uaf.setIp(ip);
		uaf.setSesion(id);
		reportarError(uaf, error);
	}

	public UsuarioAutenticacionAutorizacionDTO solicitarNuevaClave(String correo, String id, String ip,  String urlServer)
			throws ServerException {
		if (correo == null)
			errorDesdeNuevaClave(correo, id, ip, "No se envio el correo de recuperacion");
		if (id == null)
			errorDesdeNuevaClave(correo, id, ip, "No se envio la identificacion del usuario");
		UsuarioFilterDTO filter = new UsuarioFilterDTO();
		filter.setIdentificacion(id);
		UsuarioDTO usuario = usuarioService.consultaUnica(filter);
		if (usuario == null) {
			errorDesdeNuevaClave(correo, id, ip,
					"Revisa los datos de acceso. El numero de id no esta en la base de datos");			
		} else {
			if (usuario.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				errorDesdeNuevaClave(correo, id, ip,
						"Revisa los datos de acceso. El usuario se encuentra inactivo");
			if (usuario.getCorreo() == null)
				errorDesdeNuevaClave(correo, id, ip,
						"No tienes correo registrado para enviarte la nueva clave");
			if (correo !=null && usuario.getCorreo().toLowerCase().compareTo(correo.toLowerCase()) != 0)
				errorDesdeNuevaClave(correo, id, ip,
						"Revisa los datos de acceso. el correo electronico no es el mismo que tienes registrado");
			try {
				return authorizationService.makeTokenLink(usuario.getLlaveTabla(), usuario.getCorreo(), ip,
						urlServer);
			} catch (Exception e) {
				errorDesdeNuevaClave(correo, id, ip, e.getMessage());
			}	
		}
		

		return null;
	}

	// PAra el api de flex despues lo puedo quitar
	public UsuarioAutenticacionDTO autenticar(UsuarioAutenticacionFilterDTO dto, boolean fromApi, String urlServer)
			throws ServerException {
		long diasVigencia = 0;
		if (!fromApi) {
			if (dto.getClaveAnterior() == null)
				reportarError(dto, "Por favor actualice su version de software");
			if (fechaMinima == null) {
				try {
					fechaMinima = usuarioAutenticacionMapper.fechaMinima();
				} catch (Exception e) {
					reportarError(dto, e.getMessage());
				}
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
		if (SessionContext.getCurrentTokenOrNull() != null && dto.getClave() == null) {

			sesion = usuarioSesionService.getUserSession(SessionContext.getCurrentToken());
			if (sesion == null)
				reportarError(dto, "Autenticacion incorrecta por token");
			if (sesion != null && sesion.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				reportarError(dto, "Se encuentra inactiva la sesion");
			if (sesion != null && sesion.getFechaCierre() != null && sesion.getFecha().compareTo(new Date()) > 0)
				reportarError(dto, "Usuario perdio autenticacion por tiempo.");
			autenticacion = new UsuarioAutenticacionDTO();
			autenticacion.setUsuario((sesion != null) ? sesion.getUsuario() : null);
		} else {
			if (dto.getClave() != null && dto.getSesion() == null && SessionContext.getCurrentTokenOrNull() != null) {
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
			if (autenticacion != null && autenticacion.getFechaMaxima() != null) {
				if (((autenticacion.getFechaMaxima().getTime() - new Date().getTime()) / (24 * 3600000)) <= -7) {
					reportarError(dto, "Por seguridad, es necesario actualizar la clave de acceso");
				}
			}
		}

		UsuarioDTO usuario = usuarioService.consultaXId((autenticacion != null) ? autenticacion.getUsuario() : null);
		if (usuario.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			reportarError(dto, "El usuario no se encuentra activo");
		
		OrganizacionDTO organizacion = null;
		if (!fromApi) {
			organizacion = organizacionService.obtenerPrincipalPropiedades(usuario.getLlaveTabla());
		} else {
			SharedToken cachedSession = usuarioSesionService.getSessionCacheByUser(usuario.getLlaveTabla());
			if (cachedSession != null) {
				sesion = new UsuarioSesionDTO();
				sesion.setLlaveTabla(cachedSession.getToken());
				sesion.setUsuario(cachedSession.getUser());
				sesion.setFechaCierre(cachedSession.getFechaCierre());
				sesion.setPrivada(cachedSession.getPrivada());
				sesion.setEstado(SharedConstants.STATE_ACTIVE);
			}
		}
		if (sesion == null) {
			sesion = new UsuarioSesionDTO();

			if (sesion.getFechaCierre() == null && autenticacion != null && autenticacion.getFechaMaxima() != null)
				sesion.setFechaCierre(autenticacion.getFechaMaxima());
			sesion.setUsuario(usuario.getLlaveTabla());
			sesion.setIp(dto.getIp());
			sesion.setPrivada(true);
			sesion = usuarioSesionService.guardar(sesion);
			if (organizacion != null) {
				if (Propiedades.obtenerParametro(organizacion, Propiedades.APP_DFA) != null) {
					// Mientras terminamos lo del flex
					if (dto.getIp() != null)
						authorizationService.makeTokenNumber(usuario.getLlaveTabla(), usuario.getCorreo(), dto.getIp(),
								urlServer);
				}
			}
		}
		if (autenticacion != null)
			autenticacion.setToken(sesion.getLlaveTabla());

		if (!fromApi) {

			PropiedadDTO _propLicence = Propiedades.obtenerParametro(organizacion,	Propiedades.OCULTAR_MENSAJE_LICENCIA);
			if (_propLicence == null) {
				String fechaTrial = usuarioAutenticacionMapper.consultarValidez();
				if (fechaTrial == null)
					reportarError(dto, "El sistema no tiene configurada la fecha de la licencia");

				/*
				 * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); try { Date
				 * date = formatter.parse(fechaTrial); diasVigencia = (date.getTime() - new
				 * Date().getTime()) / (24 * 3600000); if (diasVigencia < 0) reportarError(dto,
				 * "Se ha vencido la licencia del sistema. " + fechaTrial); } catch
				 * (ParseException e) { reportarError(dto,
				 * "El formato de la fecha de licencia esta incorrecto"); }
				 */

				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

					// Fecha con vencimiento a las 12:00 PM
					LocalDateTime fechaVencimiento = LocalDate.parse(fechaTrial, formatter).atTime(12, 0); // 12:00 PM

					long millisVencimiento = fechaVencimiento.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

					diasVigencia = (millisVencimiento - System.currentTimeMillis()) / (24 * 3600000);

				} catch (Exception e) {
					reportarError(dto, "El formato de la fecha de licencia está incorrecto");
				}

				if (diasVigencia < 0)
					reportarError(dto, "Se ha vencido la licencia del sistema. " + fechaTrial);

				if (diasVigencia >= 0 && diasVigencia <= 5)
					autenticacion.setMensaje(
							"Quedan " + (diasVigencia + 1) + " dias para que se cumpla el periodo de su licencia");
			}
			// Esto lo uso en la app mobile la idea es cambiarlo
			// autenticacion
			// .setTableroControl(usuarioAutenticacionMapper.cantidadAsignaciones(autenticacion.getUsuario()));
		}
		return autenticacion;
	}

	public UsuarioAutenticacionDTO checkToken(String ip) throws ServerException {
		UsuarioSesionDTO sesion = usuarioSesionService.checkToken();
		if (sesion == null || sesion.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0
				|| (sesion.getFechaCierre() != null && sesion.getFecha() != null
						&& sesion.getFecha().compareTo(new Date()) > 0)) {
			UsuarioSesionErrorDTO use = new UsuarioSesionErrorDTO();
			use.setIp(ip);
			use.setFecha(new Date());
			use.setSesion(SessionContext.getCurrentToken());
			use.setError("Error validando Token");
			errorService.saveSimple(use);
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		}

		UsuarioAutenticacionDTO autenticacion = new UsuarioAutenticacionDTO();
		autenticacion.setToken(sesion.getLlaveTabla());
		autenticacion.setUsuario(sesion.getUsuario());
		return autenticacion;
	}

	private Date getNewMaximunDate(String user) throws ServerException {
		if (user == null)
			throw new ServerException("No se identifica el usuario para calcular el tiempo de recuperacion de clave");
		OrganizacionDTO org = organizacionService.obtenerPrincipalPropiedades(user);
		String timeToNewPassword = Propiedades.obtenerValor(org, Propiedades.TIEMPO_NUEVA_CLAVE);
		if (timeToNewPassword == null || timeToNewPassword.isEmpty()) {
			Calendar newDate = Calendar.getInstance();
			newDate.add(Calendar.MONTH, 2);
			return newDate.getTime();
		}

		try {
			int days = Integer.parseInt(timeToNewPassword);
			if (days == 0) {
				return null;
			}
			Calendar newDate = Calendar.getInstance();
			newDate.add(Calendar.DAY_OF_MONTH, days);
			return newDate.getTime();
		} catch (NumberFormatException e) {
			throw new ServerException(
					"Existe un error en la propiedad TIEMPO DE SOLICITAR NUEVA CLAVE, el valor no es numerico : "
							+ timeToNewPassword + e.getMessage());
		}

	}

	public void dobleFactorAutenticacion(String pUser, String pCode, String pIP) throws ServerException {
		authorizationService.validateToken(pUser, pCode, pIP);
	}

}