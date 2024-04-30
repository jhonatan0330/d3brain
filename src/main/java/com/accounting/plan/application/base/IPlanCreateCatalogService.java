package com.accounting.plan.application.base;

import com.accounting.plan.domain.CatalogDTO;
import com.shared.domain.ServerException;

public interface IPlanCreateCatalogService {

	CatalogDTO call(CatalogDTO catalog) throws ServerException;
	
	CatalogDTO callDelete(CatalogDTO catalog) throws ServerException;

}
