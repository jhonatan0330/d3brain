package com.softure.shared.application;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;

import com.softure.java.dto.exception.ServerException;
import com.softure.shared.domain.SharedConstants;
import com.softure.shared.domain.SharedDataObject;
import com.softure.shared.domain.SharedDataObjectFilter;
import com.softure.shared.infrastructure.SharedCRUDMapperMybatis;

public class SharedCRUDService<T extends SharedDataObject, TFilter extends SharedDataObjectFilter> {
	
	protected SharedCRUDMapperMybatis<T, TFilter> mapper;
	
	public void update(T dto, String updateUser) throws ServerException {
		if(updateUser == null) throw new ServerException("Ingrese los datos del usuario que realiza los cambios");
		try {
			dto.setUpdatedAt(new Date());
			dto.setUpdatedUser(updateUser);
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public T updateAndFindById(T dto, String user) throws ServerException {
		update(dto, user);
		return findById(dto.getId());
	}

	public T findOne(TFilter dto) throws ServerException {
		T result = null;
		try {
			result = mapper.selectOne(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return result;
	}

	public T findById(String llave) throws ServerException {
		throw new ServerException("Este metodo debe ser sobreescrito en cada servicio");
	}

	public int count(TFilter dto) throws ServerException {
		try {
			return mapper.count(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public T delete(String idToInactivate, String user) throws ServerException {
		T dto = findById(idToInactivate);
		if(dto==null) throw new ServerException("No se identifica el objeto a inactivar");
		if(dto.getState().compareTo(SharedConstants.STATE_INACTIVE)==0) throw new ServerException("Este objeto ya se encuentra inactivo");
		dto.setState(SharedConstants.STATE_INACTIVE);
		try {
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	public T restore(String idToRestore, String token) throws ServerException {
		T dto = findById(idToRestore);
		if(dto==null) throw new ServerException("No se identifica el objeto a Activar");
		if(dto.getState().compareTo(SharedConstants.STATE_ACTIVE)==0) throw new ServerException("Este objeto ya se encuentra Activo");
		dto.setState(SharedConstants.STATE_ACTIVE);
		try {
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;
	}
	
	public String save(T dto) throws ServerException {
		if(dto.getCreatedUser() == null) throw new ServerException("Ingrese los datos del usuario que realiza el ingreso del registro");
		dto.setCreatedAt(new Date());
		dto.setId(generateIdUUID());
		try {
			mapper.insert(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto.getId();
	}
	
	public T saveAndFindById(T dto) throws ServerException {
		save(dto);
		return findById(dto.getId());
	}
	
	public List<T> findMany(TFilter dto) throws ServerException {
		pageFilter(dto);
		try {
			return mapper.selectMany(dto); 
		}catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public void pageFilter(TFilter dto){
		if(dto.getStartRow()==null) dto.setStartRow(0);
		if(dto.getEndRow()==null || dto.getEndRow()==0) dto.setEndRow(200);
	}
	
	public String generateIdUUID(){
		UUID uuid = UUID.randomUUID();
		String gen = uuid.toString();
		gen = gen.replaceAll("-", "");
		return gen;
	}
	
}