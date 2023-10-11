package com.accounting.plan.application;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.infrastructure.CreateCatalogTablesMapper;
import com.softure.java.dto.exception.ServerException;

@Service("CreateCatalogTablesAccountingService")
public class CreateCatalogTablesService {

	@Autowired
	private CreateCatalogTablesMapper mapper;

	public void createTemporal(String code)throws ServerException {
		try {
			mapper.createTemporal(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public void createPuntual(String code)throws ServerException {
		try {
			mapper.createPuntual(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public void createVoucher(String code)throws ServerException {
		try {
			mapper.createVoucher(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public void createRegister(String code)throws ServerException {
		try {
			mapper.createRegister(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public void createAccumulate(String code)throws ServerException {
		try {
			mapper.createAccumulate(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public void createAuxiliar(String code)throws ServerException {
		try {
			mapper.createAuxiliar(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
}