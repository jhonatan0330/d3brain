package com.softure.java.application;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioSesionMapper;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.domain.IDataMapper;
import com.softure.java.domain.IDataObject;
import com.softure.java.domain.IDataObjectFilter;
import com.softure.java.dto.exception.ServerException;

public class ServiceGeneric<T extends IDataObject, TFilter extends IDataObjectFilter> {
	
	protected IDataMapper<T, TFilter> mapper;
	
	@Autowired private UsuarioSesionMapper usuarioSesionMapper;
	
	public T updateDB(String token, T dto) throws ServerException {
		getUserFlex(token);
		try {
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}

	public T findUnique(String token, TFilter dto) throws ServerException {
		T result = null;
		try {
			result = mapper.selectOne(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return result;
	}

	public T getDB(String llave) throws ServerException {
		throw new ServerException("Este metodo debe ser sobreescrito en cada servicio");
	}

	public int count(TFilter dto) throws ServerException {
		try {
			return mapper.count(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public T inactivateDB(String token, String id) throws ServerException {
		getUserFlex(token);
		T dto = getDB(id);
		if(dto==null) throw new ServerException("No se identifica el objeto a inactivar");
		if(dto.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		try {
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	public T activateDB(String token, String id) throws ServerException {
		getUserFlex(token);
		T dto = getDB(id);
		if(dto==null) throw new ServerException("No se identifica el objeto a Activar");
		if(dto.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0) throw new ServerException("Este objeto ya se encuentra Activo");
		dto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		try {
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	
	public T saveDB(String token, T dto) throws ServerException {
		getUserFlex(token);
		dto.setLlaveTabla(generarLlave());
		try {
			mapper.insert( dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		dto = getDB(dto.getLlaveTabla());
		return dto;
	}

	public List<T> listarConsulta(TFilter dto) throws ServerException {
		paginar(dto);
		try {
			return mapper.selectMany(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public String getUserFlex(String token) throws ServerException{
		if(token!=null){
			UsuarioSesionFilterDTO filter = new UsuarioSesionFilterDTO();
			filter.setLlaveTabla(token);
			UsuarioSesionDTO sesion = usuarioSesionMapper.consultar(filter);
			if(sesion==null) throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario" );
			if(sesion.getFechaCierre()!=null && sesion.getFechaCierre().compareTo(new Date())<0) throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario" );
			return sesion.getUsuario();
		}
		throw new ServerException("Usuario perdio autenticacion.\nCODE:caud_usuario" );
	}
	
	public void paginar(TFilter dto){
		if(dto.getPaginacionRegistroFinal()==0) dto.setPaginacionRegistroFinal(200);
	}
	
	public String generarLlave(){
		UUID uuid = UUID.randomUUID();
		String gen = uuid.toString();
		gen = gen.replaceAll("-", "");
		return gen;
	}
	
	
	public T inactivateDirect(T dto) throws ServerException {
		dto = getDB(dto.getLlaveTabla());
		if(dto==null) throw new ServerException("No se identifica el objeto a inactivar");
		if(dto.getEstado().compareTo(ConstantesGenerales.ESTADO_INACTIVO)==0) throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		try {
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	public T activateDirect(T dto) throws ServerException {
		dto = getDB(dto.getLlaveTabla());
		if(dto==null) throw new ServerException("No se identifica el objeto a Activar");
		if(dto.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)==0) throw new ServerException("Este objeto ya se encuentra Activo");
		dto.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		try {
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	public T saveDirect(T dto) throws ServerException {
		dto.setLlaveTabla(generarLlave());
		try {
			mapper.insert(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		dto = getDB(dto.getLlaveTabla());
		return dto;
	}
	
	public T updateDirect(T dto) throws ServerException {
		try {
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
}