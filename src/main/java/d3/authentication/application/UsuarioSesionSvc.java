package d3.authentication.application;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.binding.BindingException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.CacheManager;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.authentication.domain.UsuarioSesionDTO;
import d3.authentication.domain.UsuarioSesionFilterDTO;
import d3.authentication.infrastructure.UsuarioSesionMapper;
import d3.document.application.field.Propiedades;
import d3.shared.application.D3Utils;
import d3.property.application.PropertyGetWithCacheService;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;

@Service("usuarioSesionService")
public class UsuarioSesionSvc {

	private final UsuarioSesionMapper usuarioSesionMapper;
	private final PropertyGetWithCacheService getPropertyService;
	private final CacheManager cacheService;

	public UsuarioSesionSvc(@Lazy UsuarioSesionMapper usuarioSesionMapper,
			@Lazy PropertyGetWithCacheService getPropertyService, @Lazy CacheManager cacheService) {
		this.usuarioSesionMapper = usuarioSesionMapper;
		this.getPropertyService = getPropertyService;
		this.cacheService = cacheService;
	}

	public UsuarioSesionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. UsuarioSesion");
		UsuarioSesionFilterDTO dto = new UsuarioSesionFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioSesionMapper.consultar(dto);
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioSesionDTO guardar(UsuarioSesionDTO dto) throws ServerException {

		dto.setFecha(new Date());
		dto.setFechaCierre(getFechaCierre(dto.getUsuario()));

		dto.setLlaveTabla(D3Utils.generarLlave());
		dto.setEstado(SharedConstants.STATE_ACTIVE);
		try {
			usuarioSesionMapper.insertar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public String actualizarSesion(String token) throws ServerException {
		UsuarioSesionDTO bd = consultaXId(token);
		if (bd != null) {
			int tiempo = getUserSessionTime(bd.getUsuario());
			if (tiempo != 0) {
				bd.setFechaCierre(new Date(new Date().getTime() + (tiempo * 60 * 1000)));
				try {
					usuarioSesionMapper.actualizar(bd);
				} catch (Exception e) {
					throw new ServerException(e.getCause().getMessage());
				}
			}
			return bd.getUsuario();
		}
		return getUserFlex(token);
	}

	public Date getFechaCierre(String usuario) throws ServerException {
		int tiempo = getUserSessionTime(usuario);
		if (tiempo != 0) {
			return new Date(new Date().getTime() + (tiempo * 60 * 1000));
		}
		return null;
	}

	private int getUserSessionTime(String pUser) throws ServerException {
		Integer _time = cacheService.getSessionTime(pUser);
		if (_time == null) {
			if (cacheService.getMainOrganization() == null)
				cacheService.setMainOrganization(usuarioSesionMapper.obtenerOrganizacion());
			PropiedadDTO _prop = getPropertyService.obtenerPropiedad(PropiedadValorDefinidoDTO.ORGANIZACION,
					cacheService.getMainOrganization(), Propiedades.APP_SESSION_TIME, pUser);
			if (_prop == null) {
				_time = 0;
			} else {
				try {
					_time = Integer.parseInt(_prop.getValor());
				} catch (NumberFormatException e) {
					_time = 0;
				}
			}
			cacheService.putSessionTime(pUser, _time);
		}
		return _time;
	}

	public UsuarioSesionDTO checkToken(String token) throws ServerException {
		UsuarioSesionDTO result = getSessionCache(token);
		if (result == null)
			result = consultaXId(token);
		if (result != null && result.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0
				&& (result.getFechaCierre() == null || result.getFechaCierre().getTime() > new Date().getTime())) {
			return result;
		}
		return null;

	}

	public void closeAllSession(String userId, String token) throws ServerException {
		try {
			usuarioSesionMapper.closeAllSession(userId, token);
			cacheService.clearSessionMap();
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public String getUserSystemKey() throws ServerException {
		if (cacheService.getMainUser() != null)
			return cacheService.getMainUser();
		try {
			cacheService.setMainUser(usuarioSesionMapper.obtenerPrincipal());
			if (cacheService.getMainUser() == null)
				throw new ServerException(
						"Revisa la organizacion principal, revisa el usuario system y tambien valida que el usuario se encuentre activo");
			return cacheService.getMainUser();
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public String getUserSystemMail() throws ServerException {
		if (cacheService.getMainUserMail() != null)
			return cacheService.getMainUserMail();
		try {
			cacheService.setMainUserMail(usuarioSesionMapper.obtenerPrincipalMail());
			if (cacheService.getMainUserMail() == null)
				throw new ServerException(
						"Revisa la organizacion principal, revisa el usuario system y tambien valida que el usuario se encuentre activo");
			return cacheService.getMainUserMail();
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public UsuarioSesionDTO generateAdministratorToken() throws ServerException {
		String usuarioSystem = getUserSystemKey();
		UsuarioSesionDTO sesion = cacheService.getSession(usuarioSystem);
		if (sesion != null)
			return sesion;

		sesion = new UsuarioSesionDTO();
		sesion.setFecha(new Date());
		sesion.setUsuario(usuarioSystem);
		sesion.setPrivada(true);
		// Aqui la idea es no guardar en base de datos la clave del administrador
		sesion.setLlaveTabla(usuarioSystem);
		sesion.setEstado(SharedConstants.STATE_ACTIVE);
		cacheService.putSession(sesion.getLlaveTabla(), sesion);
		// sesion = usuarioSesionService.save(sesion);
		return sesion;
	}

	public String getTokenPublic(String userId, String ip) throws ServerException {
		UsuarioSesionDTO sesion = new UsuarioSesionDTO();
		sesion.setUsuario(userId);
		sesion.setIp(ip);
		sesion = guardar(sesion);
		return sesion.getLlaveTabla();
	}

	public void logout(String token) throws ServerException {
		UsuarioSesionDTO sesion = consultaXId(token);
		if (sesion == null)
			throw new ServerException("Token incorrecto");
		if (sesion.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0)
			throw new ServerException("Se encuentra inactiva la sesion");
		sesion.setFechaCierre(new Date());
		try {
			usuarioSesionMapper.actualizar(sesion);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		cacheService.getSessionMap().remove(token);
	}

	public String getUserFlex(String token) throws ServerException {
		UsuarioSesionDTO sesion = getSessionCache(token);
		return sesion.getUsuario();
	}

	public boolean isPublicToken(String token) throws ServerException {
		UsuarioSesionDTO sesion = getSessionCache(token);
		return !sesion.getPrivada();
	}

	private UsuarioSesionDTO getSessionCache(String token) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		UsuarioSesionDTO sesion = cacheService.getSession(token);
		if (sesion == null)
			sesion = getUserSession(token);
		if (sesion == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		if (sesion.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
			cacheService.getSessionMap().remove(token);
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		}
		if (sesion.getFechaCierre() != null && sesion.getFechaCierre().compareTo(new Date()) < 0) {
			cacheService.getSessionMap().remove(token);
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		}
		return sesion;
	}

	public UsuarioSesionDTO getUserSession(String token) throws ServerException {
		try {
			UsuarioSesionFilterDTO filter = new UsuarioSesionFilterDTO();
			filter.setLlaveTabla(token);
			UsuarioSesionDTO _sesion = usuarioSesionMapper.consultar(filter);
			cacheService.putSession(token, _sesion);
			return _sesion;
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	// TEngo que mejorar el tema de las sesiones por el momento esta pausado
	public UsuarioSesionDTO getSessionCacheByUser(String userId) {
		if (userId == null || userId.isEmpty())
			return null;
		for (Map.Entry<String, UsuarioSesionDTO> entry : cacheService.getSessionMap().entrySet()) {
			UsuarioSesionDTO dto = entry.getValue();
			if (dto != null && userId.equals(dto.getUsuario())) {
				if (dto.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0
						|| (dto.getFechaCierre() != null && dto.getFechaCierre().compareTo(new Date()) < 0)) {
					cacheService.removeSession(entry.getKey());
				} else {
					return dto;
				}
			}
		}
		return null;
	}

}