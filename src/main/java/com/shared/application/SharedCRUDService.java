package com.shared.application;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.shared.domain.SharedDataObject;
import com.shared.domain.SharedDataObjectFilter;
import com.shared.infrastructure.SharedCRUDMapperMybatis;

public class SharedCRUDService<T extends SharedDataObject, TFilter extends SharedDataObjectFilter> {
	
	protected SharedCRUDMapperMybatis<T, TFilter> mapper;
	
	public void update(T dto) throws ServerException {
		try {
			mapper.update(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public T updateAndFindById(T dto) throws ServerException {
		update(dto);
		return findById(dto.getKey());
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
		dto.setKey(generateIdUUID());
		try {
			mapper.insert(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto.getKey();
	}
	
	public T saveAndFindById(T dto) throws ServerException {
		save(dto);
		return findById(dto.getKey());
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