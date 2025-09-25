package com.softure.logisticpymes.application;

import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;
import com.softure.logisticpymes.infrastructure.UsuarioMapper;
import com.softure.notification.application.ActividadSvc;
import com.softure.property.application.PropertyCRUDSvc;

@Service("usuarioService")
public class UsuarioSvc extends BasicSvc<UsuarioDTO, UsuarioFilterDTO> {
	
	@Autowired @Lazy  private UsuarioMapper usuarioMapper;
	@Autowired @Lazy  private ActividadSvc actividadSvc;
	@Autowired @Lazy  private UsuarioAutenticacionSvc usuarioAutenticacionSvc;
	@Autowired @Lazy  private PropertyCRUDSvc propertySvc;

	@Override
	public UsuarioDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Usuario");
		UsuarioFilterDTO dto = new UsuarioFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = usuarioMapper;
	}
	
	@Override
	public UsuarioDTO activar(UsuarioDTO dto, String token) throws ServerException {
		// BEGIN Usuario_activar
		return super.activar(dto, token);
		// END Usuario_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioDTO actualizar( UsuarioDTO dto, String token) throws ServerException {
		// BEGIN Usuario_actualizar
		UsuarioDTO bd = consultaXId(dto.getLlaveTabla());
		//Cambio la clave en caso que el rol tenga credenciales
		if(bd.getIdentificacion().compareTo(dto.getIdentificacion())!=0){
			UsuarioAutenticacionFilterDTO autenticacionFilter = new UsuarioAutenticacionFilterDTO();
			autenticacionFilter.setUsuario(dto.getLlaveTabla());
			autenticacionFilter.setEstado(SharedConstants.STATE_ACTIVE);
			UsuarioAutenticacionDTO autenticacion = usuarioAutenticacionSvc.consultaUnica(autenticacionFilter);
			if(autenticacion!=null){
				if(autenticacion.getClave().compareTo(autenticacion.getSesion())==0)autenticacion.setClave(dto.getIdentificacion());
				autenticacion.setSesion(dto.getIdentificacion());
				usuarioAutenticacionSvc.actualizar(autenticacion, token);
			}
		}
		if(dto.getCorreo()!=null) dto.setCorreo(dto.getCorreo().toLowerCase());
		return super.actualizar(dto, token);
		// END Usuario_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public UsuarioDTO inactivar(UsuarioDTO dto, String token) throws ServerException {
		// BEGIN Usuario_inactivar
		dto = super.inactivar(dto, token);
		actividadSvc.validateActivitiesToInactivateUser(dto.getLlaveTabla());
		propertySvc.inactivateAllPropertiesOfUser(dto.getLlaveTabla(), token);
		UsuarioAutenticacionFilterDTO autenticacionFilter = new UsuarioAutenticacionFilterDTO();
		autenticacionFilter.setUsuario(dto.getLlaveTabla());
		autenticacionFilter.setEstado(SharedConstants.STATE_ACTIVE);
		List<UsuarioAutenticacionDTO> autenticaciones = usuarioAutenticacionSvc.listarConsulta(autenticacionFilter);
		for (UsuarioAutenticacionDTO autenticacion : autenticaciones) {
			autenticacion.setEstado(SharedConstants.STATE_INACTIVE);
			usuarioAutenticacionSvc.inactivar(autenticacion, token);
		}
		return dto;
		// END Usuario_inactivar
	}
	
	@Override
	public UsuarioDTO consultaUnica(UsuarioFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(UsuarioFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<UsuarioDTO> listarConsulta(UsuarioFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<UsuarioDTO> listarRol(UsuarioFilterDTO dto)throws ServerException{
		// BEGIN region listarRol
		if(dto.getRol()==null) {
			if(dto.getEstado()==null) dto.setEstado(SharedConstants.STATE_ACTIVE);
			return listarConsulta(dto);
		}
		// END region listarRol
		paginar(dto);
		try {
			return usuarioMapper.listarRol(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	public UsuarioDTO guardar(UsuarioDTO dto, String token) throws ServerException {
		UsuarioFilterDTO filtro  = new UsuarioFilterDTO();
		filtro.setIdentificacion(dto.getIdentificacion());
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		if(contarResultados(filtro)!=0) throw new ServerException("Ya existe ese ID en la BD y esta activo.\n Id : " + dto.getIdentificacion());
		if(dto.getImagen()==null) dto.setImagen(SharedConstants.AVATAR);
		if(dto.getCorreo()!=null) dto.setCorreo(dto.getCorreo().toLowerCase());
		return super.guardar(dto, token);
	}

	public List<UsuarioDTO> getUsersState(String document)throws ServerException{
		return usuarioMapper.getUsersState(document);
	}
	
	public UsuarioDTO changePicture(String url, String token) throws ServerException {
		UsuarioDTO bd = consultaXId(getUserFlex(token));
		bd.setImagen(url);
		return update(bd);
	}
	
	public UsuarioDTO getUserByDocument(String pDocument) throws ServerException {
		return usuarioMapper.getUserByDocument(pDocument);
	}

}