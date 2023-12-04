package com.accounting.plan.application;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.application.base.IPlanCreateCatalogService;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.accounting.plan.infrastructure.CreateCatalogTablesMapper;
import com.shared.domain.ServerException;

@Service("PlanCreateCatalogTemplateAccountingService")
public class PlanCreateCatalogService implements IPlanCreateCatalogService {

	@Autowired
	private CatalogService catalogService;
	@Autowired
	private CreateCatalogTablesMapper mapper;

	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CatalogDTO call(CatalogDTO catalog) throws ServerException {
		validateCatalog(catalog);
		catalogService.save(catalog);
		catalog = catalogService.getById(catalog.getKey());
		createTemporal(catalog.getCode());
		createPuntual(catalog.getCode());
		createVoucher(catalog.getCode());
		createRegister(catalog.getCode());
		// createAccumulate(catalog.getCode());
		createAuxiliar(catalog.getCode());
		return catalog;
	}

	private void validateCatalog(CatalogDTO catalog) throws ServerException {
		if(catalog ==null) throw new ServerException("No se reconoce catalogo");
		if(catalog.getCode() == null) throw new ServerException("El codigo del catalogo es obligatorio");
		if(!catalog.getCode().matches("[0-9A-Za-z]+") || catalog.getCode().length() < 5) throw new ServerException("El codigo solo puede tener letras y numeros y no puede tener espacios. Ademas debe tener mas de 4 digitos");
		if(catalog.getName()==null)throw new ServerException("El nombre del catalogo es obligatorio");
		if(catalog.getInitialDate()==null || catalog.getFinalDate()==null)throw new ServerException("La fecha de inicio y de fin del catalogo es obligatorio");
		if(catalog.getInitialDate().compareTo(catalog.getFinalDate())>0)throw new ServerException("La fecha de inicio debe ser menor a la fecha de fin del catalogo");
		CatalogFilterDTO filter = new CatalogFilterDTO();
		filter.setCode(catalog.getCode());
		if(catalogService.count(filter)!=0) throw new ServerException("Ya existe un catalogo con este codigo");
		
	}

	private void createTemporal(String code) throws ServerException {
		try {
			mapper.createTemporal(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	private void createPuntual(String code) throws ServerException {
		try {
			mapper.createPuntual(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	private void createVoucher(String code) throws ServerException {
		try {
			mapper.createVoucher(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	private void createRegister(String code) throws ServerException {
		try {
			mapper.createRegister(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	private void createAuxiliar(String code) throws ServerException {
		try {
			mapper.createAuxiliar(code);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

}
