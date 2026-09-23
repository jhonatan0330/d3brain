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
import d3.authentication.domain.OrganizacionDTO;
import d3.authentication.domain.UsuarioSesionDTO;
import d3.authentication.domain.UsuarioSesionFilterDTO;
import d3.authentication.infrastructure.UsuarioSesionMapper;
import d3.configuration.application.PropertyGetWithCacheService;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.document.application.field.Propiedades;
import d3.shared.application.D3Utils;
import d3.shared.application.SessionContext;
import d3.shared.domain.SharedToken;
import d3.users.application.UsuarioSvc;
import d3.users.domain.UsuarioDTO;

@Service("usuarioSesionService")
public class UsuarioSesionSvc {

	private final UsuarioSesionMapper usuarioSesionMapper;
	private final PropertyGetWithCacheService getPropertyService;
	private final CacheManager cacheService;
	private final UsuarioSvc usuarioService;
	private final OrganizacionSvc organizacionService;

	public UsuarioSesionSvc(@Lazy UsuarioSesionMapper usuarioSesionMapper,
			@Lazy PropertyGetWithCacheService getPropertyService, @Lazy CacheManager cacheService,
			@Lazy UsuarioSvc usuarioService, @Lazy OrganizacionSvc organizacionService) {
		this.usuarioSesionMapper = usuarioSesionMapper;
		this.getPropertyService = getPropertyService;
		this.cacheService = cacheService;
		this.usuarioService = usuarioService;
		this.organizacionService = organizacionService;
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

	public String actualizarSesion() throws ServerException {
		UsuarioSesionDTO bd = consultaXId(SessionContext.getCurrentToken());
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
		return getUserToken(SessionContext.getCurrentToken()).getUser();
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
			OrganizacionDTO _org = cacheService.getMainOrganization();
			if (_org == null) {
				_org = organizacionService.obtenerPrincipal();
				cacheService.setMainOrganization(_org);
			}
			PropiedadDTO _prop = getPropertyService.obtenerPropiedad(PropiedadValorDefinidoDTO.ORGANIZACION,
					_org.getLlaveTabla(), Propiedades.APP_SESSION_TIME, pUser);
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

	public UsuarioSesionDTO checkToken() throws ServerException {
		SharedToken st = getSessionCache(SessionContext.getCurrentToken());
		UsuarioSesionDTO result = new UsuarioSesionDTO();
		result.setLlaveTabla(st.getToken());
		result.setUsuario(st.getUser());
		result.setFechaCierre(st.getFechaCierre());
		result.setPrivada(st.getPrivada());
		result.setEstado(SharedConstants.STATE_ACTIVE);
		return result;
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
		SharedToken st = cacheService.getSession(usuarioSystem);
		if (st == null) {
			UsuarioDTO user = usuarioService.consultaXId(usuarioSystem);
			st = new SharedToken();
			st.setToken(usuarioSystem);
			st.setUser(usuarioSystem);
			if (user != null) {
				st.setUserId(user.getIdentificacion());
				st.setUserName(user.getNombre());
			}
			st.setPrivada(true);
			cacheService.putSession(usuarioSystem, st);
		}
		UsuarioSesionDTO sesion = new UsuarioSesionDTO();
		sesion.setFecha(new Date());
		sesion.setUsuario(usuarioSystem);
		sesion.setPrivada(st.getPrivada());
		sesion.setLlaveTabla(st.getToken());
		sesion.setEstado(SharedConstants.STATE_ACTIVE);
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

	public SharedToken getUserToken(String token) throws ServerException {
		return getSessionCache(token);
	}

	private SharedToken getSessionCache(String token) throws ServerException {
		if (token == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		SharedToken cached = cacheService.getSession(token);
		if (cached != null) {
			if (cached.getFechaCierre() != null && cached.getFechaCierre().compareTo(new Date()) < 0) {
				cacheService.removeSession(token);
			} else {
				return cached;
			}
		}
		UsuarioSesionDTO sesion = getUserSession(token);
		if (sesion == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		if (sesion.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
			cacheService.removeSession(token);
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		}
		if (sesion.getFechaCierre() != null && sesion.getFechaCierre().compareTo(new Date()) < 0) {
			cacheService.removeSession(token);
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		}
		UsuarioDTO user = usuarioService.consultaXId(sesion.getUsuario());
		if (user == null || user.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		SharedToken st = new SharedToken();
		st.setToken(token);
		st.setUser(user.getLlaveTabla());
		st.setUserId(user.getIdentificacion());
		st.setUserName(user.getNombre());
		st.setFechaCierre(sesion.getFechaCierre());
		st.setPrivada(sesion.getPrivada());
		cacheService.putSession(token, st);
		return st;
	}

	public UsuarioSesionDTO getUserSession(String token) throws ServerException {
		try {
			UsuarioSesionFilterDTO filter = new UsuarioSesionFilterDTO();
			filter.setLlaveTabla(token);
			return usuarioSesionMapper.consultar(filter);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	// TEngo que mejorar el tema de las sesiones por el momento esta pausado
	public SharedToken getSessionCacheByUser(String userId) {
		if (userId == null || userId.isEmpty())
			return null;
		for (Map.Entry<String, SharedToken> entry : cacheService.getSessionMap().entrySet()) {
			SharedToken st = entry.getValue();
			if (st != null && userId.equals(st.getUser())) {
				if (st.getFechaCierre() != null && st.getFechaCierre().compareTo(new Date()) < 0) {
					cacheService.removeSession(entry.getKey());
				} else {
					return st;
				}
			}
		}
		return null;
	}

}