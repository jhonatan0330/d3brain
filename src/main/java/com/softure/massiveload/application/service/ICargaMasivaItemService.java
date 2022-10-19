package com.softure.massiveload.application.service;

import java.util.List;

import com.softure.java.dto.exception.ServerException;

import com.softure.massiveload.domain.vo.CargaMasivaItem;
import com.softure.massiveload.domain.filter.CargaMasivaItemFilterDTO;

public interface ICargaMasivaItemService {
	
	int count(CargaMasivaItemFilterDTO filter) throws ServerException;

	CargaMasivaItem findById(String llave) throws ServerException;

	List<CargaMasivaItem> find(String token, CargaMasivaItemFilterDTO filter);

	CargaMasivaItem get(String token, CargaMasivaItemFilterDTO filter) throws ServerException;
	
	CargaMasivaItem save(String token, CargaMasivaItem dto) throws ServerException;

	CargaMasivaItem update(String token, CargaMasivaItem dto, String id) throws ServerException;

	CargaMasivaItem activate(String token, String id) throws ServerException;

	CargaMasivaItem inactivate(String token, String id) throws ServerException;
}
