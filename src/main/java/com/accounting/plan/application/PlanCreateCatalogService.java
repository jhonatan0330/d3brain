package com.accounting.plan.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.application.base.IPlanCreateCatalogService;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.softure.java.dto.exception.ServerException;

@Service("PlanCreateCatalogTemplateAccountingService")
public class PlanCreateCatalogService implements IPlanCreateCatalogService {

	@Autowired
	private CatalogService catalogService;
	@Autowired
	private CreateCatalogTablesService createTableService;
	
	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CatalogDTO call(CatalogDTO catalog, String token) throws ServerException {
		validateCatalog(catalog);
		catalog = catalogService.save(catalog, token);
		createTableService.createTemporal(catalog.getCode());
		createTableService.createPuntual(catalog.getCode());
		createTableService.createVoucher(catalog.getCode());
		createTableService.createRegister(catalog.getCode());
		createTableService.createAccumulate(catalog.getCode());
		createTableService.createAuxiliar(catalog.getCode());
		return catalog;
	}

	private void validateCatalog(CatalogDTO catalog) throws ServerException {
		if(catalog ==null) throw new ServerException("No se reconoce catalogo");
		if(catalog.getCode() == null) throw new ServerException("El codigo del catalogo es obligatorio");
		if(catalog.getName()==null)throw new ServerException("El nombre del catalogo es obligatorio");
		if(catalog.getInitialDate()==null || catalog.getFinalDate()==null)throw new ServerException("La fecha de inicio y de fin del catalogo es obligatorio");
		if(catalog.getInitialDate().compareTo(catalog.getFinalDate())>0)throw new ServerException("La fecha de inicio debe ser menor a la fecha de fin del catalogo");
		CatalogFilterDTO filter = new CatalogFilterDTO();
		filter.setCode(catalog.getCode());
		if(catalogService.count(filter)!=0) throw new ServerException("Ya existe un catalogo con este codigo");
		
	}

}
