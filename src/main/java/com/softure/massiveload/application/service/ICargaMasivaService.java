package com.softure.massiveload.application.service;

import java.util.List;

import com.softure.java.dto.exception.ServerException;

import com.softure.massiveload.domain.vo.CargaMasiva;
import com.softure.massiveload.domain.filter.CargaMasivaFilterDTO;

public interface ICargaMasivaService {
	
	int count(CargaMasivaFilterDTO filter) throws ServerException;

	CargaMasiva findById(String llave) throws ServerException;

	List<CargaMasiva> find(String token, CargaMasivaFilterDTO filter);

	CargaMasiva get(String token, CargaMasivaFilterDTO filter) throws ServerException;
	
	CargaMasiva save(String token, CargaMasiva dto) throws ServerException;

	CargaMasiva update(String token, CargaMasiva dto, String id) throws ServerException;

	CargaMasiva activate(String token, String id) throws ServerException;

	CargaMasiva inactivate(String token, String id) throws ServerException;
}
