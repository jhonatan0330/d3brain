package com.softure.authentication.application;

// BEGIN region interImport
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioSesionMapper;

@Service("usuarioSesionService")
public class UsuarioSesionSvc {
	
	@Autowired @Lazy 
	private UsuarioSesionMapper usuarioSesionMapper;

	private Map<String, UsuarioSesionDTO> sessionMap = new LinkedHashMap<String, UsuarioSesionDTO>(2000, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		@Override
	    protected boolean removeEldestEntry(Map.Entry<String, UsuarioSesionDTO> eldest) {
	        return size() > 2000;
	    }
	};

	public UsuarioSesionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. UsuarioSesion");
		UsuarioSesionFilterDTO dto = new UsuarioSesionFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioSesionMapper.consultar(dto);
	}


/*	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioSesionDTO actualizar( UsuarioSesionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesion_actualizar
		return super.actualizar(dto, token);
		// END UsuarioSesion_actualizar
	}*/
	
/*	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioSesionDTO inactivar(UsuarioSesionDTO dto, String token) throws ServerException {
		// BEGIN UsuarioSesion_inactivar
		dto.setFechaCierre(new Date());
		return super.inactivar(dto, token);
		// END UsuarioSesion_inactivar
	}*/
	
/*	public UsuarioSesionDTO consultaUnica(UsuarioSesionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}*/
	
/*	public int contarResultados(UsuarioSesionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}*/
	
/*	public List<UsuarioSesionDTO> listarConsulta(UsuarioSesionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}*/
	

	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioSesionDTO guardar(UsuarioSesionDTO dto) throws ServerException {
		dto.setLlaveTabla(generarLlave());
		try {
			usuarioSesionMapper.insertar(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public String actualizarSesion(String token) throws ServerException {
		UsuarioSesionFilterDTO bdFilter = new UsuarioSesionFilterDTO();
		bdFilter.setSecurityToken(token);
		bdFilter.setUsuario(getUserFlex(token));
		int tiempo = usuarioSesionMapper.tiempoSesion(bdFilter.getUsuario());
		if(tiempo!=0) {
			UsuarioSesionDTO bd = consultaXId(token);
			bd.setFechaCierre(new Date(new Date().getTime() + (tiempo *60 * 1000)));
			try {
				usuarioSesionMapper.actualizar(bd);
			} catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
			return bd.getUsuario();
		}
		return bdFilter.getUsuario();
	}
	
	public Date getFechaCierre(String usuario) throws ServerException {
		int tiempo = usuarioSesionMapper.tiempoSesion(usuario);
		if(tiempo!=0) {
			return new Date(new Date().getTime() + (tiempo *60 * 1000));
		}
		return null;
	}
	
	public UsuarioSesionDTO checkToken(String token)throws ServerException{
		UsuarioSesionDTO result = getSessionCache(token);
		if(result ==null) result = consultaXId(token);
		if(result !=null && result.getEstado().compareTo(SharedConstants.STATE_ACTIVE)==0  
				&& (result.getFechaCierre()== null || result.getFechaCierre().getTime() > new Date().getTime())) {
			return result;
		}
		return null;	
		
	}
	public void closeAllSession (String userId, String token) throws ServerException {
		try {
			usuarioSesionMapper.closeAllSession(userId, token);
			sessionMap.clear();
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public String getUserSystemKey() throws ServerException {
		try {
			String _key = usuarioSesionMapper.obtenerPrincipal();
			if (_key == null)
				throw new ServerException("Revisa la organizacion principal, revisa el usuario system y tambien valida que el usuario se encuentre activo");
			return _key;
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public String getUserSystemMail() throws ServerException {
		try {
			String _key = usuarioSesionMapper.obtenerPrincipalMail();
			if (_key == null)
				throw new ServerException("Revisa la organizacion principal, revisa el usuario system y tambien valida que el usuario se encuentre activo");
			return _key;
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public UsuarioSesionDTO generateAdministratorToken() throws ServerException {
		String usuarioSystem = getUserSystemKey();
		UsuarioSesionDTO sesion = sessionMap.get(usuarioSystem);
		if(sesion != null) return sesion;
		
		sesion = new UsuarioSesionDTO();
		sesion.setFecha(new Date());
		sesion.setUsuario(usuarioSystem);
		sesion.setPrivada(true);
		//Aqui la idea es no guardar en base de datos la clave del administrador
		sesion.setLlaveTabla(usuarioSystem);
		sessionMap.put(sesion.getLlaveTabla(), sesion);
		//sesion = usuarioSesionService.save(sesion);
		return sesion;
	}
	

	public String getTokenPublic(String userId, String ip) throws ServerException{
		UsuarioSesionDTO sesion = new UsuarioSesionDTO();
		sesion.setFecha(new Date());
		sesion.setFechaCierre(getFechaCierre(userId));
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
		sessionMap.remove(token);
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
		UsuarioSesionDTO sesion = sessionMap.get(token);
		if(sesion == null) {
			UsuarioSesionFilterDTO filter = new UsuarioSesionFilterDTO();
			filter.setLlaveTabla(token);
			try {
				sesion = usuarioSesionMapper.consultar(filter);
				sessionMap.put(token, sesion);
			} catch (BindingException ex) {
				throw new ServerException(ex.getMessage());
			} catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
		}
		if (sesion == null)
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		if (sesion.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
			sessionMap.remove(token);
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		}
		if (sesion.getFechaCierre() != null && sesion.getFechaCierre().compareTo(new Date()) < 0) {
			sessionMap.remove(token);			
			throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario");
		}
		return sesion;
	}
	
	//Copiado de basicsvc
	private String generarLlave() {
		UUID uuid = UUID.randomUUID();
		String gen = uuid.toString();
		gen = gen.replaceAll("-", "");
		return gen;
	}
}